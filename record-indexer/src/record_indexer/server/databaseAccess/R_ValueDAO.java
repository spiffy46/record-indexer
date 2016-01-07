package record_indexer.server.databaseAccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import record_indexer.shared.model.r_value;

public class R_ValueDAO {

	public boolean INSERT(r_value r, Connection connection){
		String sql = "INSERT INTO main.r_values(image_id,field_id,row_id,actual_val)";
		sql = sql + "VALUES(";
		sql = sql + "\'" + r.getImage_id() + "\',";
		sql = sql + "\'" + r.getField_id() + "\',";
		sql = sql + "\'" + r.getRow_id() + "\',";
		sql = sql + "\'" + r.getActual_val() + "\')";
		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			System.out.println("Unable to add r_value with command:");
			System.out.println(sql);
			return false;
		}
		return true;
	}
	
	/*public ResultSet SELECT(r_value i, Connection connection){
		String sql = "SELECT * FROM main.r_values WHERE actual_val=\'";
		sql = sql + i.getActual_val() + "\';";
		ResultSet result = null;
		
		try{
			Statement stmt = connection.createStatement();
			result = stmt.executeQuery(sql);
		}catch(SQLException e){
			System.out.println("Unable to SELECT FROM R_VALUES with command:");
			System.out.println(sql);
			return null;
		}
		return result;
	}*/
	
	public static void submitBatch(int batch, int numfields, List<String> values, Connection connection) throws SQLException{
		String sql = "INSERT INTO main.r_values(image_id,field_id,row_id,actual_val)";
		sql = sql + "VALUES(\'" + batch + "\',";
		int rowid = 1;
		int fieldid = 1;
		try{
			for(String tempStr: values){
				String modSQL = sql + "\'" + fieldid + "\',\'" + rowid + "\',\'" + tempStr + "\');";
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(modSQL);
				fieldid++;
				if(fieldid > numfields){
					fieldid = 1;
					rowid++;
				}
			}
		}catch(SQLException e){
			throw new SQLException(String.format("Error in R_ValueDAO submitBatch()"));
		}
	}
}
