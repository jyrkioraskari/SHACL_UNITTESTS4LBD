package de.rwth_aachen.dc.lbd;

import java.util.Iterator;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.ValidityReport;

import openllet.jena.PelletReasonerFactory;

public class BOTOntologyTest {
	final OntModel model;
	

	public BOTOntologyTest() {
		model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		model.read("./src/main/resources/opm.ttl");
        model.read("./src/main/resources/bot.ttl");
        model.read("./src/main/resources/Duplex_A_20110505_LBD.ttl");
		
		// print validation report
		final ValidityReport report = model	.validate();
		printIterator(report.getReports(), "Validation Results");

	}

	public static void printIterator(final Iterator<?> i, final String header)
	{
		System.out.println(header);
		for (int c = 0; c < header.length(); c++)
			System.out.print("=");
		System.out.println();

		if (i.hasNext())
			while (i.hasNext())
				System.out.println(i.next());
		else
			System.out.println("<EMPTY>");

		System.out.println();
	}
	
	public static void main(String[] args) {
		new BOTOntologyTest();
	}
}
