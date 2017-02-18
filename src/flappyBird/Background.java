package flappyBird;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background {
	BufferedImage image2;
//	BufferedImage image3;
	int x2;
	int y2;
//	int x3;
//	int y3;
	int width2;
	int height2;
//	int width3;
//	int height3;
	public Background(int n) throws IOException{
		y2 = 0;
		image2 = ImageIO.read(getClass().getResource("bg.png"));
		width2 = image2.getWidth();
		height2 = image2.getHeight();
		x2 = n;
//		y3 =0;
//		image3 = ImageIO.read(getClass().getResource("bg.png"));
//		width3 = image3.getWidth();
//		height3 = image3.getHeight();
//		x3 = 432;
	}
	public void step(){
		x2--;
		if(x2<= -(432)){
			x2=432;
		}
//		x3--;
//		if(x3<= -(432)){
//			x3=432;
//		}
	}
	public void paint(Graphics g) {
		g.drawImage(image2, x2, y2, null);
//		g.drawImage(image3, x3, y3, null);
	}
}
