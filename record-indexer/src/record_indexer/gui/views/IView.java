package record_indexer.gui.views;

public interface IView {
	
	void setHost(String value);
	String getHost();
	
	void setPort(String value);
	String getPort();
	void giveErrorMessage(String string);
	void setProjectList(String string, String string2);
	String getUser();
	void setUser(String value);
	void setPassword(String value);
	String getPassword();
	void setBatchList(String string);
	
}

