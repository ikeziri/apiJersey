package apiJersey;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/jpaService")
public class JpaService {


	/*
	 * http://localhost:8080/rest/jpaService/consulta
	 * http://invest-182620.appspot.com/rest/jpaService/consulta
	 */
	@GET
	@Path("/consulta")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCliente(@Context HttpServletRequest request, @DefaultValue("111") @QueryParam("numero") int numero) throws SQLException, IOException {
		Connection conn = ConnectionRequest.getConnection(request);
		final String selectSql = "SELECT nome FROM invest.acoes";
		ArrayList<Client> clientes = new ArrayList<>();
		try (ResultSet rs = conn.prepareStatement(selectSql).executeQuery()) {
			while (rs.next()) {
				String savedIp = rs.getString("nome");
				Client client = new Client();
				client.setName(savedIp);
				client.setNumero(numero);
				clientes.add(client);
			}
		}

		return Response.ok(clientes, MediaType.APPLICATION_JSON).build();
	}
}
