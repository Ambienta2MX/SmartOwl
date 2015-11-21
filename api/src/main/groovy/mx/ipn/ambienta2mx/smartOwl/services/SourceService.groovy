package mx.ipn.ambienta2mx.smartOwl.services
import mx.ipn.ambienta2mx.smartOwl.enums.StateCode

interface SourceService{

  def getJSONFromUrlWithToken(String url,String token,Map coordinates)
  def getFileUrlsForStation(StateCode stateCode)

}
