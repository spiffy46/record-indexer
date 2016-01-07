package record_indexer.shared.model;

import java.util.ArrayList;

import org.w3c.dom.Element;

import record_indexer.DataImporter;

public class user {
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private int indexedrecords;
	private int image_id;
	
	public user()
	{
		username = "";
		password = "";
		firstname = "";
		lastname = "";
		email = "";
		indexedrecords = 0;
	}

	public user(Element e)
	{
		ArrayList<Element> atributes = DataImporter.getChildElements(e);
		username = atributes.get(0).getFirstChild().getNodeValue();
		password = atributes.get(1).getFirstChild().getNodeValue();
		firstname = atributes.get(2).getFirstChild().getNodeValue();
		lastname = atributes.get(3).getFirstChild().getNodeValue();
		email = atributes.get(4).getFirstChild().getNodeValue();
		indexedrecords = Integer.parseInt(atributes.get(5).getFirstChild().getNodeValue());
	}

	public int getImage_id(){
		return image_id;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getEmail() {
		return email;
	}

	public int getIndexedrecords() {
		return indexedrecords;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIndexedrecords(int indexedrecords) {
		this.indexedrecords = indexedrecords;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + indexedrecords;
		result = prime * result
				+ ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		user other = (user) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (indexedrecords != other.indexedrecords)
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
	
}
