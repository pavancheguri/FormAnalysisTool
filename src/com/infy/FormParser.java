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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Assumes UTF-8 encoding. JDK 7+. */
public class FormParser {

	/**
	   Constructor.
	   @param aFileName full name of an existing, readable file.
	  */
	  public FormParser(String aFileName){
		    fFilePath = Paths.get("C:\\Temp\\"+aFileName);
	  }
	  
	public static void main(String... aArgs) throws Exception {
	  FormParser parser = new FormParser("0102AASIG.txt");
	  parser.processLineByLine();
	  log("Done.");
	}
  
  
  
  /** Template method that calls {@link #processLine(String)}.  */
	public List<String> processLineByLine() throws Exception {
		List<String> fields = new ArrayList<String>();

			Scanner scanner =  new Scanner(fFilePath, ENCODING.name());
			String indx = "";
			int count = 1;
			List<String> list = new ArrayList<String>();
			while (scanner.hasNextLine()){
				if( count > 1)	{	
					list.add(scanner.nextLine());
				}else {
					if(count==1){
						indx = scanner.nextLine().substring(0,10);
					}else {
						scanner.nextLine();
					}
				}
				count ++;				 
			}   

			for (String sp : list){
				if(sp.contains(indx)){
					break;
				}
				fields.add(sp.substring(1));
				System.out.println(sp.substring(1));
			}
		return fields;
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

