package mx.ipn.ambienta2mx.smartOwl
import java.lang.Void as Should

class ParseIntegrationSpec extends Specification{

  @Shared service = new ParseDataService()  

  Should "parse the text of the file"(){
    given:"the information file"
      def weatherFile = new File()
    then:
      weatherFile
  }  
  
}
