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
   * Output is ordered by how good of a match it is according to ts_rank (note that the name is
   * weighted slightly higher than the address).
   *
   * @param query The string that the user used as a search query.
   * @param city The name of the city to search in. Use "Unknown city" for locations without a city.
   * @param lax If true, the strictness of the search is relaxed and word portions are "or"ed instead
   * of "and"ed. Thus, not all words are required and we'll usually get more results (but results will
   * be less accurate).
   * @return A list of SlimLocation matching the query, if any. Wrapped in a Try in case of database
   * failure.
   */
  def queryLocations(query: String, city: String, lax: Boolean)(implicit db: ActiveDatabase):
      Try[Seq[SlimLocation]] = {
    Try {
      DB.withConnection(db.name) { implicit connection =>
        val tsQuery = toTsQuery(query, lax)
        val cityConditional = if(city.toLowerCase != "unknown city") {
            "LOWER(city) = LOWER({city})"
          } else {
            "city IS NULL"
          }

        val sqlQuery = SQL(
           """
            SELECT id, name, address, ts_rank(text_vector, to_tsquery('eatsafe_english', {query})) AS rank
              FROM location_search
              INNER JOIN location ON(location_id = id)
              WHERE """ + cityConditional + """ AND text_vector @@ to_tsquery('eatsafe_english', {query})
              ORDER BY rank DESC;
           """
          ).on("city" -> city, "query" -> tsQuery)
        
        sqlQuery().map {
          row => SlimLocation(row[Int]("id"), row[String]("name"), row[Option[String]]("address"))
        }.toList
      }
    }
  }

  /**
   * Converts a user-created query string into a TSQUERY compatible string.
   * @param query The user created query.
   * @param lax If true, the query will search for any instances of the word. Otherwise all words
   * must be matched. This is quite intelligent, with similar words and synonyms working, as well as
   * with useless words (like "the") being ignored.
   */
  def toTsQuery(query: String, lax: Boolean): String = {
    val wordSeparator = if(lax) "|" else "&"
    escapeQuery(query).replace(" ", wordSeparator)
  }

  /** Escapes illegal values in a query that is about to be turned into a TSVALUE. */
  def escapeQuery(query: String): String = {
    query
      .trim
      .replace("""\""", """\\""")
      .replace("""!""", """\!""")
      .replace(""":""", """\:""")
      .replace("""|""", """\|""")
      .replace("""&""", """\&""")
      .replace("""(""", """\(""")
      .replace(""")""", """\)""")
  }
}