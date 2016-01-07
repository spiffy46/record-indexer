package record_indexer.shared.communication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import record_indexer.client.ClientFacade;
import record_indexer.shared.model.*;

public class DownloadBatchResult {
	private String OUTPUT;
	private int BATCH_ID;
	private int PROJECT_ID;
	private String IMAGE_URL;
	private int FIRST_Y_COORD;
	private int RECORD_HEIGHT;
	private int NUM_RECORDS;
	private int NUM_FIELDS;
	private List<field> FIELDS;
	
	public DownloadBatchResult()
	{
		this.OUTPUT = "FAILED";
		FIELDS = new ArrayList<field>();
	}
	
	public DownloadBatchResult(ResultSet rs)
	{
		this.OUTPUT = "FAILED";
		FIELDS = new ArrayList<field>();
		try {
			int numfields = 0;
			while(rs.next()){
				this.OUTPUT = "TRUE";
				numfields++;
				field f = new field();
				this.BATCH_ID = rs.getInt(1);
				this.PROJECT_ID = rs.getInt(2);
				this.IMAGE_URL = rs.getString(3);
				this.FIRST_Y_COORD = rs.getInt(4);
				this.RECORD_HEIGHT = rs.getInt(5);
				this.NUM_RECORDS = rs.getInt(6);
				f.setID(rs.getInt(7));
				f.setField_id(rs.getInt(8));
				f.setTitle(rs.getString(9));
				f.setHelphtml(rs.getString(10));
				f.setXcoord(rs.getInt(11));
				f.setWidth(rs.getInt(12));
				f.setKnowndata(rs.getString(13));
				FIELDS.add(f);
			}
			this.NUM_FIELDS = numfields;
		} catch (SQLException e) {
			this.OUTPUT = "FAILED";
		}
	}

	public String getOUTPUT() {
		return OUTPUT;
	}

	public int getBATCH_ID() {
		return BATCH_ID;
	}

	public String getIMAGE_URL() {
		return IMAGE_URL;
	}

	public int getFIRST_Y_COORD() {
		return FIRST_Y_COORD;
	}

	public int getRECORD_HEIGHT() {
		return RECORD_HEIGHT;
	}

	public int getNUM_RECORDS() {
		return NUM_RECORDS;
	}

	public int getNUM_FIELDS() {
		return NUM_FIELDS;
	}
	
	public List<field> getFIELDS(){
		return FIELDS;
	}

	public void setOUTPUT(String oUTPUT) {
		OUTPUT = oUTPUT;
	}

	public void setBATCH_ID(int bATCH_ID) {
		BATCH_ID = bATCH_ID;
	}

	public void setIMAGE_URL(String iMAGE_URL) {
		IMAGE_URL = iMAGE_URL;
	}

	public void setFIRST_Y_COORD(int fIRST_Y_COORD) {
		FIRST_Y_COORD = fIRST_Y_COORD;
	}

	public void setHEIGHT(int hEIGHT) {
		RECORD_HEIGHT = hEIGHT;
	}

	public void setNUM_RECORDS(int nUM_RECORDS) {
		NUM_RECORDS = nUM_RECORDS;
	}

	public void setNUM_FIELDS(int nUM_FIELDS) {
		NUM_FIELDS = nUM_FIELDS;
	}
	
	public void setFIELDS(List<field> fIELDS){
		this.FIELDS = fIELDS;
	}
	
	public String toString()
	{
		if(this.OUTPUT.equals("TRUE"))
		{
			String tempStr = "";
			tempStr = (tempStr + this.BATCH_ID + '\n' + this.PROJECT_ID + '\n' + "http://" + ClientFacade.getHost() + ":" + ClientFacade.getPort() + "/" + this.IMAGE_URL + '\n' + this.FIRST_Y_COORD + '\n' + this.RECORD_HEIGHT + '\n' + this.NUM_RECORDS + '\n' + this.NUM_FIELDS + '\n');
			for(int i = 0; i < this.FIELDS.size(); i++)
			{
				field tmpField = this.FIELDS.get(i);
				tempStr = (tempStr + tmpField.getID() + '\n' + tmpField.getField_id() + '\n' + tmpField.getTitle() + '\n' + "http://" + ClientFacade.getHost() + ":" + ClientFacade.getPort() + "/" + tmpField.getHelphtml() + '\n' + tmpField.getXcoord() + '\n' + tmpField.getWidth() + '\n');
				if(!tmpField.getKnowndata().equals("")){
					tempStr = tempStr + "http://" + ClientFacade.getHost() + ":" + ClientFacade.getPort() + "/" + tmpField.getKnowndata() + '\n';
				}
			}
			return tempStr;
		}
		else{
			return OUTPUT + '\n';
		}
	}

}
