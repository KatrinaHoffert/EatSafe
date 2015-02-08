package dataReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.WordUtils;

import au.com.bytecode.opencsv.CSVReader;

public class CSVLoader {

	private Writer writer;
	private char seprator;

	/**
	 * Public constructor to build CSVLoader object with
	 * Writer details. The writer is closed on success
	 * or failure.
	 * @param writer
	 */
	public CSVLoader(Writer writer) {
		this.writer = writer;
		//Set default separator
		this.seprator = ',';
	}
	
	/**
	 * Parse CSV file using OpenCSV library and load in 
	 * given file, with correct location Id and inspection Id. 
	 * @param csvFile Input CSV file
	 * @param location Id for this new location
	 * @param the start of inspection Id for all the inspections
	 * @throws Exception
	 */
	public int loadCSV(String csvFile, int locationId, int inspectionId) throws Exception {

		CSVReader csvReader = null;
		if (null == this.writer) {
			throw new Exception("Not a valid writer.");
		}
		try {
			
			csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-16"), this.seprator);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error occured while executing file. "
					+ e.getMessage());
		}

		
		//get rid of the header row of each file
		String[] headerRow = csvReader.readNext();

		if (null == headerRow) {
			csvReader.close();
			throw new FileNotFoundException(
					"No columns defined in given CSV file." +
					"Please check the CSV file format.");
		}

		String[] nextLine;
		
		// a 2D matrix for input data
		ArrayList<String[]> dataMatrix = new ArrayList<String[]>();
		String str = "";
		
		try {
			Date date = null;
			while ((nextLine = csvReader.readNext()) != null) {

				if (null != nextLine) {
					
					String[] recordLine = new String[10];
					int index = 0;
					
					for (String string : nextLine) {
						date = DateUtil.convertToDate(string); // try to see if the string is a date
						if (null != date) {
							recordLine[index] = date + str;
						} else {
							recordLine[index] = string;
						}
						index++;
					}
					dataMatrix.add(recordLine);
				}
			}
					
			// Insert location
			this.writer.write(String.format("INSERT INTO location(id, name, address, postcode, city, rha)\n"
					+ " VALUES (%d, \'%s\', \'%s\', \'%s\', \'%s\', \'%s\');\n\n", 
					locationId, WordUtils.capitalizeFully(dataMatrix.get(0)[1]).replaceAll("'","''"), // location ID in unique and increased by one, initial cap the location name
					dataMatrix.get(0)[3], dataMatrix.get(0)[5].substring(dataMatrix.get(0)[5].length() - 7), //get the postcode, which is the last 7 char
					WordUtils.capitalizeFully(dataMatrix.get(0)[5].substring(0, dataMatrix.get(0)[5].lastIndexOf(','))),  // get the city name, which is before the comma
					dataMatrix.get(0)[7]));
			
			for (int i = 0; i < dataMatrix.size(); i ++) {
				
				if (i == 0 || !(dataMatrix.get(i)[2].equals(dataMatrix.get(i-1)[2]))) {
					
					//Insert inspection
					this.writer.write(String.format("INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)\n"
							+ " VALUES (%d, %d, \'%s\', \'%s\', \'%s\');\n\n", 
							inspectionId, locationId, dataMatrix.get(i)[2], dataMatrix.get(i)[4], dataMatrix.get(i)[6]));
					inspectionId ++;// after insert the new inspection, should increase the inspectionId by one, to guarantee the uniqueness
					if (!dataMatrix.get(i)[9].equals("")) {
						//Insert violation
						this.writer.write(String.format("INSERT INTO violation(inspection_id, violation_id)\n"
								+ " VALUES (%d, %s);\n\n", 
								inspectionId - 1, dataMatrix.get(i)[9].substring(0, dataMatrix.get(i)[9].lastIndexOf('-') - 1)));
					}
					
				} else {//a violation for the same inspection
					if (!dataMatrix.get(i)[9].equals("")) {
						//Insert violation
						this.writer.write(String.format("INSERT INTO violation(inspection_id, violation_id)\n"
								+ " VALUES (%d, %s);\n\n", 
								inspectionId - 1, dataMatrix.get(i)[9].substring(0, dataMatrix.get(i)[9].lastIndexOf('-') - 1)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error occured while loading data from file to files."
							+ e.getMessage());
		} finally {
			csvReader.close();
		}
		return inspectionId;
	}

	public char getSeprator() {
		return seprator;
	}

	public void setSeprator(char seprator) {
		this.seprator = seprator;
	}

}
