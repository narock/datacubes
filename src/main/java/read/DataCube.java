package read;

import org.apache.jena.ontology.OntModel;

public interface DataCube {
	
	void setOntModel ( OntModel ontModel );
	
	OntModel getOntModel ();
	
}