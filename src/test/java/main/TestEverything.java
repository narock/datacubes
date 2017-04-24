package main;

import owl.MergedModel;
import owl.GetDataFromUrl;
import java.util.ArrayList;
import com.hp.hpl.jena.ontology.OntModel;

public class TestEverything {
	
	public static void main (String[] args) {
	
		String dir = "/Users/narock/Dropbox/DataCubes/ontologies/owl/with_Instances/";
		
		ArrayList <String> files = new ArrayList <String> ();
		files.add(dir + "DigitalObject.owl");
		files.add(dir + "PersistentID.owl");
		files.add(dir + "MetadataDescription.owl");
		
		// create the in-memory ontology model from the provided files
		MergedModel model = new MergedModel();
		OntModel ontModel = model.readFromFiles(files);
		
		// use the hasURL property in the ontology to get the URLs and download the data
		GetDataFromUrl data = new GetDataFromUrl ();
		data.downloadFromOntModel( ontModel );
		
		// use the ontology to determine what file type we are dealing with
		
		// read the data from that file type into an RDF Data Cube
		
	}
	
}