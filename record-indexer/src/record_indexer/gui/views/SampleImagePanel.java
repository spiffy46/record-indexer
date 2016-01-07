package record_indexer.gui.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class SampleImagePanel extends JPanel{
	private BufferedImage myImage;
	
	public SampleImagePanel(){

		myImage = null;
		setBackground(Color.DARK_GRAY);
		setPreferredSize(new Dimension(700,400));
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(myImage!=null){
			g.drawImage(myImage, 0, 0, null);
		}
	}
	
	public void setImage(URL url) throws IOException{
		try {
			myImage = ImageIO.read(url);
			Dimension d = getPreferredSize();
			myImage = toBufferedImage(myImage.getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH));
			this.repaint();
			this.revalidate();
		} catch (IOException e) {
			throw new IOException();
		}
	}
	
	private static BufferedImage toBufferedImage(Image img){
		if(img instanceof BufferedImage){
			return (BufferedImage)img;
		}
		
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		
		return bimage;
	}
}
