package it.unibo.lsd;

import freemarker.template.TemplateException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;

@RestController
public class LSDController {

    @GetMapping("/lsd")
    public String lsd(@RequestParam(value = "url") String url, @RequestParam(value = "lang", defaultValue = "en", required = false) String lang) throws TemplateException, IOException {
        return PageBuilder.getInstance().getPage(ModelGenerator.getInstance().getModel(url, lang));
    }

    @RequestMapping(value="/source",  produces = "application/rdf+xml")
    public String source(@RequestParam(value = "url") String url) {
        Model m = ModelFactory.createDefaultModel();
        m.read(url);
        StringWriter sw = new StringWriter();
        m.write(sw);
        return sw.getBuffer().toString();
    }

}