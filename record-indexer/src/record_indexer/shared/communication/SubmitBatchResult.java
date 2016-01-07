package record_indexer.shared.communication;

public class SubmitBatchResult {
	private String OUTPUT;
	
	public SubmitBatchResult(){
		this.OUTPUT = "FAILED";
	}
	
	public SubmitBatchResult(boolean success){
		if(success)
			this.OUTPUT = "TRUE";
		else
			this.OUTPUT = "FAILED";
	}

	public String getOUTPUT() {
		return OUTPUT;
	}
	public void setOUTPUT(String oUTPUT) {
		OUTPUT = oUTPUT;
	}
	public String toString(){
		return OUTPUT + '\n';
	}
}
