package models

import scala.util.{Try, Success, Failure} 
import anorm._
import play.api.db.DB
import play.api.Play.current
import globals.ActiveDatabase

/**
 * Embodies the searching functionalities of the site, which allow searching for locations.
 */
object SearchEngine {
  /**
   * Queries the locations with a full text name and address search utilizing Postgres's TSVECTOR
   * type, which allows for natural language full text searches. It allows for a fast, fuzzy search.
   *
   * @param search The string that the user used as a search query.
   * @param city The name of the city to search in. Use "Unknown city" for locations without a city.
   * @return A list of SlimLocation matching the query, if any. Wrapped in a Try in case of database
   * failure.
   */
  def queryLocations(search: String, city: String)(implicit db: ActiveDatabase):
      Try[Seq[SlimLocation]] = {
    Try {
      DB.withConnection(db.name) { implicit connection =>
        val query = if(city.toLowerCase != "unknown city") {
          SQL(
           """
            SELECT id, name, address
              FROM location_search
              INNER JOIN location ON(location_id = id)
              WHERE LOWER(city) = LOWER({city}) AND text_vector @@ plainto_tsquery('english', {query});
           """
          ).on("city" -> city, "query" -> search)
        }
        else {
          SQL(
           """
            SELECT id, name, address
              FROM location_search
              INNER JOIN location ON(location_id = id)
              WHERE city IS NULL AND text_vector @@ plainto_tsquery('english', {query});
           """
          ).on("city" -> city, "query" -> search)
        }
        
        query().map {
          row => SlimLocation(row[Int]("id"), row[String]("name"), row[Option[String]]("address"))
        }.toList
      }
    }
  }
}