package dataReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
					"No columns defined in given CSV file: " + csvFile + 
					"\nPlease check the CSV file format. Continue...\n");
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
					
			
			String locationName = WordUtils.capitalizeFully(dataMatrix.get(0)[1]).replaceAll("'","''"); //initial cap the location name, replace the single quote with two single quotes
			String locationAddress = testNull(dataMatrix.get(0)[3]).replaceAll("'","''");//address may contain single quote
			String locationPostcode = getPostcode(dataMatrix.get(0)[5]);
			String locationCity = getCity(dataMatrix.get(0)[5]).replaceAll("'","''");//city name may contain single quote
			String locationRHA = testNull(dataMatrix.get(0)[7]);
			
			// Insert location
			this.writer.write(String.format("INSERT INTO location(id, name, address, postcode, city, rha)\n"
					+ " VALUES (%d, \'%s\', \'%s\', \'%s\', \'%s\', \'%s\');\n\n", 
					locationId, locationName, locationAddress, locationPostcode, locationCity, locationRHA));
			// location ID in unique and increased by one
			
			//this last ID is used because: their reports have duplicated records: a same violation under same inspection
			//see "Regina Qu'Appelle_Pilot Butte_Pilot Butte Recreation Hall Kitchen [Pilot But...].csv"
			List<Integer> lastViolationId = new ArrayList<Integer>();
			
			for (int i = 0; i < dataMatrix.size(); i ++) {
				
				if (i == 0 || !(dataMatrix.get(i)[2].equals(dataMatrix.get(i-1)[2]))) {//test if this is a new inspection; if yes, insert; if no, just insert violations 
					
					String inspectionDate = testNull(dataMatrix.get(i)[2]);
					String inspectionType = testNull(dataMatrix.get(i)[4]);
					String reinspectionPriority = testNull(dataMatrix.get(i)[6]);
					
					//Insert inspection
					this.writer.write(String.format("INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)\n"
							+ " VALUES (%d, %d, \'%s\', \'%s\', \'%s\');\n\n", 
							inspectionId, locationId, inspectionDate, inspectionType, reinspectionPriority));
					inspectionId ++;// after insert the new inspection, should increase the inspectionId by one, to guarantee the uniqueness
					
					if (!dataMatrix.get(i)[9].equals("")) {
						//Insert violation
						int violationId = getViolationId(dataMatrix.get(i)[9]);
						lastViolationId.clear();;
						lastViolationId.add(violationId);
						this.writer.write(String.format("INSERT INTO violation(inspection_id, violation_id)\n"
								+ " VALUES (%d, %d);\n\n", 
								inspectionId - 1, violationId));
					}
					
				} else {//a violation for the same inspection
					if (!dataMatrix.get(i)[9].equals("")) {
						//Insert violation
						int violationId = getViolationId(dataMatrix.get(i)[9]);
						if(!lastViolationId.contains(violationId)) {
							lastViolationId.add(violationId);
							this.writer.write(String.format("INSERT INTO violation(inspection_id, violation_id)\n"
									+ " VALUES (%d, %d);\n\n", 
									inspectionId - 1, violationId));
						}
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

	/**
	 * get the violation ID, which is a integer
	 * For some reason, their reports don't have violation 9, but have violation 8a and 8b.
	 * To maintain consistency(also the database store ID as integer), will treat 8a as 8, 8b as 9, 
	 * @param string
	 * @return integer of violation ID
	 */
	private int getViolationId(String string) {
		String tempID = string.substring(0, string.lastIndexOf('-') - 1);
		if(tempID.equals("8a")) {
			return 8;
		}else if(tempID.equals("8b")) {
			return 9;
		}else {
			return Integer.parseInt(tempID);
		}
	}

	/**
	 * get the city name, which is before the comma
	 * @param string
	 * @return string of city, or "DATA MISSING" if the pattern is not match
	 */
	private String getCity(String string) {
		if(string.equals("") || string.length() < 7) {
			return "DATA MISSING";
		}else if(string.matches("^.+, .+ {5}[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} \\d{1}[A-Z]{1}\\d{1}$")) {
			return WordUtils.capitalizeFully(string.substring(0, string.lastIndexOf(',')));
		}else {
			return "DATA MISSING";
		}
	}

	/**
	 * get the postcode of a location, which is the last 7 characters of the string
	 * @param string
	 * @return string of postcode, or "DATA MISSING" if the pattern is not match
	 */
	private String getPostcode(String string) {
		if(string.equals("") || string.length() < 7) {
			return "DATA MISSING";
		}else if(string.substring(string.length() - 7).matches("^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} \\d{1}[A-Z]{1}\\d{1}$")) {
			return string.substring(string.length() - 7);
		}else {
			return "DATA MISSING";
		}
	}

	/**
	 * test if the string contains nothing
	 * @param string
	 * @return "DATA MISSING" if contains nothing, or the string itself otherwise
	 */
	private String testNull(String string) {
		if(string.equals("")) {
			return "DATA MISSING";
		}else {
			return string;
		}
	}

	public char getSeprator() {
		return seprator;
	}

	public void setSeprator(char seprator) {
		this.seprator = seprator;
	}

}
