package util;

public class Config {
	
	// Configuration parameters
	
	// be sure to include the trailing /
	public static final String downloadDir = "/Users/narock/Desktop/download/";

	// download index
	public static int downloadIndex = 0;
	
	// fill values
	// keep the rdf range consistent. if the range is xsd:int then use int fill value, if string use string fill value
	public static int fillValueInteger = -9999;
	public static String fillValueString = "no_data";
		
}