package it.unibo.lsd.model;

import java.util.ArrayList;
import java.util.List;

public class LSDSKOSVoc {

    private String uri;
    private String title, description;
    private List<LSDSKOSConcept> concepts, topConcepts=new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LSDSKOSConcept> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<LSDSKOSConcept> concepts) {
        this.concepts = concepts;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LSDSKOSConcept> getTopConcepts() {
        return topConcepts;
    }

    public void setTopConcepts(List<LSDSKOSConcept> topConcepts) {
        this.topConcepts = topConcepts;
    }
}
