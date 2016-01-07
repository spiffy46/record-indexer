package record_indexer.shared.model;


import org.w3c.dom.Element;

public class r_value {
	private int image_id;
	private int field_id;
	private int row_id;
	private String actual_val;
	
	public r_value(){
		image_id = 0;
		field_id = 0;
		row_id = 0;
		actual_val = "";
	}
	public r_value(Element e)
	{
		//ArrayList<Element> atributes = DataImporter.getChildElements(e);
		actual_val = e.getFirstChild().getNodeValue();
	}

	public int getImage_id() {
		return image_id;
	}

	public int getField_id() {
		return field_id;
	}

	public int getRow_id() {
		return row_id;
	}

	public String getActual_val() {
		return actual_val;
	}

	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}

	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	public void setRow_id(int row_id) {
		this.row_id = row_id;
	}

	public void setActual_val(String actual_val) {
		this.actual_val = actual_val;
	}	
}
