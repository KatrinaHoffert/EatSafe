package models

import scala.util.{Try, Success, Failure} 
import anorm._
import play.api.db.DB
import play.api.Play.current


/**
 * case class for each location (restaurant),
 * ID: unique id of the location,
 * inspections: a sequence of inspections of this location
 */
case class Location (id: Int, name: String, address: String, postalCode: String, city: String,
  regionalHealthAuthority: String, inspections: Seq[Inspection])

object Location {
  /**
   * To retrieve data from database.
   */
  
  
//  def getLocationById(id: Int): Try[Location] = {
//    // TODO -- returning dummy data right now
//    Try(Location(123, "Foo", "123 Fake St", "S1K 2N3", "Saskatoon", "Saskatoon Health Authority",
//        Seq.empty[Inspection]))
//  }
  
  /**
   * Get a list of violations for a location's inspection
   * The primary key a location's inspection is the combination of the id of location and inspection date
   */
  def getViolations(id:Int, inspectionDate:String): Try[Seq[Violation]] = {
    Try{
      DB.withConnection { implicit connection =>
        val query = SQL(
           """
             SELECT violation_id, violation_des, violation_des, violation_priority
             FROM violations a, violation_types b
             WHERE id = {id} AND violation_date = {inspectionDate} AND 
             a.violation_id = b.violation_id;
           """    
        ).on("id" -> id, "inspectionDate" -> inspectionDate)
        
        query().map (
          row =>
            Violation(row[Int]("violation_id"), row[String]("violation_des"),
                row[String]("violation_des"), row[String]("violation_priority"))
        ).toList
      }
    }
  }
  
  
  /**
   * Get a list of inspections for a location
   * The primary key a location the id
   */
  def getInspection(id:Int): Try[Seq[Inspection]] = {
    Try{
      DB.withConnection { implicit connection =>
        val query = SQL(
           """
             SELECT inspection_date, inspection_type, reinspection_priority
             FROM inspections
             WHERE id = {id};
           """    
        ).on("id" -> id)
        
        query().map (
          row =>
            getViolations(id, row[String]("inspection_date")) match {
              case Success(violationsList) => 
                Inspection(row[String]("inspection_date"), row[String]("inspection_type"), 
                row[String]("reinspection_proority"), violationsList)
              case Failure(e) => 
                Inspection(row[String]("inspection_date"), row[String]("inspection_type"), 
                row[String]("reinspection_proority"), Nil)
            }
        ).toList
      }
    }
  }

  /**
   * Get a location by id
   */ 
  def getLocationById(id:Int): Try[Location] = {
    Try{
      DB.withConnection { implicit connection =>
        val query = SQL(
           """
             SELECT location_restaruant_name, location_address, location_postcode, location_city, location_rha
             FROM location
             WHERE id = {id};
           """    
        ).on("id" -> id)
        
        query().map (
          row =>
            getInspection(id) match {
              case Success(inspectionList) =>
                Location(id, row[String]("location_restaruant_name"), row[String]("location_address"),
                row[String]("location_postcode"), row[String]("location_city"), row[String]("location_rha"),
                inspectionList)
              case Failure(e) => Location(id, row[String]("location_restaruant_name"), row[String]("location_address"),
                row[String]("location_postcode"), row[String]("location_city"), row[String]("location_rha"),
                Nil) 
            }
        ).toList.head
      }
    }
  }

  
    /**
   * Get a list of all the location in a city
   */ 
  def getLocationByCity(city:String): Try[Seq[Location]] = {
    Try{
      DB.withConnection { implicit connection =>
        val query = SQL(
           """
             SELECT id, location_restaruant_name, location_address, location_postcode, location_city, location_rha
             FROM location
             WHERE city = {city};
           """    
        ).on("city" -> city)
        
        query().map (
          row =>
            getInspection(row[Int]("id")) match {
              case Success(inspectionList) =>
                Location(row[Int]("id"), row[String]("location_restaruant_name"), row[String]("location_address"),
                row[String]("location_postcode"), row[String]("location_city"), row[String]("location_rha"),
                inspectionList)
              case Failure(e) => 
                Location(row[Int]("id"), row[String]("location_restaruant_name"), row[String]("location_address"),
                row[String]("location_postcode"), row[String]("location_city"), row[String]("location_rha"),
                Nil) 
            }
        ).toList
      }
    }
  }
}