package investimento;

import java.io.IOException;
import java.math.BigDecimal;
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

import apiJersey.ConnectionRequest;
import investimento.objeto.Acao;


@Path("/investimentoResource")
public class InvestimentoResource {


	/*
	 * http://localhost:8080/rest/investimentoResource/listarAcoes
	 * http://invest-182620.appspot.com/rest/investimentoResource/listarAcoes
	 */
	@GET
	@Path("/listarAcoes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarAcoes(@Context HttpServletRequest request) throws SQLException, IOException {
		return Response.ok(InvestimentoController.listarAcoes( ConnectionRequest.getConnection(request)), MediaType.APPLICATION_JSON).build();
	}
	
	/*
	 * http://localhost:8080/rest/investimentoResource/listarAcoesConsolidada
	 * http://invest-182620.appspot.com/rest/investimentoResource/listarAcoesConsolidada
	 */
	@GET
	@Path("/listarAcoesConsolidada")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarAcoesConsolidada(@Context HttpServletRequest request) throws SQLException, IOException {
		return Response.ok(InvestimentoController.listarAcoesConsolidada( ConnectionRequest.getConnection(request)), MediaType.APPLICATION_JSON).build();
	}

	/*
	 * http://localhost:8080/rest/investimentoResource/comprarAcao?nome=AAAA&quantidade=900&valor=10.1&custo=15
	 * http://invest-182620.appspot.com/rest/investimentoResource/comprarAcao?nome=AAAA&quantidade=900&valor=10.1&custo=15
	 */
	@GET
	@Path("/comprarAcao")
	@Produces(MediaType.APPLICATION_JSON)
	public Response comprarAcao(@Context HttpServletRequest request
			,@QueryParam("nome") String nome
			,@DefaultValue("CURRENT") @QueryParam("data") String data
			,@QueryParam("quantidade") Integer quantidade
			,@QueryParam("valor") BigDecimal valor
			,@QueryParam("custo") BigDecimal custo
			) throws SQLException, IOException {
		ArrayList<Acao> acoes = new ArrayList<>();
		Acao acao = new Acao(nome, data, quantidade, valor, custo);
		acoes.add(acao);
		System.out.println(acao.toString());
		return Response.ok(InvestimentoController.comprarAcoes(ConnectionRequest.getConnection(request), acoes), MediaType.APPLICATION_JSON).build();
	}
	/*
	 * http://localhost:8080/rest/investimentoResource/venderAcao?nome=AAAA&quantidade=900&valor=10.1&custo=15
	 * http://invest-182620.appspot.com/rest/investimentoResource/venderAcao?nome=AAAA&quantidade=900&valor=10.1&custo=15
	 */
	@GET
	@Path("/venderAcao")
	@Produces(MediaType.APPLICATION_JSON)
	public Response venderAcao(@Context HttpServletRequest request
			,@QueryParam("nome") String nome
			,@DefaultValue("CURRENT") @QueryParam("data") String data
			,@QueryParam("quantidade") Integer quantidade
			,@QueryParam("valor") BigDecimal valor
			,@QueryParam("custo") BigDecimal custo
			) throws Exception {
		ArrayList<Acao> acoes = new ArrayList<>();
		Acao acao = new Acao(nome, data, quantidade, valor, custo);
		acoes.add(acao);
		System.out.println(acao.toString());
		return Response.ok(InvestimentoController.venderAcoes(ConnectionRequest.getConnection(request), acoes), MediaType.APPLICATION_JSON).build();
	}
}
