package record_indexer.gui.misc;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import record_indexer.shared.model.field;

@SuppressWarnings("serial")
public class Sync extends Observable implements Serializable{

	String[][] myValues;
	boolean[][] myStatuses;
	Dimension currentCell;
	int batchNum;
	int projectNum;
	Trie[] myTries;
	boolean hasKnownData;
	private SpellCorrector mySpellCorrector;
	private List<String> batchResultList;
	
	public Sync(){
		projectNum = -1;
		batchNum = -1;
		hasKnownData = false;
		currentCell = new Dimension(-1,-1);
		mySpellCorrector = new SpellCorrector();
	}
	
	public Sync(int rows, int cols){
		projectNum = -1;
		batchNum = -1;
		hasKnownData = false;
		myValues = new String[rows][cols];
		myStatuses = new boolean[rows][cols];
		for(int i = 0; i < rows; i++){
			Arrays.fill(myStatuses[i], true);
		}
		currentCell = new Dimension(-1,-1);
		mySpellCorrector = new SpellCorrector();
	}
	
	public void changeCurrentCell(Dimension d){
		if(currentCell.equals(d))
			return;
		
		if(currentCell.width != d.width){
			Trie tmpTrie = myTries[d.width];
			if(tmpTrie != null){
				mySpellCorrector.setTrie(tmpTrie);
				hasKnownData = true;
			}else{
				hasKnownData = false;
			}
		}
		
		setChanged();
		currentCell = d;
		notifyObservers(currentCell);
	}
	
	public Dimension getCurrentCell(){
		return currentCell;
	}
	
	public void changeCurrentCellContents(String value){
		String data = value;
		myValues[currentCell.height][currentCell.width] = data;
		setChanged();
		notifyObservers(data);
		
		if(!hasKnownData)
			return;
		
		if(mySpellCorrector.find(data))
			validateData(true);
		else
			validateData(false);
	}
	
	public void validateData(boolean b){
		setChanged();
		this.setStatusAt(b, currentCell.height, currentCell.width);
		notifyObservers(b);
	}
	
	public String getData(){
		return myValues[currentCell.height][currentCell.width];
	}
	
	public String getData(int h, int w){
		return myValues[h][w];
	}

	public boolean getStatusAt(int h, int w) {
		return myStatuses[h][w];
	}
	
	public List<String> getBatchResultList(){
		return batchResultList;
	}
	
	public void setBatchResultList(List<String> l){
		batchResultList = l;
	}
	
	public void setStatusAt(boolean s, int h, int w){
		myStatuses[h][w] = s;
	}
	
	public Object[] suggestSimilar(int h, int w){
		Trie tmpTrie = mySpellCorrector.getTrie();
		mySpellCorrector.setTrie(myTries[w]);
		Object[] ans = mySpellCorrector.suggestSimilarWords(getData(h, w));
		mySpellCorrector.setTrie(tmpTrie);
		return ans;
	}

	public void createTries(List<field> parseFields) {
		myTries = new Trie[parseFields.size()];
		for(int i = 0; i < parseFields.size(); i++){
			field f = parseFields.get(i);
			String knownDataURL = f.getKnowndata();
			if(knownDataURL.equals("")){
				myTries[i] = null;
			}else{
				mySpellCorrector.setTrie(new Trie());
				mySpellCorrector.useDictionary(knownDataURL);
				myTries[i] = mySpellCorrector.getTrie();
			}
		}
	}
	
	public boolean[][] getStatuses(){
		return myStatuses;
	}
	
	public void setStatuses(boolean[][] v){
		myStatuses = v;
	}
	
	public void setValues(String[][] v){
		myValues = v;
	}
	
	public String[][] getValues(){
		return myValues;
	}

	public void setBatchNum(int parseInt) {
		batchNum = parseInt;
	}
	
	public int getBatchNum(){
		return batchNum;
	}
	
	public void setProjectNum(int num){
		projectNum = num;
	}
	
	public int getProjectNum(){
		return projectNum;
	}
}
