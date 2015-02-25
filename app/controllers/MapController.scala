package controllers

import scala.util.{Try, Success, Failure} 
import play.api._
import play.api.mvc._
import models._
import models.Location
import globals.Globals.defaultDB

import java.net.URLEncoder
import play.api.libs.ws.WS
import play.api.Play.current
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

object MapController extends Controller {

	def formatAddress(address: String){
	
			//replaces " " with "+" in the address for the get request
			//I also plan to use the same format in URL
			return URLEncoder.encode(address, "UTF-8");

	}


	/* This is nor working as of yet. I need to find a way to make this work so that the geoencoding can be done in the backend

	//takes in an address and retunrs a lat long
	// I will have to do better error checking than I have done here.
	def geocodeAddress(address: String): Option[(Double, Double)] = {

			implicit val timeout = Timeout(50000 milliseconds)
	
			val encodedAddress = this.formatAddress(address)

			val latitudeAndLongitude = WS.url("http://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&sensor=true").get()
   
			val future = latitudeAndLongitude map {
      			response => (response.json \\ "location")
    		}

    		// Wait until the future completes (Specified the timeout above)

    		val result = Await.result(future, timeout.duration).asInstanceOf[List[play.api.libs.json.JsObject]]

    		val latitude = (result(0) \\ "lat")(0).toString.toDouble
    		val longitude = (result(0) \\ "lng")(0).toString.toDouble
    		return Option(latitude, longitude)

	}
	*/


	def showMap(address: String, city: String) = Action {
        Ok(views.html.locations.displayMap(address,city))
	}

}