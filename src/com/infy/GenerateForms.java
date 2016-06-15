package com.infy;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class GenerateForms {

	public static void main(String[] args) {
		List<String> filesList = new LinkedList<String>();
		List<String> propsList = new LinkedList<String>();
		Scanner scanner;
		try {
			scanner = new Scanner(Paths.get("C:\\Temp\\Generate\\List for Forms Dumped.txt"),StandardCharsets.UTF_8.name());
			while (scanner.hasNextLine()){
				filesList.add(scanner.nextLine().substring(1));
			} 

			scanner =  new Scanner(Paths.get("C:\\Temp\\Generate\\ALLPROPFORMS_trim.txt"),StandardCharsets.UTF_8.name());
			while (scanner.hasNextLine()){
				propsList.add(scanner.nextLine().trim());
			} 
			System.out.println(propsList.size());
			System.out.println(filesList.size());

			StringBuffer sb = new StringBuffer();
			List<String> properties = new LinkedList<String>();
			for(String props:propsList){
				if((props.contains("%%%DMGINDXBEG%%%")	|| props.contains("%%%DMG2NDXBEG%%%"))){
					if(sb.length()>0) {
						properties.add(sb.toString());
						sb=new StringBuffer();
					}
					sb.append(props);
					sb.append(System.getProperty("line.separator"));

				}else{
					sb.append(props);
					sb.append(System.getProperty("line.separator"));
				}
			}
			properties.add(sb.toString());

			for(int i=0;i<filesList.size();i++){
				PrintWriter  buf=new PrintWriter (
						new FileWriter ("C:/Temp/Generate/" + filesList.get(i) + ".txt")  );
				for(int j=0;j<properties.size();j++){
					if(i==j){
						buf.write(properties.get(j));
						buf.flush();
						buf.close();
					}
				}
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
