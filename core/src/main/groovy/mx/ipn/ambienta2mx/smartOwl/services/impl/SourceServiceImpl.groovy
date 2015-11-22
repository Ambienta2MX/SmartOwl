package mx.ipn.ambienta2mx.smartOwl.services.impl

import org.springframework.stereotype.Service
import groovy.json.JsonSlurper
import org.ccil.cowan.tagsoup.Parser
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

  def getFileUrlsForStation(StateCode stateCode){
    def stationUrls = []
    //TODO: Externalize CONAGUA hostName
    def stations = []
    def hostName = "smn.cna.gob.mx"
    def formedUrl = "emas/${stateCode.key}MP10T" 
    def url = "http://${hostName}/${formedUrl}.html"

    def slurper = new XmlSlurper(new Parser())
    def htmlParse = slurper.parse(url)
    stationUrls = htmlParse.'**'.find{ it.@id == 'mapa_solo' }.div.findAll{ it.@id.toString().startsWith("estacion") }*.@onclick
    stationUrls.each{ station ->
      stations << (station.toString() =~ /\/.*.htm/)[0]
    }
    stations = stations*.replace("estac","txt")
    stations.collect{ station ->
      "http://${hostName}${station.replace(/10.htm/,"_10M.TXT")}"
    }
  }

  def convertCoordinatesToDecimal(def coordinates){
    def values = coordinates.collect{ new Float(it) }
    new BigDecimal(values[0]+values[1]/60+values[2]/3600).setScale(4,BigDecimal.ROUND_HALF_UP)
  } 
  
  def getFileUrlsOfCountry(){
    def countryFileUrls = [:]

    StateCode.values().each{ stateCode ->
      countryFileUrls[stateCode.key] = getFileUrlsForStation(stateCode)
    }

    countryFileUrls
  }

}
