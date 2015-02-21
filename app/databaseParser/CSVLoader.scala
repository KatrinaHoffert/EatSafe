package databaseParser

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.Writer
import java.util.ArrayList
import java.util.Date
import java.util.List
import org.apache.commons.lang3.text.WordUtils
import au.com.bytecode.opencsv.CSVReader

/**
 * Loads and interprets a CSV file, creating SQL ouput that can be run in the Postgres console.
 * @param writer The writer to write the SQL output to.
 */
class CSVLoader(writer: Writer) {
  val separator = ','

  /**
   * Interprets a given CSV file, writing the appropriate SQL queries to the Writer that was
   * passed to this object's constructor. The columns of the CSV file are as follows:
   *
   * 0. Download date (unused).
   * 1. Location name.
   * 2. Inspection date.
   * 3. Location address.
   * 4. Inspection type.
   * 5. Location city and postal code.
   * 6. Reinspection priority.
   * 7. Location regional health authority.
   * 8. Violation type priority (unused).
   * 9. Violation ID and name (only ID is used).
   *
   * Each row of the CSV file represents either an inspection or a violation in an inspection. If
   * there's no violations in an inspection, then we get a single row for the inspection with the
   * violation columns left blank. If there has been violations, then there will be a row (with
   * duplicate information) for each violation in the inspection.
   *
   * Note that there's a few places where fields are left blank in the source CSVs. The output
   * will use "DATA MISSING" as a placeholder.
   * @param csvFile Path to the CSV file to load.
   * @param locationId The ID to use for the location.
   * @param inspectionIdIn The first inspection ID to use.
   * @return The next inspection ID that should be used.
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
   * Get the violation ID, which is a integer uniquely identifying a violation. For some reason, th
   * reports don't have violation 9, but have violation 8a and 8b. To maintain consistency we
   * treat 8a as 8 and 8b as 9,
   * @param string The CSV column for the violation name.
   * @return Integer of violation ID.
   */
  def getViolationId(string: String): Int = {
    val tempID = string.substring(0, string.lastIndexOf('-') - 1)
    if(tempID == "8a") {
      8
    }
    else if(tempID == "8b") {
      9
    }
    else {
      tempID.toInt
    }
  }

  /**
   * Get the city name, which is before the comma in its column of the CSV file.
   * @param string The full CSV column.
   * @return City name, if it exists, or a placeholder if it does not.
   */
  def getCity(string: String): String = {
    if(string == "" || string.length < 7) {
      "DATA MISSING";
    }
    else if(string.matches("^.+, .+$")) {
      WordUtils.capitalizeFully(string.substring(0, string.lastIndexOf(',')))
    }
    else {
      "DATA MISSING";
    }
  }

  /**
   * Gets the postcode of a location, which is the last 7 characters of the string.
   * @param string Full 
   * @return string of postcode, or "DATA MISSING" if the pattern is not matched
   */
  def getPostcode(string: String): String = {
    if(string == "" || string.length < 7) {
      "DATA MISSING"
    }
    else if(string.substring(string.length - 7).matches("^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} \\d{1}[A-Z]{1}\\d{1}$")) {
      string.substring(string.length - 7)
    }
    else {
      "DATA MISSING"
    }
  }

  /**
   * Tests if the string is empty, returning a placeholder if it is or the original data otherwise.
   * @param string The full column from the CSV file.
   * @return "DATA MISSING" if contains nothing or the string itself otherwise.
   */
  def testNull(string: String): String = if(string == "") "DATA MISSING" else string
}
