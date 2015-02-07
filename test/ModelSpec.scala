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
 * Test Suite of the current model, the tests being focused on the Locations class
 * and its methods, mostly because the other classes (Inspection and Violation) are simple
 * case classes with nothing to test but prebuilt getters and setters (we will assume scala knows
 * what its doing) the sets of tests are wrapped in functions in order to make things more
 * readable and reusable in the future.
 */
@RunWith(classOf[JUnitRunner])
class ModelSpec extends Specification with Mockito {
  
  //run test functions here
  this.getViolationsTests();
  this.getInspectionsTests();
  this.getLocationByIdTests();
  this.getLocationsByCityTests();

  
  /**
   * Run the set of tests to test the getLocationById method of the Locations class
   * this suite tests, good inputs, bad inputs and that the good inputs pass something back
   * that is what it was suppose to.
   */
  def getLocationByIdTests()
  {
    
    //currently, this gets the default database source, in future, we can
    //easily add reference to a testing database in the play conf/application.conf
    //as for now, they will be the same data anyway so i wont worry 
    "getLocationById" should {
      
      "return a success with proper data when given good data" in {
      //Test good data and expected results here
       
        val goodLoc = Location.getLocationById(1)
        var pass = false
         goodLoc match {
           case Success(loc) => pass = true
           case Failure(e) =>
           { 
             pass = false;
             "error message" mustEqual e.getMessage()
           }
         }
       pass mustEqual true
      }
      //data here is based off of the database files
    "return good data when given a good ID" in 
    {
        val goodLoc = Location.getLocationById(2)
        var pass = false
         goodLoc match
         {
           case Success(loc) =>
           {
               loc.id must beEqualTo(2)// cause thats what i passed in
               loc.name must beEqualTo("7 Eleven") .ignoreSpace //ignore space due to trailing whitespace
               loc.address must beEqualTo("835 A Broadway AVE") .ignoreSpace //ignore space due to trailing whitespace
               loc.postalCode must beEqualTo("S7N 1B5") .ignoreSpace //ignore space due to trailing whitespace   
               pass = true
           }
           case Failure(e) =>
           {
             pass = false
             //this prints the error message within the test
             "error message" mustEqual e.getMessage()
           }
         } 
        pass mustEqual true
    }
    "return failure with a bad id (empty result set)" in {
      //Test good data and expected results here
       
        val goodLoc = Location.getLocationById(445)
        var pass = false
         goodLoc match {
           case Success(loc) => pass = false
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
  
    /**
   * Run the set of tests to test the getLocationbyCity method of the Locations class
   * this suite tests, good inputs, bad inputs and that the good inputs pass something back
   * that is what it was suppose to. in this case, Saskatoon is the only city currently in the system
   * so the number of Locations passed back should line up with whats in the database
   */
  def getLocationsByCityTests()
  {
    "GetLocationsByCity" should
    {
      "return a success when given 'Saskatoon'" in 
      {
      //Test good data and expected results here
       
        val goodLocList = Location.getLocationByCity("Saskatoon")
        var pass = false
         goodLocList match {
           case Success(locList) => pass = true
           case Failure(e) =>
           { 
             pass = false;
             "error message" mustEqual e.getMessage()
           }
         }
       pass mustEqual true
      }
      //based on current database
      "return a sequence of Locations with 22 values when given 'Saskatoon'" in 
      {
        val goodLocList = Location.getLocationByCity("Saskatoon")
        var pass = false
         goodLocList match {
           case Success(locList) => 
           {
             pass = true
             locList.length must beEqualTo(22)
           }
           case Failure(e) => 
           {
             "error message" mustEqual e.getMessage()
             pass = false
           }
         }
       pass mustEqual true
      }
      "return failure when given anything but 'Saskatoon'" in 
      {
        val goodLocList = Location.getLocationByCity("This City Does NOT Exist")
        var pass = false
         goodLocList match {
           case Success(locList) => 
           {
             pass = false
           }
           case Failure(e) => 
           {
             pass = true
           }
         }
       pass mustEqual true
        
      }
    }   
  }
   /**
   * Run the set of tests to test the getViolations method of the Locations class
   * this suite tests, good inputs, bad inputs and that the good inputs pass something back
   * that is what it was suppose to.
   */
def getViolationsTests()
{
  "getViolations" should
  {
    "return success when given proper inputs" in
      {
      //Test good data and expected results here
       
      //TODO get good data
        val vioList = Location.getViolations(2, "TODODATE")
        var pass = false
         vioList match {
           case Success(vio) => pass = true
           case Failure(e) =>
           { 
             pass = false;
             "error message" mustEqual e.getMessage()
           }
         }
       pass mustEqual true
      }
      //based on current database
      "given a certian id, the number of returned violations should be correct" in 
      {
        val vioList = Location.getViolations(2, "TODODATE")
        var pass = false
         vioList match {
           case Success(vio) => 
           {
             pass = true
             vio.length must beEqualTo(22)
           }
           case Failure(e) => 
           {
             "error message" mustEqual e.getMessage()
             pass = false
           }
         }
       pass mustEqual true
      }
      "return failure when given bad data" in 
      {
        val vioList = Location.getViolations(9001, "2019-01-17")
        var pass = false
         vioList match {
           case Success(vio) => 
           {
             pass = false
           }
           case Failure(e) => 
           {
             pass = true
           }
         }
       pass mustEqual true
        
      }
  }
}
  /**
   * Run the set of tests to test the getInspections method of the Locations class
   * this suite tests, good inputs, bad inputs and that the good inputs pass something back
   * that is what it was suppose to.
   */
def getInspectionsTests()
{
  "getInspections" should
  {
    "return success when given proper inputs" in 
      {
      //Test good data and expected results here
       
      
        val InspList = Location.getInspection(2)
        var pass = false
         InspList match {
           case Success(insp) => pass = true
           case Failure(e) =>
           { 
             pass = false;
             "error message" mustEqual e.getMessage()
           }
         }
       pass mustEqual true
      }
      //based on current database
      "given a good id, return the right ammount of inspections" in 
      {
        val InspList = Location.getInspection(2)
        var pass = false
         InspList match {
           case Success(insp) => 
           {
             pass = true
             insp.length must beEqualTo(22/*TODO get the right number*/)
           }
           case Failure(e) => 
           {
             "error message" mustEqual e.getMessage()
             pass = false
           }
         }
       pass mustEqual true
      }
      "return failure when given bad data" in 
      {
        val vioList = Location.getInspection(9001)
        var pass = false
         vioList match {
           case Success(vio) => 
           {
             pass = false
           }
           case Failure(e) => 
           {
             pass = true
           }
         }
       pass mustEqual true
        
      }
  }
}
  
}
