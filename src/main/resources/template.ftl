<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>${title}</title>
	<link href="./owl.css" rel="stylesheet" type="text/css" />
	<link href="./Primer.css" rel="stylesheet" type="text/css" />
	<link href="./rec.css" rel="stylesheet" type="text/css" />
	<link href="./extra.css" rel="stylesheet" type="text/css" />
	<!--link rel="shortcut icon" href="./favicon.ico" /-->
	<script src="./jquery.js"></script>
	<script src="./jquery.scrollTo.js"></script>
	<script src="./marked.min.js"></script>
	<script>
		$(document).ready(
				function() {
					jQuery(".markdown").each(function(el) {
						jQuery(this).after(marked(jQuery(this).text())).remove()
					});
					var list =
						$('a[name="
						    ${url}
							"]');
							if (list.size() != 0) {
								var element = list.first();
								$.scrollTo(element);
							}
						});
	</script>
</head>

<body>
	<#list page.schemes as scheme>

        <div class="head">

            <#if scheme.title??>
      		<h1>${scheme.title}</h1>
      		</#if>

      		<dl>
      		    <dt>IRI:</dt>
      			<dd>${scheme.uri}</dd>
      		</dl>
      		<#if scheme.topConcepts?size != 0>
      		<dl>
      		    <dt>Top Concepts:</dt>
      		        <ul>
                    <#list scheme.topConcepts as topConcept>
                        <li><a href="#${topConcept.ref}" title="${topConcept.iri}"><span>${topConcept.label}</span></a></li>
                    </#list>
                    </ul>
            </dl>
            </#if>
            <#if scheme.issued??>
            <dl>
                <dt>Issued:</dt>
                <dd>${scheme.issued}</dd>
            </dl>
            </#if>
            <#if scheme.modified??>
            <dl>
                <dt>Modified:</dt>
                <dd>${scheme.modified}</dd>
            </dl>
            </#if>
      		<dl>
      		    <dt>Other visualisation:</dt>
            	<dd><a href="./source?url=${url}">Source</a></dd>
            </dl>
      	<div>

      	<hr />

        <#if scheme.description??>
        <h2>Abstract</h2>
        <span class="markdown">${scheme.description}</span>
        </#if>

        <#if scheme.concepts?size != 0>
        <div id="toc">
            <h2>Table of Content</h2>
        	<ol>
        		<li><a href="#concepts">Concepts</a></li>
        	</ol>
        </div>
        </#if>

        <#if scheme.concepts?size != 0>
        <div id="concepts">
        	<h2>Concepts</h2>
        	<ul class="hlist">
        	    <#list scheme.concepts as concept>
        	        <li><a href="#${concept.ref}" title="${concept.iri}">${concept.label}</a></li>
        	    </#list>
        	</ul>
        </div>
        </#if>

        <#list scheme.concepts as concept>
            <div id="${concept.ref}" class="entity"><a name="${concept.iri}"></a>

            	<h3>${concept.label}<sup title="class" class="type-c">concept</sup><span class="backlink"> back to <a href="#toc">ToC</a> or <a href="#concepts">Concept ToC</a></span></h3>
            	<p><strong>IRI:</strong> ${concept.iri}</p>

            	<#if concept.definition??><div class="comment"><span class="markdown">${concept.definition}</span></div></#if>
            	<#if concept.inScheme??><dl class="definedBy"><dt>in scheme</dt><dd><a href="${concept.inScheme}">${concept.inScheme}</a></dd></dl></#if>


                <#if concept.broaderConcepts?size != 0>
                    <dl class="description"><dt>has broader concepts</dt>
                    <#list concept.broaderConcepts as broaderConcept>
                        <dd><a href="#${broaderConcept.ref}" title="${broaderConcept.iri}">${broaderConcept.label}</a><sup title="class" class="type-c">concept</sup></dd>
                    </#list>
                    </dl>
                </#if>

                <#if concept.narrowerConcepts?size != 0>
                    <dl class="description"><dt>has narrower concepts</dt>
                    <#list concept.narrowerConcepts as narrowerConcept>
                        <dd><a href="#${narrowerConcept.ref}" title="${narrowerConcept.iri}">${narrowerConcept.label}</a><sup title="class" class="type-c">concept</sup></dd>
                    </#list>
                    </dl>
                </#if>


            </div>
        </#list>

    </#list>

    <div id="namespacedeclarations">
        <h2>Namespace Declarations <span class="backlink"> back to <a href="#toc">ToC</a></span></h2>
        <dl>
            <#list page.namespaces as namespace>
            <dt>${namespace.prefix}</dt><dd>${namespace.value}</dd>
            </#list>
        </dl>
    </div>



    <p class="endnote">
    This HTML document was obtained by processing the RDF source code through LSD, Live SKOS Documentation, developed by <a href="http://luigiasprino.it">Luigi Asprino</a>.
    LSD's style is based on <a href="https://essepuntato.it/lode/">LODE</a> (Live Ontology Documentation Environment).</p>

</body>

</html>