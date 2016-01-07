package record_indexer.server.databaseAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import record_indexer.shared.model.user;

public class UserDAO {
	
	public boolean INSERT(user u, Connection connection)
	{
		String sql = "INSERT INTO main.users(username,password,firstname,lastname,email,indexedrecords)";
		sql = sql + "VALUES(";
		sql = sql + "\'" + u.getUsername() + "\',";
		sql = sql + "\'" + u.getPassword() + "\',";
		sql = sql + "\'" + u.getFirstname() + "\',";
		sql = sql + "\'" + u.getLastname() + "\',";
		sql = sql + "\'" + u.getEmail() + "\',";
		sql = sql + u.getIndexedrecords() + ")";
		
		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			System.out.println("Unable to add User with command:");
			System.out.println(sql);
			return false;
		}
		return true;
	}
	
	/*public static ResultSet SELECT(user i, Connection connection){
		String sql = "SELECT * FROM main.users WHERE username=\'";
		sql = sql + i.getUsername() + "\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
		}catch(SQLException e){
			System.out.println("Unable to SELECT FROM USERS with command:");
			System.out.println(sql);
			return null;
		}
		return result;
	}*/
	
	public static boolean assertSubmit(user u, int batch, Connection connection)throws SQLException{
		String sql = "SELECT * FROM main.users WHERE username=\'" + u.getUsername() + "\' AND password=\'" + u.getPassword() + "\' AND image_id=\'" + batch + "\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			if(result.next())
				return true;
			else
				return false;
		}catch(SQLException e){
			throw new SQLException(String.format("Error in UserDAO assertSubmit()"));
		}
	}
	
	public static boolean assertSearch(user u, Connection connection)throws SQLException{
		String sql = "SELECT * FROM main.users WHERE username=\'" + u.getUsername() + "\' AND password=\'" + u.getPassword() + "\'";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			if(result.next())
				return true;
			else
				return false;
		}catch(SQLException e){
			throw new SQLException(String.format("Error in UserDAO assertSearch()"));
		}
	}
	
	public static ResultSet validateUser(user i, Connection connection) throws SQLException{
		String sql = "SELECT * FROM main.users Where username=\'";
		sql = sql + i.getUsername() + "\' AND password=\'" + i.getPassword() + "\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			return result;
		}catch(SQLException e){
			throw new SQLException(String.format("Error in UserDAO validateUser()"));
		}
	}
	
	public static int workingOnBatch(user i, Connection connection) throws SQLException{
		String sql = "SELECT * FROM main.users Where username =\'";
		sql = sql + i.getUsername() + "\' AND password=\'" + i.getPassword() + "\' AND image_id=\'0\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);

			if(result.next()){return result.getInt("id");}
			else{return -1;}
			
		}catch(SQLException e){
			throw new SQLException(String.format("Error in UserDAO workingOnBatch()"));
		}
	}	
	
	public static void submitBatch(user u, int numrec, Connection connection) throws SQLException{
		String sql = "UPDATE main.users SET indexedrecords=indexedrecords + " + numrec + ", image_id=\'0\' WHERE username=\'" + u.getUsername() + "\' AND password=\'" + u.getPassword() + "\';";;
		
		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			throw new SQLException(String.format("Error in UserDAO submitBatch()"));
		}	
	}
}
