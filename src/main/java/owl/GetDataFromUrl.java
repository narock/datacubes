package owl;

import com.hp.hpl.jena.util.iterator.*;

import util.Config;
import util.Namespaces;
import util.DownloadFile;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;

public class GetDataFromUrl {
	
	
    public void downloadFromOntModel (OntModel ontModel) {
		
    	// download object
    	DownloadFile downloadFile = new DownloadFile ();
    	
    	// set up the URIs and property names we'll need later
    	String hasUrlUri = Namespaces.pid + "#hasUrl";
        String fileAccessPathUri = Namespaces.pid + "#FileAccessPath";
    	Property hasUrl = ontModel.createProperty( hasUrlUri );
    	
    	// get the FileAccessPath class from the ontology model
    	OntClass fileAccessPath = ontModel.getOntClass( fileAccessPathUri );

        // get instances of the FileAccessPath class
    	boolean success = false;
        ExtendedIterator<? extends OntResource> it = fileAccessPath.listInstances();
        while( it.hasNext() ){
        
        	// grab the property we're interested in, and its value, from the instances of FileAccessPath
        	Individual in = (Individual) it.next();            
            RDFNode node = in.getPropertyValue( hasUrl );
            String[] parts = node.toString().split("\\^"); // remove the anyURI type information
            String url = parts[0].trim();
            parts = url.split("/");
            String filename = parts[ parts.length-1 ];
            success = downloadFile.download( url, Config.downloadDir + filename );
            System.out.println( "Downloading: " + url + ", " + success);
            
        }
    
    }
    
}
