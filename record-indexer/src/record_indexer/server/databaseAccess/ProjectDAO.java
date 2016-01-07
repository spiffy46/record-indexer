package record_indexer.server.databaseAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import record_indexer.shared.model.project;

public class ProjectDAO {

	public boolean INSERT(project p, Connection connection)
	{
		String sql = "INSERT INTO main.projects(title,recordsperimage,firstycoord,recordheights)";
		sql = sql + "VALUES(";
		sql = sql + "\'" + p.getTitle() + "\',";
		sql = sql + "\'" + p.getRecordsperimage() + "\',";
		sql = sql + "\'" + p.getFirstycoord() + "\',";
		sql = sql + p.getRecordheight() + ")";
		
		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			System.out.println("Unable to add Project with command:");
			System.out.println(sql);
			return false;
		}
		return true;
	}
	
	/*public ResultSet SELECT(project i, Connection connection){
		String sql = "SELECT * FROM main.images WHERE title=\'";
		sql = sql + i.getTitle() + "\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
		}catch(SQLException e){
			System.out.println("Unable to SELECT FROM PROJECTS with command:");
			System.out.println(sql);
			return null;
		}
		return result;
	}*/
	
	public static ResultSet getProjects(Connection connection)throws SQLException{
		String sql = "SELECT * FROM main.projects;";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			return result;
		}catch(SQLException e){
			throw new SQLException(String.format("Error in ProjectDAO getProjects"));
		}
	}
}
