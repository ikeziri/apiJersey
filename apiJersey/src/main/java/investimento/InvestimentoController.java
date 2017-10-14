package investimento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
				acao.setDate(fmt.format(rs.getDate("data")));
				acoes.add(acao);
			}
		}
		
		return acoes;
	}
}
