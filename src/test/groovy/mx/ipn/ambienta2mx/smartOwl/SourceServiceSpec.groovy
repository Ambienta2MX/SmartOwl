package mx.ipn.ambienta2mx.smartOwl

import mx.ipn.ambienta2mx.smartOwl.services.SourceService
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

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

  @Unroll("Should return the JSON when https://api.forecast.io/forecast/ is consulted given the latitude #_latitude and longitude #_longitude") 
  def "Should get the lines from file given an URL"(){
    given:"an url"
      def url = "https://api.forecast.io/forecast/"
    when:
      def jsonStructure = service.getJSONStructure(url,_latitude,_longitude)
    then: 
     
    where:
      _latitude | _longitude
        19.395  | -99.025
        19.029  | -98.206 
        23.543  | -103.025
  }

}
