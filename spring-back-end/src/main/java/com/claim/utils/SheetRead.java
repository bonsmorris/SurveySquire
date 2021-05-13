package com.claim.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.crypto.Data;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.claim.entity.Point;
import com.claim.entity.UploadFiles;
import com.claim.repository.PointRepository;
import com.claim.repository.UploadFilesRepository;
import com.claim.repository.UserRepository;

//import org.apache.poi.ss.usermodel.XSSFWorkbook;
import org.apache.poi.ss.*;

@Component
public class SheetRead {

	@Autowired 
	private UserRepository userRepository;
	@Autowired
	private PointRepository pointRepository;
	@Autowired
	private UploadFilesRepository uploadFilesRepository;

	public void importSheet(MultipartFile file, Long userId) throws IOException {

		// String excelFilePath = "C:\\Users\\Bon Scott\\Desktop\\LATERAL G.xls";
//		FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\Bon Scott\\Desktop\\" + fileName));
		UploadFiles uploadFile = new UploadFiles();
		uploadFile.setFileName(file.getOriginalFilename());
		uploadFile.setUser(userRepository.findById(userId).get());
		UploadFiles savedFile = uploadFilesRepository.save(uploadFile);
		
		InputStream inputStream = file.getInputStream();
		
		ArrayList<String> strings = new ArrayList<String>();

		Workbook workbook = new HSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();
		ArrayList<Point> points = new ArrayList<Point>();
		while (iterator.hasNext()) {
			Point point = new Point();
			point.setFile(savedFile);
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			int x = 0;
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				if (x == 0) {
					point.setPointName(getValue(cell));
				} else if (x == 1) {
					point.setLatitude(getValue(cell));
				} else if (x == 2) {
					point.setLongitude(getValue(cell));
				} else if (x == 3) {
					point.setElevation(getValue(cell));
				} else if (x == 4) {
					point.setCode(getValue(cell));
				} else if (x == 5) {
					point.setAttributes(getValue(cell));
				}
				x++;
			}

			points.add(point);
		}

		pointRepository.saveAll(points);
		workbook.close();
		inputStream.close();
	}

	private String getValue(Cell cell) {
		String value = "";
		switch (cell.getCellType()) {
		case STRING:
			value = cell.getStringCellValue();
			break;
		case BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case NUMERIC:
			value = String.valueOf(cell.getNumericCellValue());
			break;
		}
		return value;
	}

	private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
		Workbook workbook = null;

		if (excelFilePath.endsWith("xlsx")) {
			workbook = new HSSFWorkbook(inputStream);
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}

		return workbook;
	}

//	String dataFolder; //link to folder//
//		
//	//format excel data as string//
//	//DataFormat dataFormatter = new DataFormat("");
//	
//	//Read excel file into input stream
//	try(InputStream inp = new FileInputStream(dataFolder + "example.xlsx")) {
//		//create workbook from input stream
//		Workbook wb = WorkbookFactory.create(inp);
//		
//	for (Sheet s : wb) {
//		System.out.println(s.getSheetName());
//	}
//	
//	//read content of first sheet
//	Sheet sheet = wb.getSheetAt(0);
//	
//	//loop through rows of this sheet
//	for (Row rw : sheet) {
//		for (Cell cell : rw) {
//			
//		}
//	}
//	}
}
