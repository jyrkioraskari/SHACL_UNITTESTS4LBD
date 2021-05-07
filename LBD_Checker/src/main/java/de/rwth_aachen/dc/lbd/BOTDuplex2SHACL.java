package de.rwth_aachen.dc.lbd;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.linkedbuildingdata.namespace.BOT;

public class BOTDuplex2SHACL {

    private Model shacl_model = ModelFactory.createDefaultModel();
    private Graph shacl_graph = shacl_model.getGraph();

    Map<String, Node> converter_map = new HashMap<>();

    public BOTDuplex2SHACL() {
        URL duplex_converterL1BF_url = ClassLoader.getSystemResource("ifc2lbd_Duplex_output_L1BF.ttl");
        URL duplex_lbdttl_url = ClassLoader.getSystemResource("Model files/LBD/Duplex.ttl");
        try {
            File duplex_converterL1BFl_file = new File(duplex_converterL1BF_url.toURI());
            Graph duplex_converterL1BFl_Graph = RDFDataMgr.loadGraph(duplex_converterL1BFl_file.getAbsolutePath());

            duplex_converterL1BFl_Graph.find(Node.ANY, Node.ANY, BOT.Site).forEachRemaining(x -> {
                converter_map.put(BOT.Site.toString(), x.getSubject());
            });

            duplex_converterL1BFl_Graph.find(Node.ANY, Node.ANY, BOT.Building).forEachRemaining(x -> {
                converter_map.put(BOT.Building.toString(), x.getSubject());
            });

            duplex_converterL1BFl_Graph.find(Node.ANY, Node.ANY, Node.ANY).forEachRemaining(x -> {
                if (x.getPredicate().toString().contains("globalIdIfcRoot_attribute_simple"))
                    converter_map.put(x.getObject().getLiteralLexicalForm(), x.getSubject());
            });

            File duplex_lbdttl_file = new File(duplex_lbdttl_url.toURI());
            Graph sampleLBD4DuplexGraph = RDFDataMgr.loadGraph(duplex_lbdttl_file.getAbsolutePath());
            sampleLBD4DuplexGraph.find(null, null, BOT.Site).forEachRemaining(x -> {
                createSHACL(sampleLBD4DuplexGraph, x.getSubject());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        shacl_model.write(System.out, "TTL");
    }

    long counter = 0;
    String baseURI = "https://dot.dc.rwth-aachen.de/lbd#";

    private void createSHACL(Graph sampleLBD, Node n) {
        System.out.println("node: " + n);
        Node node_shape_class = NodeFactory.createURI("http://www.w3.org/ns/shacl#NodeShape");
        Node targetNode_property = NodeFactory.createURI("http://www.w3.org/ns/shacl#targetNode");
        Optional<Triple> sample_graph_guid = getBOTGUID(sampleLBD, n);
        if (sample_graph_guid.isPresent()) {
            String guid;
            if (sample_graph_guid.get().getObject().isLiteral())
                guid = sample_graph_guid.get().getObject().getLiteralValue().toString();
            else
                guid = sample_graph_guid.get().getObject().getURI();
            Node target_node_in_converter_graph = converter_map.get(guid);
            if (target_node_in_converter_graph == null) {
                System.out.println("Node does not exist in converter graph: " + n + " guid:" + guid);
                return;
            }
            Node node_shape = NodeFactory.createURI(baseURI + "shape_" + counter++);
            shacl_graph.add(Triple.create(node_shape, RDF.type.asNode(), node_shape_class));
            shacl_graph.add(Triple.create(node_shape, targetNode_property, target_node_in_converter_graph));
            sampleLBD.find(n, Node.ANY, Node.ANY).forEachRemaining(x -> {
                if (x.getPredicate().toString().startsWith(BOT.bot_ns)) {
                    System.out.println("--> " + x);
                    createSHACL(sampleLBD, node_shape, x.getPredicate(), x.getObject());
                }
            });
        }
    }

    private void createSHACL(Graph sampleLBD, Node parent_Shape, Node bot_property, Node n) {
        System.out.println("node: " + n);
        Node node_shape_class = NodeFactory.createURI("http://www.w3.org/ns/shacl#NodeShape");
        Node targetNode_property = NodeFactory.createURI("http://www.w3.org/ns/shacl#targetNode");
        Optional<Triple> sample_graph_guid = getBOTGUID(sampleLBD, n);
        if (sample_graph_guid.isPresent()) {
            String guid;
            if (sample_graph_guid.get().getObject().isLiteral())
                guid = sample_graph_guid.get().getObject().getLiteralValue().toString();
            else
                guid = sample_graph_guid.get().getObject().getURI();
            Node target_node_in_converter_graph = converter_map.get(guid);
            if (target_node_in_converter_graph == null) {
                System.out.println("Node does not exist in converter graph: " + n + " guid:" + guid);
                return;
            }
            
            
            Node sh_property_property  = NodeFactory.createURI("http://www.w3.org/ns/shacl#property");
            Node sh_path_property  = NodeFactory.createURI("http://www.w3.org/ns/shacl#path");
            Node sh_minCount_property  = NodeFactory.createURI("http://www.w3.org/ns/shacl#minCount");
            Node sh_hasValue_property  = NodeFactory.createURI("http://www.w3.org/ns/shacl#hasValue");
                        
            Node property  = NodeFactory.createAnon();
            shacl_graph.add(Triple.create(parent_Shape, sh_property_property, property));
            shacl_graph.add(Triple.create(property, sh_path_property, bot_property));
            shacl_graph.add(Triple.create(property, sh_minCount_property, NodeFactory.createLiteralByValue(1,XSDDatatype.XSDinteger)));
            shacl_graph.add(Triple.create(property, sh_hasValue_property, target_node_in_converter_graph));
            
            Node node_shape = NodeFactory.createURI(baseURI + "shape_" + counter++);
            shacl_graph.add(Triple.create(node_shape, RDF.type.asNode(), node_shape_class));
            shacl_graph.add(Triple.create(node_shape, targetNode_property, target_node_in_converter_graph));
            
            sampleLBD.find(n, Node.ANY, Node.ANY).forEachRemaining(x -> {
                if (x.getPredicate().toString().startsWith(BOT.bot_ns)) {
                    System.out.println("--> " + x);
                    createSHACL(sampleLBD, node_shape, x.getPredicate(), x.getObject());
                }
            });
        }
    }

    private Optional<Triple> getBOTGUID(Graph sampleLBD, Node n) {
        Node p_guid = NodeFactory.createURI("https://example.org/rvt#ifcguid");
        Optional<Triple> t = sampleLBD.find(n, p_guid, Node.ANY).nextOptional();
        if (t.isEmpty())
            t = sampleLBD.find(n, Node.ANY, BOT.Site).nextOptional();
        if (t.isEmpty())
            t = sampleLBD.find(n, Node.ANY, BOT.Building).nextOptional();

        if (t.isEmpty()) {
            System.out.println("Empty-->> ");
            sampleLBD.find(n, Node.ANY, Node.ANY).forEachRemaining(x -> {
                System.out.println(x);
            });
            System.out.println("<----Empty ");
        }
        return t;
    }

    public static void main(String[] args) {
        new BOTDuplex2SHACL();
    }

}
