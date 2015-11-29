package mx.ipn.ambienta2mx.smartOwl.services

interface ParseDataService{
  def getWeatherModelFromFile(File file)
  def getWeatherModelFromJSON(latitude,longitude);
  def getPollutionModelFromJSON(latitude,longitude);
}
