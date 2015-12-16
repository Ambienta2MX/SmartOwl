package mx.ipn.ambienta2mx.smartOwl.services.impl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.jsoup.Jsoup
import mx.ipn.ambienta2mx.smartOwl.services.ParseDataService
import mx.ipn.ambienta2mx.smartOwl.services.SourceService
import mx.ipn.ambienta2mx.smartOwl.domain.Weather
import mx.ipn.ambienta2mx.smartOwl.domain.Pollution
import mx.ipn.ambienta2mx.smartOwl.enums.AirQualityDescription

@Service
class ParseDataServiceImpl implements ParseDataService{

  @Value('${sources.forecast.url}')
  String apiUrl
  @Value('${sources.forecast.token}')
  String token

  @Autowired
  SourceService sourceService

  def getWeatherModelFromFile(File file){
    def classFields = Weather.declaredFields.grep{ !it.synthetic }*.name
    def dataMatrix = []
    def infoLines = []

    def dateRegex = /([0-9]{2}\/){2}([0-9]){4}\s([0-9]{2})\:[0-9]{2}/
    def dataRegex = /[0-9]+(\.[0-9]+)?/

    def linesWithoutDate = file.readLines().collect{ line ->
      line.replaceAll(dateRegex,'').trim()
    }
    def nonEmptyLines = linesWithoutDate.findAll{ it }
    def dividedLine = []
    nonEmptyLines.each{ line ->
      dividedLine = line.tokenize(" ")
      if(dividedLine.every{ item -> item ==~ dataRegex})
        dataMatrix << dividedLine
      else
        infoLines << dividedLine
    }
  }

  def getWeatherModelFromJSON(latitude,longitude,weather){
    def model = sourceService.getJSONFromUrlWithToken(apiUrl,token,['latitude':latitude,
                                                                    'longitude':longitude])

    def fields = Weather.class.declaredFields.grep{ !it.synthetic }*.name
    fields.each{ field ->
      weather[field] = weather[field] ?: model[field]
    }

    weather.provider << "Forecast.io"
    weather
  }

  def getPollutionModelFromJSON(latitude,longitude,pollution){
    def model = sourceService.getJSONFromUrlWithToken(apiUrl,token,['latitude':latitude,
                                                                    'longitude':longitude])

    def fields = Pollution.class.declaredFields.grep{ !it.synthetic }*.name

    fields.each{ field ->
      pollution[field] = pollution[field] ?: model[field]
    }
    
    pollution 
  }

  def getPollutionModelFromHtml(String html){
    def table = Jsoup.parse(html) 
    def data = table.select('.tdcur')
    def airQualityDiv = table.select(".aqivalue")

    [airQualityDescription:getQualityAir(airQualityDiv.attr("title")),
     airQuality:airQualityDiv.text(),
     sulphurDiode:data.find{ it.attr("id") == "cur_so2"}.text(),
     nitrogenDioxide:data.find{ it.attr("id") == "cur_no2"}.text(),
     carbonMonoxide:data.find{ it.attr("id") == "cur_o3"}.text()] 
  }

  private String getQualityAir(String qualityAir){
    AirQualityDescription.valueOf(qualityAir.toUpperCase().replace(" ","_"))
  }

}
