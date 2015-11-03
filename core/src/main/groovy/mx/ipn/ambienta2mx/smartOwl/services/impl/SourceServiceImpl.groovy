package mx.ipn.ambienta2mx.smartOwl.services.impl

import org.springframework.stereotype.Service
import groovy.json.JsonSlurper
import org.ccil.cowan.tagsoup.Parser
import mx.ipn.ambienta2mx.smartOwl.services.SourceService
import mx.ipn.ambienta2mx.smartOwl.Api
import mx.ipn.ambienta2mx.smartOwl.enums.StateCode

@Service
class SourceServiceImpl implements SourceService{

  def getFileLines(url){
    def lines = []

    url.toURL().eachLine{ line ->
      lines << line
    }

    lines
  }

  def getJSONFromUrlWithToken(String url,String token,Map coordinates){
    def jsonSlurper = new JsonSlurper()
    def api = Api.instance
    api.setApiUrlAndClientKey(url,token)
    def connection = new URL("${api.getApiUrlRequest()}/${coordinates.latitude},${coordinates.longitude}")
    def jsonStructure = jsonSlurper.parseText(connection.text)
    jsonStructure.currently
  }

  def getFileUrlsForStations(StateCode stateCode){
    def stationUrls = []
    //TODO: Externalize conagua hostName
    def hostName = "smn.cna.gob.mx"
    def formedUrl = "emas/${stateCode.key}MP10T" 
    def url = "http://${hostName}/${formedUrl}.html"

    def slurper = new XmlSlurper(new Parser())
    def htmlParse = slurper.parse(url)
    stationUrls = htmlParse.'**'.find{ it.@id == 'mapa_solo' }.div.findAll{ it.@id.toString().startsWith("estacion") }*.@id 
    stationUrls 
  }

}
