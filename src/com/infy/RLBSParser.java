package com.infy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
	  for(RLBS rlbs: parser.processLineByLine()){
		System.out.println(rlbs.getName());  
	  }
	  log("Done.");
	}
  
  
  
  /** Template method that calls {@link #processLine(String)}.  */
	public Set<RLBS> processLineByLine() throws IOException {
		Set<RLBS> rlbsList= new LinkedHashSet<RLBS>();

		try {
			Scanner scanner =  new Scanner(fFilePath, ENCODING.name()).useDelimiter("\\Z");
			String content = new Scanner(fFilePath).useDelimiter("\\Z").next();
			int count = 1;
			List<String> list = new ArrayList<String>();
			StringBuffer sb = new StringBuffer();
			//System.out.println(content);
			while (scanner.hasNextLine()){
				if( count > 8)	{
					String s = scanner.nextLine();
					list.add(s.substring(0, s.length()-8));
					//System.out.println(s);
					//sb.append(s);
					//System.out.println(sb.toString());
				}else {
					String s = scanner.nextLine();
					//sb.append(s);
				}
				
				count ++;				 
			}   
//			(?<=ADD\\s+STRUCTURE\\s+NAME=).*?(?=\) 
			//System.out.println(Pattern.quote("STRUCTURE NAME=") );
			/*String regexString = "(?s)(ADD\\s+STRUCTURE\\sNAME=)(.*?)\\s";
			 Pattern pattern = Pattern.compile(regexString);
		        Matcher matcher = pattern.matcher(content);
		        System.out.println(matcher.find()+"----"+matcher.groupCount());
		        if(matcher.find()){
		        		//System.out.println(matcher.group(0));
		        		//System.out.println(matcher.group(1));
		        }*/
			
			RLBS rlbs = null;
			Set<String> rules = null;
			int found =0;
			boolean flag =false;
			for (String sp : list){
				
				if(sp.trim().contains("ADD    STRUCTURE NAME=")){
					rlbs = new RLBS();
					rules = (Set<String>) new LinkedHashSet<String>();
					String[] names = sp.split("=");
					rlbs.setName(names[1].substring(0,names[1].lastIndexOf(" ")));
					System.out.println(names[1].substring(0,names[1].lastIndexOf(" ")));
					flag = true;
				}else if( flag && sp.trim().contains("STRUCTURE RULE=( ")){
					//System.out.println(sp.trim());
					
					rules.add(sp.trim().substring(18));
					flag=true;
				}else if ( flag && !sp.trim().equals("-")){
					rules.add(sp.trim().substring(1));
					//System.out.println(sp.trim().substring(2));
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
    //System.out.println(String.valueOf(aObject));
  }
  
  private String quote(String aText){
    String QUOTE = "'";
    return QUOTE + aText + QUOTE;
  }
} 

