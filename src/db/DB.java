package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection conn = null;
	
	public static Connection getConnection() {

		if(conn == null) {
			try {
				Properties prop = getProperties();
				conn = DriverManager.getConnection(prop.getProperty("dburl"), prop);
			} catch (SQLException e ) {
				throw new DbException(e.getMessage());
			}
		}
		
		return conn;
		
	}
	
	private static Properties getProperties() {
		
		try(FileInputStream fi = new FileInputStream("db.properties")){
			
			Properties prop = new Properties();
			prop.load(fi);
			return prop;
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeStatement(Statement st) {
		if(st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
}
