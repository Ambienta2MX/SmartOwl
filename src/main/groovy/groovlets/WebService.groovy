import mx.ipn.ambienta2mx.smartOwl.services.ParseDataService
import groovy.json.*

def parseDataService = new ParseDataService()

def model = parseDataService.getWeatherModelFromJSON(params.latitude,params.longitude)
println new JsonBuilder(model).toPrettyString() 
response.contentType='application/json'
