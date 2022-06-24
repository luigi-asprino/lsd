package it.unibo.lsd.model;

import java.util.List;

public class LSDPage {

    private List<LSDSKOSVoc> schemes;
    private List<LSDNamespace> namespaces;

    public LSDPage(List<LSDSKOSVoc> schemes) {
        this.schemes = schemes;
    }

    public List<LSDSKOSVoc> getSchemes() {
        return schemes;
    }

    public void setSchemes(List<LSDSKOSVoc> schemes) {
        this.schemes = schemes;
    }

    public List<LSDNamespace> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(List<LSDNamespace> namespaces) {
        this.namespaces = namespaces;
    }
}
