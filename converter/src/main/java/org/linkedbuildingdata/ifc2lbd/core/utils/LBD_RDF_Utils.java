package org.linkedbuildingdata.ifc2lbd.core.utils;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.linkedbuildingdata.ifc2lbd.namespace.IfcOWL;

import com.openifctools.guidcompressor.GuidCompressor;

public abstract class LBD_RDF_Utils {
    /**
    * Creates URIs for the elements in the output graph. The IfcRoot elements
    * (that have a GUID) are given URI that contais the guid in the standard
    * uncompressed format.
    * 
    * The uncompressed GUID form is created using the implementation by Tulke and
    * Co. (The OPEN IFC JAVA TOOLBOX)
    * 
    * @param r
    *            A ifcOWL RDF node in a Apache Jena RDF store.
    * @param m
    *            The Apache Jena RDF Store for the output.
    * @param product_type
    *            The LBD product type to be shown on the URI
    * @param ifcOWL_ns the ifcOWL name space class instance
    * @param uriBase   the used base URI
    * @return    Returns the created LBD Jena resource
    */
   public static Resource createformattedURIRecource(Resource r, Model m, String product_type,IfcOWL ifcOWL_ns, String uriBase) {
       
       String guid = IfcOWLUtils.getGUID(r, ifcOWL_ns);
       if (guid == null) {
           String localName = r.getLocalName();
           if (localName.startsWith("IfcPropertySingleValue")) {
               if (localName.lastIndexOf('_') > 0)
                   localName = localName.substring(localName.lastIndexOf('_') + 1);
               Resource uri = m.createResource(uriBase + "propertySingleValue_" + localName);
               uri.addProperty(OWL.sameAs, r);
               return uri;
           }
           if (localName.toLowerCase().startsWith("ifc"))
               localName = localName.substring(3);
           Resource uri = m.createResource(uriBase + product_type.toLowerCase() + "_" + localName);
           uri.addProperty(OWL.sameAs, r);
           return uri;
       } else {
           Resource guid_uri = m.createResource(uriBase + product_type.toLowerCase() + "_" + GuidCompressor.uncompressGuidString(guid));
           guid_uri.addProperty(OWL.sameAs, r);
           return guid_uri;
       }
   }
}
