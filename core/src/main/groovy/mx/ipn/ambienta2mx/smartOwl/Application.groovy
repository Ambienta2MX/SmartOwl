package mx.ipn.ambienta2mx.smartOwl

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
class Application{

  static void main(args){
    ApplicationContext ctx = SpringApplication.run(Application,args) 
  } 

}
