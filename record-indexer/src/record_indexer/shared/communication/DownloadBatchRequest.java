package record_indexer.shared.communication;

public class DownloadBatchRequest {
	private String USER;
	private String PASSWORD;
	private int PROJECT;
	
	public DownloadBatchRequest()
	{
		this.USER = "";
		this.PASSWORD = "";
		this.PROJECT = 0;
	}
	
	public DownloadBatchRequest(String u, String p, int num)
	{
		this.USER = u;
		this.PASSWORD = p;
		this.PROJECT = num;
	}

	public String getUSER() {
		return USER;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public int getPROJECT() {
		return PROJECT;
	}

	public void setUSER(String uSER) {
		USER = uSER;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public void setPROJECT(int pROJECT) {
		PROJECT = pROJECT;
	}
	
	public String toString(){
		return "USERNAME: " + USER + " PASSWORD: " + PASSWORD + " PROJECTID: " + PROJECT; 
	}

}
