package in.codifi.funds.utility;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import org.mariadb.jdbc.MariaDbDataSource;

@ApplicationScoped
public class MariaDBDataSourceFactory {

	public static DataSource createDataSource() throws SQLException {
		MariaDbDataSource dataSource = new MariaDbDataSource();
//		dataSource.setUrl("jdbc:mariadb://10.10.0.165:6020/UploadsDB");
		// local
		dataSource.setUrl("jdbc:mariadb://127.0.0.1:6020/UploadsDB");
		dataSource.setUser("GWCUpl");
		dataSource.setPassword("GWCNetra2020");
		return dataSource;
	}

}
