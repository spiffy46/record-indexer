package record_indexer.gui;

import java.awt.*;

import record_indexer.gui.controllers.Controller;
import record_indexer.gui.views.RecordIndexerFrame;

public class GUI {

	public static void main(String[] args) {
		final String host = args[0];
		final String port = args[1];
		EventQueue.invokeLater(
				new Runnable() {
					public void run() {
						Controller controller = new Controller();
						RecordIndexerFrame frame = new RecordIndexerFrame();			
						frame.setController(controller);			
						controller.setView(frame);
						controller.initialize(host,port);
						frame.createComponents();
						frame.loginScreen();
					}
				}
		);

	}

}