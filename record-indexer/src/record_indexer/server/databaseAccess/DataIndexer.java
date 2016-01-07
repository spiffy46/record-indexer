package record_indexer.server.databaseAccess;

import java.util.ArrayList;

import org.w3c.dom.Element;

import record_indexer.DataImporter;
import record_indexer.shared.model.*;

public class DataIndexer {
	private ArrayList<user> users = new ArrayList<user>();
	private ArrayList<project> projects = new ArrayList<project>();
	private DatabaseAccessDriver db;
	
	public DataIndexer(Element root){
		ArrayList<Element> rootElements = DataImporter.getChildElements(root);
		
		ArrayList<Element> userElements = DataImporter.getChildElements(rootElements.get(0));
		for(Element userElement : userElements){
			users.add(new user(userElement));
		}
		
		ArrayList<Element> projectElements = DataImporter.getChildElements(rootElements.get(1));
		for(Element projectElement : projectElements){
			projects.add(new project(projectElement));
		}
	}
	
	public void convertToSql()
	{
		db = new DatabaseAccessDriver();
		try{
			db.initialize();
			db.convertToSql(users, projects);
		}catch(DatabaseException e){
			return;
		} catch (ClassNotFoundException e) {
			return;
		}
	}
	
}
