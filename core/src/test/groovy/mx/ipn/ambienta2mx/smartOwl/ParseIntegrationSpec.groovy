package mx.ipn.ambienta2mx.smartOwl
import mx.ipn.ambienta2mx.smartOwl.services.impl.ParseDataServiceImpl
import java.lang.Void as Should
import spock.lang.Specification 
import spock.lang.Shared

class ParseIntegrationSpec extends Specification{

  @Shared service = new ParseDataServiceImpl()

  Should "parse the text of the file"(){
    given:"the file name"
      def fileName = "DFFile.txt"
    and:"the file with the weather and pollution data"
      def urlFile = this.class.classLoader.getResource(fileName).getFile()
      def file = new File(urlFile) 
    when: 
      def model = service.getWeatherModelFromFile(file)
    then:
      model 
  }

}
