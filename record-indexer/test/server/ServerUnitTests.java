package server;



import org.junit.* ;

import record_indexer.DataImporter;
import record_indexer.client.*;
import record_indexer.server.*;
import record_indexer.shared.communication.*;
import static org.junit.Assert.* ;

public class ServerUnitTests {
	private Server server;
	private ClientFacade facade;
	
	@Before
	public void setup() {
		server = new Server("8080");
		server.run();
		facade = new ClientFacade("localhost", 8080);
	}
	
	@After
	public void teardown() {
		DataImporter.parseXML();
		server.stop();
	}
	
	
	@Test
	public void validateUser() {
		try{
			DataImporter.parseXML();
			
			ValidateUserResult testResult = new ValidateUserResult("TRUE", "Test", "One", 0); 
			assertEquals(testResult.toString(),facade.validateUser(new ValidateUserRequest("test1","test1")).toString());	//test1
			testResult = new ValidateUserResult("TRUE", "Test", "Two", 0);
			assertEquals(testResult.toString(),facade.validateUser(new ValidateUserRequest("test2","test2")).toString());	//test2
			testResult = new ValidateUserResult("TRUE", "Sheila", "Parker", 0);
			assertEquals(testResult.toString(),facade.validateUser(new ValidateUserRequest("sheila","parker")).toString());	//sheila parker
			assertEquals("FALSE\n",facade.validateUser(new ValidateUserRequest("sheilasdfj;lskja","parker")).toString());	//invalid user

		}catch(ClientException e){
			System.out.println("Failed ValidateUser()");
		}
	}
	
	@Test
	public void getProjects() {
		try{
			DataImporter.parseXML();

			String result = "1\n1890 Census\n2\n1900 Census\n3\nDraft Records\n";
			assertEquals(result ,facade.getProjects(new GetProjectsRequest("test1","test1")).toString());				//test1
			assertEquals(result ,facade.getProjects(new GetProjectsRequest("test2","test2")).toString());				//test2
			assertEquals(result ,facade.getProjects(new GetProjectsRequest("sheila","parker")).toString());				//sheila parker
			assertEquals("FAILED\n" ,facade.getProjects(new GetProjectsRequest("sheidfsdfla","parker")).toString());	//invalid user

		}catch(ClientException e){
			System.out.println("Failed getProjects()");
		}
	}
	
	@Test
	public void getSampleImage() {
		try{
			DataImporter.parseXML();

			String result = "http://localhost:8080/images/1890_image0.png\n";
			assertEquals(result ,facade.getSampleImage(new GetSampleImageRequest("test1","test1", 1)).toString()); 				//batch from first project
			result = "http://localhost:8080/images/1900_image0.png\n";
			assertEquals(result ,facade.getSampleImage(new GetSampleImageRequest("test2","test2", 2)).toString());				//batch from second project
			result = "http://localhost:8080/images/draft_image0.png\n";
			assertEquals(result ,facade.getSampleImage(new GetSampleImageRequest("sheila","parker", 3)).toString());			//batch from third project
			assertEquals("FAILED\n" ,facade.getSampleImage(new GetSampleImageRequest("sheidfsdfla","parker", 3)).toString());	//invalid user
			assertEquals("FAILED\n" ,facade.getSampleImage(new GetSampleImageRequest("sheila","parker", 5)).toString());		//invalid project id

		}catch(ClientException e){
			System.out.println("Failed getProjects()");
		}
	}
	
	@Test
	public void downloadBatch() {
		try{
			DataImporter.parseXML();

			String result = "FAILED\n";
			assertEquals(result ,facade.downloadBatch(new DownloadBatchRequest("test","test1", 1)).toString());		//invalid user
			result = "1\n1\nhttp://localhost:8080/images/1890_image0.png\n199\n60\n8\n4\n1\n1\nLast Name\nhttp://localhost:8080/fieldhelp/last_name.html\n60\n300\nhttp://localhost:8080/knowndata/1890_last_names.txt\n2\n2\nFirst Name\nhttp://localhost:8080/fieldhelp/first_name.html\n360\n280\nhttp://localhost:8080/knowndata/1890_first_names.txt\n3\n3\nGender\nhttp://localhost:8080/fieldhelp/gender.html\n640\n205\nhttp://localhost:8080/knowndata/genders.txt\n4\n4\nAge\nhttp://localhost:8080/fieldhelp/age.html\n845\n120\n";
			assertEquals(result ,facade.downloadBatch(new DownloadBatchRequest("test2","test2", 1)).toString());	//assigns batch 1 to test2
			result = "FAILED\n";
			assertEquals(result, facade.downloadBatch(new DownloadBatchRequest("test2","test2", 1)).toString());	//downloading a batch to a user who already has one
			assertEquals(result, facade.downloadBatch(new DownloadBatchRequest("test2", "test2", 6)).toString());	//invalid project id

		}catch(ClientException e){
			System.out.println("Failed downloadBatch()");
		}
	}
	
