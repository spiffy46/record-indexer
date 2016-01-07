package record_indexer.server.databaseAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import record_indexer.shared.model.image;

public class ImageDAO {

	public boolean INSERT(image i, Connection connection){
		
		String sql = "INSERT INTO main.images(file,project_id)";
		sql = sql + "VALUES(";
		sql = sql + "\'" + i.getFile() + "\',";
		sql = sql + i.getProject_id() + ");";
		
		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			System.out.println("Unable to add Image with command:");
			System.out.println(sql);
			return false;
		}
		return true;
	}
	
	public ResultSet SELECT(image i, Connection connection){
		String sql = "SELECT * FROM main.images WHERE file=\'";
		sql = sql + i.getFile() + "\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
		}catch(SQLException e){
			System.out.println("Unable to SELECT FROM IMAGE with command:");
			System.out.println(sql);
			return null;
		}
		return result;
	}
	
	public static boolean assertSubmit(int batch, int numrec, int numfield, Connection connection) throws SQLException{
		String sql = "SELECT * FROM main.images WHERE id=\'" + batch + "\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			if(result.next()){
				int projectID = result.getInt("project_id");
				sql = "SELECT * FROM main.projects WHERE id=\'" + projectID + "\';";
				
				stmt = connection.createStatement();
				result = stmt.executeQuery(sql);
				int expectednumrec = result.getInt("recordsperimage");
				if(expectednumrec == numrec){
					sql = "SELECT * FROM main.fields WHERE project_id=\'" + projectID + "\';";
					
					stmt = connection.createStatement();
					result = stmt.executeQuery(sql);
					int expectednumfields=-1;
					while(result.next()){
						expectednumfields = result.getInt("field_id");
					}
					if(expectednumfields == numfield){return true;}
				}
			}
		}catch(SQLException e){
			throw new SQLException(String.format("Error in ImageDAO getSampleImage()"));
		}
		return false;
	}
	public static ResultSet getSampleImage(int projectNum, Connection connection) throws SQLException{
		String sql = "SELECT * FROM main.images WHERE project_id=\'" + projectNum + "\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			return result;
		}catch(SQLException e){
			throw new SQLException(String.format("Error in ImageDAO getSampleImage()"));
		}

	}
	
	public static ResultSet downloadBatch(int projectNum, int userId, Connection connection) throws SQLException{
		String sql = "SELECT * FROM main.images WHERE user_id=\'0\' AND completed=\'0\' AND project_id=\'" + projectNum +"\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
			if(result.next()){
				int imageid = result.getInt("id");
				sql = "SELECT main.images.id, main.images.project_id, main.images.file, main.projects.firstycoord, main.projects.recordheights, main.projects.recordsperimage,";
				sql = sql + " main.fields.id, main.fields.field_id, main.fields.title, main.fields.helphtml, main.fields.xcoord, main.fields.width, main.fields.knowndata";
				sql = sql + " FROM main.images JOIN main.projects ON main.images.id=\'" + imageid + "\' and main.images.project_id = main.projects.id";
				sql = sql + " Join main.fields ON main.images.project_id = main.fields.project_id;";
				stmt = connection.createStatement();
				result = stmt.executeQuery(sql);
				
				sql = "UPDATE main.images SET user_id=\'" + userId + "\' WHERE id=\'" + imageid + "\';";
				stmt = connection.createStatement();
				stmt.executeUpdate(sql);
				
				sql = "UPDATE main.users SET image_id=\'" + imageid + "\'" + " WHERE id=\'" + userId + "\';";
				stmt = connection.createStatement();
				stmt.executeUpdate(sql);
			}
		}catch(SQLException e){
			throw new SQLException(String.format("Error in ImageDAO downloadBatch()"));
		}
		return result;
	}

	public static void submitBatch(int batch, Connection connection) throws SQLException{
		String sql = "UPDATE main.images SET completed=\'1\', user_id=\'0\' WHERE id=\'" + batch + "\';";

		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			throw new SQLException(String.format("Error in ImageDAO submitBatch()"));
		}	
	}
}
