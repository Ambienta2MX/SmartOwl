package mx.ipn.ambienta2mx.smartOwl.services.impl

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import wslite.rest.*
import mx.ipn.ambienta2mx.smartOwl.services.WeatherService
import mx.ipn.ambienta2mx.smartOwl.domain.Weather

@Service
class WeatherServiceImpl implements WeatherService{
  
  @Value('${modules.path.hardAnt}')
  String hardAntUrl 
  @Value('${modules.path.fastEagle}')
  String fastEagleUrl
  @Value('${sources.weatherUnderground.url}')
  String weatherUndergroundUrl

  @Autowired
  ParseDataServiceImpl parseDataService

  @Autowired
  SourceServiceImpl sourceService

  def findWeatherModel(String latitude,String longitude){
    def weather = new Weather()    
    weather.dateCreated = new Date()
    weather = getDataFromWeatherUnderground(weather,latitude,longitude)
    weather = getDataFromForecast(weather,latitude,longitude)
    def fields = weather.class.declaredFields.findAll{ !it.synthetic }*.name
    def weatherModel = [:]
    fields.each{ field ->
      weatherModel[field] = weather[field]
    } 
    weatherModel.latitude = latitude
    weatherModel.longitude = longitude

    def client = new RESTClient("${hardAntUrl}")
    def response = client.post(path:"/weather",accept:ContentType.JSON){
      type ContentType.JSON
      json weatherModel
    }

    weather
  }

  def getDataFromWeatherUnderground(Weather weather,String latitude,String longitude){
    def weatherUndergroundVertical = new URL("${fastEagleUrl}/weatherUndergroundStation?latitude=$latitude&longitude=$longitude")
    def stationCode = new JsonSlurper().parse(weatherUndergroundVertical).id.first()
    def client = new RESTClient(weatherUndergroundUrl)
    def response = client.get(path:"/stationdata?station=${stationCode}&format=json").json.conds["${stationCode}"]

    if(response){
      weather.humidity = weather.humidity ?: new Double(response.humidity ?: 0)
      weather.visibility = weather.visibility ?: new Double(response.visibilitysm ?: 0)
      weather.weatherTime = weather.weatherTime ?: new Date(new Long(response.updated ?: 0))
      weather.dewPoint = weather.dewPoint ?: new Double(response.mindewpoint ?: 0)
      weather.temperature = weather.temperature ?: (((new Double(response.mintemp ?: 0) + new Double(response.maxtemp ?: 0))/2)-32)/1.8
      weather.pressure = weather.pressure ?: (new Double(response.minpressure ?: 0) + new Double(response.maxpressure ?: 0))/2
      weather.provider << 'WeatherUnderground'
    }

    weather
  }

  def getDataFromForecast(Weather weather,String latitude,String longitude){
    parseDataService.getWeatherModelFromJSON(latitude,longitude,weather)
  } 

}
