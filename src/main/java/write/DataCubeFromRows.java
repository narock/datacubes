package write;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import read.file.AsciiMetadata;

public class DataCubeFromRows {
	
	String newline = "\n";
	String datasetName = null;
	Random rand = new Random();

	private String randomNumberAsString () {

		int n = rand.nextInt(500) + 1; // [1,500]
		return String.valueOf(n);
	}
	
	public String dataCubeDescriptionAsTurtle (String fileAccessPathUri, ArrayList <AsciiMetadata> metadata) { 
				
		String turtle = 
				
			"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . " + newline +
			"@prefix owl: <http://www.w3.org/2002/07/owl#> . " + newline +
			"@prefix qb: <http://purl.org/linked-data/cube#> . " + newline +
			"@prefix sdmx: <http://purl.org/linked-data/sdmx#> . " + newline +
			"@prefix sdmx-concept: <http://purl.org/linked-data/sdmx/2009/concept#> . " + newline +
			"@prefix sdmx-dimension: <http://purl.org/linked-data/sdmx/2009/dimension#> . " + newline +
			"@prefix sdmx-attribute: <http://purl.org/linked-data/sdmx/2009/attribute#> . " + newline +
			"@prefix sdmx-measure: <http://purl.org/linked-data/sdmx/2009/measure#> . " + newline +
			"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . " + newline +
			"@prefix esip: <http://www.esipfed.org/datacube#> . " + newline +
			"@prefix interval: <http://reference.data.gov.uk/def/intervals/> . " + newline +
			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> . " + newline;

			// Measure Properties 
			// Java starts counting at 0, but rows and columns in data start counting at 1
			// Here we start at 1 to be consistent, first value is always empty
			for ( int i=1; i<metadata.size(); i++ ) {
				
				AsciiMetadata m = metadata.get(i);
				
				String[] parts = m.getDataType().split("#");
				
				turtle = turtle + "esip:" + m.getLocalName() + " a rdf:Property, " + newline +
								  "qb:MeasureProperty; " + newline +
								  " rdfs:label \"" + m.getLocalName() + "\"@en;" + newline +
								  " rdfs:subPropertyOf sdmx-measure:obsValue; " + newline +
								  " rdfs:range xsd:" + parts[1].trim() + " . " + newline;
			}
			turtle = turtle + newline;

			// DataStructureDefinition
			String randValue = "dsd" + randomNumberAsString();
			turtle = turtle + "esip:" + randValue + " a qb:DataStructureDefinition; " + newline;
			
			// The measure(s)
			// Java starts counting at 0, but rows and columns in data start counting at 1
			// Here we start at 1 to be consistent, first value is always empty
			for ( int i=1; i<metadata.size(); i++ ) {
				AsciiMetadata m = metadata.get(i);
				turtle = turtle + "qb:component [qb:measure esip:" + m.getLocalName() + "]; " + newline;
			}
			turtle = turtle + " . " + newline;
			
			// DataSet definition
			String randValue2 = "dataSet" + randomNumberAsString();
			this.datasetName = randValue2;
			turtle = turtle + "esip:" + randValue2 + " a qb:DataSet; " + newline +			
							  " rdfs:label \"DataCube for " + fileAccessPathUri + "\"@en; " + newline +
							  " qb:structure esip:" + randValue + " ; . " + newline;

			return turtle;
			
	}
	
	public String dataCubeRowsToTurtle ( ArrayList <AsciiMetadata> metadata, List <String[]> allRows, int offset ) { 

		int counter = 0;
		String turtle = "";
		
		// first row might be header values, offset tell us where the data starts
		for ( int i=offset; i<allRows.size(); i++ ) { 
	
			String[] row = allRows.get(i);
			
			// Observations
			turtle = turtle + "esip:obs" + String.valueOf(counter) + " a qb:Observation; " + newline + 
							  " qb:dataSet esip:" + this.datasetName + " ; " + newline; 
			counter++;
			
			// columns within each row
			for ( int j=0; j<row.length; j++ ) {
				// Java starts counting at 0, but rows and columns in data start counting at 1
				// Here we start at 1 to be consistent, first value is always empty
				AsciiMetadata m = metadata.get(j+1);
				
				if ( m.getDataType().equals("http://www.w3.org/2001/XMLSchema#string") ) {
					turtle = turtle + " esip:" + m.getLocalName() + " \"" + String.valueOf(row[j]) + "\" ; ";
				} else {
					turtle = turtle + " esip:" + m.getLocalName() + " " + String.valueOf(row[j]) + " ; ";
				}
				
				if ( j == row.length-1 ) { 
					turtle = turtle + " . " + newline;
				} else {
					turtle = turtle + newline;
				}
		 
			}
			
		}
		
		return turtle;
		 
	}
}