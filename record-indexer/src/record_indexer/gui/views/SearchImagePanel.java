package record_indexer.gui.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SearchImagePanel extends JPanel {

	private BufferedImage myImage;

	public SearchImagePanel(){

		myImage = null;
		setBackground(Color.DARK_GRAY);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(myImage!=null){
			g.drawImage(myImage, 0, 0, null);
		}
	}
	
	public void setImage(URL url){
		try {
			myImage = ImageIO.read(url);
			
			myImage = getScaledImage(this.getWidth(), this.getHeight());
			this.repaint();
			this.revalidate();
		} catch (IOException e) {
			System.out.println("Unable to download batch");
		}
	}
	
	private BufferedImage getScaledImage(int newWidth, int newHeight){
		BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, myImage.getType());
		Graphics2D g = scaledImage.createGraphics();
		g.drawImage(myImage, 0, 0, newWidth, newHeight, null);
		g.dispose();
		return scaledImage;
	}
}
