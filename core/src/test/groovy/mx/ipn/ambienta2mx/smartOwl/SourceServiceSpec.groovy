package mx.ipn.ambienta2mx.smartOwl

import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared
import spock.lang.Ignore
import java.util.Properties
import java.lang.Void as Should
import mx.ipn.ambienta2mx.smartOwl.services.impl.SourceServiceImpl
import mx.ipn.ambienta2mx.smartOwl.enums.StateCode

class SourceServiceSpec extends Specification{
  
  @Shared service = new SourceServiceImpl()

  @Unroll("Should return the JSON when url is consulted given the latitude #_latitude and longitude #_longitude") 
  def "Should get the lines from file given an URL"(){
    given:"an url and the token from source"
      def propertiesFile = new Properties()
      def inputStream = this.class.classLoader.getResourceAsStream("sourcesInfo.properties")
      propertiesFile.load(inputStream) 
      def url = propertiesFile.getProperty "source.url"
      def token = propertiesFile.getProperty "source.token"

    and:"the location points"
      def locationPoints = [latitude:_latitude,
                           longitude:_longitude]
    when:
      def jsonStructure = service.getJSONFromUrlWithToken(url,token,locationPoints)

    then: 
      jsonStructure.humidity
      jsonStructure.ozone
    
    where:
      _latitude | _longitude
        19.395  | -99.025
        19.029  | -98.206 
        23.543  | -103.025
  }

  Should "get the urls of each station"(){ 
    given:"the state code"
      def stateCode = StateCode.DF
    when:
      def urls = service.getFileUrlsForStation(stateCode)
    then:
      urls.size() == 63  
      urls.first().startsWith("http://smn.cna.gob.mx")
  }
  
  Should "get the urls of the country"(){
    given:
      StateCode.metaClass.static.values = {[StateCode.BC,StateCode.DF]}
    when:
      def countryUrls = service.getFileUrlsOfCountry()
    then:
      countryUrls.first().latitude.class.simpleName == "BigDecimal"
      countryUrls.first().url.class.simpleName == "String"
  }

  Should "get the decimal coordinates from degrees to decimal"(){
    given:"the longitude with minutes and seconds"
      def latitude = ["28","53","47"]
      def longitude = ["113","33","37"] 
    when:
      def decimalCoordinates = service.convertCoordinatesToDecimal(latitude,longitude)
    then:
      decimalCoordinates.latitude == 28.8964
      decimalCoordinates.longitude == -113.5603
  }
  
  Should "get the latitude and longitude of the file"(){
    given:"the url"
      def fileUrl = "http://smn.cna.gob.mx/emas/txt/BC05_10M.TXT"
    when:
      def coordinates= service.getUrlCoordinates(fileUrl)
    then:
      coordinates.latitude == 28.8964
      coordinates.longitude == -113.5603
  }
}
