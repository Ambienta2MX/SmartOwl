package mx.ipn.ambienta2mx.smartOwl

@Singleton
class Api{

  String apiUrl
  String clientKey 

  def getApiUrlRequest(){
    "${apiUrl}/${clientKey}"
  }  
  
  def setApiUrlAndClientKey(url,key){
    apiUrl = url
    clientKey = key
  }

}
