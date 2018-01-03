package read;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import owl.GetDataFromUrl;
import owl.Instances;
import read.file.*;
import util.Namespaces;
import java.util.Iterator;

public class DataCubeFactory {
	
	public DataCube createDataCubeFromFile( Individual digitalObject, OntModel ontModel, 
			String filePath, String fileAccessPathUri, boolean verbose ) { 
		
		Instances instances = new Instances ();
		
		// determine the type of the data arrangement
		String dataArrangementType = null;
		Property isDescribedBy = ontModel.getOntProperty( Namespaces.digitalObject + "isDescribedBy" );
		String metadataUri = instances.getObjectPropertyUri( digitalObject, ontModel, isDescribedBy );
		Individual metadataIn = instances.getIndividualFromUri( metadataUri, ontModel );
		Property hasDataArrangement = ontModel.getOntProperty( Namespaces.metadataDescription + "hasDataArrangement" );
		String dataArrangementUri = instances.getObjectPropertyUri( metadataIn, ontModel, hasDataArrangement );
		Individual dataArrangementIn = instances.getIndividualFromUri( dataArrangementUri, ontModel );	
		  // true get's us the most specific types and not all the types (e.g. super-classes)
		for (Iterator <Resource> i = dataArrangementIn.listRDFTypes( true ); i.hasNext(); ) {
			  Resource cls = (Resource) i.next();
			  // this is a hack - fix this
			  // RDF type also includes http://www.w3.org/2002/07/owl#NamedIndividual
			  if ( cls.toString().contains("Arrangement") ) { dataArrangementType = cls.toString(); }
		}
				
		// create the correct object
		DataCube datacube = null;
		if ( dataArrangementType.equals( Namespaces.metadataDescription + "TsvArrangement") ) { 
			TsvFile tsvFile = new TsvFile();
			OntModel oModel = tsvFile.readData( filePath, fileAccessPathUri, dataArrangementIn, ontModel, verbose );
			datacube = tsvFile;
			datacube.setOntModel( oModel );
		}
		
		// return the filled datacube 
		return datacube; 
	}
	
	public DataCube createDataCube( Individual digitalObject, String accessPathType, String accessPathUri, 
			OntModel ontModel, boolean verbose ) {
			
		if ( accessPathType.equals( Namespaces.pid + "FileAccessPath") ) {
			
			Instances instances = new Instances ();
			Individual accessPathInstance = instances.getIndividualFromUri( accessPathUri, ontModel );
			GetDataFromUrl getData = new GetDataFromUrl ();
			String path = getData.downloadFromOntModel( accessPathInstance, ontModel, verbose );
			DataCube datacube = createDataCubeFromFile( digitalObject, ontModel, path, accessPathUri, verbose );
		
			return datacube;
			
		}
		
		return null;
		
	}
	
}