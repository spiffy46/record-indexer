package record_indexer.shared.communication;

import java.sql.ResultSet;
import java.sql.SQLException;

import record_indexer.client.ClientFacade;

public class GetSampleImageResult {
	private String OUTPUT;
	private String IMAGE_URL;
	
	public GetSampleImageResult()
	{
		this.OUTPUT = "FAILED";
	}
	
	public GetSampleImageResult(ResultSet rs)
	{
		try{
			if(rs.next()){
				this.OUTPUT = "TRUE";
				this.IMAGE_URL = rs.getString("file");
			}else{
				this.OUTPUT = "FAILED";
			}
		}catch(SQLException e){
			this.OUTPUT = "FAILED";
		}
	}

	public String getOUTPUT() {
		return OUTPUT;
	}

	public String getIMAGE_URL() {
		return IMAGE_URL;
	}

	public void setOUTPUT(String oUPUT) {
		OUTPUT = oUPUT;
	}

	public void setIMAGE_URL(String iMAGE_URL) {
		IMAGE_URL = iMAGE_URL;
	}
	
	public String toString()
	{
		if(this.OUTPUT.equals("TRUE"))
		{
			return "http://" + ClientFacade.getHost() + ":" + ClientFacade.getPort() + "/" + this.IMAGE_URL + "\n";
		}
		else{
			return OUTPUT + "\n";
		}
	}
}
