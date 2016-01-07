package record_indexer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import record_indexer.server.databaseAccess.DataIndexer;


/**
 * Imports SQLite table data from file using file pathway as command line parameter
 * @author tander46
 *
 */

public class DataImporter {
	public static Pattern subStr = Pattern.compile(".xml");
	public static Matcher matcher;
	public static String PATH_WAY = "indexer_data/Records";
	
	public static void main(String[] args) {
		String path = args[0];
		deleteFiles();
		copy(path, PATH_WAY);
		parseXML();
	}
		/**
		 * Transfers information from an XML file into a SQLite database
		 * @param pathway The path of the zip file
		 */
		public static void parseXML()
		{
			File xmlFile = new File(PATH_WAY);
			
			xmlFile = findXML(xmlFile);
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				
				Element root = doc.getDocumentElement();
				
				DataIndexer dIndexer = new DataIndexer(root);
				dIndexer.convertToSql();
				
			} catch (ParserConfigurationException e) {
				System.out.println("Caught ParserConfigurationException in DataImporter parseXML()");
				e.printStackTrace();
			} catch (SAXException e) {
				System.out.println("Caught SAXException in DataImporter parseXML()");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Caught IOException in DataImporter parseXML()");
				e.printStackTrace();
			}
		}
		
		public static File findXML(File f){
			if(f.isFile()){
				matcher = subStr.matcher(f.getName());
				if(matcher.find()){
					return f;
				}else{
					return null;
				}
			}else{
				File[] list = f.listFiles();
				for(File tmpFile: list){
					File forFile = findXML(tmpFile);
					if(forFile != null){
						return forFile;
					}
				}
			}
			return null;
		}
		
		public static ArrayList<Element> getChildElements(Node node)
		{
			ArrayList<Element> result = new ArrayList<Element>();
			
			NodeList children = node.getChildNodes();
			for(int i = 0; i < children.getLength(); i++)
			{
				Node child = children.item(i);
				if(child.getNodeType() == Node.ELEMENT_NODE){
					result.add((Element)child);
				}
			}
			return result;
		}
		
		public static void copy(String source, String target){
			File file = new File(source);
			String pSource = file.getParent();
			makeLocalCopy(pSource,target);
		}
		
		/**
		 * Makes a local copy of all files in a given pathway
		 * @param pathway The path of the file or directory
		 */
		public static void makeLocalCopy(String source, String target)
		{
			Path SOURCE = Paths.get(source);
			Path TARGET = Paths.get(target);
			File file = new File(source);
			try {
					Files.copy(SOURCE, TARGET, StandardCopyOption.REPLACE_EXISTING);
					if(file.isDirectory()){
						for(File f: file.listFiles()){
							makeLocalCopy(f.getPath(), target + "/" + f.getName());
						}
					}
			} catch (IOException e) {
				System.out.println("Unable to copy files");
				e.printStackTrace();
			}
		}
		
		/**
		 * Deletes all content in a given pathway
		 * @param pathway The path of the file or directory
		 */
		public static void deleteFiles()
		{
			File directory = new File(PATH_WAY);
			for(File f: directory.listFiles()){
				if(f.isFile())
					f.delete();
				else{
					deleteSub(f);
					f.delete();
				}
			}
		}
		
		public static void deleteSub(File file){
			for(File f: file.listFiles()){
				if(f.isFile())
					f.delete();
				else{
					deleteSub(f);
					f.delete();
				}
			}
		}

}
