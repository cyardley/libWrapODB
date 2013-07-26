package wrapodb;

import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;

import wrapodb.WrapDatabase;
import wrapodb.WrapODB;

public class TestWrapper {
	String path;
	WrapODB wrapODB = null;
	WrapDatabase wrapDatabase = null;
	
	public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
		@SuppressWarnings("unused")
		public void run() {
			  String sql = "Select * from \"StudentInfo\"";
			  TestWrapper w = new TestWrapper("StudentInfo.odb", "C:/Users/Casey/workspace/StudentInfoReports/bin/StudentInfoReports/data", sql);
		  }
	    });
	}
	
	public TestWrapper(String dbName, String p, String sql){ //dbname is the name of your odb file.. e.g. "myFile.odb"
		path = p; //path to working directory
		wrapODB = new WrapODB(path, dbName, "hsqldb");
		wrapDatabase = new WrapDatabase(path + "/hsqldb");

		//Extract from the ODB file
		if(!wrapODB.UnwrapDB()){
			System.out.println("Error extracting DB from ODB file");
		}
		//Open the HSQLDB
		if(!wrapDatabase.OpenDB()){
			System.out.println("Could not open DB");
		}
		//Query Database
		ResultSet rs = wrapDatabase.QueryDB(sql); //This is a standard java.sql.ResultSet;
		try { while(rs.next()){
				System.out.println(rs.getString("First Name")); //print column "First Name"
		} } catch (SQLException e) { e.printStackTrace(); }
			//you can perform any queries here... (including inserts)
		//Update the ODB file
		if(!wrapODB.WrapDB()){ //currently saves the 
			System.out.println("Error saving DB to ODB file");
		}
		//Close the HyperSQLDB connection
		wrapDatabase.CloseDB();

	}
}
