package mx.ipn.ambienta2mx.smartOwl.services.impl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.jsoup.Jsoup
import mx.ipn.ambienta2mx.smartOwl.services.ParseDataService
import mx.ipn.ambienta2mx.smartOwl.services.SourceService
import mx.ipn.ambienta2mx.smartOwl.domain.Weather
import mx.ipn.ambienta2mx.smartOwl.domain.Pollution
import mx.ipn.ambienta2mx.smartOwl.enums.AirQualityDescription

@Service
class ParseDataServiceImpl implements ParseDataService{

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

  def getWeatherModelFromJSON(latitude,longitude){
    def sourceService = new SourceServiceImpl()

    //Quitar url y token y obtener fuentes desde base
    def url = "https://api.forecast.io/forecast"
    def token = "754371a499f300218874a3d937c09830"

    def model = sourceService.getJSONFromUrlWithToken(url,token,['latitude':latitude,
                                                                 'longitude':longitude])

    def weather = new Weather(precipIntensity:model.precipIntensity,
                              precipProbability:model.precipProbability,
                              temperature:model.temperature,
                              apparentTemperature:model.apparentTemperature,
                              humidity:model.humidity,
                              windSpeed:model.windSpeed,
                              windBearing:model.windBearing,
                              visibility:model.visibility,
                              cloudCover:model.cloudCover,
                              pressure:model.pressure)

    ['WeatherInfo':weather]
  }

  def getPollutionModelFromJSON(latitude,longitude){
    def sourceService = new SourceServiceImpl()

    //Quitar url y token y obtener fuentes desde base
    def url = "https://api.forecast.io/forecast"
    def token = "754371a499f300218874a3d937c09830"

    def model = sourceService.getJSONFromUrlWithToken(url,token,['latitude':latitude,
                                                                 'longitude':longitude])

    def pollution = new Pollution(airQuality:model.airQuality,
                                  ozone:model.ozone,
                                  sulphurDiode:model.sulphurDiode,
                                  nitrogenDioxide:model.nitrogenDioxide,
                                  carbonMonoxide:model.carbonMonoxide,
                                  uv:model.uv)
    ['PollutionInfo':pollution]

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
    AirQualityDescription.valueOf(qualityAir.toUpperCase())
  }

}
