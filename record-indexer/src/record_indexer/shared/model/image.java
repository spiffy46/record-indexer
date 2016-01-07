package record_indexer.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import record_indexer.DataImporter;

public class image {
	private String file;
	private List<record> records;
	private int project_id;
	
	public image()
	{
		file = "";
		records = new ArrayList<record>();
	}
	
	public image(Element e)
	{
		records = new ArrayList<record>();
		
		ArrayList<Element> atributes = DataImporter.getChildElements(e);
		file = atributes.get(0).getFirstChild().getNodeValue();
		if(atributes.size() > 1)
		{
			ArrayList<Element> recordElements = DataImporter.getChildElements(atributes.get(1));
			for(Element recordElement: recordElements){
				records.add(new record(recordElement));
			}
		}
	}

	public String getFile() {
		return file;
	}

	public List<record> getRecords() {
		return records;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setRecords(List<record> records) {
		this.records = records;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((records == null) ? 0 : records.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		image other = (image) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (records == null) {
			if (other.records != null)
				return false;
		} else if (!records.equals(other.records))
			return false;
		return true;
	}
}
