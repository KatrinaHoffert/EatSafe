package dataReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

public class CSVLoader {

	private static final 
		String SQL_INSERT = "INSERT INTO ${table}(${keys}) VALUES(${values})";
	private static final String TABLE_REGEX = "\\$\\{table\\}";
	private static final String KEYS_REGEX = "\\$\\{keys\\}";
	private static final String VALUES_REGEX = "\\$\\{values\\}";

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
		if(null == this.writer) {
			throw new Exception("Not a valid writer.");
		}
		try {
			
			csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-16"), this.seprator);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error occured while executing file. "
					+ e.getMessage());
		}

		String[] headerRow = csvReader.readNext();

		if (null == headerRow) {
			csvReader.close();
			throw new FileNotFoundException(
					"No columns defined in given CSV file." +
					"Please check the CSV file format.");
		}

		String questionmarks = StringUtils.repeat("?,", headerRow.length);
		questionmarks = (String) questionmarks.subSequence(0, questionmarks
				.length() - 1);

		String query = SQL_INSERT.replaceFirst(TABLE_REGEX, "location"); // table name needed
		query = query
				.replaceFirst(KEYS_REGEX, StringUtils.join(headerRow, ","));
		query = query.replaceFirst(VALUES_REGEX, questionmarks);

		System.out.println("Query: " + query);

		String[] nextLine;
		Connection con = null;
		PreparedStatement ps = null;
		
		
		try {
			ps = con.prepareStatement(query);

			final int batchSize = 1000;
			int count = 0;
			Date date = null;
			while ((nextLine = csvReader.readNext()) != null) {

				if (null != nextLine) {
					int index = 1;
					for (String string : nextLine) {
						date = DateUtil.convertToDate(string);
						if (null != date) {
							ps.setDate(index++, new java.sql.Date(date
									.getTime()));
						} else {
							ps.setString(index++, string);
						}
					}
					ps.addBatch();
				}
				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
			}
			ps.executeBatch(); // insert remaining records
			this.writer.write(ps.toString() + "\n");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error occured while loading data from file to files."
							+ e.getMessage() + ps.toString());
		} finally {
			if (null != ps)
				ps.close();
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
