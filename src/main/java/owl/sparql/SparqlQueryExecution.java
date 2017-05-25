package owl.sparql;

import java.util.ArrayList;
import com.hp.hpl.jena.query.ARQ;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

public class SparqlQueryExecution {
	
	public ArrayList <String> getSparqlResults ( ResultSet results, String sparqlParameter ) {
			
		ArrayList <String> sparqlResults = new ArrayList <String> ();
		
		while ( results.hasNext() ) {
			 
	         //QuerySolution soln = results.nextSolution();
			 //RDFNode node = soln.get( sparqlParameter );
			 //sparqlResults.add( node.toString() );
			QuerySolution binding = results.nextSolution();
			Resource sub = (Resource) binding.get("s");
			Resource prd = (Resource) binding.get("p");
			Resource obj = (Resource) binding.get("o");

		    System.out.println("Subject: "+sub.getURI());
		    System.out.println("Predicate: "+prd.getURI());
		    System.out.println("Object: "+obj.getURI());
			 
		}
		
		return sparqlResults;
			   
	}
	
	public ResultSet queryMemoryModel ( OntModel ontModel, String sparqlQueryString ) {
    
		//Query query = QueryFactory.create(sparqlQueryString);
        //ARQ.getContext().setTrue(ARQ.useSAX);
    	//QueryExecution qexec = QueryExecutionFactory.create(query, ontModel);
    	
		Query query = QueryFactory.create( sparqlQueryString );
		QueryExecution qexec = QueryExecutionFactory.create( query, ontModel ) ;
		ResultSet results = qexec.execSelect() ;
    	qexec.close();
    	return results; 
    	
    }
	
}