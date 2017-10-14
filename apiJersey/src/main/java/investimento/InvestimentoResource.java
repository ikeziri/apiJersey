package investimento;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import apiJersey.ConnectionRequest;


@Path("/investimentoResource")
public class InvestimentoResource {


	/*
	 * http://localhost:8080/rest/investimentoResource/listaAcoes
	 * http://invest-182620.appspot.com/rest/investimentoResource/listaAcoes
	 */
	@GET
	@Path("/listaAcoes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaAcoes(@Context HttpServletRequest request) throws SQLException, IOException {
		return Response.ok(InvestimentoController.getAcoes( ConnectionRequest.getConnection(request)), MediaType.APPLICATION_JSON).build();
	}
}
