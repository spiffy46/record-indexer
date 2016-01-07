package record_indexer.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import record_indexer.DataImporter;

public class record {
	private List<r_value> values;
	private int image_id;
	private int row_id;
	
	public record()
	{
		values = new ArrayList<r_value>();
	}
	
	public record(Element e)
	{
		values = new ArrayList<r_value>();

		ArrayList<Element> atributes = DataImporter.getChildElements(e);
		ArrayList<Element> valueElements = DataImporter.getChildElements(atributes.get(0));
		for(Element valueElement: valueElements){
			values.add(new r_value(valueElement));
		}
	}

	public List<r_value> getValues() {
		return values;
	}

	public int getImage_id() {
		return image_id;
	}

	public int getRow_id() {
		return row_id;
	}

	public void setRow_id(int row_id) {
		this.row_id = row_id;
	}

	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}

	public void setValues(List<r_value> values) {
		this.values = values;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		record other = (record) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}
}
