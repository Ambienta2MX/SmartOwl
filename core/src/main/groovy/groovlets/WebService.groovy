import mx.ipn.ambienta2mx.smartOwl.services.impl.ParseDataServiceImpl
import groovy.json.*

def parseDataService = new ParseDataServiceImpl()
def model = parseDataService.getWeatherModelFromJSON(params.latitude,params.longitude)
println new JsonBuilder(model).toPrettyString() 
response.contentType='application/json'
