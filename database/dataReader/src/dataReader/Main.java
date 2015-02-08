package dataReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Main {
	
	private static final String FOLDER_PATH = "../InspectionReport"; // path of inspection report folder
	private static final String FILE_NAME = "../statements.sql"; // output file's name
	
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
	public static void writeComment(Writer writer) {
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
	public static void insertViolationType(Writer writer) {
		try {
			writer.write("INSERT INTO violation_type (id, description, name,  priority)\n"
					+ "VALUES (1, \'Potentially hazardous foods and perishable foods must be stored at 4oC/40oF or below. Hazardous foods must be thawed in a refrigerator or under cold, running water.\', \'Refrigeration/Cooling/Thawing (must be 4°C/40°F or lower)\', \'Critical Item\'),"
					+ "\n"
					+ "(2, \'Cook foods to an internal temperature of: "
					+ "a) 63oC (145oF) or above for: eggs (if prepared for immediate service); medium rare beef and veal steaks and roasts; "
					+ "b) 68oC (155oF) or above for: game farm meat products; "
					+ "c) 70oC (158oF) for: fish; "
					+ "d) 71oC (160oF) or above for: ground beef/pork/veal; food made with ground beef/pork/veal, e.g. sausages, meatballs; pork chops, ribs and roasts; "
					+ "e) 74oC (165oF) or above for: ground chicken/turkey; food made with ground chicken/turkey or mixtures containing poultry, meat, fish, or eggs; chicken and turkey breasts, legs, thighs and wings; stuffing (inside a carcass); stuffed pasta; hot dogs; leftovers; egg dishes (if not prepared as specified in 2a); and stuffed fish; "
					+ "f) 85oC (185oC) or above for: chicken and turkey, whole bird. "
					+ "Reheat foods rapidly to an internal temperature of 74oC (165oF) prior to serving. "
					+ "Hot Holding must maintain an internal temperature of 60oC (140oF) or higher.\', \'Cooking/Reheating/Hot Holding (must be 60°C/140°F or higher)\', \'Critical Item\'),"
					+ "\n"
					+ "(3, \'Foods must be stored in food grade containers, properly labelled and protected from contamination at all times.\', \'Storage/Preparation of Foods\', \'Critical Item\'),"
					+ "\n"
					+ "(4, \'Hand washing must be properly done at appropriate times and intervals. An accessible, plumbed hand basin with hot and cold running water, soap in a dispenser and single-use paper towels in wall-mounted dispensers are required in food preparation areas."
					+ "Hand washing Procedure: "
					+ "a) Wet hands and exposed arms (at least up to wrist) with warm running water; "
					+ "b) Apply liquid soap; "
					+ "c) Vigorously rub together wet surfaces for at least 20 seconds, lathering at least up to wrist; "
					+ "d) Use a nailbrush under fingernails and other very dirty areas; "
					+ "e) Thoroughly rinse with clean, warm water running from wrists to fingertips; "
					+ "f) Apply soap and lather vigorously again; "
					+ "g) Rinse hands and wrists thoroughly; "
					+ "h) Dry hands with a single-use paper towel; and "
					+ "i) Use paper towel to turn off tap.\', \'Hand Washing Facilities/Practices\', \'Critical Item\'),"
					+ "\n"
					+ "(5, \'Good personal hygiene must be practiced at all times. "
					+ "Food handlers with infectious or contagious diseases (or symptoms) should not work. \', \'Food Handler Illness/Hygiene/Habits\', \'Critical Item\'),"
					+ "\n"
					+ "(7, \' Proper dish washing procedures must be followed. "
					+ "Mechanical washing: dishwashers must be National Sanitation Foundation (NSF) approved or equivalent, designed to wash at 60oC (140oF) and utilize an approved sanitizing agent. "
					+ "Manual washing: (wash/rinse/sanitize in a three-compartment sink): "
					+ "first compartment - clean hot water 44oC (111oF) with detergent; "
					+ "second compartment - clean hot water 44oC (111oF); "
					+ "third compartment - approved sanitizing method.\', \'Cleaning/Sanitizing of Equipment/Utensils\', \'Critical Item\'),"
					+ "\n"
					+ "(10, \'Food must be protected from contamination during storage, preparation, display, service and transport. No food is to be stored on the floor unless it is in an approved container. The lowest shelf is to be high enough to allow easy cleaning of the floor.\', \'Food Protection\', \'General Item\'),"
					+ "\n"
					+ "(11, \'An accurate, metal-stemmed (food-grade) probe thermometer must be available to monitor temperatures of potentially hazardous foods.\', \'Accurate Thermometer Available to Monitor Food Temperatures\', \'General Item\'),"
					+ "\n"
					+ "(12, \'Approved dishwashing facilities must be installed and properly maintained. An adequate supply of cleaning supplies, chemicals, etc. must be available at all times. ''Clean-in-place'' equipment must be washed and sanitized according to manufacturers instructions.\', \'Construction/Storage/Cleaning of Equipment/Utensils\', \'General Item\'),"
					+ "\n"
					+ "(14, \'All restaurants are to be free of vermin.\', \'Insect/Rodent Control\', \'General Item\'),"
					+ "\n"
					+ "(15, \'Floors, walls and ceilings of all rooms in which food is stored, prepared or served or in which dishes, utensils and equipment are washed or stored should be kept clean and in good repair.\', \'Construction/Maintenance and/or Cleaning of Premises\', \'General Item\'),"
					+ "\n"
					+ "(16, \'Approved plumbing must be installed and properly maintained to prevent food contamination.  "
					+ "Light shields or shatterproof bulbs are to be provided in every room in which food is prepared or stored. "
					+ "Unless otherwise approved, every restaurant is to have a ventilation system that prevents the accumulation of odours, smoke, grease/oils and condensation.\', \'Plumbing/Lighting/Ventilation\', \'General Item\');"
					+ "\n\n\n");
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
		if (listOfFiles == null) {
			System.err.print("NO FILES IN THIS FOLDER!");
		}
		
		int locationId = 1;
		int inspectionId = 1;
		
		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().startsWith("FoodInspectionReport")) {
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
