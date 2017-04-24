package main;

import owl.MergedModel;
import owl.GetDataFromUrl;
import java.util.ArrayList;
import com.hp.hpl.jena.ontology.OntModel;

public class TestDownload {
	
	public static void main (String[] args) {
	
		String dir = "/Users/narock/Dropbox/DataCubes/ontologies/owl/with_Instances/";
		
		ArrayList <String> files = new ArrayList <String> ();
		files.add(dir + "DigitalObject.owl");
		files.add(dir + "PersistentID.owl");
		files.add(dir + "MetadataDescription.owl");
		
		MergedModel model = new MergedModel();
		OntModel ontModel = model.readFromFiles(files);
		
		GetDataFromUrl data = new GetDataFromUrl ();
		data.downloadFromOntModel( ontModel );
		
	}
	
}