package investimento.objeto;

import java.math.BigDecimal;

public class Acao {
private String nome;
private String date;
private Integer quantidade;
private BigDecimal valor;
private BigDecimal custo;

public String getNome() {
	return nome;
}
public void setNome(String nome) {
	this.nome = nome;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
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
}
