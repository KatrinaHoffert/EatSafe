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
import globals.Globals.testDB
import globals.ActiveDatabase


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
      
      "return a success with proper data when given good data" in new WithApplication {
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
    "return good data when given a good ID" in new WithApplication 
    {
       
        val goodLoc = Location.getLocationById(7)
        var pass = false
         goodLoc match
         {
           case Success(loc) =>
           {
               loc.id must beEqualTo(7)// cause thats what i passed in
               loc.name must beEqualTo("Burstall Curling Rink - Kitchen") .ignoreSpace //ignore space due to trailing whitespace
               loc.address must beEqualTo("Maharg Ave") .ignoreSpace //ignore space due to trailing whitespace
               loc.postalCode must beEqualTo("S0N 0H0") .ignoreSpace //ignore space due to trailing whitespace   
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
     "throw error when it cannont connect to db" in new WithApplication 
    {
        val goodLoc = Location.getLocationById(7)((new ActiveDatabase("badDatabase")))
        var pass = false
         goodLoc match
         {
           case Success(loc) =>
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
      "return a success when given 'Saskatoon'" in new WithApplication 
      {
      //Test good data and expected results here
       
        val goodLocList = Location.getLocationsByCity("Saskatoon")
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
      "return a sequence of Locations with 929 values when given 'Saskatoon'" in new WithApplication 
      {
        val goodLocList = Location.getLocationsByCity("Saskatoon")
        var pass = false
         goodLocList match {
           case Success(locList) => 
           {
             pass = true
             locList.length must beEqualTo(929)
           }
           case Failure(e) => 
           {
             "error message" mustEqual e.getMessage()
             pass = false
           }
         }
       pass mustEqual true
      }
      "return a failure when given a bad database'" in new WithApplication 
      {
        val goodLocList = Location.getLocationsByCity("Saskatoon")((new ActiveDatabase("badDatabase")))
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
    "return success when given proper inputs" in new WithApplication
      {
      //Test good data and expected results here
       
      //TODO get good data
        val vioList = Violation.getViolations(2)
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
      "given a certian id, the number of returned violations should be correct" in new WithApplication 
      {
        val vioList = Violation.getViolations(31)
        var pass = false
         vioList match {
           case Success(vio) => 
           {
             pass = true
             vio.length must beEqualTo(3)
           }
           case Failure(e) => 
           {
             "error message" mustEqual e.getMessage()
             pass = false
           }
         }
       pass mustEqual true
      }
       "return false if connection cannot be made" in new WithApplication 
      {
        val vioList = Violation.getViolations(1)((new ActiveDatabase("BadDatabase")))
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
    "return success when given proper inputs" in new WithApplication
      {
      //Test good data and expected results here
       
      
        val InspList = Inspection.getInspections(2)
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
      "given a good id, return the right ammount of inspections" in new WithApplication 
      {
        val InspList = Inspection.getInspections(15)
        var pass = false
         InspList match {
           case Success(insp) => 
           {
             pass = true
             insp.length must beEqualTo(3)
           }
           case Failure(e) => 
           {
             "error message" mustEqual e.getMessage()
             pass = false
           }
         }
       pass mustEqual true
      }
      "return a failure if there is a bad database" in new WithApplication 
      {
        val InspList = Inspection.getInspections(15)((new ActiveDatabase("badDatabase")))
        var pass = false
         InspList match {
           case Success(insp) => 
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
