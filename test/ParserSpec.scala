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
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ParserSpec extends Specification {
  "Parser" should {
  "run with changed parameters" in {
   Main.FILE_NAME = "database/test/test.sql"
   Main.FOLDER_PATH = "database/test/testReports/"

   Main.main(Array("")) 
   true
  }
}


  
}
