package com.infy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Assumes UTF-8 encoding. JDK 7+. */
public class RLBSParser {

	// PRIVATE 
	  private final Path fFilePath;
	  private final static Charset ENCODING = StandardCharsets.UTF_8; 
	  
	  public RLBSParser(String aFileName){
		    fFilePath = Paths.get("C:\\Temp\\"+aFileName);
	  }
	  
	public static void main(String... aArgs) throws IOException {
	  RLBSParser parser = new RLBSParser("RLBS.txt");
	  parser.processLineByLine();
	  log("Done.");
	}
  
  
  
  /** Template method that calls {@link #processLine(String)}.  */
	public Set<RLBS> processLineByLine() throws IOException {
		Set<RLBS> rlbsList= new HashSet<RLBS>();

		try {
			Scanner scanner =  new Scanner(fFilePath, ENCODING.name());
			
			int count = 1;
			List<String> list = new ArrayList<String>();
			while (scanner.hasNextLine()){
				if( count > 8)	{	
					list.add(scanner.nextLine());
				}else {
					scanner.nextLine();
				}
				count ++;				 
			}   
			
			RLBS rlbs = null;
			Set<String> rules = null;
			int found =0;
			boolean flag =false;
			for (String sp : list){
				
				if(sp.contains("ADD STRUCTURE NAME=")){
					rlbs = new RLBS();
					rules = (Set<String>) new HashSet<String>();
					rlbs.setName(sp.substring(19));
					System.out.println(sp.substring(19));
					flag = true;
				}else if( flag && sp.contains("STRUCTURE RULE=")){
					System.out.println(sp);
					flag=true;
				}else if ( flag && !sp.trim().equals("-")){
					rules.add(sp.substring(2));
					System.out.println(sp.substring(2));
				}else{
					if( rlbs != null && rules !=null ){
						rlbs.setRules(rules);;
						rlbsList.add(rlbs);
					}
					flag= false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return rlbsList;
	}
  
  
   
  
  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }
  
  private String quote(String aText){
    String QUOTE = "'";
    return QUOTE + aText + QUOTE;
  }
} 

