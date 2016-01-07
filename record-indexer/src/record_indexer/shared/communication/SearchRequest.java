package record_indexer.shared.communication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchRequest {
	private String USER;
	private String PASSWORD;
	private List<String> FIELDS;
	private List<String> VALUES;
	
	public SearchRequest(){
		this.USER = "";
		this.PASSWORD = "";
		this.FIELDS = new ArrayList<String>();
		this.VALUES = new ArrayList<String>();
	}
	
	public SearchRequest(String user, String password, String fields, String values){
		this.USER = user;
		this.PASSWORD = password;
		this.FIELDS = new ArrayList<String>();
		this.VALUES = new ArrayList<String>();
		
		List<String> fList = Arrays.asList(fields.split(","));
		
		for(String field: fList){
			if(!field.equals(""))
				this.FIELDS.add(field.trim());
		}
		
		List<String> vList = Arrays.asList(values.split(","));
		
		for(String value: vList){
			if(!values.equals(""))
				this.VALUES.add(value.trim());
		}
	}

	public String getUSER() {
		return USER;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public List<String> getFIELDS() {
		return FIELDS;
	}
	public List<String> getVALUES() {
		return VALUES;
	}
	public void setUSER(String uSER) {
		USER = uSER;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	public void setFIELDS(List<String> fIELDS) {
		FIELDS = fIELDS;
	}
	public void setVALUES(List<String> vALUES) {
		VALUES = vALUES;
	}
	
	public String toString(){
		String str = "USERNAME: " + USER + " PASSWORD: " + PASSWORD + '\n' + "Fields:\n" + FIELDS.toString() + '\n' + VALUES.toString() + '\n';
		return str;
	}
}
