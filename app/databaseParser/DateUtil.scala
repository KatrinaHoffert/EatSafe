package databaseParser

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.List

/**
 * Has simple methods for attempting to convert dates.
 */
object DateUtil {
  /**
   * List of date formats that will be tried (in this order) to try and convert dates.
   */
  val dateFormats = Seq(
    new SimpleDateFormat("M/dd/yyyy"),
    new SimpleDateFormat("dd.M.yyyy"),
    new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"),
    new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"),
    new SimpleDateFormat("dd.MMM.yyyy"),
    new SimpleDateFormat("dd-MMM-yyyy"),
    new SimpleDateFormat("EEEE, MMMM dd, yyyy")
  )

  /**
   * Convert String from various formats (defined in `dateFormats`) into a Date object. A null input
   * is gracefully handled.
   *
   * @param input A possible date as a string.
   * @return Some Date object if input string is parsed successfully, otherwise None.
   */
  def convertToDate(input: String): Option[Date] = {
    if(input == null) {
      None
    }
    else {
      for(format <- dateFormats) {
        var date: Option[Date] = None
        try {
          format.setLenient(false)
          date = Some(format.parse(input))
        } catch {
          case ex: ParseException => // Do nothing (maybe a different format or not a date?)
        }
        
        if(date.isDefined) return date
      }

      None
    }
  }
}