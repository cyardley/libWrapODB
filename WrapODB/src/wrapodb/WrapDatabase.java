package wrapodb;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WrapDatabase {

	private Connection con = null;
	private String path;
	
	public WrapDatabase(String p){
		new File(p).mkdir();
		path = p;
	}
	
	public boolean OpenDB(){
        // Load the HSQL Database Engine JDBC driver
        // hsqldb.jar should be in the class path or made part of the current jar
        try{ 
        	Class.forName("org.hsqldb.jdbcDriver");
        }catch(Exception e){
        	System.out.println("Could not load org.hsqldb.jdbcDriver");
        	e.printStackTrace();
        	return false;
        }
        try {
			con = DriverManager.getConnection("jdbc:hsqldb:file:" + path + "/db",
			        "sa",
			        "");
		} catch (SQLException e) {
			System.out.println("Could not connect to database");
			System.out.println(e.toString());
			return false;
		}
        return true;
	}
	
	public void CloseDB(){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet QueryDB(String sql){
		if(con==null) return null;
        Statement statement;
        ResultSet rs = null;
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(sql);
			statement.close();
		} catch (SQLException e) {
			System.out.println("Error with SQL Query");
			e.printStackTrace();
			return null;
		}
		return rs;
	}
	
}
