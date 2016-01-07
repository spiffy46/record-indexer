package record_indexer.gui.views;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;

import record_indexer.shared.model.field;

@SuppressWarnings("serial")
public class HelpHtmlPanel extends JEditorPane implements Observer{

	List<field> fields;
	
	public HelpHtmlPanel(){
		setContentType("text/html");
		setEditable(false);
	}
	
	public void setPage(int fieldNum){
		String url = fields.get(fieldNum).getHelphtml();
		if(url != ""){
			try {
				setPage(url);
			} catch (IOException e) {
				System.out.println("Bad url: " + url);
			}
		}
	}
	
	public List<field> getFields(){
		return fields;
	}
	
	public void setFields(List<field> f){
		fields = f;
	}
	
	public int getFieldsSize(){
		if(fields == null){
			return 0;
		}
		else{
			return fields.size();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg.getClass() == Dimension.class){
			Dimension d = (Dimension)arg;
			int fieldNum = (int)d.getWidth();
			setPage(fieldNum);
		}
		
	}
}
