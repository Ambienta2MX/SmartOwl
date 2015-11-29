package mx.ipn.ambienta2mx.smartOwl

import mx.ipn.ambienta2mx.smartOwl.services.impl.DistanceServiceImpl
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared
import java.lang.Void as Should

class DistanceServiceSpec extends Specification{

  @Shared service = new DistanceServiceImpl()   

  Should "get the Levenshtein distance between the two strings"(){
    given:"two strings"
      def stringA = "sittng" 
      def stringB = "kitten"
    when:
      def distance = service.getLevenshteinDistance(stringA,stringB)
    then:
      distance == 3
  }

}
