package mx.ipn.ambienta2mx.smartOwl.services.impl
import mx.ipn.ambienta2mx.smartOwl.services.ParseDataService
import mx.ipn.ambienta2mx.smartOwl.domain.Weather

class ParseDataServiceImpl implements ParseDataService{

  //TODO:Desacoplar objecto mediante inversión de control con Spring 
  def sourceService 

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

  }
  
}
