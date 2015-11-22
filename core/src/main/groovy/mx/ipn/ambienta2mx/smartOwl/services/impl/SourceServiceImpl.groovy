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
      "http://${hostName}${station.replace(/10.htm/,"_10M.TXT")}".toString()
    }
  }

  def convertCoordinatesToDecimal(def degressLatitude,def degressLongitude){
    def jsonSlurper = new JsonSlurper()
    def latitudeValues = degressLatitude.collect{ new Float(it) }
    def longitudeValues = degressLongitude.collect{ new Float(it) }
    def decimalLatitude = new BigDecimal(latitudeValues[0]+latitudeValues[1]/60+latitudeValues[2]/3600).setScale(4,BigDecimal.ROUND_HALF_UP)
    def decimalLongitude = new BigDecimal(longitudeValues[0]+longitudeValues[1]/60+longitudeValues[2]/3600).setScale(4,BigDecimal.ROUND_HALF_UP)
    def connection = new URL("http://mapserver.inegi.org.mx/traninv/servicios/geo/itrf92/${decimalLongitude}/${decimalLatitude}")
    def jsonStructure = jsonSlurper.parseText(connection.text)[0].itrf92

    [latitude:jsonStructure.y,
     longitude:jsonStructure.x]
  } 
  
  def getFileUrlsOfCountry(){
    def countryFileUrls = []

    StateCode.values().each{ stateCode ->
      getFileUrlsForStation(stateCode).each{ url ->
        def urlInfo = getUrlCoordinates(url)
        urlInfo.url = url

        countryFileUrls  << urlInfo
      }
    }

    countryFileUrls
  }

  def getUrlCoordinates(String fileUrl){
    def url = "http://smn.cna.gob.mx/emas/txt/BC05_10M.TXT"  
    def writer = url.toURL().filterLine{ it ==~ /.*[0-9].*\".*/ }
    def latitude_longitude = writer.toString().replaceAll(/[^\d]/," ")
    latitude_longitude = (latitude_longitude =~ /([0-9]+\s){3}/)
    convertCoordinatesToDecimal(latitude_longitude[1][0].tokenize(),latitude_longitude[0][0].tokenize())
  }

}
