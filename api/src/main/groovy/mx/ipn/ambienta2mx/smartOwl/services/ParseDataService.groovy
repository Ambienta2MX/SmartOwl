package mx.ipn.ambienta2mx.smartOwl.services

interface ParseDataService{
  def getWeatherModelFromFile(File file)
  def getWeatherModelFromJSON(latitude,longitude,weather);
  def getPollutionModelFromJSON(latitude,longitude,pollution);
  def getPollutionModelFromHtml(String html)
}
