package mx.ipn.ambienta2mx.smartOwl.services.impl

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PollutionServiceImpl{
  
  @Autowired
  ParseDataServiceImpl parseDataService

  @Autowired
  SourceServiceImpl sourceService

  def findPollutionModel(String latitude,String longitude){
    // conaguaStation[url], weatherUndergoundStation[id]
    def url = new URL("http://localhost/api/fasteagle/pollutionStation?latitude=$latitude&longitude=$longitude")
    def imageCode = new JsonSlurper().parse(url).img
    def html = sourceService.getTablesWithPollutionData(imageCode)
    parseDataService.getPollutionModelFromHtml(html)
    //TODO post to localhost/api/hardant/pollution
  }
}