package org.linkedbuildingdata.ifc2lbd.namespace;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

public class UNIT extends abstract_NS{
	public static final String UNIT_ns = "http://qudt.org/vocab/unit/";
	public static final Resource METER =resource(UNIT_ns,"M");
	public static final Resource MILLI_METER =resource(UNIT_ns,"MilliM");
	public static final Resource SQUARE_METRE =resource(UNIT_ns,"M2");
	public static final Resource SQUARE_MILLI_METRE =resource(UNIT_ns,"MilliM2");
	public static final Resource CUBIC_METRE =resource(UNIT_ns,"M3");
	public static final Resource CUBIC_MILLI_METER =resource(UNIT_ns,"MilliM3");
	public static final Resource RADIAN =resource(UNIT_ns,"RAD");
	public static void addNameSpace(Model model)
	{
		model.setNsPrefix("unit", UNIT_ns);
	}
	
}