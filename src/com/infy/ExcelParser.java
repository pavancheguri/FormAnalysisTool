package com.infy;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {
	private final String filePath;
	private InputStream inputStream = null;
	private XSSFWorkbook  workbook =null;
	public ExcelParser(String fileName) {
		this.filePath = fileName;
	}

	public static void main(String[] args) throws Exception {
		ExcelParser parser = new ExcelParser("C:\\Temp\\FormsList.xlsx");
		parser.processLineByLine();
		log("Done.");

	}

	/** Template method that calls {@link #processLine(String)}.  */
	public List<EDLFile> processLineByLine() throws Exception {

		inputStream= new FileInputStream(filePath);
		workbook= new XSSFWorkbook(inputStream);
		XSSFSheet  sheet = workbook.getSheetAt(0);

		EDLFile edlfile = null;
		List<EDLFile> edlfiles = new LinkedList<>();

		for (int j=1; j< sheet.getLastRowNum() + 1; j++) {
			edlfile = new EDLFile();
			Row row = sheet.getRow(j);
			edlfile.setRowNum(j);
			edlfile.setMember(row.getCell(0).toString().substring(0, row.getCell(0).toString().length() - 3));//Member Name i.e, File Name
			edlfile.setDescription(row.getCell(1).getStringCellValue());
			edlfile.setDtn((int)(row.getCell(2).getNumericCellValue()));
			edlfile.setAssigned(row.getCell(8)!=null?row.getCell(8).toString():"");
			edlfile.setStatus(row.getCell(9)!=null?row.getCell(9).toString():"");
			edlfiles.add(edlfile);
			System.out.println(edlfile.getMember());
		}
		
		return edlfiles;
	}
	
	public Map<String,String> getTagPositions(){
		XSSFSheet  sheet1 = workbook.getSheetAt(1);
		Map<String,String> tagPosMap = new HashMap<String,String>();
		for (int j=1; j< sheet1.getLastRowNum() + 1; j++) {
			Row row = sheet1.getRow(j);
			tagPosMap.put(row.getCell(0).getStringCellValue(), String.valueOf((int)row.getCell(1).getNumericCellValue()));
		}
		return tagPosMap;
	}


	private static void log(Object aObject){
		 System.out.println(String.valueOf(aObject));
	}

}
