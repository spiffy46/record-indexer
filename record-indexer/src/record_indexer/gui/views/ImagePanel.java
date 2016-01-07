package record_indexer.gui.views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;

import record_indexer.gui.misc.Sync;
import record_indexer.shared.model.field;

@SuppressWarnings("serial")
class DrawingRect implements Serializable{
	private Rectangle2D rect;
	private Color color = Color.blue;
	
	public DrawingRect(Rectangle2D r){
		rect = r;
	}
	
	public void draw(Graphics2D g2){
		Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .4f);
		g2.setComposite(comp);
		g2.setColor(color);
		g2.fill(rect);
	}
}

@SuppressWarnings("serial")
public class ImagePanel extends JPanel implements Observer, Serializable{
	private transient Sync observed;
	
	private boolean inverted;
	private boolean highlighted;
	private Dimension currentHighlight;
	
	private transient RecordIndexerFrame root;
	
	public int x = 0;
	public int y = 0;
	public final double[] ZOOMSTATES = {.25, .5, .75, 1, 1.5 , 2.5, 3.5};
	private int currentState;
	private int firstycoord;
	private int recordheight;
	private int numrecords;
	private List<field> imageFields;
	private DrawingRect highlightRect;

	private transient BufferedImage myImage;
	private transient BufferedImage defaultImage;
	private transient BaseImagePanel parent;
			
	public ImagePanel(final BaseImagePanel p){
		currentHighlight = new Dimension(0,0);
		inverted = false;
		highlighted = true;
		parent = p;
		myImage = null;
		defaultImage = null;
		setBackground(Color.DARK_GRAY);
		currentState = 3;
		
		MouseAdapter m = getMouseAdapter();
		this.addMouseListener(m);
		this.addMouseWheelListener(m);
		this.addMouseMotionListener(m);
	}
	
	public void setParent(final BaseImagePanel p){
		parent = p;
	}
	
	public ImagePanel(){
		inverted = false;
		highlighted = true;
		myImage = null;
		defaultImage = null;
		setBackground(Color.DARK_GRAY);
		currentState = 3;
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(myImage!=null){
			g.drawImage(myImage, x, y, null);
		}
		if(highlighted && highlightRect != null){
			highlightRect.draw((Graphics2D)g);
		}
	}
	
	@Override
	public Dimension getPreferredSize(){
		this.repaint();
		return super.getPreferredSize();
	}
	
	public void setImage(List<String> batchResultList){
		try {
			URL url = new URL(batchResultList.get(2));
			defaultImage = ImageIO.read(url);
			firstycoord = Integer.parseInt(batchResultList.get(3));
			recordheight = Integer.parseInt(batchResultList.get(4));
			numrecords = Integer.parseInt(batchResultList.get(5));
			imageFields = RecordIndexerFrame.parseFields(batchResultList);
			
			myImage = getScaledImage(defaultImage.getWidth(), defaultImage.getHeight());
			int newHeight = myImage.getHeight();
			int newWidth = myImage.getWidth();
			this.setPreferredSize(new Dimension(newWidth * 2,newHeight *2));
			x = getWidth()/2;
			y = getHeight()/2;
			x = x - myImage.getWidth()/2;
			y = y - myImage.getHeight()/2;
			this.repaint();
			this.revalidate();
		} catch (IOException e) {
			System.out.println("Unable to download batch");
		}
	}
	
	public void setImage(BufferedImage i){
		defaultImage = i;
		int newWidth = (int)(defaultImage.getWidth() * ZOOMSTATES[currentState]);
		int newHeight = (int)(defaultImage.getHeight() * ZOOMSTATES[currentState]);
		myImage = getScaledImage(newWidth, newHeight);
		this.repaint();
		this.revalidate();
	}
	
	public DrawingRect highlight(Dimension d, double ZOOM){
		int xCoord = imageFields.get((int)d.getWidth()).getXcoord();
		int yCoord = firstycoord + recordheight * (int)d.getHeight();
		int width = imageFields.get((int)d.getWidth()).getWidth();
		Rectangle2D rectangle = new Rectangle2D.Double((int)(xCoord*ZOOM) + x, (int)(yCoord*ZOOM) + y, (int)Math.round(width*ZOOM), (int)Math.round(recordheight*ZOOM));
		return new DrawingRect(rectangle);
	}
	
