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
  // Used in Postgres's COPY syntax to symbolize a NULL
  val SQL_NULL = """\N"""

  // These string builders store the tab delimited content that will be used to create the COPY
  // input. It's a very simple, tab delimited format. As long as we insert them in the order that
  // is shown here, there's no foreign key violations.
  val tabDelimitedLocations = new collection.mutable.StringBuilder
  val tabDelimitedInspections = new collection.mutable.StringBuilder
  val tabDelimitedViolations = new collection.mutable.StringBuilder

  /**
   * Interprets a given CSV file and loads it into an internal format. Once all CSVs have been
   * loaded, call writeSqlFile to write out the SQL commands that will insert the loaded content
   * into our database.
   * 
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
    var locationAddress = getNullable(WordUtils.capitalizeFully(allRows(0).locationAddress))
    var locationPostalcode = allRows(0).postalCode
    var locationCity = allRows(0).city
    var locationRha = getNullable(allRows(0).locationRha)

    tabDelimitedLocations.append(locationId)
    tabDelimitedLocations.append("\t")
    tabDelimitedLocations.append(locationName)
    tabDelimitedLocations.append("\t")
    tabDelimitedLocations.append(locationAddress)
    tabDelimitedLocations.append("\t")
    tabDelimitedLocations.append(locationPostalcode)
    tabDelimitedLocations.append("\t")
    tabDelimitedLocations.append(locationCity)
    tabDelimitedLocations.append("\t")
    tabDelimitedLocations.append(locationRha)
    tabDelimitedLocations.append("\n")

    // Some reports have duplicated records: a same violation under same inspection
    // see "Regina Qu'Appelle_Pilot Butte_Pilot Butte Recreation Hall Kitchen [Pilot But...].csv"
    val lastViolationId = new ArrayList[Integer]
    var lastInspectionDate = "Anything that doesn't match the current inspection date :P"

    for(row <- allRows) {
      // Test if this is a new inspection; if yes, insert; if no, just insert violations
      if(row.inspectionDate != lastInspectionDate) {
        lastInspectionDate = row.inspectionDate

        tabDelimitedInspections.append(inspectionId)
        tabDelimitedInspections.append("\t")
        tabDelimitedInspections.append(locationId)
        tabDelimitedInspections.append("\t")
        tabDelimitedInspections.append(convertToIso8601(row.inspectionDate))
        tabDelimitedInspections.append("\t")
        tabDelimitedInspections.append(row.inspectionType)
        tabDelimitedInspections.append("\t")
        tabDelimitedInspections.append(row.reinspectionPriority)
        tabDelimitedInspections.append("\n")

        inspectionId += 1

        // Insert the first violation
        if(row.violation != "") {
          lastViolationId.clear
          lastViolationId.add(row.violationId)

          tabDelimitedViolations.append(inspectionId - 1) // Note inspection was previously incremented
          tabDelimitedViolations.append("\t")
          tabDelimitedViolations.append(row.violationId)
          tabDelimitedViolations.append("\n")
        }
      }
      // Otherwise it's a violation for the previous inspection
      else if(row.violation != "") {
        val violationId = row.violationId
        if(!lastViolationId.contains(violationId)) {
          lastViolationId.add(violationId)

          tabDelimitedViolations.append(inspectionId - 1)
          tabDelimitedViolations.append("\t")
          tabDelimitedViolations.append(row.violationId)
          tabDelimitedViolations.append("\n")
        }
      }
      else {
        println("WARNING: Found row with no violation but not a new inspection")
        println("         in file: " + csvFile)
      }
    }

    inspectionId
  }

  /**
   * Writes out the SQL file. You must call this to get the results of having loaded in CSVs.
   */
  def writeSqlFile = {
    writer.write("COPY location (id, name, address, postcode, city, rha) FROM STDIN;\n")
    writer.write(tabDelimitedLocations.toString + "\\.\n\n")

    writer.write("COPY inspection (id, location_id, inspection_date, inspection_type, reinspection_priority) FROM STDIN;\n")
    writer.write(tabDelimitedInspections.toString + "\\.\n\n")

    writer.write("COPY violation (inspection_id, violation_id) FROM STDIN;\n")
    writer.write(tabDelimitedViolations.toString + "\\.\n")
  }

  /**
   * Tests if the string is empty, returning an SQL NULL if it is or the original data otherwise.
   * @param string The full column from the CSV file.
   * @return "\N" if contains nothing or the string itself otherwise.
   */
  private def getNullable(string: String): String = if(string == "") SQL_NULL else string

  /**
   * Converts a date (as a string) from the format used in the CSV files to ISO 8601.
   * 
   * So "Monday, January 01, 1970" becomes "1970-01-01".
   */
  def convertToIso8601(input: String): String = {
    val date = new SimpleDateFormat("EEEE, MMMM dd, yyyy").parse(input)
    new SimpleDateFormat("yyy-MM-dd").format(date)
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
  ) {
    def postalCode: String = {
      val strLength = locationCityAndPostalCode.size

      // Obviously not a postal code (note that postal codes always have spaces in the CSVs)
      if(locationCityAndPostalCode == "" || strLength < 7) {
        SQL_NULL
      }
      // Possibly a postal code? Do a rough check to see if it looks like one. If it walks and talks
      // like a duck, it's probably a duck.
      else if(locationCityAndPostalCode.substring(strLength - 7).matches("""^[A-Z]\d[A-Z] \d[A-Z]\d$""")) {
        locationCityAndPostalCode.substring(strLength - 7)
      }
      else {
        SQL_NULL
      }
    }

    def city: String = {
      if(locationCityAndPostalCode == "") {
        SQL_NULL
      }
      else if(locationCityAndPostalCode.matches("^.+, .+$")) {
        WordUtils.capitalizeFully(locationCityAndPostalCode.substring(0,
            locationCityAndPostalCode.lastIndexOf(',')))
      }
      else if(locationCityAndPostalCode.matches(", .+$")) {
        // Some files have this column as ", SASKATCHEWAN" (see
        // 'Saskatoon_Other Locations_Leaning Maple Meats - Catering [MCKILLOP].csv'
        SQL_NULL
      }
      else {
        // If not match the above regexs, then it only contains the city name
        locationCityAndPostalCode;
      }
    }

    /**
     * Get the violation ID, which is a integer uniquely identifying a violation. For some reason, the
     * reports don't have violation 9, but have violation 8a and 8b. To maintain consistency we
     * treat 8a as 8 and 8b as 9,
     * @param string The CSV column for the violation name.
     * @return Integer of violation ID.
     */
    def violationId: Int = {
      val tempID = violation.substring(0, violation.lastIndexOf('-') - 1).trim
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
  }
}