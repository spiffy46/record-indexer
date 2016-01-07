package record_indexer.shared.communication;

public class GetProjectsRequest {
	private String USER;
	private String PASSWORD;
	
	public GetProjectsRequest()
	{
		this.USER = "";
		this.PASSWORD = "";
	}
	
	public GetProjectsRequest(String u, String p)
	{
		this.USER = u;
		this.PASSWORD = p;
	}

	public String getUSER() {
		return USER;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setUSER(String uSER) {
		USER = uSER;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	
	public String toString(){
		return USER + ", " + PASSWORD;
	}
}
