package record_indexer.gui.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


@SuppressWarnings("serial")
public class SpellCorrector implements Serializable{
	private Trie myTrie;
	private transient Scanner s;
	
	public SpellCorrector(){
		myTrie = new Trie();
	}
	
	public void useDictionary(String knownDataURL){
		URL url;
		try {
			url = new URL(knownDataURL);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			s = new Scanner(in);
			s.useDelimiter(",");
			
			while(s.hasNext()){
				myTrie.add(s.next().toLowerCase());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Object[] suggestSimilarWords(String inputWord){
		String tempStr = inputWord.toLowerCase();
		return myTrie.findSimilar(tempStr);
	}
	
	public boolean find(String inputWord){
		String tempStr = inputWord.toLowerCase();
		return myTrie.find(tempStr);
	}
	
	public Trie getTrie(){
		return myTrie;
	}
	
	public void setTrie(Trie t){
		myTrie = t;
	}
}
