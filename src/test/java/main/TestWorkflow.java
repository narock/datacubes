package main;

import owl.MergedModel;

import java.util.ArrayList;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Individual;

import cubeQueries.Queries;
import read.DataCubeFactory;
import read.DataCube;
import util.Namespaces;
import owl.Instances;

public class TestWorkflow {
	
	public static void main (String[] args) {
	
		// turn on print statements to show status updates during testing
		boolean verbose = true;
				
		String dir = args[0]; // args[0] should include the trailing "/"
		
		ArrayList <String> files = new ArrayList <String> ();
		files.add(dir + "BCO-DMO_examples/552076/DigitalObject_552076.owl");
		files.add(dir + "BCO-DMO_examples/552076/PersistentID_552076.owl");
		files.add(dir + "BCO-DMO_examples/552076/MetadataDescription_552076.owl");
		
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
		if ( verbose ) { System.out.println("Number of Digital Objects found: " + size + "\n"); }
		
		// loop over all the Digital Objects
		String pidUri;
		String accessPathUri;
		Property rdfType = ontModel.getOntProperty( "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" );
		Property isIdentifiedBy = ontModel.getOntProperty( Namespaces.digitalObject + "isIdentifiedBy" );
		Property hasAccessPath = ontModel.getOntProperty( Namespaces.pid + "hasAccessPath" );
		for ( int index=0; index<size; index++ ) {

			// get the Persistent ID associated with this Digital Object
			if ( verbose ) { System.out.println("Getting Persistent ID from Digital Object..."); }
			pidUri = instances.getObjectPropertyUri( digitalObjects.get(index), ontModel, isIdentifiedBy );
			System.out.println( pidUri );

			// get the Access Path from the Persistent ID individual
			if ( verbose ) { System.out.println("Getting Access Path from Persistent ID..."); }
			Individual pidIn = instances.getIndividualFromUri( pidUri, ontModel );
			accessPathUri = instances.getObjectPropertyUri( pidIn, ontModel, hasAccessPath );

			// get the type of Access Path
			if ( verbose ) { System.out.println("Getting Type of Access Path..."); }
			Individual accessPathIn = instances.getIndividualFromUri( accessPathUri, ontModel );
			String accessPathType = instances.getObjectPropertyUri( accessPathIn, ontModel, rdfType );
			
			// use the Access Path Type to create and populate DataCube
			if ( verbose ) { System.out.println("Creating DataCube..."); }
			DataCube datacube = dcFactory.createDataCube( digitalObjects.get(index), accessPathType, accessPathUri, 
					ontModel, verbose );
						
			// try a SPARQL query over the datacube
			if ( verbose ) { System.out.println("Generating ontology model from DataCube..."); }
			OntModel cubeOntModel = datacube.getOntModel();
			Queries query = new Queries ();
			ArrayList <String> results = 
					query.hasExactValue( "Calanus_finmarchicus", cubeOntModel, "?obs", true );
			for ( int i=0; i<results.size(); i++ ) {
				System.out.println("Calanus finmarchicus found in observation: " + results.get(i));
			}
			
		}
		
	}
	
}