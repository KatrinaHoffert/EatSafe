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
  "Parser" should {
  "run with changed parameters" in new WithApplication{
   Main.FILE_NAME = "database/test/test.sql"
   Main.FOLDER_PATH = "database/test/testReports/"

   Main.main(Array("")) 
   var sb = new StringBuffer();
   var s = new String();
   var fr = new FileReader(new File("database/test/test.sql"));
            // be sure to not have line starting with "--" or "/*" or any other non aplhabetical character
 
   var br = new BufferedReader(fr);
   (s = br.readLine())
   while(s != null)
   {
     sb.append(s);
     (s = br.readLine())
   }
   br.close();
   var inst = sb.toString().split(";");
   
   var i = 1;
  
     /*System.out.println(inst(i))
     System.out.println("------------------------------------------------------------------------------------------")
     i = i +1;*/
     DB.withConnection("test") { implicit connection =>
        val query1 = SQL("begin;").execute()
        val query2 = SQL("DROP TABLE IF EXISTS location CASCADE;").executeUpdate()
        val query3 = SQL("DROP TABLE IF EXISTS inspection CASCADE;").executeUpdate()
        val query4 = SQL("DROP TABLE IF EXISTS violation CASCADE;").executeUpdate()
        val query5 = SQL(
            """CREATE TABLE location(
                id SERIAL PRIMARY KEY,
                name TEXT NOT NULL,
                address TEXT NOT NULL,
                postcode TEXT NOT NULL,
                city TEXT NOT NULL,
                rha TEXT NOT NULL
                );
              """).executeUpdate()
        val query6 = SQL(
            """CREATE TABLE inspection(
                id SERIAL PRIMARY KEY,
                location_id INT NOT NULL,
                inspection_date DATE NOT NULL,
                inspection_type TEXT NOT NULL,
                reinspection_priority TEXT NOT NULL,
                FOREIGN KEY (location_id)
                    REFERENCES location
                        ON UPDATE NO ACTION
                        ON DELETE NO ACTION
            );
              """).executeUpdate()
        val query7 = SQL(
            """CREATE TABLE violation(
                inspection_id INT NOT NULL,
                violation_id INT NOT NULL,
                PRIMARY KEY (inspection_id, violation_id),
                FOREIGN KEY (inspection_id)
                    REFERENCES inspection
                        ON UPDATE NO ACTION
                        ON DELETE NO ACTION,
                FOREIGN KEY (violation_Id)
                    REFERENCES violation_type
                        ON UPDATE NO ACTION
                        ON DELETE NO ACTION
            );
              """).executeUpdate()
          //insert all the files into the database
          while(i<inst.length)
          {
            val queryInsert = SQL(inst(i)).executeInsert();
            i = i + 1;
          }
              
          
              
        val queryend = SQL("rollback;").execute()

        }
   
 }
}


  
}
