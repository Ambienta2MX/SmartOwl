package mx.ipn.ambienta2mx.smartOwl.services.impl
import mx.ipn.ambienta2mx.smartOwl.services.ParseDataService
import mx.ipn.ambienta2mx.smartOwl.domain.Weather

class ParseDataServiceImpl implements ParseDataService{

  //TODO:Desacoplar objecto mediante inversión de control con Spring 
  def sourceService 

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