	public void zoomIN(){
		if(currentState < 6 && defaultImage != null){
			Point p = getCenter(ZOOMSTATES[currentState]);
			currentState++;
			int newWidth = (int)(defaultImage.getWidth() * ZOOMSTATES[currentState]);
			int newHeight = (int)(defaultImage.getHeight() * ZOOMSTATES[currentState]);
			myImage = getScaledImage(newWidth, newHeight);
			if(newWidth*2 < getWidth())
				newWidth = getWidth()/2;
			if(newHeight*2 < getHeight())
				newHeight = getHeight()/2;
			this.setPreferredSize(new Dimension(newWidth * 2,newHeight * 2));
			setViewPortCenter(p,ZOOMSTATES[currentState]);

			this.repaint();
			this.revalidate();
			
			if(currentState == 6){
				parent._buttonPanel.enableZoomIN(false);
			}
			if(currentState == 1){
				parent._buttonPanel.enableZoomOUT(true);
			}
		}
	}
	
	public void zoomOUT(){
		if(currentState > 0 && defaultImage != null){
			Point p = getCenter(ZOOMSTATES[currentState]);
			currentState--;
			highlightRect = highlight(currentHighlight, ZOOMSTATES[currentState]);
			int newWidth = (int)(defaultImage.getWidth() * ZOOMSTATES[currentState]);
			int newHeight = (int)(defaultImage.getHeight() * ZOOMSTATES[currentState]);
			myImage = getScaledImage(newWidth, newHeight);
			if(newWidth*2 < getWidth())
				newWidth = getWidth()/2;
			if(newHeight*2 < getHeight())
				newHeight = getHeight()/2;
			this.setPreferredSize(new Dimension(newWidth*2,newHeight*2));
			setViewPortCenter(p,ZOOMSTATES[currentState]);
			this.repaint();
			this.revalidate();
			
			if(currentState == 0){
				parent._buttonPanel.enableZoomOUT(false);
			}
			if(currentState == 5){
				parent._buttonPanel.enableZoomIN(true);
			}
		}
	}
	
	public void invert(){
		inverted = !inverted;
		
		for(int i = 0; i < defaultImage.getWidth(); i++){
			for(int j = 0; j < defaultImage.getHeight(); j++){
				int rgba = defaultImage.getRGB(i, j);
				Color col = new Color(rgba, true);
				col = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());
				defaultImage.setRGB(i,j,col.getRGB());
			}
		}
		
