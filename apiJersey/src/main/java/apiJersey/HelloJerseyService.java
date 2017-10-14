package apiJersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/helloJersey")
public class HelloJerseyService {
	
/*	
   http://localhost:8080/rest/helloJersey/client
   http://invest-182620.appspot.com/rest/helloJersey/client
 */
	
       @GET
       @Path("/client")
       @Produces(MediaType.APPLICATION_JSON)
       public Client getCliente() {
             Client client = new Client();
             client.setName("Hello World Camilo");
             client.setNumero(1);
             return client;
       }
}
