package de.rwth_aachen.dc.lbd;

import java.io.File;
import java.net.URL;

import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;
import org.junit.Test;
import org.linkedbuildingdata.ifc2lbd.IFCtoLBDConverter;
import static org.junit.Assert.fail;


public class TestConverterUsingJenaSHACL {

    @Test
    public void testConversionBOTCore() {
        System.out.println("Start");
        URL ifc_file_url = ClassLoader.getSystemResource("Duplex.ifc");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_rulesetBotCore.ttl");
        try {
            File ifc_file = new File(ifc_file_url.toURI());
            IFCtoLBDConverter c1nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/lbd#", false, 1);
            Model m1nb = c1nb.convert(ifc_file.getAbsolutePath());
            Graph graph_m1nb = m1nb.getGraph();

            File rule1_file = new File(rule_file_url.toURI());
            Graph shapesGraph = RDFDataMgr.loadGraph(rule1_file.getAbsolutePath());
            Shapes shapes = Shapes.parse(shapesGraph);

            ValidationReport report = ShaclValidator.get().validate(shapes, graph_m1nb);
            if (!report.conforms()) {
                System.out.println("false");
                fail("Conversion output does not conform SHACL_rulesetLevel1");
                ShLib.printReport(report);
                report.getModel().write(System.out);
            } else
                System.out.println("Actually ok");
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Conversion using set SHACL_rulesetLevel1 had an error: " + e.getMessage());
        }
    }

    
    @Test
    public void testConversionLevel3() {
        System.out.println("Start");
        URL ifc_file_url = ClassLoader.getSystemResource("Duplex.ifc");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_rulesetLevel3.ttl");
        try {
            File ifc_file = new File(ifc_file_url.toURI());

            IFCtoLBDConverter c1nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", false, 3);
            Model m1nb = c1nb.convert(ifc_file.getAbsolutePath());
            Graph graph_m1nb = m1nb.getGraph();

            File rule1_file = new File(rule_file_url.toURI());
            Graph shapesGraph = RDFDataMgr.loadGraph(rule1_file.getAbsolutePath());
            Shapes shapes = Shapes.parse(shapesGraph);

            ValidationReport report = ShaclValidator.get().validate(shapes, graph_m1nb);
            if (!report.conforms()) {
                System.out.println("false");
                fail("Conversion output does not conform SHACL_rulesetLevel3");
                ShLib.printReport(report);
                RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
            } else
            {
                System.out.println("Actually ok");
                ShLib.printReport(report);
                RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);

            }
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Conversion using set SHACL_rulesetLevel3 had an error: " + e.getMessage());
        }
    }

    @Test
    public void testConversionBugIfcRelAggregatesNotConverting() {
        System.out.println("Start");
        URL ifc_file_url = ClassLoader.getSystemResource("SampleHouse.ifc");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_rulesetLevel2.ttl");
        try {
            File ifc_file = new File(ifc_file_url.toURI());

            IFCtoLBDConverter c1nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", false, 1);
            Model m1nb = c1nb.convert(ifc_file.getAbsolutePath());
            //m1nb.removeAll();
            m1nb.write(System.out, "TTL");
            Graph graph_m1nb = m1nb.getGraph();

            File rule1_file = new File(rule_file_url.toURI());
            Graph shapesGraph = RDFDataMgr.loadGraph(rule1_file.getAbsolutePath());
            Shapes shapes = Shapes.parse(shapesGraph);

            ValidationReport report = ShaclValidator.get().validate(shapes, graph_m1nb);
            if (!report.conforms()) {
                System.out.println("false");
                fail("Conversion output does not conform SHACL_rulesetLevel3");
                ShLib.printReport(report);
                RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
            } else
                System.out.println("Actually ok");

        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Conversion using set SHACL_rulesetLevel4 had an error: " + e.getMessage());
        }
    }

    @Test
    public void testConversionBugIfcVirtualElement() {
        System.out.println("Start");
        URL ifc_file_url = ClassLoader.getSystemResource("IFC_Schependomlaan.ifc");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_rulesetLevel2.ttl");
        try {
            File ifc_file = new File(ifc_file_url.toURI());

            IFCtoLBDConverter c1nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", false, 1);
            Model m1nb = c1nb.convert(ifc_file.getAbsolutePath());
            m1nb.write(System.out, "TTL");
            Graph graph_m1nb = m1nb.getGraph();

            File rule1_file = new File(rule_file_url.toURI());
            Graph shapesGraph = RDFDataMgr.loadGraph(rule1_file.getAbsolutePath());
            Shapes shapes = Shapes.parse(shapesGraph);

            ValidationReport report = ShaclValidator.get().validate(shapes, graph_m1nb);
            if (!report.conforms()) {
                System.out.println("false");
                fail("Conversion output does not conform SHACL_rulesetLevel2");
                ShLib.printReport(report);
                RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
            } else
                System.out.println("Actually ok");

        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Conversion using set SHACL_rulesetLevel4 had an error: " + e.getMessage());
        }
    }
    
    @Test
    public void testDuplexAJapanBug() {
        System.out.println("Start");
        URL ifc_file_url = ClassLoader.getSystemResource("Duplex_AJ.ifc");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_rulesetLevel2.ttl");
        try {
            File ifc_file = new File(ifc_file_url.toURI());

            IFCtoLBDConverter c1nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", false, 1);
            Model m1nb = c1nb.convert(ifc_file.getAbsolutePath());
            m1nb.write(System.out, "TTL");
            Graph graph_m1nb = m1nb.getGraph();

            File rule1_file = new File(rule_file_url.toURI());
            Graph shapesGraph = RDFDataMgr.loadGraph(rule1_file.getAbsolutePath());
            Shapes shapes = Shapes.parse(shapesGraph);

            ValidationReport report = ShaclValidator.get().validate(shapes, graph_m1nb);
            if (!report.conforms()) {
                System.out.println("false");
                fail("Conversion output does not conform SHACL_rulesetLevel3");
                ShLib.printReport(report);
                RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
            } else
                System.out.println("Actually ok");

        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Conversion using set SHACL_rulesetLevel4 had an error: " + e.getMessage());
        }
    }

}
