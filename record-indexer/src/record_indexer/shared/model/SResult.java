package record_indexer.shared.model;

import record_indexer.client.ClientFacade;

public class SResult {
	private String IMAGE_ID;
	private String IMAGE_URL;
	private String RECORD_NUM;
	private String FIELD_ID;
	
	public SResult(){
		this.IMAGE_ID = "";
		this.IMAGE_URL = "";
		this.RECORD_NUM = "";
		this.FIELD_ID = "";
	}
	
	public SResult(String imageid, String imageurl, String recordnum, String fieldid){
		this.IMAGE_ID = imageid;
		this.IMAGE_URL = imageurl;
		this.RECORD_NUM = recordnum;
		this.FIELD_ID = fieldid;
	}

	public String getIMAGE_ID() {
		return IMAGE_ID;
	}

	public String getIMAGE_URL() {
		return IMAGE_URL;
	}

	public String getRECORD_NUM() {
		return RECORD_NUM;
	}

	public String getFIELD_ID() {
		return FIELD_ID;
	}

	public void setIMAGE_ID(String iMAGE_ID) {
		IMAGE_ID = iMAGE_ID;
	}

	public void setIMAGE_URL(String iMAGE_URL) {
		IMAGE_URL = iMAGE_URL;
	}

	public void setRECORD_NUM(String rECORD_NUM) {
		RECORD_NUM = rECORD_NUM;
	}

	public void setFIELD_ID(String fIELD_ID) {
		FIELD_ID = fIELD_ID;
	}
	
	public String toString(){
		String str = IMAGE_ID + '\n' + "http://" + ClientFacade.getHost() + ":" + ClientFacade.getPort() + "/" + IMAGE_URL + '\n' + RECORD_NUM + '\n' + FIELD_ID + '\n';
		return str;
	}
	
}
