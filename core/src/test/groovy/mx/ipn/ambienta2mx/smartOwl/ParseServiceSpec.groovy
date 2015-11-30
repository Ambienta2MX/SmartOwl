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

  Should "get the pollution info model"(){
    given:"the html" 
      def path = this.class.classLoader.getResource("HtmlToParse.html").getFile()
      def html = new File(path).text
    when:
      def model = service.getPollutionModelFromHtml(html)
    then:
      model.airQuality == "78"
  }

}
