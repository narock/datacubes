package read.file;

import read.DataCube;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

import util.Config;
import util.FileWrite;
import util.Namespaces;
import write.DataCubeFromRows;
import owl.Instances;
import read.file.AsciiMetadata;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class TsvFile implements DataCube {
	
	private OntModel dataCubeOntModel;
	public OntModel getOntModel () { return dataCubeOntModel; }
	public void setOntModel ( OntModel model ) { dataCubeOntModel = model; }
		
	private ArrayList <AsciiMetadata> getMetadata ( OntModel ontModel, Individual dataArrangement ) {
		
		Instances instances = new Instances ();
		
		// get all the Data Column Instances
		Property property = ontModel.getOntProperty( Namespaces.metadataDescription + "hasDataColumn" );
		ArrayList <String> dataColumnUris = instances.getObjectPropertyUris(dataArrangement, ontModel, property);
		Property containsDataElement = ontModel.getOntProperty( Namespaces.metadataDescription + "containsDataElement" );
		
		// now that we know how many data columns there are define an ArrayList of the appropriate size
		ArrayList <AsciiMetadata> metadata = new ArrayList <AsciiMetadata> (dataColumnUris.size()+1);
		for (int i=0; i<dataColumnUris.size()+1; i++) { 
			AsciiMetadata m = new AsciiMetadata ();
			metadata.add(m); 
		}
		
		// loop over all the data columns
		for ( int i=0; i<dataColumnUris.size(); i++ ) {
			
			Individual dataColumn = instances.getIndividualFromUri(dataColumnUris.get(i), ontModel);
			// for each Data Column get the column number
			Integer columnNumber = Integer.valueOf(instances.getDataPropertyValue(ontModel, dataColumn, 
					Namespaces.metadataDescription + "hasColumnNumber") );
			// get the data element
			String dataElementUri = instances.getObjectPropertyUri(dataColumn, ontModel, containsDataElement);
			Individual dataElement = instances.getIndividualFromUri(dataElementUri, ontModel);
			// from the data element
			String localName = instances.getDataPropertyValue(ontModel, dataElement, 
					Namespaces.metadataDescription + "hasLocalName");
			String dataType = instances.getDataPropertyValue(ontModel, dataElement, 
					Namespaces.metadataDescription + "hasDataType");
			String fillValue = instances.getDataPropertyValue(ontModel, dataElement, 
					Namespaces.metadataDescription + "hasFillValue");
			String commonName = instances.getDataPropertyValue(ontModel, dataElement, 
					Namespaces.metadataDescription + " hasCommonName");
			String units = instances.getDataPropertyValue(ontModel, dataElement, 
					Namespaces.metadataDescription + " hasUnits");
		
			AsciiMetadata m = metadata.get(columnNumber);
			m.setLocalName( localName );
			m.setDataType( dataType );
			m.setFillValue( fillValue );
			m.setUnits( units );
			m.setCommonName( commonName );
			metadata.set( columnNumber, m );
		
		}
		
		return metadata;
		
	}
	
	public OntModel readData( String filePath, String fileAccessPathUri, Individual dataArrangement, OntModel ontModel ) {
		
		DataCubeFromRows dataCubeWriter = new DataCubeFromRows ();
		
		TsvParserSettings settings = new TsvParserSettings();
	    // the file used in the example uses '\n' as the line separator sequence.
	    // the line separator sequence is defined here to ensure systems such as MacOS and Windows
	    // are able to process this file correctly (MacOS uses '\r'; and Windows uses '\r\n').
	    settings.getFormat().setLineSeparator("\n");

	    // creates a TSV parser
	    TsvParser parser = new TsvParser(settings);

	    // parses all rows in one go.
	    List <String[]> allRows = parser.parseAll( new File ( filePath ) );
	    //String[] headers = allRows.get(0);
	    
	    // get parameter names and data types from the headers
	    ArrayList <AsciiMetadata> metadata = getMetadata( ontModel, dataArrangement );
	    
	    // get datacube representation in turtle format
	    String turtle1 = dataCubeWriter.dataCubeDescriptionAsTurtle(fileAccessPathUri, metadata);
	    String turtle2 = dataCubeWriter.dataCubeRowsToTurtle(metadata, allRows, 1);
	    String turtle = turtle1 + turtle2;
	    	
	    // write turtle out to file for testing
	    FileWrite fw = new FileWrite();
	    String[] parts = fileAccessPathUri.split("#");
	    String f = Config.downloadDir + "datacube_" + parts[1].trim() + ".ttl";
	    fw.newFile( f, turtle );
	    
	    // read into a new ontology model
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
		try {
			model.read(new FileInputStream(f),null,"TTL");
		} catch ( FileNotFoundException exp ) {
			System.out.println( exp );
		}
		
	    return model;
	    		

	}
	
}