package mx.ipn.ambienta2mx.smartOwl.services.impl

import org.springframework.stereotype.Service
import mx.ipn.ambienta2mx.smartOwl.services.DistanceService

@Service
class DistanceServiceImpl implements DistanceService{

  def levenshteinDistance(String stringA,String stringB){

  } 

  def minimum(Long a,Long b,Long c){
    Math.min(Math.min(a,b),c)
  }
}
