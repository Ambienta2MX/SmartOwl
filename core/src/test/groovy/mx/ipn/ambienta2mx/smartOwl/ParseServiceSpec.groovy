package mx.ipn.ambienta2mx.smartOwl

import mx.ipn.ambienta2mx.smartOwl.services.impl.ParseDataServiceImpl
import mx.ipn.ambienta2mx.smartOwl.services.impl.SourceServiceImpl
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared
import spock.lang.Ignore
import java.lang.Void as Should

class ParseServiceSpec extends Specification{
 
  @Shared service = new ParseDataServiceImpl()

  SourceServiceImpl sourceServiceImpl 
 
  @Ignore
  @Unroll("Should get the longitude #_longitude and altitude #_altitude given the url: #_url") 
  def "Should get the longitude and altitude"(){
    given:"an url and the lines of the readed file"
      def url = "http://smn.cna.gob.mx/emas/txt/DF15_10M.TXT" 
      
    when:
      def locationInfo = service.getLocationInfo(lines)
    then:
      assert true
    where:
      _longitude    | _altitude | _url
      "19°18'36\""  | 2348      | "http://smn.cna.gob.mx/emas/txt/DF15_10M.TXT"
      "99°12'14\""  | 2200      | "http://smn.cna.gob.mx/emas/txt/DF06_10M.TXT"
      "99°09'44\""  | 2946      | "http://smn.cna.gob.mx/emas/txt/DF08_10M.TXT"
      "99°09'29\""  | 2281      | "http://smn.cna.gob.mx/emas/txt/DF09_10M.TXT"
  }

  @Ignore
  @Unroll("Given the #_logitude and #_altitude") 
  Should "get the weather model"(){
    given:"the json data from collaborator"
      def sourceService = Mock(ParseDataService)
      1 * sourceService.getJSONFromUrlWithToken(url,token,coordinates) >> {
        ['apparentTemperature':53,
         'cloudCover':0.8,
         'dewPoint':40.5,
         'humidity':0.85]
      }
    when:
      def weatherInfo = service.getWeatherModelFromJSON()
    then:
      weatherInfo.class.simpleName == "Weather" 
      weatherInfo.humidity == 0.85
      weatherInfo.cloudCover == 0.8
  }

  Should "get the Levenshtein distance between the two strings"(){
    given:"two strings"
      def stringA = "Saturday" 
      def stringB = "Sunday"
    when:
      def distance = service.getLevenshteinDistance(stringA,stringB)
    then:
      distance == 3
  }
}
