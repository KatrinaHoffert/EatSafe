import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import models._
import scala.util._

/**
 * Because of the relative simplicity of the Violation and Inspection
 * Classes, they will be tested implicitly, during the testing of the more
 * complex, Location class, since the locationbyID method and locationsByCity
 * methods will both be returning Location objects that contain inspections
 * and violations.
 * 
 */
@RunWith(classOf[JUnitRunner])
class ModelSpec extends Specification {
  
  this.getLocationByIdTests();
  
   
  /**
   * This function tests the violation class, it has a few basic tests
   * like making sure the ID is between 1 and 16, which it should be, and
   * checking the description is not null/empty. As well, severity has
   * only three possible values, so it should be one of those
   * 
   * violation:  the violation object to be put through tests
   * testName: an incremental name like "Violation Test 1", "Violation Test 2"
   *          to make it easier to see what tests are failing, instead of 
   *          having every test called ViolationTest
   */
  def ViolationTests(violation: Violation, testName: String)
  {
    "Violations TEST" should 
    {
      "ID should be between 1-16" in 
      {  
        violation.id must beBetween(1,16);//this includes 1 and 16
      }
      "description should not be empty" in 
      {
        violation.description must not have length(0);
  
      }
      "severity of a violation should be 'Critial Item' or 'General Item' " in
      {
        violation.severity must beEqualTo("Critial Item") or beEqualTo("General Item")
      }
      
    }
  }
   /**
    * Testing of the Inspection Class, much like Violation tests, 
    * these are basically data acceptance tests
    * note that this set of tests will call the ViolationTests for all
    * the violations in the Violations array
    */
  def InspectionTests(inspection: Inspection, testName: String)
  {
    testName should
    {
      "Inspection type should be 'Routine', 'Follow-up', or 'Special'" in 
      {
        inspection.inspectionType must beEqualTo("Routine") or beEqualTo("Follow-up") or beEqualTo("Special")
      }
      "Reinspection Priority should be 'Low','Moderate' or 'High'" in 
      {
          inspection.reinspectionPriority must beEqualTo("Low") or beEqualTo("Moderate") or beEqualTo("High")
      }
      //my thinking is that an inspection might be clean and have
      //no violations, thus this could be empty.
      //but if it has stuff, it should be the right stuff
      "If violations is not null, test each violation" in 
      {
        true//todo
        /*
          Will be something like 
          if(inspection.violation not null)
          {
            for each violation
              this.ViolationTests(violation, "test "+i)
          }
         */
      }
      
    }
  }
  
  /**
   * Basic tests for locations
   * will do more testing for the location Functions in another part
   */
  def LocationTests(location: Location, testName: String, city: String)
  {
    testName should
    {
      "ID should be positive number" in
      {
        location.id must beGreaterThan(0)
      }
      "city should not be null and equal to city parameter(if its not null)" in 
      {
        //used for testing of the getLocationsByCity, so you can make sure 
        //all returned locatoins are from the specified city
        //if city is null, dont do this extra stuff
        true//todo
      }
      "Postcode should be a real postcode" in 
      {
        true//todo some sort of regex or string parser      
      }
      "Inspections should not be null and pass tests" in 
      {
        //run InspectionTests on all inspections
        true//todo
      }
    }
  }
  
  def getLocationByIdTests()
  {
    /*
     * todo, run function in a number of senarios and do tests based on that
     * run with good inputs, bad inputs, possibly without network connection.
     * find or create a database record with no violations perhaps. 
     */
     val x = Location.getLocationById(123)
     x match {
       case Success(v) => this.LocationTests(v, "TestTest", "Saskatoon")
       case Failure(e) => throw new IllegalStateException("ohno")
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
