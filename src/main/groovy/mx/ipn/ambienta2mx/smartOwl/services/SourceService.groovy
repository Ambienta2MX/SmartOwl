package mx.ipn.ambienta2mx.smartOwl.services
import mx.ipn.ambienta2mx.smartOwl.Api
import groovy.json.JsonSlurper

class SourceService{
  
  def getFileLines(url){
    def lines = []  

    url.toURL().eachLine{ line ->
      lines << line
    }

    lines
  }

  def getJSONFromUrlWithToken(url,token,coordinates){
    def jsonSlurper = new JsonSlurper()
    def api = Api.instance
    api.setApiUrlAndClientKey(url,token)
    def connection = new URL("${api.getApiUrlRequest()}/${coordinates.latitude},${coordinates.longitude}") 
    def jsonStructure = jsonSlurper.parseText(connection.text) 
    jsonStructure.currently
  }
  
}
