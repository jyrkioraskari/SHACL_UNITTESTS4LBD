package de.rwth_aachen.dc.lbd;

import java.io.InputStream;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

public class CheckLBD_Ontology {
    private OntModel ontology_model = ModelFactory.createOntologyModel();
    private Model model = ModelFactory.createDefaultModel();

    public CheckLBD_Ontology() {
        try {
            readOWLOntology();
            model.setNsPrefix("sh", "http://www.w3.org/ns/shacl#");
            model.setNsPrefix("bot", "https://w3id.org/bot#");
            
            Property di = ontology_model.createProperty("https://schema.org/domainIncludes");
            Property ri = ontology_model.createProperty("https://schema.org/rangeIncludes");
            Resource node_shape = model.createResource("http://www.w3.org/ns/shacl#NodeShape");
            Property path = model.createProperty("http://www.w3.org/ns/shacl#path");
            Property property = model.createProperty("http://www.w3.org/ns/shacl#property");
            Property or = model.createProperty("http://www.w3.org/ns/shacl#or");
            Property targetClass  = model.createProperty("http://www.w3.org/ns/shacl#targetClass");
            Property shClass  = model.createProperty("http://www.w3.org/ns/shacl#class");
            
            ontology_model.listOntProperties().forEachRemaining(x -> {
                if (x.toString().startsWith("https://w3id.org/bot#")) {
                    Resource ps=model.createResource();
                    ps.addProperty(RDF.type, node_shape);
                    
                    
                    x.listDomain().forEachRemaining(y -> {
                        ps.addProperty(targetClass, y);
                    });
                    Resource pr=model.createResource();
                    ps.addProperty(property, pr);
                    pr.addProperty(path, x);
                    x.listPropertyValues(di).forEachRemaining(y -> {
                    });
                    x.listPropertyValues(ri).forEachRemaining(y -> {
                    });
                    
                    x.listRange().forEachRemaining(r -> {
                        pr.addProperty(shClass, r);
                    });

                    /*x.listDomain().forEachRemaining(y -> {
                        System.out.println("-d > " + y);
                    });*/
                }

            });
            model.write(System.out,"TTL");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readOWLOntology() {
        InputStream in = null;
        try {
            in = CheckLBD_Ontology.class.getResourceAsStream("/bot.ttl");

            this.ontology_model.read(in, null, "TTL");
        } finally {
            try {
                in.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new CheckLBD_Ontology();
    }
}
