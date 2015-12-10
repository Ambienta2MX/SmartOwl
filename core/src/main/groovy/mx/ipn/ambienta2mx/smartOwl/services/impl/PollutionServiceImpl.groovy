package mx.ipn.ambienta2mx.smartOwl.services.impl

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import wslite.rest.*

@Service
class PollutionServiceImpl{
  
  @Value("${modules.path.hardAnt}")
  String hardAntUrl 
  @Value("${modules.path.fastEagle}")
  String fastEagleUrl

  @Autowired
  ParseDataServiceImpl parseDataService

  @Autowired
  SourceServiceImpl sourceService

  def findPollutionModel(String latitude,String longitude){
    //TODO:Externalize urls
    def url = new URL("http://127.0.0.1:7777/pollutionStation?latitude=$latitude&longitude=$longitude")
    def imageCode = new JsonSlurper().parse(url).img
    def html = sourceService.getTablesWithPollutionData(imageCode)
    def pollutionModel = parseDataService.getPollutionModelFromHtml(html)
    pollutionModel.latitude = latitude
    pollutionModel.longitude = longitude
    def client = new RESTClient("http://192.168.100.4:7001")
    def response = client.post(path:"/pollution",accept:ContentType.JSON){
      type ContentType.JSON
      json pollutionModel
    }
   
    new JsonSlurper().parseText(response.contentAsString) 
  }

}
