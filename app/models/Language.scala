package models

import scala.util.{Try, Success, Failure}
import play.api.Play.current

/**
 * Represents a language that the application can be in.
 *
 * @param code Two digit language code. Eg, "en" for English.
 * @param name Native full name of the language. Eg, "Français" for French.
 */
case class Language(code: String, name: String)

object Language {
  /**
   * Gets a list of all languages supported from the configuration file. The "langs.mapping" field
   * is a JSON object with keys as 2 character country codes and the values as full, native language
   * names.
   */
  def getSupportedLanguages: Try[Seq[Language]] = {
    Try {
      val languageMappings = for {
        langs <- current.configuration.getConfig("langs")
        mapping <- langs.getConfig("mapping")
      } yield mapping
      val countryCodes = languageMappings.get.keys

      countryCodes.map { code =>
        Language(code, languageMappings.get.getString(code).get)
      }.toList.sortBy(_.code)
    }
  }
}