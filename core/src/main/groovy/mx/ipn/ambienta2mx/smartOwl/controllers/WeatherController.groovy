package mx.ipn.ambienta2mx.smartOwl.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import mx.ipn.ambienta2mx.smartOwl.services.impl.WeatherServiceImpl

@RestController
@RequestMapping(value="/weather")
class WeatherController{
  
  @Autowired
  WeatherServiceImpl weatherServiceImpl 

  @RequestMapping(method= RequestMethod.GET)
  ResponseEntity<Map> show(@RequestParam(value="latitude")String latitude,@RequestParam(value="longitude")String longitude){
    def model = weatherServiceImpl.findWeatherModel(latitude,longitude)
    new ResponseEntity<Map>(model,HttpStatus.OK)
  }
}