		for(int i = 0; i < myImage.getWidth(); i++){
			for(int j = 0; j < myImage.getHeight(); j++){
				int rgba = myImage.getRGB(i, j);
				Color col = new Color(rgba, true);
				col = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());
				myImage.setRGB(i,j,col.getRGB());
			}
		}
		
		parent.getRoot();
		this.repaint();
		this.revalidate();
	}
	
	public void toggle() {
		highlighted = !highlighted;
		repaint();
	}
	
	private Point getCenter(double zoomState){
		int centerX = getWidth()/2;
		int centerY = getHeight()/2;
		int centerIX = x + myImage.getWidth()/2;
		int centerIY = y + myImage.getHeight()/2;
		int xdif = centerX - centerIX;
		int ydif = centerY - centerIY;
		centerX = xdif + myImage.getWidth()/2;
		centerY = ydif + myImage.getHeight()/2;
		Point p = new Point(centerX, centerY);
		return new Point((int)(p.x/zoomState),(int)(p.y/zoomState));
	}
	
	private void setViewPortCenter(Point p, double zoomState){

		int newCenterX = (int)(p.x*zoomState);
		int newCenterY = (int)(p.y*zoomState);
		int centerX = getWidth()/2;
		int centerY = getHeight()/2;
		
		x = centerX - newCenterX;
		y = centerY - newCenterY;
		
		if(x < getWidth()/2 - getPreferredSize().width/2)
			x = getWidth()/2 - getPreferredSize().width/2;
		if(y < getHeight()/2 - getPreferredSize().height/2)
			y = getHeight()/2 - getPreferredSize().height/2;
		if(x > getPreferredSize().width - myImage.getWidth())
			x = getPreferredSize().width - myImage.getWidth();
		if(y > getPreferredSize().height - myImage.getHeight())
			y = getPreferredSize().height - myImage.getHeight();
		highlightRect = highlight(currentHighlight, ZOOMSTATES[currentState]);
	}
	
	private BufferedImage getScaledImage(int newWidth, int newHeight){
		BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, defaultImage.getType());
		Graphics2D g = scaledImage.createGraphics();
		g.drawImage(defaultImage, 0, 0, newWidth, newHeight, null);
		g.dispose();
		return scaledImage;
	}
	
	private void adjustPosition(int dx, int dy){
		x = x + dx;
		y = y + dy;
		if(x < getWidth()/2 - getPreferredSize().width/2)
			x = getWidth()/2 - getPreferredSize().width/2;
		if(y < getHeight()/2 - getPreferredSize().height/2)
			y = getHeight()/2 - getPreferredSize().height/2;
		if(x > getPreferredSize().width - myImage.getWidth())
			x = getPreferredSize().width - myImage.getWidth();
		if(y > getPreferredSize().height - myImage.getHeight())
			y = getPreferredSize().height - myImage.getHeight();
		highlightRect = highlight(currentHighlight, ZOOMSTATES[currentState]);
		
		this.repaint();
	}
	
	public void setRoot(RecordIndexerFrame r){
		root = r;
	}
	
	public RecordIndexerFrame getRoot(){
		return root;
	}
	
	public void setObserved(Sync o){
		observed = o;
	}

	public void setInverted(boolean b){
		if(b)
			invert();
	}
	
	public boolean getInverted(){
		return inverted;
	}
	
	public void setZoom(int z){
		if(z > currentState){
			while(currentState != z){
				zoomIN();
			}
		}else if(z < currentState){
			while(currentState != z){
				zoomOUT();
			}
		}
	}
	
	public int getZoom(){
		return currentState;
	}
	
	public void setHighlighted(boolean b){
		highlighted = b;
	}
	
	public boolean getHighlighted(){
		return highlighted;
	}
	
	public void setImageLoc(Dimension d){
		x = d.width;
		y = d.height;
		repaint();
	}
	
	public Dimension getImageLoc(){
		return new Dimension(x,y);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(arg.getClass() == Dimension.class){
			currentHighlight = (Dimension)arg;
			highlightRect = highlight(currentHighlight, ZOOMSTATES[currentState]);
			this.repaint();
		}
	}
	
	private MouseAdapter getMouseAdapter(){
		MouseAdapter mouseAdapter = new MouseAdapter(){
			private Point2D lastPoint;
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e){
				if(myImage == null)
					return;
				
				int scrollAmount = 0;
				if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL){
					scrollAmount = e.getUnitsToScroll();
				}else{
					scrollAmount = e.getWheelRotation();
				}
				
				if(scrollAmount > 0){
					zoomOUT();
				}
				
				if(scrollAmount < 0){
					zoomIN();
				}
			}
			
			@Override 
			public void mousePressed(MouseEvent e){
				if(myImage == null)
					return;
				
				lastPoint = new Point2D.Double(e.getX(), e.getY());
				double ZOOM = ZOOMSTATES[currentState];

				for(int i = 0; i < imageFields.size(); i++){
					field f = imageFields.get(i);
					if(lastPoint.getX() > (f.getXcoord()*ZOOM)+x && lastPoint.getX() < ((f.getXcoord()+f.getWidth())*ZOOM)+x){
						int tmpY = firstycoord;
						for(int j = 0; j < numrecords; j++){
							if(lastPoint.getY() > tmpY*ZOOM + y && lastPoint.getY() < ((tmpY+recordheight)*ZOOM) + y){
								currentHighlight = new Dimension(i,j);
								highlightRect = highlight(currentHighlight, ZOOMSTATES[currentState]);
								repaint();
								
								observed.changeCurrentCell(currentHighlight);
							}
							tmpY+=recordheight;
						}	
					}
				}
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e){
				if(myImage == null)
					return;
				
				int dx = e.getX() - (int)lastPoint.getX();
				int dy = e.getY() - (int)lastPoint.getY();
				
				adjustPosition(dx, dy);
				
				lastPoint = new Point2D.Double(e.getX(), e.getY());
			}
			
		};
		
		return mouseAdapter;
	}		
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
		
		if(defaultImage == null){
			out.writeInt(0);
		}else{
			out.writeInt(1);
			ImageIO.write(defaultImage, "png", out);
		}
	}
	
	private void readObject(ObjectInputStream in)throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		final int imageCount = in .readInt();
		if(imageCount == 0){
			defaultImage = null;
			myImage = null;
		}else{
			setImage(ImageIO.read(in));
		}
		
		MouseAdapter m = getMouseAdapter();
		this.addMouseListener(m);
		this.addMouseWheelListener(m);
		this.addMouseMotionListener(m);
		
	}
	
}
