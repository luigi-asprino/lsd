package it.unibo.lsd;

import it.unibo.lsd.model.LSDNamespace;
import it.unibo.lsd.model.LSDPage;
import it.unibo.lsd.model.LSDSKOSConcept;
import it.unibo.lsd.model.LSDSKOSVoc;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ModelGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ModelGenerator.class);

    private static final String NO_SCHEME = "NO_SCHEME";

    private static ModelGenerator instance;

    private ModelGenerator() {

    }

    public static ModelGenerator getInstance() {
        if (instance == null) {
            instance = new ModelGenerator();
        }
        return instance;
    }

    private static String getLabel(Model model, Resource r, String lang) {

        List<Property> labelPredicates = new ArrayList<>();
        labelPredicates.add(SKOS.prefLabel);
        labelPredicates.add(RDFS.label);
        labelPredicates.add(DC.title);

        for (Property p : labelPredicates) {
            Statement statements = model.getProperty(r, p, lang);
            if (statements != null) {
                return statements.getString();
            }
        }

        String result = r.getLocalName();

        if (result.length() == 0) {
            result = r.getURI().substring(r.getURI().lastIndexOf('#') + 1);
        }

        return result;
    }

    private static String getDescription(Model model, Resource r, String lang) {

        List<Property> labelPredicates = new ArrayList<>();
        labelPredicates.add(SKOS.definition);
        labelPredicates.add(RDFS.comment);
        labelPredicates.add(DC.description);

        for (Property p : labelPredicates) {
            Statement statements = model.getProperty(r, p, lang);
            if (statements != null) {
                return statements.getString();
            }
        }

        return null;
    }

    public Map<String, Object> getModel(String url, String lang) {
        Map<String, LSDSKOSConcept> uriToConcept = new HashMap<>();
        Map<String, Object> root = new HashMap<>();


        Model model = ModelFactory.createDefaultModel();
        model.read(url);


        Map<String, List<LSDSKOSConcept>> schemeURIToConcepts = new HashMap<>();
        schemeURIToConcepts.put(NO_SCHEME, new ArrayList<>());
        model.listStatements(null, null, SKOS.ConceptScheme).forEachRemaining(s -> schemeURIToConcepts.put(s.getSubject().getURI(), new ArrayList<>()));

        model.listStatements(null, SKOS.inScheme, (RDFNode) null).forEachRemaining(s -> {
            if (s.getObject().isResource()) {
                schemeURIToConcepts.put(s.getObject().asResource().getURI(), new ArrayList<>());
            }
        });

        model.listStatements(null, RDF.type, SKOS.Concept).forEachRemaining(s -> {


            LSDSKOSConcept concept = new LSDSKOSConcept();

            concept.setIri(s.getSubject().getURI());

            concept.setLabel(getLabel(model, s.getSubject(), lang));

            String description = getDescription(model, s.getSubject(), lang);
            if (description != null) {
                concept.setDefinition(description);
            }

            concept.setRef(DigestUtils.md5Hex(concept.getIri()).substring(0, 7));

            StmtIterator si = model.listStatements(s.getSubject(), SKOS.inScheme, (RDFNode) null);
            if (!si.hasNext()) {
                schemeURIToConcepts.get(NO_SCHEME).add(concept);
            }
            si.forEachRemaining(iss -> {
                if (iss.getObject().isResource()) {
                    schemeURIToConcepts.get(iss.getObject().asResource().getURI()).add(concept);
                }
                concept.setInScheme(iss.getObject().asResource().getURI());
            });

            logger.trace("Adding Concept {} '{}'", concept.getIri(), concept.getLabel());
            uriToConcept.put(concept.getIri(), concept);

        });

        logger.trace("URI To Concept");
        uriToConcept.forEach((k, v) -> {
            logger.trace("Concept {} {}", k, v.getIri());
        });

        schemeURIToConcepts.forEach((u, cl) -> cl.sort(new Comparator<LSDSKOSConcept>() {
            @Override
            public int compare(LSDSKOSConcept o1, LSDSKOSConcept o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        }));

        model.listStatements(null, RDF.type, SKOS.Concept).forEachRemaining(s -> {

            logger.trace("Retrieving Broader Concepts for Concept {}", s.getSubject().getURI());
            LSDSKOSConcept concept = uriToConcept.get(s.getSubject().getURI());
            concept.setBroaderConcepts(getLsdskosConcepts(uriToConcept, model, s.getSubject(), SKOS.broader));
            concept.setNarrowerConcepts(getLsdskosConcepts(uriToConcept, model, s.getSubject(), SKOS.narrower));

        });


        List<LSDSKOSVoc> schemes = new ArrayList<>();

        model.listStatements(null, null, SKOS.ConceptScheme).forEachRemaining(s -> {
            LSDSKOSVoc voc = new LSDSKOSVoc();

            voc.setUri(s.getSubject().getURI());
            voc.setTitle(getLabel(model, s.getSubject(), lang));

            String description = getDescription(model, s.getSubject(), lang);
            if (description != null) {
                voc.setDescription(description);
            }

            if (!schemeURIToConcepts.get(s.getSubject().getURI()).isEmpty()) {
                voc.setConcepts(schemeURIToConcepts.get(s.getSubject().getURI()));
            }

            schemes.add(voc);


            voc.setTopConcepts(getLsdskosConcepts(uriToConcept, model, s.getSubject(), SKOS.hasTopConcept));
        });

        if (schemes.isEmpty()) {
            logger.trace("No schemes");
            LSDSKOSVoc voc = new LSDSKOSVoc();
            voc.setConcepts(schemeURIToConcepts.get(NO_SCHEME));
            voc.setUri(url);
            schemes.add(voc);
            root.put("title", "LSD");
        } else {
            root.put("title", schemes.get(0).getTitle());
        }

        List<LSDNamespace> namespaces = new ArrayList<>();
        model.getNsPrefixMap().forEach((k,v)->namespaces.add(new LSDNamespace(k,v)));

        LSDPage p = new LSDPage(schemes);
        p.setNamespaces(namespaces);
        root.put("page", p);
        root.put("url", url);

        return root;
    }

    private List<LSDSKOSConcept> getLsdskosConcepts(Map<String, LSDSKOSConcept> uriToConcept, Model model, Resource concept,  Property p) {
        List<LSDSKOSConcept> relatedConcepts = new ArrayList<>();
        model.listStatements(concept, p, (RDFNode) null).forEachRemaining(sb -> {
            if (sb.getObject().isResource()) {
                LSDSKOSConcept broaderConcept;
                if (uriToConcept.containsKey(sb.getObject().asResource().getURI())) {
                    broaderConcept = uriToConcept.get(sb.getObject().asResource().getURI());
                } else {
                    broaderConcept = new LSDSKOSConcept();
                    broaderConcept.setIri(sb.getObject().asResource().getURI());
                    broaderConcept.setLabel(sb.getObject().asResource().getLocalName());
                }
                relatedConcepts.add(broaderConcept);
            }
        });
        return relatedConcepts;
    }

}
