package read.file;

import read.DataCube;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TsvFile implements DataCube {
	
	public OntModel readData() {
		
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
		return ontModel;
		
	}
	
}