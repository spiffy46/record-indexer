package record_indexer.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import record_indexer.DataImporter;


public class project {
	private String title;
	private int recordsperimage;
	private int firstycoord;
	private int recordheight;
	private List<field> fields;
	private List<image> images;
	private int id;
	
	public project()
	{
		title = "";
		recordsperimage = 0;
		firstycoord = 0;
		recordheight = 0;
		fields = new ArrayList<field>();
		images = new ArrayList<image>();
		id = 0;
	}
	
	public project(Element e)
	{
		fields = new ArrayList<field>();
		images = new ArrayList<image>();
		
		ArrayList<Element> atributes = DataImporter.getChildElements(e);
		title = atributes.get(0).getFirstChild().getNodeValue();
		recordsperimage = Integer.parseInt(atributes.get(1).getFirstChild().getNodeValue());
		firstycoord = Integer.parseInt(atributes.get(2).getFirstChild().getNodeValue());
		recordheight = Integer.parseInt(atributes.get(3).getFirstChild().getNodeValue());
		
		ArrayList<Element> fieldsElements = DataImporter.getChildElements(atributes.get(4));
		for(Element fieldElement: fieldsElements){
			fields.add(new field(fieldElement));
		}
		
		ArrayList<Element> imageElements = DataImporter.getChildElements(atributes.get(5));
		for(Element imageElement: imageElements){
			images.add(new image(imageElement));
		}
			
	}

	public int getID(){
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public int getRecordsperimage() {
		return recordsperimage;
	}

	public int getFirstycoord() {
		return firstycoord;
	}

	public int getRecordheight() {
		return recordheight;
	}

	public List<field> getFields() {
		return fields;
	}

	public List<image> getImages() {
		return images;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setRecordsperimage(int recordsperimage) {
		this.recordsperimage = recordsperimage;
	}

	public void setFirstycoord(int firstycoord) {
		this.firstycoord = firstycoord;
	}

	public void setRecordheight(int recordheight) {
		this.recordheight = recordheight;
	}

	public void setFields(List<field> fields) {
		this.fields = fields;
	}

	public void setImages(List<image> images) {
		this.images = images;
	}
	
	public String toString(){
		return (title + ", " + recordsperimage + ", " + firstycoord + ", " + recordheight + ", " + fields.size() + ", " + images.size() + ", ");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + firstycoord;
		result = prime * result + ((images == null) ? 0 : images.hashCode());
		result = prime * result + recordheight;
		result = prime * result + recordsperimage;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		project other = (project) obj;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		if (firstycoord != other.firstycoord)
			return false;
		if (images == null) {
			if (other.images != null)
				return false;
		} else if (!images.equals(other.images))
			return false;
		if (recordheight != other.recordheight)
			return false;
		if (recordsperimage != other.recordsperimage)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
}
