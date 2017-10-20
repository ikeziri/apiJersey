package investimento;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonSyntaxException;

import investimento.objeto.Acao;

public class InvestimentoController {
	public static ArrayList<Acao> listarAcoes(Connection connection) throws SQLException, IOException {
		ArrayList<Acao> acoes = new ArrayList<>();
		final String selectSql = "SELECT nome, custo, quantidade, valor, data FROM invest.acoes";
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

		try (ResultSet rs = connection.prepareStatement(selectSql).executeQuery()) {
			while (rs.next()) {
				Acao acao = new Acao();
				acao.setNome(rs.getString("nome"));
				acao.setCusto(rs.getBigDecimal("custo"));
				acao.setQuantidade(rs.getInt("quantidade"));
				acao.setValor(rs.getBigDecimal("valor"));
				acao.setData(fmt.format(rs.getDate("data")));
				apurarCotacaoDia(acao);
				acoes.add(acao);
			}
		}
		return acoes;
	}

	public static ArrayList<Acao> listarAcoesConsolidada(Connection connection) throws SQLException, IOException {
		ArrayList<Acao> acoes = new ArrayList<>();
		final String selectSql = "SELECT nome, SUM(quantidade) AS quantidade , SUM( CASE WHEN quantidade > 0  THEN valor * quantidade + custo ELSE custo END) / SUM( CASE WHEN quantidade > 0 THEN quantidade else 0 END) AS valor  FROM invest.acoes GROUP BY nome HAVING quantidade > 0";

		try (ResultSet rs = connection.prepareStatement(selectSql).executeQuery()) {
			while (rs.next()) {
				Acao acao = new Acao();
				acao.setNome(rs.getString("nome"));
				acao.setQuantidade(rs.getInt("quantidade"));
				acao.setValor(rs.getBigDecimal("valor"));
				apurarCotacaoDia(acao);
				acoes.add(acao);
			}
		}
		return acoes;
	}

	public static boolean comprarAcoes(Connection connection, ArrayList<Acao> acoes) throws SQLException {
		Statement statement = connection.createStatement();
		for (Iterator<Acao> iterator = acoes.iterator(); iterator.hasNext();) {
			Acao acao = (Acao) iterator.next();
			String insert = String.format(
					"INSERT INTO invest.acoes ( nome , data, quantidade, valor, custo ) VALUES(" + "'%s'," + "'%s',"
							+ "%s," + "%s," + "%s)",
					acao.getNome(), acao.getData(), acao.getQuantidade(), acao.getValor(), acao.getCusto());
			statement.executeUpdate(insert);
		}
		return true;
	}

	public static boolean venderAcoes(Connection connection, ArrayList<Acao> acoes) throws Exception {
		Statement statement = connection.createStatement();
		for (Iterator<Acao> iterator = acoes.iterator(); iterator.hasNext();) {
			Acao acao = (Acao) iterator.next();
			String select = String.format("SELECT SUM(quantidade) AS qtd FROM invest.acoes WHERE NOME = " + "'%s'",
					acao.getNome());
			try (ResultSet rs = connection.prepareStatement(select).executeQuery()) {
				if (rs.next()) {
					if (acao.getQuantidade().compareTo(rs.getInt("qtd")) > 0) {
						throw new Exception("Quantidade de acoes vendidas (" + acao.getQuantidade()
								+ ") maior que a quantidade existente (" + rs.getInt("qtd") + ")");
					}
				} else {
					throw new Exception("Ação não disponível para vennda.");
				}
			}
			String insert = String.format(
					"INSERT INTO invest.acoes ( nome , data, quantidade, valor, custo ) VALUES(" + "'%s'," + "'%s',"
							+ "%s," + "%s," + "%s)",
					acao.getNome(), acao.getData(), acao.getQuantidade() * -1, acao.getValor(), acao.getCusto());
			statement.executeUpdate(insert);
		}
		return true;
	}

	private static void apurarCotacaoDia(Acao acao) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet("http://cotacoes.economia.uol.com.br/snapQuote.html?code=" + acao.getNome()
					+ ".SA&_=" + (new Date()).getTime());
			request.addHeader("content-type", "application/json");
			HttpResponse result = httpClient.execute(request);
			String json = EntityUtils.toString(result.getEntity(), "UTF-8");
			System.out.println(json);
			com.google.gson.Gson gson = new com.google.gson.Gson();
			CotacaoUol cotacaoUol = gson.fromJson(json, CotacaoUol.class);
			acao.setValorAtual(new BigDecimal(cotacaoUol.getPrice().replace(',', '.')));
			acao.setValorAbertura(new BigDecimal(cotacaoUol.getOpen().replace(',', '.')));
		} catch (IOException e) {
			
		} catch (JsonSyntaxException e) {
//			quando nao consegue fazer o parser eh pq nao encontrou a cotação
		}
	}
}
