package record_indexer.server.databaseAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import record_indexer.shared.model.field;

public class FieldDAO {

	public boolean INSERT(field f, Connection connection)
	{
		String sql = "INSERT INTO main.fields(title,xcoord,width,helphtml,project_id,field_id,knowndata)";
		sql = sql + "VALUES(";
		sql = sql + "\'" + f.getTitle() + "\',";
		sql = sql + "" + f.getXcoord() + ",";
		sql = sql + "" + f.getWidth() + ",";
		sql = sql + "\'" + f.getHelphtml() + "\',";
		sql = sql + "" + f.getProject_id() + ",";
		sql = sql + "" + f.getField_id() + ",";
		sql = sql + "\'" + f.getKnowndata() + "\')";
		
		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			System.out.println("Unable to add Field with command:");
			System.out.println(sql);
			return false;
		}
		return true;
	}
	
	/*public ResultSet SELECT(field i, Connection connection){
		String sql = "SELECT * FROM main.fields WHERE title=\'";
		sql = sql + i.getTitle() + "\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
		}catch(SQLException e){
			System.out.println("Unable to SELECT FROM FIELDS with command:");
			System.out.println(sql);
			return null;
		}
		return result;
	}*/
	
	public static boolean assertSearch(List<String> fields, Connection connection) throws SQLException{
		String sql = "SELECT * FROM main.fields";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			int range = 0;
			while(result.next())
				range++;
			for(String temp: fields){
				if(Integer.parseInt(temp)> range || Integer.parseInt(temp) <=0){
					return false;
				}
			}
		}catch(SQLException e){
			throw new SQLException(String.format("Error in FieldDAO assertSearch()"));
		}
		return true;
	}
	
	public static ResultSet getFields(int projectNum, Connection connection) throws SQLException{
		String sql = "SELECT * FROM main.fields";
		if(projectNum > 0)
			sql = sql + " WHERE project_id=\'" + projectNum + "\'";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			return result;
		}catch(SQLException e){
			throw new SQLException(String.format("Error in FieldDAO getFields()"));
		}
	}
	
	public static ResultSet search(List<String> fields, List<String> values, Connection connection) throws SQLException{
		String sql = "SELECT main.images.id, main.images.file, main.r_values.row_id, main.fields.id FROM main.fields JOIN main.images ON (";
		for(int i = 0; i < fields.size(); i++){
			if(i != 0)
				sql = sql + " OR ";
			sql = sql + "main.fields.id=\'" + fields.get(i) + "\'";
		}
		sql = sql + ") AND main.fields.project_id = main.images.project_id JOIN main.r_values ON main.images.id = main.r_values.image_id AND main.fields.field_id = main.r_values.field_id AND (";
		
		for(int i = 0; i < values.size(); i++){
			if(i != 0)
				sql = sql + " OR ";
			sql = sql + "main.r_values.actual_val=\'" + values.get(i) + "\' COLLATE NOCASE";
		}
		sql = sql + ")";
		
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
		}catch(SQLException e){
			throw new SQLException(String.format("Error in FieldDAO search()"));

		}
		return result;
	}
}
