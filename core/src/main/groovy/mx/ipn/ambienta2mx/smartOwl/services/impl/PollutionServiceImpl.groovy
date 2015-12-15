package mx.ipn.ambienta2mx.smartOwl.services.impl

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import wslite.rest.*
import mx.ipn.ambienta2mx.smartOwl.services.PollutionService

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
    def url = new URL("${fastEagleUrl}/pollutionStation?latitude=$latitude&longitude=$longitude")
    def imageCode = new JsonSlurper().parse(url).img
    def html = sourceService.getTablesWithPollutionData(imageCode)
    def pollutionModel = parseDataService.getPollutionModelFromHtml(html)
    pollutionModel.latitude = latitude
    pollutionModel.longitude = longitude
    def client = new RESTClient("${hardAntUrl}")
    def response = client.post(path:"/pollution",accept:ContentType.JSON){
      type ContentType.JSON
      json pollutionModel
    }
   
    new JsonSlurper().parseText(response.contentAsString) 
  }

}
