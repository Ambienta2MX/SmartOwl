package mx.ipn.ambienta2mx.smartOwl.services.impl

import org.springframework.stereotype.Service
import groovy.json.JsonSlurper
import mx.ipn.ambienta2mx.smartOwl.services.SourceService
import mx.ipn.ambienta2mx.smartOwl.Api
import mx.ipn.ambienta2mx.smartOwl.enums.StateCode

@Service
class SourceServiceImpl implements SourceService{

  def getJSONFromUrlWithToken(String url,String token,Map coordinates){
    def jsonSlurper = new JsonSlurper()
    def api = Api.instance
    api.setApiUrlAndClientKey(url,token)
    def connection = new URL("${api.getApiUrlRequest()}/${coordinates.latitude},${coordinates.longitude}")
    def jsonStructure = jsonSlurper.parseText(connection.text)
    jsonStructure.currently
  }

  def getTablesWithPollutionData(String imageCode){
    //TODO:Externalize URL
    def url = "http://sg1.aqicn.org/aqicn/cache/webwgt/${imageCode}/widget.v1.js" 
    def text = url.toURL().readLines().first()
    def jsonSlurper = new JsonSlurper()
    text = text.replaceAll(/aqicnWidgetLoaderCallback\(|\);try.*\*\/|src='data:image\/png;base64,[a-zA-Z0-9+\/]+={0,2}'|nowrap[=true]?|true/,"")
    def json = jsonSlurper.parseText(text)
    def tables = (json.xxl =~ /<table.*>.*<\/table>/)[0]
    tables 
  } 

}
