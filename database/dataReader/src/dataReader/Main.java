package dataReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Main {
	
	private static final String FOLDER_PATH = "/Users/Doris/Downloads/InspectionReport";
	private static final String FILE_NAME = "sql.txt";
	
	public static void main(String[] args) {
		try {
			//the folder location of all csv files, and open a writer stream for output
			readFolder(FOLDER_PATH,getWriterStream(FILE_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Writer getWriterStream(String fileName) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("sql.txt"), "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return writer;
	}
	
	//Read a folder of CSV files, with same start 
	public static void readFolder(String folderPath, Writer writer) {
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
					locationId ++;
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
		System.out.println("Location #: " + (locationId -1) + "\nInspection #: " + (inspectionId - 1));
	}
}
