package mx.ipn.ambienta2mx.smartOwl.domain

@groovy.transform.Canonical
class Weather{
  Date weatherTime
  String description
  Double precipIntensity
  Double precipProbability
  Double rainfallIntensity
  Double rainfallProbability
  Double temperature
  Double apparentTemperature
  Double dewPoint
  Double humidity
  Double windSpeed
  Double windDirection
  Double windBearing  
  Double visibility
  Double cloudCover
  Double pressure
  List<String> provider = []
  Date dateCreated
}
