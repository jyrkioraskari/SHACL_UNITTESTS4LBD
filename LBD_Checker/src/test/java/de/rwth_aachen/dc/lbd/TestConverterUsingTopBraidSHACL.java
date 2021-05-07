package de.rwth_aachen.dc.lbd;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.junit.Test;
import org.linkedbuildingdata.ifc2lbd.IFCtoLBDConverter;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.rules.RuleUtil;
import org.topbraid.shacl.validation.ValidationUtil;

public class TestConverterUsingTopBraidSHACL {

    
    @Test
    public void testFilter() {
        URL LBD_file_url = ClassLoader.getSystemResource("Duplex_A_20110505_LBD.ttl");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_Filter.ttl");

        try {
            Model dataModel = JenaUtil.createMemoryModel();

            File lbd_file = new File(LBD_file_url.toURI());
            RDFDataMgr.read(dataModel, lbd_file.getAbsolutePath());

            Model shapesModel = JenaUtil.createMemoryModel();
            File shaclFile = new File(rule_file_url.toURI());
            RDFDataMgr.read(shapesModel, shaclFile.getAbsolutePath());

            Model inferenceModel = JenaUtil.createDefaultModel(); 
            inferenceModel = RuleUtil.executeRules(dataModel, shapesModel, inferenceModel, null);  
            
            inferenceModel.write(System.out,"TTL");
            
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Alignmenttest failed: " + e.getMessage());
        }
    }

    @Test
    public void testFilterGeometry() {
        URL LBD_file1_url = ClassLoader.getSystemResource("LBD/Duplex.ttl");
        URL LBD_file2_url = ClassLoader.getSystemResource("LBD/Duplex_geometry.xml");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_FilterGeometry.ttl");

        try {
            Model dataModel = JenaUtil.createMemoryModel();
            File LBD_file1 = new File(LBD_file1_url.toURI());
            RDFDataMgr.read(dataModel, LBD_file1.getAbsolutePath());

            File LBD_file2 = new File(LBD_file2_url.toURI());
            RDFDataMgr.read(dataModel, LBD_file2.getAbsolutePath());
                      
            Model shapesModel = JenaUtil.createMemoryModel();
            File shaclFile = new File(rule_file_url.toURI());
            RDFDataMgr.read(shapesModel, shaclFile.getAbsolutePath());

            Model inferenceModel = JenaUtil.createDefaultModel(); 
            inferenceModel = RuleUtil.executeRules(dataModel, shapesModel, inferenceModel, null);  
            
            inferenceModel.write(System.out,"TTL");
            
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Alignmenttest failed: " + e.getMessage());
        }
    }

    
    @Test
    public void testBOTChecksum() {
        URL BOT_file_url = ClassLoader.getSystemResource("bot.ttl");
        URL LBD_file_url = ClassLoader.getSystemResource("Duplex_A_20110505_LBD.ttl");
        URL ifcOWL_file_url = ClassLoader.getSystemResource("Duplex_A_20110505_ifcOW.ttl");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_Checksum.ttl");

        try {
            File ifcOWL_file = new File(ifcOWL_file_url.toURI());
            Model dataModel = JenaUtil.createMemoryModel();
            RDFDataMgr.read(dataModel, ifcOWL_file.getAbsolutePath());

            
            File lbd_file = new File(LBD_file_url.toURI());
            RDFDataMgr.read(dataModel, lbd_file.getAbsolutePath());

            
            File bot_file = new File(BOT_file_url.toURI());
            RDFDataMgr.read(dataModel, bot_file.getAbsolutePath());

            Model shapesModel = JenaUtil.createMemoryModel();
            File shaclFile = new File(rule_file_url.toURI());
            RDFDataMgr.read(shapesModel, shaclFile.getAbsolutePath());
            
            RDFDataMgr.read(shapesModel, bot_file.getAbsolutePath());


            Resource report = ValidationUtil.validateModel(dataModel, shapesModel, false);

            OutputStream ttl_output = new OutputStream() {
                private StringBuilder string = new StringBuilder();

                @Override
                public void write(int b) throws IOException {
                    this.string.append((char) b);
                }

                public String toString() {
                    return this.string.toString();
                }
            };
            RDFDataMgr.write(ttl_output, report.getModel(), RDFFormat.TURTLE_PRETTY);
            System.out.println(ttl_output.toString());
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Alignmenttest failed: " + e.getMessage());
        }
    }

    
    @Test
    public void testifcOWLBOTAlignmentClasses() {
        URL BOT_file_url = ClassLoader.getSystemResource("bot.ttl");
        URL LBD_file_url = ClassLoader.getSystemResource("Duplex_A_20110505_LBD.ttl");
        URL ifcOWL_file_url = ClassLoader.getSystemResource("Duplex_A_20110505_ifcOW.ttl");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_Alignment.ttl");

        try {
            File ifcOWL_file = new File(ifcOWL_file_url.toURI());
            Model dataModel = JenaUtil.createMemoryModel();
            RDFDataMgr.read(dataModel, ifcOWL_file.getAbsolutePath());

            
            File lbd_file = new File(LBD_file_url.toURI());
            RDFDataMgr.read(dataModel, lbd_file.getAbsolutePath());

            
            File bot_file = new File(BOT_file_url.toURI());
            RDFDataMgr.read(dataModel, bot_file.getAbsolutePath());

            Model shapesModel = JenaUtil.createMemoryModel();
            File shaclFile = new File(rule_file_url.toURI());
            RDFDataMgr.read(shapesModel, shaclFile.getAbsolutePath());
            
            RDFDataMgr.read(shapesModel, bot_file.getAbsolutePath());


            Resource report = ValidationUtil.validateModel(dataModel, shapesModel, false);

            OutputStream ttl_output = new OutputStream() {
                private StringBuilder string = new StringBuilder();

                @Override
                public void write(int b) throws IOException {
                    this.string.append((char) b);
                }

                public String toString() {
                    return this.string.toString();
                }
            };
            RDFDataMgr.write(ttl_output, report.getModel(), RDFFormat.TURTLE_PRETTY);
            System.out.println(ttl_output.toString());
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Alignmenttest failed: " + e.getMessage());
        }
    }

