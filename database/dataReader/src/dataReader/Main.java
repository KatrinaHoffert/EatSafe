package dataReader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
	private static final String TABLE_NAME = "importData";
	private static final String FOLDER_PATH = "/Users/Doris/Downloads";
	private static final String PASSWORD = "";
	private static final String USER_NAME = "Doris";
	// Both MySQL and PostgreSQL JDBC drivers are included.
	//private static String JDBC_CONNECTION_URL = "jdbc:mysql://edjo.usask.ca/cmpt370_group09";
	private static String JDBC_CONNECTION_URL = "jdbc:postgresql://localhost/project371";
	
	public static void main(String[] args) {
		try {
			//the folder location of all csv files, the connection to DB and the table name
			readFolder(FOLDER_PATH,getConnection(), TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(JDBC_CONNECTION_URL,USER_NAME,PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}
	
	//Read a folder of CSV files, with same start 
	public static void readFolder(String folderPath, Connection connection, String tableName) {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles == null){
			System.err.print("NO FILES IN THIS FOLDER!");
		}
		for (int i = 0; i < listOfFiles.length; i++){
			File file = listOfFiles[i];
			if(file.isFile() && file.getName().startsWith("FoodInspectionReport")){
				try {
					CSVLoader loader = new CSVLoader(connection);
					loader.loadCSV(folderPath + "/" + file.getName(), tableName, false);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
