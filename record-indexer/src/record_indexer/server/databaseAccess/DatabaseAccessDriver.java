package record_indexer.server.databaseAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import record_indexer.shared.model.*;

public class DatabaseAccessDriver {
	
	
	private Connection connection;
	private Statement stmt;
	

	public DatabaseAccessDriver()
	{
	}
	
	/**
	 * Attempts to initialize the database using 'org.sqlite.JDBC'
	 * Must be done first
	 * @throws ClassNotFoundException 
	 * @throws DatabaseException 
	 */
	public void initialize() throws ClassNotFoundException, DatabaseException
	{
		try{
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
			createTables();
		}catch(ClassNotFoundException e){
			System.out.println("Unable to load database driver");
			throw new ClassNotFoundException();
		} catch (DatabaseException e) {
			System.out.println("Unable to initialize database");
			throw new DatabaseException();
		}
	}
	
	public void start() throws DatabaseException{
		try{
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}catch(ClassNotFoundException e){
			System.out.println("Unable to load database driver");
			return;
		}
	}
	
	private void createTables() throws DatabaseException
	{
		stmt = null;
		try{
			open();
			stmt = connection.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS main.users; CREATE  TABLE main.users(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  UNIQUE, username TEXT NOT NULL  UNIQUE, password TEXT NOT NULL, firstname TEXT NOT NULL, lastname TEXT NOT NULL, email TEXT NOT NULL UNIQUE, indexedrecords INTEGER NOT NULL  DEFAULT 0, image_id INTEGER NOT NULL  DEFAULT 0);");
			stmt = connection.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS main.projects; CREATE  TABLE main.projects(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  UNIQUE, title TEXT NOT NULL  UNIQUE, recordsperimage INTEGER NOT NULL, firstycoord INTEGER NOT NULL, recordheights INTEGER NOT NULL);");
			stmt = connection.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS main.fields; CREATE  TABLE main.fields(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  UNIQUE, title TEXT NOT NULL, xcoord INTEGER NOT NULL, width INTEGER NOT NULL, helphtml TEXT, project_id INTEGER, field_id INTEGER, knowndata TEXT);");
			stmt = connection.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS main.images; CREATE  TABLE main.images(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  UNIQUE, file TEXT NOT NULL UNIQUE, project_id INTEGER NOT NULL, user_id INTEGER NOT NULL DEFAULT 0, completed INTEGER NOT NULL DEFAULT 0);");
			stmt = connection.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS main.r_values; CREATE  TABLE main.r_values(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  UNIQUE, image_id INTEGER NOT NULL, field_id INTEGER NOT NULL, row_id INTEGER NOT NULL, actual_val TEXT);");
			close(true);
		}catch(SQLException e){
			System.out.println("Could not create tables");
			throw new DatabaseException();
		}
	}
	
	/**
	 * Attempts to establish a connection with the database
	 */
	private void open()
	{
		String dbName = "indexer_data/SQL-Database/Census-Database.sqlite";
		String connectionURL = "jdbc:sqlite:" + dbName;
	
		connection = null;
	
		try{
			connection = DriverManager.getConnection(connectionURL);
			connection.setAutoCommit(false);
		}catch(SQLException e){
			System.out.println("Unable to open database connection");
			return;
		}
	}
	
	/**
	 * Attempts to commit() or rollback() 
	 * @param success True if prior command executed was successful. False if unsuccessful
	 */
	public void close(boolean success) throws SQLException
	{
		try{
			if(success)
			{
				connection.commit();
			}
			else
			{
				connection.rollback();
			}
		}catch(SQLException e){
			System.out.println("Unable to save database");
			throw e;
		}finally{
			try{
				if(connection != null)
				{
					connection.close();
				}
			}catch(SQLException e){
				System.out.println("Unable to close database");
				throw e;
			}
		}
		connection = null;
	}
	
	private void addRecord(record o)
	{
		R_ValueDAO DAO = new R_ValueDAO();
		int field_id = 1;
		List<r_value> r_values = o.getValues();
		int image_id = o.getImage_id();
		int row_id = o.getRow_id();
		for(r_value nextR_Val: r_values){
			nextR_Val.setImage_id(image_id);
			nextR_Val.setRow_id(row_id);
			nextR_Val.setField_id(field_id);
			DAO.INSERT(nextR_Val, connection);
			field_id++;
		}
	}
	
