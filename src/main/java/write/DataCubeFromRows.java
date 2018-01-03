package write;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import read.file.AsciiMetadata;
import util.FileWrite;
import util.Config;

public class DataCubeFromRows {
	
	String newline = "\n";
	String datasetName = null;
	Random rand = new Random();
    FileWrite fw = new FileWrite();

	private String randomNumberAsString () {

		int n = rand.nextInt(500) + 1; // [1,500]
		return String.valueOf(n);
	}
	
	public void dataCubeDescriptionAsTurtle (String file, String fileAccessPathUri, ArrayList <AsciiMetadata> metadata) { 
				
		fw.append( file,  
				
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . " + newline +
			"@prefix owl: <http://www.w3.org/2002/07/owl#> . " + newline +
			"@prefix qb: <http://purl.org/linked-data/cube#> . " + newline +
			"@prefix sdmx: <http://purl.org/linked-data/sdmx#> . " + newline +
			"@prefix sdmx-concept: <http://purl.org/linked-data/sdmx/2009/concept#> . " + newline +
			"@prefix sdmx-dimension: <http://purl.org/linked-data/sdmx/2009/dimension#> . " + newline +
			"@prefix sdmx-attribute: <http://purl.org/linked-data/sdmx/2009/attribute#> . " + newline +
			"@prefix sdmx-measure: <http://purl.org/linked-data/sdmx/2009/measure#> . " + newline +
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . " + newline +
			"@prefix dcrdf: <http://purl.org/narock/dc/datacube#> . " + newline +
			"@prefix interval: <http://reference.data.gov.uk/def/intervals/> . " + newline +
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> . " + newline
			);

			// Measure Properties 
			// Java starts counting at 0, but rows and columns in data start counting at 1
			// Here we start at 1 to be consistent, first value is always empty
			for ( int i=1; i<metadata.size(); i++ ) {
				
				AsciiMetadata m = metadata.get(i);
				
				String[] parts = m.getDataType().split("#");
				
				// if we have a common name use that, otherwise use the local name
				String n;
				if ( m.getCommonName() != null ) { n = m.getCommonName(); } else { n = m.getLocalName(); }
				
				fw.append( file,  "dcrdf:" + n + " a rdf:Property, " + newline +
								  "qb:MeasureProperty; " + newline +
								  " rdfs:label \"" + n + "\"@en;" + newline +
								  " rdfs:subPropertyOf sdmx-measure:obsValue; " + newline +
								  " rdfs:range xsd:" + parts[1].trim() + " . " + newline
								  );
			}
			fw.append(file, newline);

			// DataStructureDefinition
			String randValue = "dsd" + randomNumberAsString();
			fw.append(file, "dcrdf:" + randValue + " a qb:DataStructureDefinition; " + newline);
			
			// The measure(s)
			// Java starts counting at 0, but rows and columns in data start counting at 1
			// Here we start at 1 to be consistent, first value is always empty
			for ( int i=1; i<metadata.size(); i++ ) {
				AsciiMetadata m = metadata.get(i);
				
				// if we have a common name use that, otherwise use the local name
				String n;
				if ( m.getCommonName() != null ) { n = m.getCommonName(); } else { n = m.getLocalName(); }
				
				fw.append(file, "qb:component [qb:measure dcrdf:" + n + "]; " + newline);
			}
			fw.append(file, " . " + newline);
			
			// DataSet definition
			String randValue2 = "dataSet" + randomNumberAsString();
			this.datasetName = randValue2;
			fw.append(file,   "dcrdf:" + randValue2 + " a qb:DataSet; " + newline +			
							  " rdfs:label \"DataCube for " + fileAccessPathUri + "\"@en; " + newline +
							  " qb:structure dcrdf:" + randValue + " ; . " + newline);
			
	}
	
	public void dataCubeRowsToTurtle ( String file, ArrayList <AsciiMetadata> metadata, List <String[]> allRows, 
			int offset ) { 

		int counter = 1;
		
		try {
			   // the helper methods in util.FileWrite open and close a BufferedWriter each time
			   // they are called. This is fine for small amounts of data, but adds huge overhead
			   // with a for loop over all the rows in a large file. Here, instead of using
			   // util.FileWrite we manually open a new output stream and keep it open during writing
			   //
		       // append to file 
		       FileWriter fstream = new FileWriter( file, true );
		       BufferedWriter out = new BufferedWriter( fstream );
		       
		       // first row might be header values, offset tell us where the data starts
		       System.out.println(" Starting loop of size: " + allRows.size());
		       for ( String[] row : allRows ) {
						
		    	   if ( counter > offset ) {

		    		   // Observations
		    		   out.write( "dcrdf:obs" + String.valueOf(counter) + " a qb:Observation; " + newline + 
					 		      " qb:dataSet dcrdf:" + this.datasetName + " ; " + newline ); 
			
		    		   // columns within each row
		    		   int size2 = row.length;
		    		   for ( int j=0; j<size2; j++ ) {
		    		   
		    			   // Java starts counting at 0, but rows and columns in data start counting at 1
		    			   // Here we start at 1 to be consistent, first value is always empty
		    			   AsciiMetadata m = metadata.get(j+1);

		    			   // if we have a common name use that, otherwise use the local name
		    			   String n;
		    			   if ( m.getCommonName() != null ) { n = m.getCommonName(); } else { n = m.getLocalName(); }
				
		    			   // check if this current value is a fill value
		    			   if ( !String.valueOf(row[j]).equals( m.getFillValue() )) {
					
		    				   // if it's a string then we need to add quotes
		    				   if ( m.getDataType().equals("http://www.w3.org/2001/XMLSchema#string") ) {
		    					   out.write( " dcrdf:" + n + " \"" + String.valueOf(row[j]) + "\" ; " );
		    				   } else {
		    					   out.write( " dcrdf:" + n + " " + String.valueOf(row[j]) + " ; " );
		    				   }
					
		    				   // is a fill value
		    			   } else { 
				
		    				   // string fill value
		    				   if ( m.getDataType().equals("http://www.w3.org/2001/XMLSchema#string") ) {
		    					   out.write( " dcrdf:" + n + " \"" + Config.fillValueString + "\" ; " ); 
		    				   }
					
		    				   // integer fill value
		    				   if ( m.getDataType().equals("http://www.w3.org/2001/XMLSchema#integer") ) {
		    					   out.write( " dcrdf:" + n + " \"" + Config.fillValueInteger + "\" ; "); 
		    				   }
					
		    			   }
				
		    			   if ( j == size2-1 ) { out.write( " . " + newline ); } else { out.write( newline ); }
		 
		    		   } // end loop over row values (columns)

		    	   } // end if offset has correct value
		    	   counter++;
		    	   
		       } // end loop over rows

		       out.close();
		       
		} catch (Exception e) { System.out.println(e); }
			 
	}
}