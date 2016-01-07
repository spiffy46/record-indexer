package record_indexer.shared.communication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubmitBatchRequest {
	private String USER;
	private String PASSWORD;
	private int BATCH;
	private int NUMFIELDS;
	private int NUMRECORDS;
	private List<String> VALUES;
	private boolean VALIDINPUT;
	
	public SubmitBatchRequest(){
		this.USER = "";
		this.PASSWORD = "";
		this.BATCH = 0;
		this.NUMFIELDS = 0;
		this.NUMRECORDS = 0;
		this.VALIDINPUT = false;
		this.VALUES = new ArrayList<String>();
	}
	
	public SubmitBatchRequest(String user, String password, int batch, String values){
		this.VALIDINPUT = true;
		this.USER = user;
		this.PASSWORD = password;
		this.BATCH = batch;
		this.VALUES = new ArrayList<String>();
		parseValues(values);
	}
	
	public void parseValues(String values){
		this.VALIDINPUT = true;
		List<String> fullRecords = Arrays.asList(values.split(";"));
		this.NUMFIELDS = -1;
		this.NUMRECORDS = fullRecords.size();
		for(String rec: fullRecords){
			List<String> rValues = Arrays.asList((rec.split(",")));
			if(NUMFIELDS == -1){
				this.NUMFIELDS = rValues.size();
			}else{
				if(NUMFIELDS != rValues.size()){
					this.VALIDINPUT = false;
				}
			}
			for(String rVal: rValues){
				VALUES.add(rVal);
			}
		}
	}
	
	public int getNUMRECORDS(){
		return NUMRECORDS;
	}
	public int getNUMFIELDS(){
		return NUMFIELDS;
	}
	public List<String> getVALUES(){
		return VALUES;
	}
	public String getUSER() {
		return USER;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public int getBATCH() {
		return BATCH;
	}
	public boolean isVALIDINPUT() {
		return VALIDINPUT;
	}

	public void setVALIDINPUT(boolean vALIDINPUT) {
		VALIDINPUT = vALIDINPUT;
	}

	public void setUSER(String uSER) {
		USER = uSER;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	public void setBATCH(int bATCH) {
		BATCH = bATCH;
	}
	public void setVALUES(List<String> vALUES){
		VALUES = vALUES;
	}
	public void addVALUE(String s){
		VALUES.add(s);
	}
	public void setNUMFIELDS(int n){
		NUMFIELDS = n;
	}
	
	public String toString(){
		String str = "USERNAME: " + USER + " PASSWORD: " + PASSWORD + " BATCH: " + BATCH + '\n' + "Values:\n";
		/*int i = 0;
		while(i < VALUES.size()){
			for(int j = 0; j < NUMFIELDS-1; j++){
				str = str + VALUES.get(i) + ", ";
				i++;
			}
			str = str + VALUES.get(i) + ";\n";
			i++;
		}*/
		str = str + VALUES.toString();
		return str;
	}
}
