package dataReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Main {
	
	private static final String FOLDER_PATH = "/Users/Doris/Downloads/InspectionReport"; // path of inspection report folder
	private static final String FILE_NAME = "statements.sql"; // output file's name
	
	public static void main(String[] args) {
		try {
			//the folder location of all csv files, and open a writer stream for output
			readFolder(FOLDER_PATH,getWriterStream(FILE_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * open a new write stream to a file
	 * @param filename
	 */
	public static Writer getWriterStream(String fileName) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return writer;
	}
	
	
	/**
	 * write comments at the beginning of the file
	 * @param writer
	 */
	public static void writeComment(Writer writer){
		try {
			writer.write("-- EatSafe\n"
					+ "-- This file will populate the database\n"
					+ "-- Before run this file, run CreateTables.sql first\n"
					+ "\n\n\n"
					+ "-- First populate the violation_type table\n"
					+ "\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * populate the violation_type table first
	 * @param writer
	 */
	public static void insertViolationType(Writer writer){
		try {
			writer.write("INSERT INTO violation_type (id, description,  priority)\n"
					+ "VALUES (1, \'Refrigeration/Cooling/Thawing (must be 4°C/40°F or lower)\', \'Critical Item\'),\n"
					+ "(2, \'Cooking/Reheating/Hot Holding (must be 60°C/140°F or higher)\', \'Critical Item\'),\n"
					+ "(3, \'Storage/Preparation of Foods\', \'Critical Item\'),\n"
					+ "(4, \'Hand Washing Facilities/Practices\', \'Critical Item\'),\n"
					+ "(5, \'Food Handler Illness/Hygiene/Habits\', \'Critical Item\'),\n"
					+ "(7, \'Cleaning/Sanitizing of Equipment/Utensils\', \'Critical Item\'),\n"
					+ "(10, \'Food Protection\', \'General Item\'),\n"
					+ "(11, \'Accurate Thermometer Available to Monitor Food Temperatures\', \'General Item\'),\n"
					+ "(12, \'Construction/Storage/Cleaning of Equipment/Utensils\', \'General Item\'),\n"
					+ "(14, \'Insect/Rodent Control\', \'General Item\'),\n"
					+ "(15, \'Construction/Maintenance and/or Cleaning of Premises\', \'General Item\'),\n"
					+ "(16, \'Plumbing/Lighting/Ventilation\', \'General Item\');\n\n\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * read a folder of files, with the same start of file name; generate the unique location Id and inspection Id
	 * close the writer at the end
	 * @param folderPath
	 * @param writer
	 */
	public static void readFolder(String folderPath, Writer writer) {
		writeComment(writer);
		insertViolationType(writer);
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles == null){
			System.err.print("NO FILES IN THIS FOLDER!");
		}
		
		int locationId = 1;
		int inspectionId = 1;
		
		for (int i = 0; i < listOfFiles.length; i++){
			File file = listOfFiles[i];
			if(file.isFile() && file.getName().startsWith("FoodInspectionReport")){
				try {
					CSVLoader loader = new CSVLoader(writer);
					inspectionId = loader.loadCSV(folderPath + "/" + file.getName(), locationId, inspectionId);
					locationId ++;	//increase the location ID for the next location
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Location #: " + (locationId -1) + "\nInspection #: " + (inspectionId - 1)); // report the total numbers
	}
}