    @Test
    public void testifcOWLBOTAlignmentRelations() {
        URL BOT_file_url = ClassLoader.getSystemResource("bot.ttl");
        URL LBD_file_url = ClassLoader.getSystemResource("Duplex_A_20110505_LBD.ttl");
        URL ifcOWL_file_url = ClassLoader.getSystemResource("Duplex_A_20110505_ifcOW.ttl");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_Alignment2.ttl");

        try {
            File ifcOWL_file = new File(ifcOWL_file_url.toURI());
            Model dataModel = JenaUtil.createMemoryModel();
            RDFDataMgr.read(dataModel, ifcOWL_file.getAbsolutePath());

            
            File lbd_file = new File(LBD_file_url.toURI());
            RDFDataMgr.read(dataModel, lbd_file.getAbsolutePath());

            
            File bot_file = new File(BOT_file_url.toURI());
            RDFDataMgr.read(dataModel, bot_file.getAbsolutePath());

            Model shapesModel = JenaUtil.createMemoryModel();
            File shaclFile = new File(rule_file_url.toURI());
            RDFDataMgr.read(shapesModel, shaclFile.getAbsolutePath());
            
            RDFDataMgr.read(shapesModel, bot_file.getAbsolutePath());


            Resource report = ValidationUtil.validateModel(dataModel, shapesModel, false);

            OutputStream ttl_output = new OutputStream() {
                private StringBuilder string = new StringBuilder();

                @Override
                public void write(int b) throws IOException {
                    this.string.append((char) b);
                }

                public String toString() {
                    return this.string.toString();
                }
            };
            RDFDataMgr.write(ttl_output, report.getModel(), RDFFormat.TURTLE_PRETTY);
            System.out.println(ttl_output.toString());
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Alignmenttest failed: " + e.getMessage());
        }
    }
    
    
    @Test
    public void testConversionBOTCore() {
        URL ifc_file_url = ClassLoader.getSystemResource("Duplex.ifc");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_rulesetBotCore.ttl");

        try {
            File ifc_file = new File(ifc_file_url.toURI());
            IFCtoLBDConverter c1nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", false, 1);
            Model dataModel = c1nb.convert(ifc_file.getAbsolutePath());

            Model shapesModel = JenaUtil.createMemoryModel();
            File shaclFile = new File(rule_file_url.toURI());
            RDFDataMgr.read(shapesModel, shaclFile.getAbsolutePath());

            Resource report = ValidationUtil.validateModel(dataModel, shapesModel, true);

            OutputStream ttl_output = new OutputStream() {
                private StringBuilder string = new StringBuilder();

                @Override
                public void write(int b) throws IOException {
                    this.string.append((char) b);
                }

                public String toString() {
                    return this.string.toString();
                }
            };
            RDFDataMgr.write(ttl_output, report.getModel(), RDFFormat.TURTLE_PRETTY);
            System.out.println(ttl_output.toString());
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Conversion using set SHACL_rulesetLevel1 had an error: " + e.getMessage());
        }
    }

    @Test
    public void testConversionBOTgen() {
        URL ifc_file_url = ClassLoader.getSystemResource("Duplex.ifc");
        URL rule_file_url = ClassLoader.getSystemResource("SHACL_Duplex_GEN.ttl");

        try {
            File ifc_file = new File(ifc_file_url.toURI());
            IFCtoLBDConverter c1nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/lbd#", false, 1);
            Model dataModel = c1nb.convert(ifc_file.getAbsolutePath());

            Model shapesModel = JenaUtil.createMemoryModel();
            File shaclFile = new File(rule_file_url.toURI());
            RDFDataMgr.read(shapesModel, shaclFile.getAbsolutePath());

            Resource report = ValidationUtil.validateModel(dataModel, shapesModel, true);

            OutputStream ttl_output = new OutputStream() {
                private StringBuilder string = new StringBuilder();

                @Override
                public void write(int b) throws IOException {
                    this.string.append((char) b);
                }

                public String toString() {
                    return this.string.toString();
                }
            };
            RDFDataMgr.write(ttl_output, report.getModel(), RDFFormat.TURTLE_PRETTY);
            System.out.println(ttl_output.toString());
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
            fail("Conversion using set SHACL_rulesetLevel1 had an error: " + e.getMessage());
        }
    }
}
