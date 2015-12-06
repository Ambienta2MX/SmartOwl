package mx.ipn.ambienta2mx.smartOwl.services.impl

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import wslite.rest.*

@Service
class PollutionServiceImpl{
  
  @Autowired
  ParseDataServiceImpl parseDataService

  @Autowired
  SourceServiceImpl sourceService

  def findPollutionModel(String latitude,String longitude){
    // conaguaStation[url], weatherUndergoundStation[id]
    def url = new URL("http://127.0.0.1:7777/pollutionStation?latitude=$latitude&longitude=$longitude")
    def imageCode = new JsonSlurper().parse(url).img
    def html = sourceService.getTablesWithPollutionData(imageCode)
    def pollutionModel = parseDataService.getPollutionModelFromHtml(html)
    //TODO: Externalize HardAntUrl
    def client = new RESTClient("http://127.0.0.1:7001")
    def response = client.post(path:"/pollution",query:[sulphurDiode:pollutionModel.sulphurDiode,
                                                        latitude:latitude,longitude:longitude])
    
    pollutionModel
  }

}
