import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import models._
import scala.util._
import play.api.db._
import java.sql._
import play.api.Play.current

import org.specs2.mock._

/**
 * Because of the relative simplicity of the Violation and Inspection
 * Classes, they will be tested implicitly, during the testing of the more
 * complex, Location class, since the locationbyID method and locationsByCity
 * methods will both be returning Location objects that contain inspections
 * and violations.
 * 
 */
@RunWith(classOf[JUnitRunner])
class ModelSpec extends Specification with Mockito {
  
  //  this.getLocationByIdTests();
    
  def getLocationByIdTests()
  {
    
    //currently, this gets the default database source, in future, we can
    //easily add reference to a testing database in the play conf/application.conf
    //as for now, they will be the same data anyway so i wont worry 
    "getLocationById" should {
      
      "return a success with proper data when given good data" in new WithApplication{
      //Test good data and expected results here
       
        val goodLoc = Location.getLocationById(1)
        var pass = false
         goodLoc match {
           case Success(v) => pass = true
           case Failure(e) => pass = false
         }
       pass mustEqual true
      }
      //data here is based off of the database files
    "return good data when given a good ID" in new WithApplication
    {
        val goodLoc = Location.getLocationById(2)
        var pass = false
         goodLoc match
         {
           case Success(v) =>
           {
               v.id must beEqualTo(2)// cause thats what i passed in
               v.name must beEqualTo("7 Eleven") .ignoreSpace //ignore space due to trailing whitespace
               v.address must beEqualTo("835 A Broadway AVE") .ignoreSpace //ignore space due to trailing whitespace
               v.postalCode must beEqualTo("S7N 1B5") .ignoreSpace //ignore space due to trailing whitespace   
               pass = true
           }
           case Failure(e) => pass = false
         } 
        pass mustEqual true
    }
    "return failure with a bad id (empty result set)" in new WithApplication{
      //Test good data and expected results here
       
        val goodLoc = Location.getLocationById(445)
        var pass = false
         goodLoc match {
           case Success(v) => pass = false
           case Failure(e) => pass = true
         }
        pass mustEqual true
    }
    
    //TODO
    //there should be a test where the database connection is not available, but we need some kind of test
    //hook in order to do this, perhaps pass in the database connection or something, so it can be mocked out to throw
    //errors   
  }
  }
  
    def getLocationsByCityTests()
  {
    /*
     * todo, run function in a number of senarios and do tests based on that
     * run with good inputs, bad inputs, possibly without network connection.
     * find or create a database record with no violations perhaps.
     * 
     */
  }
  
  
}
