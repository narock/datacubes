package util;

import java.net.URL;
import java.net.URLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;

public class DownloadFile {
	
	public boolean download ( String url, String outputFile ) {
		
		// if the output file exists then delete it
		File oFile = new File (outputFile);
		if ( oFile.exists() ) { oFile.delete(); }
		
		boolean result = true;
		BufferedInputStream in = null;
		FileOutputStream fout = null;
			
		try {
			  
			  URLConnection connection = new URL(url).openConnection();
			  connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			  connection.connect();
			
			  in = new BufferedInputStream( connection.getInputStream() );
			  fout = new FileOutputStream(outputFile);

			  byte data[] = new byte[1024];
			  int count;
			  while ((count = in.read(data, 0, 1024)) != -1) {
				  fout.write(data, 0, count);
			  }
			 			 
			  if (in != null) { in.close(); }
			  if (fout != null) { fout.close(); }
			
		} catch ( Exception e ) { System.out.println(e); result = false; }
		
		return result;
		
	}
	 
}