import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import databaseParser._

import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.io.Reader
import java.io.FileReader
import java.io.BufferedReader
import play.api.db.DB
import anorm._
import play.api.Play.current
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ParserSpec extends Specification {
  def createTableStatements(): Array[String]= {
   var sb = new StringBuffer();
   var s = new String();
   var fr = new FileReader(new File("database/CreateTables.sql"));
   var br = new BufferedReader(fr);
   
   //skip the first 3 lines of comments 
   br.readLine()
   br.readLine()
   br.readLine()
   
   //read all the lines into the stringBuffer
   (s = br.readLine())
   while(s != null)
   {
     sb.append(s);
     (s = br.readLine())
   }
   br.close()
   
   //split the file at the semi-colons
   val queryArray = sb.toString().split(";")
   return queryArray
  }
  
  def createInsertStatements(): Array[String]= {
   var sb = new StringBuffer();
   var s = new String();
   var fr = new FileReader(new File("database/test/test.sql"));
   var br = new BufferedReader(fr);
   
   //skip the first 3 lines of comments 
   br.readLine()
   br.readLine()
   br.readLine()
   
   //read all the lines into the stringBuffer
   (s = br.readLine())
   while(s != null)
   {
     sb.append(s);
     (s = br.readLine())
   }
   br.close()
   
   //split the file at the semi-colons
   val queryArray = sb.toString().split(";")
   return queryArray
  }
  
  "Parser" should {
    "run from test folder and fill database without errors" in new WithApplication{
     Main.FILE_NAME = "database/test/test.sql"
     Main.FOLDER_PATH = "database/test/testReports/"
     var numLocs = -1
     var numInspections = -1
     var robinsonRow:Row = null
     var tastyRow:Row = null
     var waterRow:Row = null
   
     Main.main(Array("")) 
    
     var tableCreateArray = createTableStatements();
     var insertStatementArray = createInsertStatements();
     DB.withConnection("test") { implicit connection =>
       val queryBegin = SQL("begin;").execute()
              
       //drop all tables and start fresh
       var i = 0;
       while(i<tableCreateArray.length)
       {
         val queryInsert = SQL(tableCreateArray(i)).executeUpdate();
         i = i + 1;
       }
              //run all the inserts for the test information
       var j = 0
       while(j<insertStatementArray.length)
       {
         val queryInsert = SQL(insertStatementArray(j)).execute();
         
         j = j + 1;
       }
       //Now that the database is filled we can go about getting data out and checking if it is correct
       //FIRST check if there is the right ammount of Locations
       val locationCount = SQL("SELECT count(id) FROM location")
       val rows = locationCount()
       numLocs = rows.head[Int]("count")
       
       //SECOND check the number of inspections is correct
       val inspectionCount = SQL("SELECT count(id) FROM inspection")
       val inspectionRows = inspectionCount()
       numInspections = inspectionRows.head[Int]("count")
       
       //get the information for "Robinson Country Cookhouse & Saloon" and check if its good
       val robinsonSQL = SQL("SELECT * FROM location WHERE name ='Robinson Country Cookhouse & Saloon'")
       val robinsonResult = robinsonSQL()
       robinsonRow = robinsonResult.head
       
       //get the info for "Tasty J's" and check it against what i should be from the csv
       val tastySQL = SQL("SELECT * FROM location WHERE name ='Tasty J''s'")
       val tastyResult = tastySQL()
       tastyRow = tastyResult.head
       
       //get info from the "Water Front Lodge" and check it against what it should be from the csv
       val waterSQL = SQL("SELECT * FROM location WHERE name ='Water Front Lodge'")
       val waterResult = waterSQL()
       waterRow = waterResult.head
       
       val queryRollback = SQL("rollback;").execute()
    }
     //checks of the values are made outside the connection, because if done inside, they simply throw psqlExceptions which are not helpful
     numLocs must beEqualTo(3).setMessage("number of locations is not good, it should be 3 and its" + numLocs)
     numInspections must beEqualTo(9).setMessage("number of inspections is not good, it should be 9 and its" + numInspections)
     robinsonRow[String]("name") must beEqualTo("Robinson Country Cookhouse & Saloon")
     robinsonRow[String]("address") must beEqualTo("Cupar")
     robinsonRow[String]("city") must beEqualTo("Cupar")
     robinsonRow[String]("postcode") must beEqualTo("DATA MISSING")
     robinsonRow[String]("rha") must beEqualTo("Regina QuAppelle Health Authority")
     tastyRow[String]("name") must beEqualTo("Tasty J's")
     tastyRow[String]("address") must beEqualTo("305 Pacific Avenue")
     tastyRow[String]("city") must beEqualTo("Luseland")
     tastyRow[String]("postcode") must beEqualTo("S0L 2A0")
     tastyRow[String]("rha") must beEqualTo("Heartland Health Authority")
     waterRow[String]("name") must beEqualTo("Water Front Lodge")
     waterRow[String]("address") must beEqualTo("DATA MISSING")
     waterRow[String]("city") must beEqualTo("#1 Johnson Street")
     waterRow[String]("postcode") must beEqualTo("DATA MISSING")
     waterRow[String]("rha") must beEqualTo("Northern Health - Mamaw/Keewa/Athab")
  }
}


  
}
