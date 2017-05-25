package owl;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.*;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;

import java.util.ArrayList;

public class Instances {
	
	public Individual getIndividualFromUri ( String uri, OntModel ontModel ) {
	
		Individual in = ontModel.getIndividual(uri);
		return in;
		
	}
	
	public ArrayList <String> getAllProperties ( Individual in, OntModel ontModel ) {
		
		ArrayList <String> properties = new ArrayList <String> ();
		for (StmtIterator j = in.listProperties(); j.hasNext(); ) {
           Statement s = j.next();
           System.out.println( s.getSubject() + "->" + s.getPredicate().toString() + " -> " + s.getObject() );
        }
        return properties;
		
	}
	
	public String getObjectPropertyUri ( Individual in, OntModel ontModel, Property property ) {
	
		String uri = null;
		for (StmtIterator j = in.listProperties(property); j.hasNext(); ) {
           Statement s = j.next();
           uri = s.getObject().toString();
        }
        return uri;
		
	}
	
	public ArrayList <String> getObjectPropertyUris ( Individual in, OntModel ontModel, Property property ) {
		
		ArrayList <String> uris = new ArrayList <String> ();
		for (StmtIterator j = in.listProperties(property); j.hasNext(); ) {
           Statement s = j.next();
           uris.add( s.getObject().toString() );
        }
        return uris;
		
	}
	
	public ArrayList <String> getInstanceUrisFromClass (OntModel ontModel, String classUri) {
    	
		ArrayList <String> instanceUris = new ArrayList <String> ();
		
    	// get the class from the ontology model
    	OntClass ontClass = ontModel.getOntClass( classUri );

        // get instances of the class
        ExtendedIterator<? extends OntResource> it = ontClass.listInstances();
        while( it.hasNext() ){
        
        	// grab the next Individual and put it into our ArrayList
        	Individual in = (Individual) it.next();  
        	instanceUris.add( in.getURI() );
            
        }
        
        // return the list of instances
        return instanceUris;
    
    }

    public ArrayList <Individual> getInstancesFromClass (OntModel ontModel, String classUri) {
    	
		ArrayList <Individual> instances = new ArrayList <Individual> ();

    	// get the class from the ontology model
    	OntClass ontClass = ontModel.getOntClass( classUri );

        // get instances of the class
        ExtendedIterator<? extends OntResource> it = ontClass.listInstances();
        while( it.hasNext() ){
        
        	// grab the next Individual and put it into our ArrayList
        	Individual in = (Individual) it.next();  
        	instances.add( in ); 
            
        }
        
        // return the list of instances
        return instances;
    
    }
    
    public String getDataPropertyValue (OntModel ontModel, Individual in, String propertyUri) {
    	
    	Property property = ontModel.createProperty( propertyUri );
    	RDFNode node = in.getPropertyValue( property );
        String[] parts = node.toString().split("\\^"); // remove type information
        String propertyValue = parts[0].trim();
        return propertyValue;
        
    }
    
}
