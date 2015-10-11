package mx.ipn.ambienta2mx.smartOwl

import mx.ipn.ambienta2mx.smartOwl.services.SourceService
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared
import java.util.Properties

class SourceServiceSpec extends Specification{
  
  @Shared service = new SourceService()
  
  @Unroll("Should return the file lines when #_url is consulted") 
  def "Should get the lines from file given an URL"(){
    given:"an url"
      def url = _url 
    when:
      def lines = service.getFileLines(url) 
    then: 
      lines.size() > 0
    where:
      _url << ["http://smn.cna.gob.mx/emas/txt/DF15_10M.TXT",
               "http://smn.cna.gob.mx/emas/txt/DF06_10M.TXT",
               "http://smn.cna.gob.mx/emas/txt/DF08_10M.TXT",
               "http://smn.cna.gob.mx/emas/txt/DF09_10M.TXT"]
  }


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
      jsonStructure.precipIntensity
    
    where:
      _latitude | _longitude
        19.395  | -99.025
        19.029  | -98.206 
        23.543  | -103.025
  }

}
