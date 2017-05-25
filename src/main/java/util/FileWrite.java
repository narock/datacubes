package util;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileWrite {
	
   public void append (String file, String line, boolean b) {
		  
		String dateStr = null;
		if (b) {
		  DateFormat dateFormat = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
		  Date date = new java.util.Date ();
		  dateStr = dateFormat.format (date);
		} 
		try {
		  // append to file, create if doesn't exist 
		  FileWriter fstream = new FileWriter(file,true);
		  BufferedWriter out = new BufferedWriter(fstream);
		  if (b) out.write(dateStr + ", " + line + "\n");
		  if (!b) out.write(line + "\n");
		  //Close the output stream
		  out.close();
	    }catch (Exception e){ }
   }
	  
   public void append (String file, String line){
     try{
       // append to file, create if doesn't exist 
       FileWriter fstream = new FileWriter(file,true);
       BufferedWriter out = new BufferedWriter(fstream);
       out.write(line + "\n");
       //Close the output stream
       out.close();
     }catch (Exception e){ }
   }
    
   public void newFile (String file, String line){
	     try{
	       // create new file 
	       FileWriter fstream = new FileWriter(file);
	       BufferedWriter out = new BufferedWriter(fstream);
	       out.write(line + "\n");
	       //Close the output stream
	       out.close();
	     }catch (Exception e){ }
   }
   
}