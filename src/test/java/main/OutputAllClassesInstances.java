package main;

import java.util.ArrayList;

import owl.MergedModel;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class OutputAllClassesInstances {
	
	public static void main (String[] args) {
	
		// Sanity Checking Code
		//
		// test code to read sample ODP files and print out all the classes and instances
		
		String dir = "/Users/narock/Dropbox/DataCubes/ontologies/owl/with_Instances/";
		
		ArrayList <String> files = new ArrayList <String> ();
		files.add(dir + "DigitalObject.owl");
		files.add(dir + "PersistentID.owl");
		files.add(dir + "MetadataDescription.owl");
		
		MergedModel model = new MergedModel();
		OntModel ontModel = model.readFromFiles(files);
		
		ExtendedIterator<OntClass> classes = ontModel.listClasses();
        while (classes.hasNext())
        {
          OntClass thisClass = (OntClass) classes.next();
          System.out.println("Found class: " + thisClass.toString());
          ExtendedIterator<? extends OntResource> instances = thisClass.listInstances();
          while (instances.hasNext())
          {
            Individual thisInstance = (Individual) instances.next();
            System.out.println("  Found instance: " + thisInstance.toString());
          }
        }
        
	}
	
}