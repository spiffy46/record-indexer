package record_indexer.shared.communication;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidateUserResult {
		private String OUTPUT;
		private String USER_FIRST_NAME;
		private String USER_LAST_NAME;
		private int NUM_RECORDS;
		
		public ValidateUserResult()
		{
			this.OUTPUT = "FAILED";
			this.USER_FIRST_NAME = "";
			this.USER_LAST_NAME = "";
			this.NUM_RECORDS = 0;
		}
		
		public ValidateUserResult(String o, String f, String l, int num)
		{
			this.OUTPUT = o;
			this.USER_FIRST_NAME = f;
			this.USER_LAST_NAME = l;
			this.NUM_RECORDS = num;
		}
		
		public ValidateUserResult(ResultSet rs)
		{
			try{
				if(rs.next()){
					this.OUTPUT = "TRUE";
					this.USER_FIRST_NAME = rs.getString("firstname");
					this.USER_LAST_NAME = rs.getString("lastname");
					this.NUM_RECORDS = rs.getInt("indexedrecords");
				}else{
					this.OUTPUT = "FALSE";
				}
			}catch(SQLException e){
				this.OUTPUT = "FAILED";
			}
		}
		public String getOUTPUT() {
			return OUTPUT;
		}

		public String getUSER_FIRST_NAME() {
			return USER_FIRST_NAME;
		}

		public String getUSER_LAST_NAME() {
			return USER_LAST_NAME;
		}

		public int getNUM_RECORDS() {
			return NUM_RECORDS;
		}

		public void setOUTPUT(String oUTPUT) {
			OUTPUT = oUTPUT;
		}

		public void setUSER_FIRST_NAME(String uSER_FIRST_NAME) {
			USER_FIRST_NAME = uSER_FIRST_NAME;
		}

		public void setUSER_LAST_NAME(String uSER_LAST_NAME) {
			USER_LAST_NAME = uSER_LAST_NAME;
		}

		public void setNUM_RECORDS(int nUM_RECORDS) {
			NUM_RECORDS = nUM_RECORDS;
		}

		public String toString()
		{
			switch(OUTPUT){
				case "TRUE":
					return ("TRUE\n" + USER_FIRST_NAME + "\n" + USER_LAST_NAME + "\n" + NUM_RECORDS + "\n");
				case "FALSE":
					return ("FALSE\n");
				case "FAILED":
					return ("FAILED\n");
				default:
					return ("FAILED\n");
			}
		}
}
