package owl.sparql;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.ontology.OntModel;

public class SparqlQueryExecution {
	
	public ArrayList <String> queryMemoryModel ( OntModel ontModel, String sparqlQueryString, String sparqlParameter ) {
    	
		Query query = QueryFactory.create( sparqlQueryString );
		QueryExecution qexec = QueryExecutionFactory.create( query, ontModel ) ;
		ResultSet results = qexec.execSelect() ;
    	
		ArrayList <String> sparqlResults = new ArrayList <String> ();
		
		while ( results.hasNext() ) {
			 
	         QuerySolution soln = results.nextSolution();
			 RDFNode node = soln.get( sparqlParameter );
			 sparqlResults.add( node.toString() );
			 
		}
		
		
		qexec.close();
    	return sparqlResults; 
    	
    }
	
}