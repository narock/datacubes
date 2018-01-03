package cubeQueries;

import java.util.ArrayList;
import org.apache.jena.ontology.OntModel;
import owl.sparql.SparqlQueryExecution;

public class Queries {
	
	public ArrayList <String> hasExactValue ( String value, OntModel cubeOntModel, String parameter, boolean verbose ) {
	
		String query = 
			"SELECT ?obs WHERE { " +
			"  ?obs a <http://purl.org/linked-data/cube#Observation> . " +
            "  ?obs <http://purl.org/narock/dc/datacube#taxon> ?value . " +
			"  FILTER ( regex (str(?value), \"" + value + "\", \"i\") ) " +
            "}";
		
		SparqlQueryExecution sparql = new SparqlQueryExecution ();
		if ( verbose ) { System.out.println("Executing SPARQL Query Over DataCube..."); }
		ArrayList <String> results = sparql.queryMemoryModel(cubeOntModel, query, parameter);
	
		return results;
		
	}
	
	public ArrayList <String> greaterThan ( String value, OntModel cubeOntModel, String parameter, boolean verbose ) {
		
		String query = 
			"SELECT ?obs WHERE { " +
			"  ?obs a <http://purl.org/linked-data/cube#Observation> . " +
            "  ?obs <http://purl.org/narock/dc/datacube#taxon> ?value . " +
			"  FILTER ( " + parameter + " > " + value + " )" +
            "}";
		
		SparqlQueryExecution sparql = new SparqlQueryExecution ();
		if ( verbose ) { System.out.println("Executing SPARQL Query Over DataCube..."); }
		ArrayList <String> results = sparql.queryMemoryModel(cubeOntModel, query, parameter);
	
		return results;
		
	}
	
	public ArrayList <String> lessThan ( String value, OntModel cubeOntModel, String parameter, boolean verbose ) {
		
		String query = 
			"SELECT ?obs WHERE { " +
			"  ?obs a <http://purl.org/linked-data/cube#Observation> . " +
            "  ?obs <http://purl.org/narock/dc/datacube#taxon> ?value . " +
			"  FILTER ( " + parameter + " < " + value + " )" +
            "}";
		
		SparqlQueryExecution sparql = new SparqlQueryExecution ();
		if ( verbose ) { System.out.println("Executing SPARQL Query Over DataCube..."); }
		ArrayList <String> results = sparql.queryMemoryModel(cubeOntModel, query, parameter);
	
		return results;
		
	}
	
	public ArrayList <String> withinRange ( String lowerLimit, String upperLimit, OntModel cubeOntModel, 
			String parameter, boolean verbose ) {
		
		String query = 
			"SELECT ?obs WHERE { " +
			"  ?obs a <http://purl.org/linked-data/cube#Observation> . " +
            "  ?obs <http://purl.org/narock/dc/datacube#taxon> ?value . " +
			"  FILTER ( " + parameter + " >= " + lowerLimit + " && " + parameter + " <= " + upperLimit + " )" +
            "}";
		
		SparqlQueryExecution sparql = new SparqlQueryExecution ();
		if ( verbose ) { System.out.println("Executing SPARQL Query Over DataCube..."); }
		ArrayList <String> results = sparql.queryMemoryModel(cubeOntModel, query, parameter);
	
		return results;
		
	}
	
}