	@Test
	public void submitBatch(){
		try{
			DataImporter.parseXML();

			String testVals = "a,b,c,d;e,f,d,g;a,s,d,f;a,s,d,f;a,s,d,f;a,s,d,f;a,s,d,f;a,s,d,f;";
			assertEquals("FAILED\n" ,facade.submitBatch(new SubmitBatchRequest("test","test1", 1, testVals)).toString());		//Invalid username/password
			facade.downloadBatch(new DownloadBatchRequest("test1", "test1",1));
			assertEquals("FAILED\n" ,facade.submitBatch(new SubmitBatchRequest("test1","test1", 2, testVals)).toString()); 	//Submitting a batch not owned
			assertEquals("FAILED\n" ,facade.submitBatch(new SubmitBatchRequest("test2","test2", 1, testVals)).toString());	//Submitting a batch owned by someone else
			assertEquals("FAILED\n" ,facade.submitBatch(new SubmitBatchRequest("test1","test1", 500, testVals)).toString());	//Invalid batch id
			assertEquals("TRUE\n" ,facade.submitBatch(new SubmitBatchRequest("test1","test1", 1, testVals)).toString());		//Submitting correct data
			ValidateUserResult testResult = new ValidateUserResult("TRUE", "Test", "One", 8); 
			assertEquals(testResult.toString(),facade.validateUser(new ValidateUserRequest("test1","test1")).toString());	//Updated records indexed
			testVals = "a,b,d;e,f,d,g;a,s,d,f;a,s,d,f;a,s,d,f;a,s,d,f;a,s,d,f;a,s,d,f;";
			facade.downloadBatch(new DownloadBatchRequest("test1", "test1",1));
			assertEquals("FAILED\n" ,facade.submitBatch(new SubmitBatchRequest("test1","test1", 2, testVals)).toString());	//Submitting wrong number of values



		}catch(ClientException e){
			System.out.println("Failed submitBatch()");
		}
	}
	
	@Test
	public void getFields(){
		try{
			DataImporter.parseXML();
			
			String result = "FAILED\n";
			assertEquals(result, facade.getFields(new GetFieldsRequest("test1", "test", 1)).toString());	//invalid username/password
			assertEquals(result, facade.getFields(new GetFieldsRequest("test1", "test1", 6)).toString());	//invalid project id
			result = "2\n1\nGender\n2\n2\nAge\n2\n3\nLast Name\n2\n4\nFirst Name\n2\n5\nEthnicity\n";
			assertEquals(result, facade.getFields(new GetFieldsRequest("test2", "test2", 2)).toString());	//returns project 2 fields;
			result = "1\n1\nLast Name\n1\n2\nFirst Name\n1\n3\nGender\n1\n4\nAge\n2\n1\nGender\n2\n2\nAge\n2\n3\nLast Name\n2\n4\nFirst Name\n2\n5\nEthnicity\n3\n1\nLast Name\n3\n2\nFirst Name\n3\n3\nAge\n3\n4\nEthnicity\n";
			assertEquals(result, facade.getFields(new GetFieldsRequest("test2", "test2")).toString());		//returns all project fields;
		}catch(ClientException e){
			System.out.println("Failed getFields()");
		}
	}
	
	@Test
	public void search(){
		try{
			DataImporter.parseXML();

			String result = "FAILED\n";
			assertEquals(result, facade.search(new SearchRequest("test1", "test", "1,2", "Jim")).toString());			//invalid username/password
			assertEquals(result, facade.search(new SearchRequest("test1", "test1", "1,2312", "Jim")).toString());		//invalid fields values
			assertEquals(result, facade.search(new SearchRequest("test1", "test", "1,2", "")).toString());				//no search values entered
			assertEquals(result, facade.search(new SearchRequest("test1", "test", "1,2", "Jim, Pam, jon")).toString());	//no values found
			result = "41\nhttp://localhost:8080/images/draft_image0.png\n1\n10\n";
			assertEquals(result, facade.search(new SearchRequest("test1", "test1", "10,2", "FOX")).toString());			//found FOX
			assertEquals(result, facade.search(new SearchRequest("test1", "test1", "10,2", "fox")).toString());			//case insensitive
			facade.downloadBatch(new DownloadBatchRequest("test1", "test1",1));
			String testVals = "Malphite,Pendleton,c,d;e,f,d,g;a,s,d,f;a,s,d,f;a,s,d,f;a,s,d,f;a,s,d,f;a,s,d,f;";
			facade.submitBatch(new SubmitBatchRequest("test1","test1", 1, testVals));
			result = "1\nhttp://localhost:8080/images/1890_image0.png\n1\n1\n";
			assertEquals(result, facade.search(new SearchRequest("test1", "test1", "1", "MALPHITE")).toString());		//finds values added after a batch is submitted
		}catch(ClientException e){
			System.out.println("Failed search()");
		}
	}
	
	/*public void download(){
		try{
			DataImporter.parseXML();

		
		}catch(ClientException e){
			System.out.println("Failed download()");
		}
	}*/

	public static void main(String[] args) {
		
		String[] testClasses = new String[] {
				"server.ServerUnitTests"
				
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}
	
}

