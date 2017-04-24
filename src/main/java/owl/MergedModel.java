package owl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class MergedModel {
	
	// create an empty Jena ontology model we'll fill in with our data
	// the ontology model is set up using OWL DL in-memory model with RDF-S inferencing (e.g. subclasses, etc.)
    final OntModel mergedModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);

    public OntModel readFromFiles (ArrayList <String> files) {
		
    	// loop over the list of files
        for ( int i=0; i<files.size(); i++ ) {
        	
			  try {
				// create a separate ontology model for each file and then merge them into the main model
        		InputStream in = new FileInputStream(files.get(i));
        		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
        		model.read(in, null);
        		in.close();
        		mergedModel.add( model );
        	  } catch ( Exception e ) { System.out.println(files.get(i) + ": " + e); }
			
		}
        
        return mergedModel;
        	    
    }
    
    // helper method that clears the contents of ontology model should we need it
    public void clearModel () { mergedModel.removeAll(); }
    
    // helper method that will output in-memory ontology model to Turtle if needed
    public void writeToTurtle ( String outputFile ) {
    	
        try {
        		OutputStream out = new FileOutputStream( new File( outputFile ) );
        		mergedModel.write( out, "TURTLE", null );
        } catch ( Exception e ) { System.out.println(e); }
        
    }
    
}
