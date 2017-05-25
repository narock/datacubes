package main;

import owl.MergedModel;

import java.util.ArrayList;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Individual;

import read.DataCubeFactory;
import read.DataCube;
import util.Namespaces;
import owl.Instances;
import owl.sparql.*;

public class TestWorkflow {
	
	public static void main (String[] args) {
	
		// turn on print statements to show status updates during testing
		boolean verbose = true;
				
		String dir = args[0]; // args[0] should include the trailing "/"
		
		ArrayList <String> files = new ArrayList <String> ();
		files.add(dir + "2333/DigitalObject_2333.owl");
		files.add(dir + "2333/PersistentID_2333.owl");
		files.add(dir + "2333/MetadataDescription_2333.owl");
		files.add(dir + "552076/DigitalObject_552076.owl");
		files.add(dir + "552076/PersistentID_552076.owl");
		files.add(dir + "552076/MetadataDescription_552076.owl");
		
		// this factory will be used to create our data cubes
		DataCubeFactory dcFactory = new DataCubeFactory ();
				
		// create the in-memory ontology model from the provided files
		MergedModel model = new MergedModel();
		OntModel ontModel = model.readFromFiles(files);
		
		// get the instances of the classes we are interested in
		Instances instances = new Instances ();
		String doClassUri = Namespaces.digitalObject + "DigitalObject";
		ArrayList <Individual> digitalObjects = instances.getInstancesFromClass(ontModel, doClassUri);
		
		// print out a status update
		int size = digitalObjects.size();
		if ( verbose ) { System.out.println("Number of Digital Objects found: " + size); }
		
		// loop over all the Digital Objects
		String pidUri;
		String accessPathUri;
		Property rdfType = ontModel.getOntProperty( "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" );
		Property isIdentifiedBy = ontModel.getOntProperty( Namespaces.digitalObject + "isIdentifiedBy" );
		Property hasAccessPath = ontModel.getOntProperty( Namespaces.pid + "hasAccessPath" );
		for ( int index=0; index<size; index++ ) {

			// get the Persistent ID associated with this Digital Object
			pidUri = instances.getObjectPropertyUri( digitalObjects.get(index), ontModel, isIdentifiedBy );
			
			// get the Access Path from the Persistent ID individual
			Individual pidIn = instances.getIndividualFromUri( pidUri, ontModel );
			accessPathUri = instances.getObjectPropertyUri( pidIn, ontModel, hasAccessPath );

			// get the type of Access Path
			Individual accessPathIn = instances.getIndividualFromUri( accessPathUri, ontModel );
			String accessPathType = instances.getObjectPropertyUri( accessPathIn, ontModel, rdfType );
			
			// use the Access Path Type to create and populate DataCube
			DataCube datacube = dcFactory.createDataCube( digitalObjects.get(index), accessPathType, accessPathUri, 
					ontModel, verbose );
						
			// try a SPARQL query over the datacube
			OntModel cubeOntModel = datacube.getOntModel();
			String query = 
					"SELECT ?s ?p ?o WHERE { " +
		            "  ?s ?p ?o . " +
		            "}";
			SparqlQueryExecution sparql = new SparqlQueryExecution ();
			ResultSet resultSet = sparql.queryMemoryModel(cubeOntModel, query);
			sparql.getSparqlResults(resultSet, null);
			
		}
		
	}
	
}