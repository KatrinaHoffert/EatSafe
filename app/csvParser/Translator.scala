package csvParser

import scala.io.Source
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.util.control.Breaks._

/**
 * Allows swapping of existing names, addresses, and cities for new values.
 * @param translationFile Path to a file to use for the translation file
 */
class Translator(translationFile: String) {
  private val translationFileContents: String = Source.fromFile(translationFile).mkString
  private val jsonArray: Seq[JsValue] = Json.parse(translationFileContents).as[Seq[JsValue]]

  /**
   * Finds the replacement object to use for this name, address, and city combination.
   */
  def findReplacement(name: String, address: String, city: String): Option[Replacement] = {
    var replacement: Option[Replacement] = None
    breakable {
      for(replacementObj <- jsonArray) {
        if(name == (replacementObj \ "name").as[String] && address == (replacementObj \ "address").as[String]
            && city == (replacementObj \ "city").as[String]) {
          println("Replacement found for " + name + ", " + address + ", " + city)
          // Found a matching entry -- get its replacement object into a Scala object
          replacement = (replacementObj \ "replacement").asOpt[JsObject].map { jsonReplacement =>
            Replacement((jsonReplacement \ "name").asOpt[String],
              (jsonReplacement \ "address").asOpt[String], (jsonReplacement \ "city").asOpt[String])
          }
          break
        }
      }
    }

    replacement
  }

  /**
   * Represents the replacements JSON object.
   */
  case class Replacement(name: Option[String], address: Option[String], city: Option[String])

  /**
   * A replacement object that replaces nothing. Useful for when we want to absolutely have a
   * Replacement object instead of something like Option[Replacement].
   */
  val identityReplacement = Replacement(None, None, None)
}