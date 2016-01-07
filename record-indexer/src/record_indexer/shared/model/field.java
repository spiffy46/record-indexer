package record_indexer.shared.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Element;

import record_indexer.DataImporter;

@SuppressWarnings("serial")
public class field implements Serializable {
	private int id;
	private String title;
	private int xcoord;
	private int width;
	private String helphtml;
	private String knowndata;
	private int project_id;
	private int field_id;
	
	

	public field()
	{
		title = "";
		xcoord = 0;
		width = 0;
		helphtml = "";
		knowndata = "";
		project_id = 0;
		field_id = 0;
	}
	
	public field(Element e)
	{
		ArrayList<Element> atributes = DataImporter.getChildElements(e);
		title = atributes.get(0).getFirstChild().getNodeValue();
		xcoord = Integer.parseInt(atributes.get(1).getFirstChild().getNodeValue());
		width = Integer.parseInt(atributes.get(2).getFirstChild().getNodeValue());
		if(atributes.size() == 5){
			helphtml = atributes.get(3).getFirstChild().getNodeValue();
			knowndata = atributes.get(4).getFirstChild().getNodeValue();
		}
		else if(atributes.size() == 4){
			if(atributes.get(3).getNodeName().equals("helphtml")){
				helphtml = atributes.get(3).getFirstChild().getNodeValue();
				knowndata = "";
			}
			else{
				knowndata = atributes.get(3).getFirstChild().getNodeValue();
				helphtml = "";
			}
		}
	}

	public int getID(){
		return id;
	}
	public String getTitle() {
		return title;
	}

	public int getXcoord() {
		return xcoord;
	}

	public int getWidth() {
		return width;
	}

	public String getHelphtml() {
		return helphtml;
	}

	public String getKnowndata() {
		return knowndata;
	}
	
	public int getProject_id() {
		return project_id;
	}
	public int getField_id() {
		return field_id;
	}

	public void setField_id(int field_id) {
		this.field_id = field_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public void setID(int id){
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setXcoord(int xcoord) {
		this.xcoord = xcoord;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHelphtml(String helphtml) {
		this.helphtml = helphtml;
	}

	public void setKnowndata(String knowndata) {
		this.knowndata = knowndata;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((helphtml == null) ? 0 : helphtml.hashCode());
		result = prime * result
				+ ((knowndata == null) ? 0 : knowndata.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + width;
		result = prime * result + xcoord;
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
		field other = (field) obj;
		if (helphtml == null) {
			if (other.helphtml != null)
				return false;
		} else if (!helphtml.equals(other.helphtml))
			return false;
		if (knowndata == null) {
			if (other.knowndata != null)
				return false;
		} else if (!knowndata.equals(other.knowndata))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (width != other.width)
			return false;
		if (xcoord != other.xcoord)
			return false;
		return true;
	}
}
