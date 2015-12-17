package mx.ipn.ambienta2mx.smartOwl.services.impl

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import wslite.rest.*
import mx.ipn.ambienta2mx.smartOwl.services.PollutionService
import mx.ipn.ambienta2mx.smartOwl.domain.Pollution

@Service
class PollutionServiceImpl implements PollutionService{
  
  @Value('${modules.path.hardAnt}')
  String hardAntUrl 
  @Value('${modules.path.fastEagle}')
  String fastEagleUrl

  @Autowired
  ParseDataServiceImpl parseDataService

  @Autowired
  SourceServiceImpl sourceService

  def findPollutionModel(String latitude,String longitude){
    def pollution = new Pollution()
    pollution.dateCreated = new Date() 
    pollution = findPollutionModelFromAirQuality(latitude,longitude,pollution)
    pollution = findPollutionModelFromForecast(latitude,longitude,pollution)
    def fields = pollution.class.declaredFields.findAll{ !it.synthetic }*.name
    def pollutionModel = [:]

    fields.each{ field ->
      pollutionModel[field] = pollution[field]
    }

    pollutionModel.latitude = latitude
    pollutionModel.longitude = longitude
    def client = new RESTClient("${hardAntUrl}")
    def response = client.post(path:"/pollution",accept:ContentType.JSON){
      type ContentType.JSON
      json pollutionModel
    }
   
    new JsonSlurper().parseText(response.contentAsString) 
  }

  def findPollutionModelFromAirQuality(String latitude,String longitude,Pollution pollution){
    def url = new URL("${fastEagleUrl}/pollutionStation?latitude=$latitude&longitude=$longitude")
    def imageCode = new JsonSlurper().parse(url).img
    def html = sourceService.getTablesWithPollutionData(imageCode)
    def fields = pollution.class.declaredFields.findAll{ !it.synthetic }*.name
    def pollutionModel = parseDataService.getPollutionModelFromHtml(html)

    if(pollutionModel)
      pollution.provider << "Air Quality" 

    pollution
  }

  def findPollutionModelFromForecast(String latitude,String longitude,Pollution pollution){
    parseDataService.getPollutionModelFromJSON(latitude,longitude,pollution)
  }
 
}
