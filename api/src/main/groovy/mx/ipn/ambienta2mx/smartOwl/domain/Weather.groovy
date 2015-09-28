package mx.ipn.ambienta2mx.smartOwl.domain

@groovy.transform.Canonical
class Weather{
  Date weatherTime
  String description
  Double precipIntensity
  Double precipProbability
  Double temperature  
  Double apparentTemperature
  Double humidity
  Double windSpeed
  Double windBearing  
  Double visibility
  Double cloudCover
  Double pressure
}
