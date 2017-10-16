package investimento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import investimento.objeto.Acao;

public class InvestimentoController {
	public static ArrayList<Acao> getAcoes(Connection connection) throws SQLException {
		ArrayList<Acao> acoes= new ArrayList<>();
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
					"INSERT INTO invest.acoes ( nome , data, quantidade, valor, custo ) VALUES("  +
							"'%s'," +
							"'%s'," +
							"%s," +
							"%s," +
							"%s)"
					,acao.getNome()
					,acao.getData()
					,acao.getQuantidade()
					,acao.getValor()
					,acao.getCusto()
					);
			statement.executeUpdate(insert);
		}
		return true;
	}
	
	public static boolean venderAcoes(Connection connection, ArrayList<Acao> acoes) throws Exception {
		Statement statement = connection.createStatement();
		for (Iterator<Acao> iterator = acoes.iterator(); iterator.hasNext();) {
			Acao acao = (Acao) iterator.next();
			String select = String.format(
					"SELECT SUM(quantidade) AS qtd FROM invest.acoes WHERE NOME = " +
							"'%s'"
							,acao.getNome()
					);
			try (ResultSet rs = connection.prepareStatement(select).executeQuery()) {
				if (rs.next()) {
					if (acao.getQuantidade().compareTo(rs.getInt("qtd"))>0) {
						throw new Exception("Quantidade de acoes vendidas (" + acao.getQuantidade() + ") maior que a quantidade existente ("
								+ rs.getInt("qtd")  + ")");
					}
				}else {
					throw new Exception("Ação não disponível para vennda.");
				}
			}
			String insert = String.format(
					"INSERT INTO invest.acoes ( nome , data, quantidade, valor, custo ) VALUES("  +
							"'%s'," +
							"'%s'," +
							"%s," +
							"%s," +
							"%s)"
							,acao.getNome()
							,acao.getData()
							,acao.getQuantidade() * -1
							,acao.getValor()
							,acao.getCusto()
					);
			statement.executeUpdate(insert);
		}
		return true;
	}
}
