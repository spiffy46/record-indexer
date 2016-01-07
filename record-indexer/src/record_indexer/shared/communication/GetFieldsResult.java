package record_indexer.shared.communication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import record_indexer.shared.model.field;

public class GetFieldsResult {
	private String OUTPUT;
	private ArrayList<field> fields;
	
	public GetFieldsResult()
	{
		this.OUTPUT = "FAILED";
		this.fields = new ArrayList<field>();
	}
	
	public GetFieldsResult(ResultSet rs)
	{
		this.OUTPUT = "FAILED";
		field tempField;
		fields = new ArrayList<field>();
		try{
			while(rs.next()){
				this.OUTPUT = "TRUE";
				tempField = new field();
				tempField.setProject_id(rs.getInt("project_id"));
				tempField.setField_id(rs.getInt("id"));
				tempField.setTitle(rs.getString("title"));
				fields.add(tempField);
			}
		}catch(SQLException e){
			this.OUTPUT = "FAILED";
		}
	}
	
	public String getOUTPUT() {
		return OUTPUT;
	}
	public ArrayList<field> getFields() {
		return fields;
	}
	public void setOUTPUT(String oUTPUT) {
		OUTPUT = oUTPUT;
	}
	public void setFields(ArrayList<field> fields) {
		this.fields = fields;
	}
	
	public String toString(){
		String out = "";
		if(OUTPUT.equals("TRUE")){
			for(field tempField: fields){
				out = out + tempField.getProject_id() + "\n" + tempField.getField_id() + "\n" + tempField.getTitle() + "\n";
			}
			return out;
		}else{
			return "FAILED\n";
		}
	}
}
