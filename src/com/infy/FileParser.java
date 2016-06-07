package com.infy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/** Assumes UTF-8 encoding. JDK 7+. */
public class FileParser {

	public FileParser(String aFileName){
	    fFilePath = Paths.get(aFileName);
	}
	 //this method for local testing only 
	public static void main(String... aArgs) throws IOException {
	  FileParser parser = new FileParser("C:\\Temp\\EDLDUMP.txt");
	  parser.processLineByLine();
	  log("Done.");
	}
  
  
  
  /** Template method that calls {@link #processLine(String)}.  */
	public List<File> processLineByLine() throws IOException {
		List<com.infy.File> objs = new ArrayList<com.infy.File>();

		try {
			Scanner scanner =  new Scanner(fFilePath, ENCODING.name());

			int count = 1;
			List<String> list = new ArrayList<String>();
			while (scanner.hasNextLine()){
				if( count > 7)	{	
					list.add(scanner.nextLine());
				}else {
					scanner.nextLine();
				}
				count ++;				 
			}   

			for (String sp : list){

				if( sp.length()>40 ){
					File obj = new File();
					obj.setName(sp.substring(0, 39).trim().replaceAll("\\(.*?\\)", ""));

					String sd =sp.substring(41);
					String[] sb = sd.split("\\s+");

					StringBuffer sbuf = new StringBuffer();
					obj.setDescription(sb[0].trim());
					for ( int i=1; i<sb.length;i++){
						try {
							Integer.parseInt( sb[i] );
							obj.setDtn(sb[i]);
							break;
						}catch(NumberFormatException e){
							obj.setDescription(obj.getDescription()+" "+sb[i] );
						}
					}
					objs.add(obj);
					System.out.println(obj.name +":"+obj.description +":"+obj.dtn);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return objs;
	}
  
  /** 
   Overridable method for processing lines in different ways.
    
   <P>This simple default implementation expects simple name-value pairs, separated by an 
   '=' sign. Examples of valid input: 
   <tt>height = 167cm</tt>
   <tt>mass =  65kg</tt>
   <tt>disposition =  "grumpy"</tt>
   <tt>this is the name = this is the value</tt>
  */
  protected void processLine(String aLine){
    //use a second Scanner to parse the content of each line 
    Scanner scanner = new Scanner(aLine);
    if (scanner.hasNext()){
      //assumes the line has a certain structure
      String name = scanner.next();
      String value = scanner.next();
      String value1 = scanner.next();
      log("Name is : " + quote(name.trim()) + ", and Value is : " + quote(value.trim()) +"value 1"+value1);
    }
    else {
      log("Empty or invalid line. Unable to process.");
    }
  }
  
  // PRIVATE 
  private final Path fFilePath;
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  
  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }
  
  private String quote(String aText){
    String QUOTE = "'";
    return QUOTE + aText + QUOTE;
  }
} 

