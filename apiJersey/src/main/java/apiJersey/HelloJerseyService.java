package apiJersey;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
       public Client getCliente(@DefaultValue("111") @QueryParam("numero") int numero) {
             Client client = new Client();
             client.setName("Hello World Camilo");
             client.setNumero(numero);
             return client;
       }
}
