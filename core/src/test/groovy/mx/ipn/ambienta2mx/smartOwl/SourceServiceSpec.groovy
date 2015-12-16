package mx.ipn.ambienta2mx.smartOwl

import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared
import spock.lang.Ignore
import java.util.Properties
import java.lang.Void as Should
import mx.ipn.ambienta2mx.smartOwl.services.impl.SourceServiceImpl
import mx.ipn.ambienta2mx.smartOwl.enums.StateCode
import org.springframework.test.util.ReflectionTestUtils

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

  Should "get the pollution info"(){
    given:"the image code"
      String imageCode = "_842w8j28siIzOV8_1NFXISI_OSMzNzMnOR8A"
    when:
      ReflectionTestUtils.setField(service,"airPollutionUrl","http://sg1.aqicn.org/aqicn/cache/webwgt");
      def html= service.getTablesWithPollutionData(imageCode)
    then:
      html.startsWith("<table")
  }

}
