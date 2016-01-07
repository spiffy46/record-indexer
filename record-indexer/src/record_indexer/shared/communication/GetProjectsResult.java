package record_indexer.shared.communication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import record_indexer.shared.model.*;

public class GetProjectsResult {
	private String OUTPUT;
	private List<project> projects;
	
	public GetProjectsResult()
	{
		this.OUTPUT = "FAILED";
		this.projects = new ArrayList<project>();
	}
	
	public GetProjectsResult(ResultSet rs)
	{
		this.OUTPUT = "TRUE";
		project tempProject;
		this.projects = new ArrayList<project>();
		try{
			while(rs.next()){
				tempProject = new project();
				tempProject.setId(rs.getInt("id"));
				tempProject.setTitle(rs.getString("title"));
				projects.add(tempProject);
			}
		}catch(SQLException e){
			this.OUTPUT = "FAILED";
		}
	}

	public String getOUTPUT() {
		return OUTPUT;
	}

	public List<project> getProjects() {
		return projects;
	}

	public void setOUTPUT(String oUTPUT) {
		OUTPUT = oUTPUT;
	}

	public void setProjects(List<project> projects) {
		this.projects = projects;
	}
	
	public String toString()
	{
		if(OUTPUT.equals("TRUE"))
		{
			String tempStr = "";
			for(int i = 0; i < this.projects.size(); i++)
			{
				tempStr = (tempStr + projects.get(i).getID() + '\n' + projects.get(i).getTitle() + '\n');
			}
			return tempStr;
		}
		else{
			return ("FAILED" + '\n');
		}
	}

	
}
