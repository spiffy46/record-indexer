package record_indexer.gui;

import java.awt.*;

import javax.swing.JFrame;

import record_indexer.gui.controllers.*;
import record_indexer.gui.views.*;

public class SearchGUI {

	public static void main(String[] args){
		EventQueue.invokeLater(
				new Runnable(){
					public void run(){
						SearchFrame frame = new SearchFrame();
						SearchController controller = new SearchController();
						frame.setLocation(100, 100);
						JFrame.setDefaultLookAndFeelDecorated(true);
						frame.setController(controller);
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						controller.setView(frame);
						controller.initialize();
						frame.pack();
						frame.setVisible(true);
					}
				}
		);
	}
}
