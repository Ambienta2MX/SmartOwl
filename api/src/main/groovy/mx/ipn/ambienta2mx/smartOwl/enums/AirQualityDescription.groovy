package mx.ipn.ambienta2mx.smartOwl.enums

enum AirQualityDescription{
  MODERATE("Moderado"),
  GOOD("Bueno"),
  NO_DATA("Sin datos"),
  UNHEALTHY("Insalubre"),
  UNHEALTHY_FOR_SENSITIVE_GROUPS("Insalubre para grupos sensibles"),
  VERY_UNHEALTHY("Muy insalubre"),
  HAZARDOUS("Arriesgado")

  final String value
  AirQualityDescription(String value){ this.value = value }

  String toString(){ value }
  String getKey(){ name() }
}
