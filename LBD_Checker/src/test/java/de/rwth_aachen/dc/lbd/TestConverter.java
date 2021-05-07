package de.rwth_aachen.dc.lbd;

import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Test;
import org.linkedbuildingdata.ifc2lbd.IFCtoLBDConverter;

import com.github.davidmoten.rtreemulti.Entry;
import com.github.davidmoten.rtreemulti.RTree;
import com.github.davidmoten.rtreemulti.geometry.Geometry;
import com.github.davidmoten.rtreemulti.geometry.Rectangle;

public class TestConverter {

    @Test
    public void testFindTestData() {
        URL file_url = ClassLoader.getSystemResource("Duplex.ifc");
        try {
            File file = new File(file_url.toURI());
            if (file == null || !file.exists())
                fail("Test data not found/available");
        } catch (Exception e) {
            fail("Test data not found/available: " + e.getMessage());
        }
    }

    @Test
    public void testTwoWallsConversion() {
        URL file_url = ClassLoader.getSystemResource("TWO WALLS.ifc");
        try {
            File ifc_file = new File(file_url.toURI());
            File temp_file = File.createTempFile("ifc2lbd", "test.ttl");
            new IFCtoLBDConverter(ifc_file.getAbsolutePath(), "https://dot.dc.rwth-aachen.de/IFCtoLBDset#", temp_file.getAbsolutePath(), 0, true, false, true, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Conversion had an error: " + e.getMessage());
        }
    }

    
    @Test
    public void testBasicConversion() {
        URL file_url = ClassLoader.getSystemResource("Duplex.ifc");
        try {
            File ifc_file = new File(file_url.toURI());
            File temp_file = File.createTempFile("ifc2lbd", "test.ttl");
            new IFCtoLBDConverter(ifc_file.getAbsolutePath(), "https://dot.dc.rwth-aachen.de/IFCtoLBDset#", temp_file.getAbsolutePath(), 0, true, false, true, false, false, false);
        } catch (Exception e) {
            fail("Conversion had an error: " + e.getMessage());
        }
    }

    @Test
    public void testOldIFCVersionConversion() {
        URL file_url = ClassLoader.getSystemResource("05111002_IFCR2_Geo_Columns_1.ifc");
        try {
            File ifc_file = new File(file_url.toURI());
            File temp_file = File.createTempFile("ifc2lbd", "test.ttl");
            new IFCtoLBDConverter(ifc_file.getAbsolutePath(), "https://dot.dc.rwth-aachen.de/IFCtoLBDset#", temp_file.getAbsolutePath(), 0, true, false, true, false, false, false);
        } catch (Exception e) {
            fail("Conversion had an error: " + e.getMessage());
        }
    }

    @Test
    public void testConversion1() {
        URL file_url = ClassLoader.getSystemResource("Duplex.ifc");
        try {
            File ifc_file = new File(file_url.toURI());
            File temp_file = File.createTempFile("ifc2lbd", "test.ttl");
            IFCtoLBDConverter c1nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", false, 1);
            c1nb.convert(ifc_file.getAbsolutePath(), temp_file.getAbsolutePath());
            @SuppressWarnings("unused")
            Model m1nb = c1nb.convert(ifc_file.getAbsolutePath());

            IFCtoLBDConverter c1wb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", true, 1);
            c1wb.convert(ifc_file.getAbsolutePath(), temp_file.getAbsolutePath());
            @SuppressWarnings("unused")
            Model m1wb = c1wb.convert(ifc_file.getAbsolutePath());

            IFCtoLBDConverter c2nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", false, 2);
            c2nb.convert(ifc_file.getAbsolutePath(), temp_file.getAbsolutePath());
            @SuppressWarnings("unused")
            Model m2nb = c2nb.convert(ifc_file.getAbsolutePath());

            IFCtoLBDConverter c2wb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", true, 2);
            c2wb.convert(ifc_file.getAbsolutePath(), temp_file.getAbsolutePath());
            @SuppressWarnings("unused")
            Model m2wb = c2wb.convert(ifc_file.getAbsolutePath());

            IFCtoLBDConverter c3nb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", false, 3);
            c3nb.convert(ifc_file.getAbsolutePath(), temp_file.getAbsolutePath());
            @SuppressWarnings("unused")
            Model m3nb = c3nb.convert(ifc_file.getAbsolutePath());

            IFCtoLBDConverter c3wb = new IFCtoLBDConverter("https://dot.dc.rwth-aachen.de/IFCtoLBDset#", true, 3);
            c3wb.convert(ifc_file.getAbsolutePath(), temp_file.getAbsolutePath());
            @SuppressWarnings("unused")
            Model m3wb = c3wb.convert(ifc_file.getAbsolutePath());

        } catch (Exception e) {
            fail("Conversion set 1 had an error: " + e.getMessage());
        }
    }

    @Test
    public void testRTree() {
        try {
            RTree<Resource, Geometry> rtree = RTree.dimensions(3).create();

            Rectangle rectangle = Rectangle.create(-0.5, -0.5, -0.5, 0.1, 0.1, 0.1);
            Resource r1 = ResourceFactory.createResource("http://example.de/1");
            rtree = rtree.add(r1, rectangle); // rtree is immutable

            Rectangle rectangle2 = Rectangle.create(-0.1, -0.1, -0.1, 1, 1, 1);

            Iterable<Entry<Resource, Geometry>> results = rtree.search(rectangle2);
            boolean correct=false;
            for (@SuppressWarnings("unused") Entry<Resource, Geometry> e : results) {
                correct=true;
            }
            if(!correct)
                fail("RTree test failed");
            
        } catch (Exception e) {
            fail("RTree fails: " + e.getMessage());
        }
    }
}
