package record_indexer.gui.misc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class Trie implements Serializable{
	private Set<String> myWords;
	
	public Trie(){
		myWords = new HashSet<String>();
	}
	
	public void add(String word){
		myWords.add(word);
	}
	
	
	public boolean find(String word){
		return myWords.contains(word);
	}
	
	public Object[] findSimilar(String word){
		Set<String> wordList = new HashSet<String>();
		Set<String> possibleWords = new HashSet<String>();
		deletion(wordList, possibleWords, word);
		transposition(wordList, possibleWords, word);
		alteration(wordList, possibleWords, word);
		insertion(wordList, possibleWords, word);
		
		String[] sArray = possibleWords.toArray(new String[0]);
		for(int i = 0; i < sArray.length; i++){
			String word2 = sArray[i];
			deletion(wordList, possibleWords, word2);
			transposition(wordList, possibleWords, word2);
			alteration(wordList, possibleWords, word2);
			insertion(wordList, possibleWords, word2);
		}
		
		Object[] ans = wordList.toArray();
		Arrays.sort(ans);
		return ans;
	}

	public void deletion(Set<String> nList, Set<String> pWords, String w)
	{
		String newW;
		if(w.length() > 1)
		{
			newW = w.substring(1, w.length());
			pWords.add(newW);
			if(find(newW))
			{
				nList.add(newW);
			}

			for(int i = 1; i < w.length()-1; i++)
			{
				newW = w.substring(0, i) + w.substring(i+1, w.length());
				pWords.add(newW);
				if(find(newW))
				{
					nList.add(newW);
				}
			}
			newW = w.substring(0, w.length()-1);
			pWords.add(newW);
			if(find(newW))
			{
				nList.add(newW);
			}
		}
	}
	
	public void transposition(Set<String> nList, Set<String> pWords, String w)
	{
		StringBuilder buildW;
		if(w.length() > 1)
		{
			for(int i = 0; i < w.length()-1; i++)
			{
				buildW = new StringBuilder(w);
				char x = w.charAt(i);
				char y = w.charAt(i+1);
				buildW.setCharAt(i, y);
				buildW.setCharAt(i+1, x);
				pWords.add(buildW.toString());
				if(find(buildW.toString()))
				{
					nList.add(buildW.toString());
				}
			}
		}
	}
	
	public void alteration(Set<String> nList, Set<String> pWords, String w)
	{
		StringBuilder buildW;
		for(int i = 0; i < w.length(); i++)
		{
			char tempChar = 'a';
			for(int j = 0; j < 26; j++)
			{
				buildW = new StringBuilder(w);
				buildW.setCharAt(i, tempChar);
				tempChar++;
				pWords.add(buildW.toString());
				if(find(buildW.toString()))
				{
					nList.add(buildW.toString());
				}
			}
		}
	}
	
	public void insertion(Set<String> nList, Set<String> pWords, String w)
	{
		StringBuilder buildW;
		for(int i = 0; i <= w.length(); i++)
		{
			char tempChar = 'a';
			for(int j = 0; j < 26; j++)
			{
				buildW = new StringBuilder(w);
				buildW.insert(i, tempChar);
				tempChar++;
				pWords.add(buildW.toString());
				if(find(buildW.toString()))
				{
					nList.add(buildW.toString());
				}
			}
		}
	}
}