	public void convertToSql(List<user> users, List<project> projects){
		int project_id = 1;
		UserDAO uDAO = new UserDAO();
		ProjectDAO pDAO = new ProjectDAO();
		FieldDAO fDAO = new FieldDAO();
		ImageDAO iDAO = new ImageDAO();
		
		open();

		for(user nextUser: users){
			uDAO.INSERT(nextUser,connection);
		}
		
		for(project nextProject: projects){
			pDAO.INSERT(nextProject, connection);
			
			List<field> fields = nextProject.getFields();
			
			int field_id = 1;
			for(field nextField: fields){
				nextField.setProject_id(project_id);
				nextField.setField_id(field_id);
				fDAO.INSERT(nextField, connection);
				field_id++;
			}
			
			List<image> images = nextProject.getImages();
			for(image nextImage: images){
				nextImage.setProject_id(project_id);
				iDAO.INSERT(nextImage, connection);
				
				List<record> records = nextImage.getRecords();
				int image_id;
				try {
					image_id = iDAO.SELECT(nextImage, connection).getInt("id");
				} catch (SQLException e) {
					System.out.println("Could not get image_id");
					try{
						close(false);
					}catch(SQLException f){
						return;
					}
					return;
				}
				int row_id = 1;
				for(record nextRecord: records){
					nextRecord.setImage_id(image_id);
					nextRecord.setRow_id(row_id);
					addRecord(nextRecord);
					row_id++;
				}
				image_id++;
			}
			project_id++;
		}
		try{
			close(true);
		}catch(SQLException e){
			System.out.println("Could not import data");
			e.printStackTrace();
		}
	}

	public ResultSet validateUser(user u) throws SQLException{
		open();
		try{
			ResultSet rs = UserDAO.validateUser(u, connection);
			return rs;
		}catch(SQLException e){
			throw e;
		}
	}
	
	public int workingOnBatch(user u) throws SQLException{
		open();
		try{
			return UserDAO.workingOnBatch(u, connection);
		}catch(SQLException e){
			throw e;
		}
	}
	
	public boolean assertSubmit(user u, int batch, int numberofrecords, int numberoffields) throws SQLException{
		open();
		try{
			if(UserDAO.assertSubmit(u, batch, connection))
				return ImageDAO.assertSubmit(batch, numberofrecords, numberoffields, connection);
			else{
				return false;
			}
		}catch(SQLException e){
			throw e;
		}
	}
	
	public boolean assertSearch(user u, List<String> fields, List<String> values) throws SQLException{
		open();
		try{
			if(values.size() > 0 && fields.size() > 0){
				if(UserDAO.assertSearch(u, connection)){
					return FieldDAO.assertSearch(fields, connection);
				}
			}
		}catch(SQLException e){
			throw e;
		}
		return false;
	}
	
	public ResultSet getSampleImage(int projectNum) throws SQLException{
		open();
		try{
			ResultSet rs = ImageDAO.getSampleImage(projectNum, connection);
			return rs;
		}catch(SQLException e){
			throw e;
		}
	}
	
	public ResultSet getFields(int projectNum) throws SQLException{
		open();
		try{
			ResultSet rs = FieldDAO.getFields(projectNum, connection);
			return rs;
		}catch(SQLException e){
			throw e;
		}
	}
	
	public ResultSet getProjects() throws SQLException{
		open();
		try{
			ResultSet rs = ProjectDAO.getProjects(connection);
			return rs;
		}catch(SQLException e){
			throw e;
		}
	}
	
	public ResultSet downloadBatch(int projectNum, int userId) throws SQLException{
		open();
		try{
			ResultSet rs = ImageDAO.downloadBatch(projectNum, userId, connection);
			return rs;
		}catch(SQLException e){
			throw e;
		}
	}
	
	public void submitBatch(user u, int batch, int numrecords, int numfields, List<String> values) throws SQLException{
		open();
		try{
			UserDAO.submitBatch(u, numrecords, connection);
			ImageDAO.submitBatch(batch, connection);
			R_ValueDAO.submitBatch(batch, numfields, values, connection);
		}catch(SQLException e){
			throw e;
		}
	}
	
	public ResultSet search(List<String> fields, List<String> values) throws SQLException{
		open();
		try{
			return FieldDAO.search(fields, values, connection);
		}catch(SQLException e){
			throw e;
		}
	}
}
