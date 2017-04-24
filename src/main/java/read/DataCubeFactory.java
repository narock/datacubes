package read;

import read.file.*;

public class DataCubeFactory {
	
	public DataCube createDataCube( String dataType ) {
	
		if ( dataType == null ) { return null; }
		
		if ( dataType == "TsvArrangement" ) { return new TsvFile(); }
		
		return null;
		
	}
	
}