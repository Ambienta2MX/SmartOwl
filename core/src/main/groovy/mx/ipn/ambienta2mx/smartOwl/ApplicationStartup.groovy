package mx.ipn.ambienta2mx.smartOwl

import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import mx.ipn.ambienta2mx.smartOwl.services.impl.SourceServiceImpl

@Component
class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{
  
  @Autowired
  StringRedisTemplate template
  
  @Autowired
  SourceServiceImpl sourceServiceImpl 
    
  @Override
  public void onApplicationEvent(final ContextRefreshedEvent event){
    if(!this.template.keys("*").size()){
      //def urls = sourceServiceImpl.getFileUrlsOfCountry() 
    }
    return 
  }

}
