package record_indexer.shared.communication;

public class GetSampleImageRequest {
	private String USER;
	private String PASSWORD;
	private int PROJECT;
	
	public GetSampleImageRequest()
	{
		this.USER = "";
		this.PASSWORD = "";
		this.PROJECT = 0;
	}
	
	public GetSampleImageRequest(String u, String p, int n)
	{
		this.USER = u;
		this.PASSWORD = p;
		this.PROJECT = n;
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
