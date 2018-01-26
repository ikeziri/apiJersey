package investimento;

public class CotacaoGoogle {
	private String t	    ; //Ticker
	private String e	    ; //Exchange
	private String l	    ; //Last Price
	private String c	    ; //Change
	private String cp	    ; //Change Percentage
	private String op	    ; //Open Price
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e;
	}
	public String getL() {
		return l;
	}
	public void setL(String l) {
		this.l = l;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	@Override
	public String toString() {
		return "CotacaoGoogle [t=" + t + ", e=" + e + ", l=" + l + ", c=" + c + ", cp=" + cp + ", op=" + op + "]";
	}

	
}
