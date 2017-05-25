package read;

import com.hp.hpl.jena.ontology.OntModel;

public interface DataCube {
	
	void setOntModel ( OntModel ontModel );
	
	OntModel getOntModel ();
	
}