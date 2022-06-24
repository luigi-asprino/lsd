package it.unibo.lsd.model;

import java.util.ArrayList;
import java.util.List;

public class LSDSKOSConcept {

    private String iri, label, inScheme, ref, definition;
    private List<LSDSKOSConcept> broaderConcepts = new ArrayList<>();
    private List<LSDSKOSConcept> narrowerConcepts = new ArrayList<>();

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<LSDSKOSConcept> getBroaderConcepts() {
        return broaderConcepts;
    }

    public void setBroaderConcepts(List<LSDSKOSConcept> broaderConcepts) {
        this.broaderConcepts = broaderConcepts;
    }

    public List<LSDSKOSConcept> getNarrowerConcepts() {
        return narrowerConcepts;
    }

    public void setNarrowerConcepts(List<LSDSKOSConcept> narrowerConcepts) {
        this.narrowerConcepts = narrowerConcepts;
    }

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInScheme() {
        return inScheme;
    }

    public void setInScheme(String inScheme) {
        this.inScheme = inScheme;
    }
}
