package databaseParser

import java.io.File
import java.io.Writer
import java.util.ArrayList
import java.util.Date
import java.text.SimpleDateFormat
import org.apache.commons.lang3.text.WordUtils
import java.io.InputStreamReader
import java.io.FileInputStream
import au.com.bytecode.opencsv.CSVReader
import scala.collection.JavaConversions._
import java.nio.charset.StandardCharsets

/**
 * Loads and interprets a CSV file, creating SQL ouput that can be run in the Postgres console.
 * @param writer The writer to write the SQL output to.
 */
class CSVLoader(writer: Writer) {
  val separator = ','

  // These string builders store the tab delimited content that will be used to create the COPY
  // input. It's a very simple, tab delimited format. As long as we insert them in the order that
  // is shown here, there's no foreign key violations.
  var tabDelimitedLocations = scala.collection.mutable.StringBuilder
  var tabDelimitedInspections = scala.collection.mutable.StringBuilder
  var tabDelimitedViolations = scala.collection.mutable.StringBuilder

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
   * will use "Unknown" as a placeholder.
   * @param csvFile Path to the CSV file to load.
   * @param locationId The ID to use for the location.
   * @param inspectionIdIn The first inspection ID to use.
   * @return The next inspection ID that should be used.
   */
  def loadCSV(csvFile: String, locationId: Int, inspectionIdIn: Int): Int = {
    var inspectionId = inspectionIdIn

    val allLines = new CSVReader(new InputStreamReader(new FileInputStream(csvFile),
        StandardCharsets.UTF_16)).readAll.drop(1)

    // Read this into our representation (see documentation of InternalCsvFormat for the CSV format)
    // For some reason, this goddamn CSV is so bad that libraries think it has extra rows.
    val allRows: Seq[InternalCsvFormat] = allLines.map {
      row =>
        InternalCsvFormat(row(1), row(2), row(3), row(4), row(5), row(6), row(7), row(9))
    }

    // Fix capitalization and escape single quotes
    var locationName = WordUtils.capitalizeFully(allRows(0).locationName)
    var locationAddress = WordUtils.capitalizeFully(getIfExists(allRows(0).locationAddress))
    var locationPostcode = getPostcode(allRows(0).locationCityAndPostalCode)
    var locationCity = getCity(allRows(0).locationCityAndPostalCode)
    var locationRha = getIfExists(allRows(0).locationRha)

    // Must remove apostrophes from the places that can have them
    if(locationName != null) locationName = locationName.replaceAll("'","''")
    if(locationAddress != null) locationAddress = locationAddress.replaceAll("'","''")
    if(locationCity != null) locationCity = locationCity.replaceAll("'","''")

    // Now we need to add the quotes around the address, city, and postal code iff they aren't null
    // The other values can't be null, so are quoted in the SQL below
    if(locationAddress != null) locationAddress = "'" + locationAddress + "'"
    if(locationCity != null) locationCity = "'" + locationCity + "'"
    if(locationPostcode != null) locationPostcode = "'" + locationPostcode + "'"

    // Insert location
    writer.write("INSERT INTO location(id, name, address, postcode, city, rha)\n" +
        " VALUES (%d, \'%s\', %s, %s, %s, \'%s\');\n\n".format(locationId, locationName,
        locationAddress, locationPostcode, locationCity, locationRha))

    //this last ID is used because: their reports have duplicated records: a same violation under same inspection
    //see "Regina Qu'Appelle_Pilot Butte_Pilot Butte Recreation Hall Kitchen [Pilot But...].csv"
    val lastViolationId = new ArrayList[Integer]

    for(i <- 0 until allRows.size) {
      // Test if this is a new inspection; if yes, insert; if no, just insert violations
      if(i == 0 || !(allRows(i).inspectionDate == (allRows(i - 1).inspectionDate))) {
        val inspectionDate = getIfExists(allRows(i).inspectionDate);
        val inspectionType = getIfExists(allRows(i).inspectionType);
        val reinspectionPriority = getIfExists(allRows(i).reinspectionPriority);

        // Insert inspection
        writer.write("INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)\n" +
            " VALUES (%d, %d, \'%s\', \'%s\', \'%s\');\n\n".format(inspectionId, locationId,
            inspectionDate, inspectionType, reinspectionPriority))
        inspectionId += 1

        if(allRows(i).violation != "") {
          // Insert violation
          val violationId = getViolationId(allRows(i).violation)
          lastViolationId.clear()
          lastViolationId.add(violationId)
          writer.write("INSERT INTO violation(inspection_id, violation_id)\n" +
              " VALUES (%d, %d);\n\n".format(inspectionId - 1, violationId))
        }
      }
      else if(allRows(i).violation != "") {
        // A violation for the same inspection
        val violationId = getViolationId(allRows(i).violation)
        if(!lastViolationId.contains(violationId)) {
          lastViolationId.add(violationId)
          writer.write("INSERT INTO violation(inspection_id, violation_id)\n" +
              " VALUES (%d, %d);\n\n".format(inspectionId - 1, violationId))
        }
      }
    }

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
    val tempID = string.substring(0, string.lastIndexOf('-') - 1).trim
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
    if(string == "") {
      null
    }
    else if(string.matches("^.+, .+$")) {
      WordUtils.capitalizeFully(string.substring(0, string.lastIndexOf(',')))
    }
    else if(string.matches(", .+$")) {
      //some files have this column as ", SASKATCHEWAN" 
      //(see 'Saskatoon_Other Locations_Leaning Maple Meats - Catering [MCKILLOP].csv')
      //need to return Unknown for this case 
      null
    }
    else {
      //if not match the above regexs, then it only contains the city name
      string;
    }
  }

  /**
   * Gets the postcode of a location, which is the last 7 characters of the string.
   * @param string Full 
   * @return string of postcode, or "Unknown" if the pattern is not matched
   */
  def getPostcode(string: String): String = {
    if(string == "" || string.length < 7) {
      null
    }
    else if(string.substring(string.length - 7).matches("^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} \\d{1}[A-Z]{1}\\d{1}$")) {
      string.substring(string.length - 7)
    }
    else {
      null
    }
  }

  /**
   * Tests if the string is empty, returning a placeholder if it is or the original data otherwise.
   * @param string The full column from the CSV file.
   * @return "Unknown" if contains nothing or the string itself otherwise.
   */
  def getIfExists(string: String): String = if(string == "") null else string
}

/**
 * A more readable, internal representation of the CSV file, with unused fields removed. The columns
 * of the CSV file are:
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
 * We skip the 0th column and 8th column, with everything else in this class represented in the order
 * they appear in the CSV.
 */
case class InternalCsvFormat(
  locationName: String,
  inspectionDate: String,
  locationAddress: String,
  inspectionType: String,
  locationCityAndPostalCode: String,
  reinspectionPriority: String,
  locationRha: String,
  violation: String
)
