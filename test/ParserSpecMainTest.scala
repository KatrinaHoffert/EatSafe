import org.specs2.mutable._
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import csvParser._

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
import scala.io.Source
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ParserSpecMainTest extends Specification {
  
  /**
   * Takes the parser output file and creates a Seq[Seq[Array[String]]] where the Array[String]
   *  holds individual table entries, Seq[Array[String]] are rows from the table and the 
   *  whole Seq[Seq[Array[String]]] is a set of tables, that have rows and entries.
   */
  def CreateTableMatrix(): Seq[Seq[Array[String]]] = {
    var x = Source.fromFile("database/test/test.sql").getLines().toList
    var seq = Seq(x.slice(1,17).map { x => x.split("\t") }.toSeq, x.slice(20,23).map { x => x.split("\t") }.toSeq, x.slice(26,35).map { x => x.split("\t") }.toSeq, x.slice(38,55).map { x => x.split("\t") }.toSeq)
    return seq
  }
  
  
"Parser" should {
  Main.FILE_NAME = "database/test/test.sql"
  Main.FOLDER_PATH = "database/test/testReports/"
  Main.main(Array("")) 
  val allData = CreateTableMatrix()
  val violationTypes = allData(0) 
  val locations = allData(1)
  val inspections = allData(2) 
  val violations = allData(3) 
  
  "Correctly create a violationType Table" in {
    violationTypes.length must beEqualTo(16)
    violationTypes(0)(0) must beEqualTo("1")
    violationTypes(15)(0) must beEqualTo("16")
  }
 
  "Correctly create a locations table" in {
    locations.length must beEqualTo(3)
    var firstLoc = locations(0)
    var secondLoc = locations(1)
    var thirdLoc = locations(2)
    thirdLoc(1) must beEqualTo("Robinson Country Cookhouse & Saloon")
    thirdLoc(2) must beEqualTo("Cupar")
    thirdLoc(3) must beEqualTo("\\N")
    thirdLoc(4) must beEqualTo("Cupar")
    thirdLoc(5) must beEqualTo("Regina QuAppelle Health Authority")
    firstLoc(1) must beEqualTo("Tasty J's")
    firstLoc(2) must beEqualTo("305 Pacific Avenue")
    firstLoc(3) must beEqualTo("S0L 2A0")
    firstLoc(4) must beEqualTo("Luseland")
    firstLoc(5) must beEqualTo("Heartland Health Authority")
    secondLoc(1) must beEqualTo("Water Front Lodge")
    secondLoc(2) must beEqualTo("\\N")
    secondLoc(3) must beEqualTo("\\N")
    secondLoc(4) must beEqualTo("\\N")
    secondLoc(5) must beEqualTo("Northern Health - Mamaw/Keewa/Athab") 
  }

  "create Inspection table right" in {
    inspections.length must beEqualTo(9)
  }
  
  "create violations table right" in {
    violations.length must beEqualTo(17)
  }
 
  
}
  
/**
 * Test the translator alone
 */
"Translator" should {
  
  val translator = new Translator("database/test/testTranslation.json")
  
  "Find the right name replacement" in {
    var locationName = "Another Test"
    var locationAddress = "1100 A Central Ave"
    var locationCity = "Saskatoon"
    val replacements = translator.findReplacement(locationName, locationAddress, locationCity)
      .getOrElse(translator.identityReplacement)
    if(replacements.name.isDefined) locationName = replacements.name.get
    if(replacements.address.isDefined) locationAddress = replacements.address.get
    if(replacements.city.isDefined) locationCity = replacements.city.get
    locationName must beEqualTo("Subway")
    locationAddress must beEqualTo("1100 A Central Ave")
    locationCity must beEqualTo("Saskatoon")
      
  }

  "Find the right city and address replacements" in {
    var locationName = "Third Test"
    var locationAddress = "1100 A Central Ave"
    var locationCity = "Saskatoon"
    val replacements = translator.findReplacement(locationName, locationAddress, locationCity)
      .getOrElse(translator.identityReplacement)
    if(replacements.name.isDefined) locationName = replacements.name.get
    if(replacements.address.isDefined) locationAddress = replacements.address.get
    if(replacements.city.isDefined) locationCity = replacements.city.get
    locationName must beEqualTo("Third Test")
    locationAddress must beEqualTo("Test Address")
    locationCity must beEqualTo("Test City")
      
  }
  
  "Doesn't change the name, city and address if there is no replacement" in {
    var locationName = "Correct Name"
    var locationAddress = "1100 A Central Ave"
    var locationCity = "Saskatoon"
    val replacements = translator.findReplacement(locationName, locationAddress, locationCity)
      .getOrElse(translator.identityReplacement)
    if(replacements.name.isDefined) locationName = replacements.name.get
    if(replacements.address.isDefined) locationAddress = replacements.address.get
    if(replacements.city.isDefined) locationCity = replacements.city.get
    locationName must beEqualTo("Correct Name")
    locationAddress must beEqualTo("1100 A Central Ave")
    locationCity must beEqualTo("Saskatoon")
      
  }
  
  "Handle name, address or city name that is null" in {
    var locationName = ""
    var locationAddress = ""
    var locationCity = "Saskatoon"
    val replacements = translator.findReplacement(locationName, locationAddress, locationCity)
      .getOrElse(translator.identityReplacement)
    if(replacements.name.isDefined) locationName = replacements.name.get
    if(replacements.address.isDefined) locationAddress = replacements.address.get
    if(replacements.city.isDefined) locationCity = replacements.city.get
    locationName must beEqualTo("")
    locationAddress must beEqualTo("")
    locationCity must beEqualTo("Saskatoon")
      
  }
}

  
}
