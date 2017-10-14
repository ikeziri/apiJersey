package apiJersey;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apphosting.api.ApiProxy;
import com.google.cloud.sql.jdbc.Connection;
import com.google.cloud.sql.jdbc.PreparedStatement;
import com.google.cloud.sql.jdbc.ResultSet;
import com.google.common.base.Stopwatch;

@SuppressWarnings("serial")
// With @WebServlet annotation the webapp/WEB-INF/web.xml is no longer required.
@WebServlet(name = "CloudSQL", description = "CloudSQL: Write low order IP address to Cloud SQL", urlPatterns = "/cloudsql")
public class CloudSqlServlet extends HttpServlet {
	Connection conn;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		final String createTableSql = "CREATE TABLE IF NOT EXISTS visits ( visit_id INT NOT NULL "
				+ "AUTO_INCREMENT, user_ip VARCHAR(46) NOT NULL, timestamp DATETIME NOT NULL, "
				+ "PRIMARY KEY (visit_id) )";
		final String createVisitSql = "INSERT INTO visits (user_ip, timestamp) VALUES (?, ?)";
		final String selectSql = "SELECT user_ip, timestamp FROM visits ORDER BY timestamp DESC " + "LIMIT 10";

		String path = req.getRequestURI();
		if (path.startsWith("/favicon.ico")) {
			return; // ignore the request for favicon.ico
		}

		PrintWriter out = resp.getWriter();
		resp.setContentType("text/plain");

		// store only the first two octets of a users ip address
		String userIp = req.getRemoteAddr();
		InetAddress address = InetAddress.getByName(userIp);
		if (address instanceof Inet6Address) {
			// nest indexOf calls to find the second occurrence of a character in a string
			// an alternative is to use Apache Commons Lang: StringUtils.ordinalIndexOf()
			userIp = userIp.substring(0, userIp.indexOf(":", userIp.indexOf(":") + 1)) + ":*:*:*:*:*:*";
		} else if (address instanceof Inet4Address) {
			userIp = userIp.substring(0, userIp.indexOf(".", userIp.indexOf(".") + 1)) + ".*.*";
		}

		Stopwatch stopwatch = Stopwatch.createStarted();
		try (PreparedStatement statementCreateVisit = conn.prepareStatement(createVisitSql)) {
			conn.createStatement().executeUpdate(createTableSql);
			statementCreateVisit.setString(1, userIp);
			statementCreateVisit.setTimestamp(2, new Timestamp(new Date().getTime()));
			statementCreateVisit.executeUpdate();

			try (ResultSet rs = conn.prepareStatement(selectSql).executeQuery()) {
				stopwatch.stop();
				out.print("Last 10 visits:\n");
				while (rs.next()) {
					String savedIp = rs.getString("user_ip");
					String timeStamp = rs.getString("timestamp");
					out.print("Time: " + timeStamp + " Addr: " + savedIp + "\n");
				}
				out.println("Elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
			}
		} catch (SQLException e) {
			throw new ServletException("SQL error", e);
		}
	}

	@Override
	public void init() throws ServletException {
		try {
			ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
			Map<String, Object> attr = env.getAttributes();
			String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");

			String url = hostname.contains("localhost:") ? System.getProperty("cloudsql-local")
					: System.getProperty("cloudsql");
			log("connecting to: " + url);
			try {
				conn = (Connection) DriverManager.getConnection(url);
			} catch (SQLException e) {
				throw new ServletException("Unable to connect to Cloud SQL", e);
			}

		} finally {
			// Nothing really to do here.
		}
	}
}
