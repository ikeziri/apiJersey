package investimento.objeto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Acao {
	private String nome;
	private String data;
	private Integer quantidade;
	private BigDecimal valor;
	private BigDecimal custo;

	public Acao(String nome, String data, Integer quantidade, BigDecimal valor, BigDecimal custo) {
		super();
		this.nome = nome;
		if ("CURRENT".equals(data)) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			this.data = fmt.format(new Date());
		} else {
			this.data = data;
		}
		this.quantidade = quantidade;
		this.valor = valor;
		this.custo = custo;
	}

	public Acao() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getCusto() {
		return custo;
	}

	public void setCusto(BigDecimal custo) {
		this.custo = custo;
	}

	@Override
	public String toString() {
		return "Acao [nome=" + nome + ", data=" + data + ", quantidade=" + quantidade + ", valor=" + valor + ", custo="
				+ custo + "]";
	}
}
