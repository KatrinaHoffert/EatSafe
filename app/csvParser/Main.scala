package csvParser

import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer

object Main {
  /**
   * Folder containing the CSVs to parse. Only files with the *.csv extension in this folder will
   * be parsed. The trailing slash is necessary. Path is relative to project directory.
   */
  var FOLDER_PATH = "database/InspectionReport/"

  /**
   * Output SQL file name.
   */
  var FILE_NAME = "database/statements.sql"

  def main(args: Array[String]): Unit = {
    val writer = new OutputStreamWriter(new FileOutputStream(FILE_NAME), "utf-8")
    val csvLoader = new CsvLoader(writer)

    insertViolationTypes(writer)
    readFolder(FOLDER_PATH, csvLoader)

    csvLoader.writeSqlFile
    writer.close()
  }

  /**
   * Creates the violation type table (which is hardcoded because it doesn't change).
   */
  def insertViolationTypes(writer: Writer): Unit = {
    writer.write("""COPY violation_type (id, description, name, priority) FROM STDIN;
1	parser.desc.1	parser.name.1	parser.priority.critical
2	parser.desc.2	parser.name.2	parser.priority.critical
3	parser.desc.3	parser.name.3	parser.priority.critical
4	parser.desc.4	parser.name.4	parser.priority.critical
5	parser.desc.5	parser.name.5	parser.priority.critical
6	parser.desc.6	parser.name.6	parser.priority.critical
7	parser.desc.7	parser.name.7	parser.priority.critical
8	parser.desc.8	parser.name.8	parser.priority.critical
9	parser.desc.9	parser.name.9	parser.priority.critical
10	parser.desc.10	parser.name.10	parser.priority.general
11	parser.desc.11	parser.name.11	parser.priority.general
12	parser.desc.12	parser.name.12	parser.priority.general
13	parser.desc.13	parser.name.13	parser.priority.general
14	parser.desc.14	parser.name.14	parser.priority.general
15	parser.desc.15	parser.name.15	parser.priority.general
16	parser.desc.16	parser.name.16	parser.priority.general
\.

""")
  }

  /**
   * Reads a folder of CSV files, generating the needed SQL (with unique location and inspection IDs).
   * @param folderPath The path to the folder containing the CSV files.
   * @param writer The writer to write SQL commands to.
   */
  def readFolder(folderPath: String, csvLoader: CsvLoader): Unit = {
    val folder = new File(folderPath)
    val files = folder.listFiles()

    if(files == null) println("ERROR: No files in this folder <" + folderPath + ">")

    var locationId = 1
    var inspectionId = 1

    for(file <- files) {
      if(file.isFile && file.getName.toLowerCase.endsWith("csv")) {
        inspectionId = csvLoader.loadCsv(folderPath + file.getName, locationId, inspectionId)
        locationId += 1
      }
    }

    println("Number of locations: " + (locationId - 1))
    println("Number of inspections: " + (inspectionId - 1))
  }
}
