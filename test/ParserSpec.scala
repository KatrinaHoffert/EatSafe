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
        val queryRollback = SQL("rollback;").execute()
        }
   
 }
}


  
}
