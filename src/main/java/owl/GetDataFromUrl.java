package owl;

import util.Config;
import util.Namespaces;
import util.DownloadFile;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Individual;

public class GetDataFromUrl {
		
    public String downloadFromOntModel (Individual accessPathInstance, OntModel ontModel, boolean verbose) {
		
    	// download object
    	DownloadFile downloadFile = new DownloadFile ();
    	    	
    	// get the property value
    	Instances instances = new Instances ();
    	String hasUrlUri = Namespaces.pid + "hasUrl";
    	String url = instances.getDataPropertyValue( ontModel, accessPathInstance, hasUrlUri);
    	
    	// set up the filename and prepare for download
        String indexStr = String.valueOf( Config.downloadIndex );
        if ( Config.downloadIndex < 10 ) { indexStr = "0" + indexStr;  } 
        Config.downloadIndex++;
        String filename = "download_" + indexStr;
            
        if (verbose) { 
           System.out.println( "  File Access Path URI: " + accessPathInstance.getURI() ); 
           System.out.println( "    Downloading: " + url );
        }
            
        // download the file
        boolean success = downloadFile.download( url, Config.downloadDir + filename );
           
        if (verbose) { 
           System.out.println( "    Downloaded to: " + Config.downloadDir + filename + ", Success = " + success);
           System.out.println();
        }
            
        // return the downloaded filename otherwise null
        String result = null;
        if ( success ) { result = Config.downloadDir + filename; }
        return result;
        
    }
    
}
