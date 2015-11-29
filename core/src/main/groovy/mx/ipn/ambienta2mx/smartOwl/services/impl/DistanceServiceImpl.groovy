package mx.ipn.ambienta2mx.smartOwl.services.impl

import org.springframework.stereotype.Service
import mx.ipn.ambienta2mx.smartOwl.services.DistanceService

@Service
class DistanceServiceImpl implements DistanceService{

  def getLevenshteinDistance(String stringA,String stringB){
    def m = stringA.size()
    def n = stringB.size()
    int[][] matrix = new int[m+1][n+1]
    fillMatrixWithZero(matrix)

    (1..m).each{ i ->
      matrix[i][0] = i
    }

    (1..n).each{ j ->
      matrix[0][j] = j
    }

    (1..n).each{ j ->
      (1..m).each{ i ->
        if(stringA[i-1] == stringB[j-1])
          matrix[i][j] = matrix[i-1][j-1]
        else
          matrix[i][j] = minimum(matrix[i-1][j]+1,
                                 matrix[i][j-1]+1,
                                 matrix[i-1][j-1]+1)
      }
    }

    matrix[m][n]
  } 

  def minimum(Long a,Long b,Long c){
    Math.min(Math.min(a,b),c)
  }

  private void fillMatrixWithZero(def matrix){
    matrix.size().times{ i ->
      matrix[i].size().times{ j ->
        matrix[i][j] = 0;
      }
    }
  }

}
