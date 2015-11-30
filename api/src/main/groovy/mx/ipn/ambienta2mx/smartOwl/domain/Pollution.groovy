package mx.ipn.ambienta2mx.smartOwl.domain

@groovy.transform.Canonical
class Pollution{
  String airQualityDescription
  Integer airQuality
  Double ozone
  Double sulphurDiode //SO2
  Double nitrogenDioxide //NO2
  Double carbonMonoxide //CO
  Double uv
  List<String> provider
  Date dateCreated
}
