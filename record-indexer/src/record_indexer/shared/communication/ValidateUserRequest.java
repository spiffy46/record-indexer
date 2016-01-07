package record_indexer.shared.communication;

public class ValidateUserRequest {
		private String USER;
		private String PASSWORD;
		
		public ValidateUserRequest()
		{
			this.USER = "";
			this.PASSWORD = "";
		}
		
		public ValidateUserRequest(String u, String p)
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
