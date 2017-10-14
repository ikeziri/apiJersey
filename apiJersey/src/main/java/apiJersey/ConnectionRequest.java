package apiJersey;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

public class ConnectionRequest {

	private static final String PWD = "29811307";
	private static final String USER = "ikeziri";
	private static final String PRD = "jdbc:google:mysql://invest-182620:southamerica-east1:invest/invest";
	private static final String LOCAL = "jdbc:mysql://google/invest?useSSL=false&cloudSqlInstance=invest-182620:southamerica-east1:invest&socketFactory=com.google.cloud.sql.mysql.SocketFactory";

	public static java.sql.Connection getConnection(HttpServletRequest request) throws SQLException {
		String jdbcUrl = request.getRequestURL().toString().contains("localhost:")
				? LOCAL
				: PRD;
		return DriverManager.getConnection(jdbcUrl, USER, PWD);
	}
}
