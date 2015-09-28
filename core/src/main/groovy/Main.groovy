import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.*
import groovy.servlet.*
import static org.eclipse.jetty.servlet.ServletContextHandler.* 

class Main{
  static main(def args){
    def server = new Server(9090)  
    def context = new ServletContextHandler(server, "/", SESSIONS)
    context.resourceBase = "src/main/groovy/groovlets"
    context.addServlet(GroovyServlet, "*.groovy")
    server.start()
  } 
}
