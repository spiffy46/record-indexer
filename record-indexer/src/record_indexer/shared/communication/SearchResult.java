package record_indexer.shared.communication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import record_indexer.shared.model.SResult;

public class SearchResult {
	private String OUTPUT;
	private List<SResult> SEARCH_RESULTS;
	
	public SearchResult(){
		this.OUTPUT = "FAILED";
		this.SEARCH_RESULTS = new ArrayList<SResult>();
	}

	public SearchResult(ResultSet rs){
		this.OUTPUT = "FAILED";
		this.SEARCH_RESULTS = new ArrayList<SResult>();

		try{
			while(rs.next()){
				this.OUTPUT = "TRUE";
				SResult temp = new SResult(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				this.SEARCH_RESULTS.add(temp);
			}
		}catch(SQLException e){
			this.OUTPUT = "FAILED";
		}
	}
	
	public String getOUTPUT() {
		return OUTPUT;
	}

	public List<SResult> getSEARCH_RESULTS() {
		return SEARCH_RESULTS;
	}

	public void setOUTPUT(String oUTPUT) {
		OUTPUT = oUTPUT;
	}

	public void setSEARCH_RESULTS(List<SResult> sEARCH_RESULTS) {
		SEARCH_RESULTS = sEARCH_RESULTS;
	}
	
	public String toString(){
		if(OUTPUT.equals("FAILED")){
			return OUTPUT + '\n';
		}else{
			String str = "";
			for(SResult temp: SEARCH_RESULTS){
				str = str + temp.toString();
			}
			return str;
		}
	}
}
