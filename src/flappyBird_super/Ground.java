package flappyBird_super;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * �����x,y�ǵ���ͼƬ�����Ͻ�
 * @author Robin
 *
 */
public class Ground {
	//����
	BufferedImage image;
	
	//��������
	int x;
	int y;
	
	//��С
	int width;
	int height;
	
	public Ground() throws IOException {
		y = 500;
		image = ImageIO.read(getClass().getResource("ground.png"));
		width = image.getWidth();
		height = image.getHeight();
		x = 0;
		
		
	}
	
	public void step(){
		x--;
		if(x<= -(109)){
			x=0;
		}
		
	}

	public void paint(Graphics g) {
		g.drawImage(image, x, y, null);		
		
	}
}
