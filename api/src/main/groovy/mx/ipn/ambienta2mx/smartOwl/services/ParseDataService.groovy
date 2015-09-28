package mx.ipn.ambienta2mx.smartOwl.services

interface ParseDataService{

  def getWeatherModelFromJSON(latitude,longitude);
  def pollutionModelFromJSON(latitude,longitude);

}
