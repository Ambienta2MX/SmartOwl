package mx.ipn.ambienta2mx.smartOwl.services.impl
import mx.ipn.ambienta2mx.smartOwl.Api
import groovy.json.JsonSlurper
import mx.ipn.ambienta2mx.smartOwl.services.SourceService

class SourceServiceImpl implements SourceService{
  
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
