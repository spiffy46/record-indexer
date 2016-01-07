package record_indexer.gui.misc;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class SaveState implements Serializable{
	private Dimension CURRENT_LOCATION;
	private String[][] BATCH_VALUES;
	private boolean[][] BATCH_STATUSES;
	private List<String> BATCHRESULTLIST;
		
	private Point WINDOW_POS;
	private int WINDOW_W;
	private int WINDOW_H;
	private int HORIZONTAL_SPLIT_POS;
	private int VERTICAL_SPLIT_POS;
	
	private int ZOOM_LEVEL;
	public int getZOOM_LEVEL() {
		return ZOOM_LEVEL;
	}

	public Dimension getIMAGE_LOCATION() {
		return IMAGE_LOCATION;
	}

	public boolean isHIGHTLIGHTED() {
		return HIGHTLIGHTED;
	}

	public boolean isINVERTED() {
		return INVERTED;
	}

	public void setZOOM_LEVEL(int zOOM_LEVEL) {
		ZOOM_LEVEL = zOOM_LEVEL;
	}

	public void setIMAGE_LOCATION(Dimension iMAGE_LOCATION) {
		IMAGE_LOCATION = iMAGE_LOCATION;
	}

	public void setHIGHTLIGHTED(boolean hIGHTLIGHTED) {
		HIGHTLIGHTED = hIGHTLIGHTED;
	}

	public void setINVERTED(boolean iNVERTED) {
		INVERTED = iNVERTED;
	}

	private Dimension IMAGE_LOCATION;
	private boolean HIGHTLIGHTED;
	private boolean INVERTED;

	private int PROJECT_ID;
	private int BATCH_ID;

	
	public SaveState(){
		this.CURRENT_LOCATION = null;
		this.BATCH_VALUES = null;
		this.BATCH_STATUSES = null;
		this.BATCHRESULTLIST = null;
		this.WINDOW_POS = null;
		this.WINDOW_W = -1;
		this.WINDOW_H = -1;
		this.HORIZONTAL_SPLIT_POS = -1;
		this.VERTICAL_SPLIT_POS = -1;
		this.ZOOM_LEVEL = -1;
		this.IMAGE_LOCATION = null;
		this.HIGHTLIGHTED = true;
		this.INVERTED = false;
		this.PROJECT_ID = -1;
		this.BATCH_ID = -1;
	}
	
	public List<String> getBATCHRESULTLIST(){
		return BATCHRESULTLIST;
	}
	
	public void setBATCHRESULTLIST(List<String> l){
		BATCHRESULTLIST = l;
	}
	

	public String[][] getBATCH_VALUES() {
		return BATCH_VALUES;
	}

	public Point getWINDOW_POS() {
		return WINDOW_POS;
	}

	public int getWINDOW_W() {
		return WINDOW_W;
	}

	public int getWINDOW_H() {
		return WINDOW_H;
	}

	public int getHORIZONTAL_SPLIT_POS() {
		return HORIZONTAL_SPLIT_POS;
	}

	public int getVERTICAL_SPLIT_POS() {
		return VERTICAL_SPLIT_POS;
	}

	public int getPROJECT_ID() {
		return PROJECT_ID;
	}

	public int getBATCH_ID() {
		return BATCH_ID;
	}
	
	public boolean[][] getBATCH_STATUSES(){
		return BATCH_STATUSES;
	}
	
	public Dimension getCURRENT_LOCATION(){
		return CURRENT_LOCATION;
	}
	
	public void setCURRENT_LOCATION(Dimension d){
		CURRENT_LOCATION = d;
	}
	
	public void setBATCH_STATUSES(boolean[][] v){
		BATCH_STATUSES = v;
	}

	public void setBATCH_VALUES(String[][] bATCH_VALUES) {
		BATCH_VALUES = bATCH_VALUES;
	}

	public void setWINDOW_POS(Point wINDOW_POS) {
		WINDOW_POS = wINDOW_POS;
	}

	public void setWINDOW_W(int wINDOW_W) {
		WINDOW_W = wINDOW_W;
	}

	public void setWINDOW_H(int wINDOW_H) {
		WINDOW_H = wINDOW_H;
	}

	public void setHORIZONTAL_SPLIT_POS(int hORIZONTAL_SPLIT_POS) {
		HORIZONTAL_SPLIT_POS = hORIZONTAL_SPLIT_POS;
	}

	public void setVERTICAL_SPLIT_POS(int vERTICAL_SPLIT_POS) {
		VERTICAL_SPLIT_POS = vERTICAL_SPLIT_POS;
	}

	public void setPROJECT_ID(int pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}

	public void setBATCH_ID(int bATCH_ID) {
		BATCH_ID = bATCH_ID;
	}
	
}
