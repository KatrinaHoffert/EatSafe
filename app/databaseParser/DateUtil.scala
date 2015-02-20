package dataReader

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.List

object DateUtil {
  // List of all date formats that we want to parse.
  // Add your own format here.
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
   * Convert String with various formats into java.util.Date
   *
   * @param input Date as a string
   * @return Date object if input string is parsed successfully else returns null.
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
          case ex: ParseException => // Do nothing
        }
        
        if(date.isDefined) return date
      }

      None
    }
  }
}