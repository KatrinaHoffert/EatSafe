package dataReader

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.Writer
import java.util.ArrayList
import java.util.Date
import java.util.List
import org.apache.commons.lang3.text.WordUtils
import au.com.bytecode.opencsv.CSVReader

class CSVLoader(writer: Writer) {
  val separator = ','

  /**
   * Parse CSV file using OpenCSV library and load in
   * given file, with correct location Id and inspection Id.
   * @param csvFile Input CSV file
   * @param location Id for this new location
   * @param the start of inspection Id for all the inspections
   */
  def loadCSV(csvFile: String, locationId: Int, inspectionIdIn: Int): Int = {
    var inspectionId = inspectionIdIn
    val csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile),
        "UTF-16"), separator)

    // Get rid of the header row (don't need it)
    val headerRow = csvReader.readNext();

    if (headerRow == null) {
      csvReader.close()
      throw new FileNotFoundException("No columns defined in given CSV file: " + csvFile +
          "\nPlease check the CSV file format.")
    }

    var nextLine = csvReader.readNext()
    val dataMatrix = new scala.collection.mutable.ArrayBuffer[Array[String]]

    // Read all rows into data matrix
    while(nextLine != null) {
      if(nextLine != null) {
        val recordLine = new Array[String](10)
        var index = 0;

        for (string <- nextLine) {
          val date = DateUtil.convertToDate(string) match {
            case Some(date) =>
              recordLine(index) = date.toString
            case None =>
              recordLine(index) = string
          }

          index += 1
        }

        dataMatrix.append(recordLine)
      }

      nextLine = csvReader.readNext()
    }


    // Fix capitalization and escape single quotes
    val locationName = WordUtils.capitalizeFully(dataMatrix(0)(1)).replaceAll("'","''")
    val locationAddress = WordUtils.capitalizeFully(testNull(dataMatrix(0)(3)).replaceAll("'","''"))
    val locationPostcode = getPostcode(dataMatrix(0)(5))
    val locationCity = getCity(dataMatrix(0)(5)).replaceAll("'","''")
    val locationRha = testNull(dataMatrix(0)(7))

    // Insert location
    writer.write("INSERT INTO location(id, name, address, postcode, city, rha)\n" +
        " VALUES (%d, \'%s\', \'%s\', \'%s\', \'%s\', \'%s\');\n\n".format(locationId, locationName,
        locationAddress, locationPostcode, locationCity, locationRha))

    //this last ID is used because: their reports have duplicated records: a same violation under same inspection
    //see "Regina Qu'Appelle_Pilot Butte_Pilot Butte Recreation Hall Kitchen [Pilot But...].csv"
    val lastViolationId = new ArrayList[Integer]

    for(i <- 0 until dataMatrix.size) {
      // Test if this is a new inspection; if yes, insert; if no, just insert violations
      if(i == 0 || !(dataMatrix(i)(2) == (dataMatrix(i - 1)(2)))) {
        val inspectionDate = testNull(dataMatrix(i)(2));
        val inspectionType = testNull(dataMatrix(i)(4));
        val reinspectionPriority = testNull(dataMatrix(i)(6));

        // Insert inspection
        writer.write("INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)\n" +
            " VALUES (%d, %d, \'%s\', \'%s\', \'%s\');\n\n".format(inspectionId, locationId,
            inspectionDate, inspectionType, reinspectionPriority))
        inspectionId += 1

        if(dataMatrix(i)(9) != "") {
          // Insert violation
          val violationId = getViolationId(dataMatrix(i)(9))
          lastViolationId.clear()
          lastViolationId.add(violationId)
          writer.write("INSERT INTO violation(inspection_id, violation_id)\n" +
              " VALUES (%d, %d);\n\n".format(inspectionId - 1, violationId))
        }
      }
      else if(dataMatrix(i)(9) != "") {
        // A violation for the same inspection
        val violationId = getViolationId(dataMatrix(i)(9))
        if(!lastViolationId.contains(violationId)) {
          lastViolationId.add(violationId)
          writer.write("INSERT INTO violation(inspection_id, violation_id)\n" +
              " VALUES (%d, %d);\n\n".format(inspectionId - 1, violationId))
        }
      }
    }

    csvReader.close()
    inspectionId
  }

  /**
   * get the violation ID, which is a integer
   * For some reason, their reports don't have violation 9, but have violation 8a and 8b.
   * To maintain consistency(also the database store ID as integer), will treat 8a as 8, 8b as 9,
   * @param string
   * @return integer of violation ID
   */
  def getViolationId(string: String): Int = {
    val tempID = string.substring(0, string.lastIndexOf('-') - 1);
    if(tempID.equals("8a")) {
      return 8;
    }
    else if(tempID.equals("8b")) {
      return 9;
    }
    else {
      return Integer.parseInt(tempID);
    }
  }

  /**
   * get the city name, which is before the comma
   * @param string
   * @return string of city, or "DATA MISSING" if the pattern is not match
   */
  def getCity(string: String): String = {
    if(string.equals("") || string.length() < 7) {
      return "DATA MISSING";
    }
    else if(string.matches("^.+, .+$")) {
      return WordUtils.capitalizeFully(string.substring(0, string.lastIndexOf(',')));
    }
    else {
      return "DATA MISSING";
    }
  }

  /**
   * get the postcode of a location, which is the last 7 characters of the string
   * @param string
   * @return string of postcode, or "DATA MISSING" if the pattern is not match
   */
  def getPostcode(string: String): String = {
    if(string.equals("") || string.length() < 7) {
      return "DATA MISSING";
    }
    else if(string.substring(string.length() - 7).matches("^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} \\d{1}[A-Z]{1}\\d{1}$")) {
      return string.substring(string.length() - 7);
    }
    else {
      return "DATA MISSING";
    }
  }

  /**
   * test if the string contains nothing
   * @param string
   * @return "DATA MISSING" if contains nothing, or the string itself otherwise
   */
  def testNull(string: String): String = {
    if(string.equals("")) {
      return "DATA MISSING";
    }else {
      return string;
    }
  }
}
