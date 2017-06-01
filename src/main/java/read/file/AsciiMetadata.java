package read.file;

public class AsciiMetadata {
	
	private String localName = null;
	private String commonName = null;
	private String dataType = null;
	private String units = null;
	private String fillValue = null;
	
	public void setLocalName ( String s ) { localName = s; }
	public void setCommonName ( String s ) { commonName = s; }
	public void setDataType ( String s ) { dataType = s; }
	public void setUnits ( String s ) { units = s; }
	public void setFillValue ( String s ) { fillValue = s; }
	
	public String getLocalName () { return localName; }
	public String getCommonName () { return commonName; }
	public String getDataType () { return dataType; }
	public String getUnits () { return units; }
	public String getFillValue () { return fillValue; }
	
}