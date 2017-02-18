package flappyBird_v2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * ����е�����
 * @author Robin
 */
public class MyBird extends JPanel {
	
	Column column1;
	Column column2;
	Bird bird;
	Bird bird2;
	Ground ground;
	BufferedImage background;
	BufferedImage gameoverImg;
	BufferedImage startImg;
	Background backgrounds1;
	Background backgrounds2;
	
	boolean start;
	int score;
	boolean gameOver;
	
	int index = 0;

	public MyBird() throws Exception {
		background = ImageIO.read(getClass().getResource("bg.png"));
		gameoverImg = ImageIO.read(getClass().getResource("gameover.png"));
		startImg = ImageIO.read(getClass().getResource("start.png"));
		start();
	}
	public void start(){
		try {
			start = false;
			gameOver = false;
			bird = new Bird(132);
			bird2 = new Bird(220);
			ground = new Ground(); 
			column1 = new Column(1);
			column2 = new Column(2);
			backgrounds1 = new Background(0);
			backgrounds2 = new Background(432);
			score = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void action() throws Exception{		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(e.getButton()==MouseEvent.BUTTON1){
					if (gameOver) {
						start();
						return;
					}
					start = true;
					bird.flappy();
				}
				if(e.getButton()==MouseEvent.BUTTON3){
					if (gameOver) {
						start();
						return;
					}
					start = true;
				bird2.flappy();
				}
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_RIGHT ){
					if(gameOver){
						start();
						return;
					}
					start = true;
					bird.flappy();
				}
				if(e.getKeyCode()==KeyEvent.VK_LEFT ){
					if(gameOver){
						start();
						return;
					}
					start = true;
					bird2.flappy();
				}
				if(e.getKeyCode()==KeyEvent.VK_SPACE ){
					if(gameOver){
						start();
						return;
					}
					start = true;
					bird.flappy();
					bird2.flappy();
				}
			}
		});
		requestFocus();
		//��ѭ��, ʱ������ 1/60 ��
		while(true){
			if(start && !gameOver){
				bird.step();
				bird2.step();
				column1.step();
				column2.step();
				backgrounds1.step();
				backgrounds2.step();
				//����Ƿ�ͨ��������
				if(bird.pass(column1, column2)){
					score+=10;
				}
				if(bird.hit(column1, column2, ground)){
					start = false;
					gameOver = true;
				}
				if(bird2.pass(column1, column2)){
					score+=10;
				}
				if(bird2.hit(column1, column2, ground)){
					start = false;
					gameOver = true;
				}
			}
			if(! gameOver) {
			bird.fly();
			bird2.fly();
			}
			ground.step();
			repaint();
			Thread.sleep(1000/70);
		}
				
	}
	@Override
	public void paint(Graphics g) {
		//����ݴ���
//		Graphics2D g2 = (Graphics2D)g;
//		RenderingHints qualityHints = new RenderingHints(
//				RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//		qualityHints.put(RenderingHints.KEY_RENDERING,
//				RenderingHints.VALUE_RENDER_QUALITY);
//		g2.setRenderingHints(qualityHints);
		//���Ʊ���
//		g.drawImage(background, 0, 0, null);//����
		backgrounds1.paint(g);
		backgrounds2.paint(g);
		//��������
		column1.paint(g);
		column2.paint(g); 
		//���Ƶ���
		ground.paint(g);
		//���Ʒ���
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 45);
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(score+"", 30, 50);
		//����С��
		bird.paint(g);
		bird2.paint(g);
		//���ƽ���״̬
		if(gameOver){
			//g.drawString("Game Over!", 70 , 190);
			g.drawImage(gameoverImg, 0, 0, null);
			return;
		}
		if(! start){
			//g.drawString("Start >>>", bird.x+35, bird.y);
			g.drawImage(startImg, 0, 0, null);
		}
	}
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("����С��--��̬��");
		MyBird myBird = new MyBird();
		frame.add(myBird);
		frame.setSize(432, 670);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		myBird.action();
	}
}

class Ground {
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
class Column {
	BufferedImage image;
	//�����ӵ��м���Ϊ���ӵ�λ��
	int x;
	int y; // 140 ~ 280
	int width;
	int height;
	int gap = 144;
	Random r = new Random();
	
	int distance = 245;
	
	public Column(int n) throws IOException { 
		image = ImageIO.read(getClass().getResource("column.png"));
		width = image.getWidth();
		height = image.getHeight();
		this.x = distance * n + 80 + 432;
		this.y = r.nextInt(120) + 220;
	}
	
	public void step(){
		x--;
		if(x<=-width/2){
			x = distance*2-width/2;
			this.y = r.nextInt(120) + 220;
		}
	}
	public void paint(Graphics g){
		//g.drawRect(x-width/2, y-height/2, width, height/2-gap/2);
		//g.drawRect(x-width/2, y+gap/2, width, height/2-gap/2);
		g.drawImage(image, x-width/2, y-height/2, null);
	}
}
class Bird {
	/** �����λ��, ���λ�����������λ�� */
	int x;	int y;
	/** ���нǶ� */
	double angle;
	/** ����֡ */
	BufferedImage[] images;
	/** ��ǰͼƬ */
	BufferedImage image;
	/** ��ǰͼƬ��� */
	int index = 0;
	/** �������ٶ� */
	final double g; 
	/** ʱ������ */
	final double t;
	/** ��ʼ�����ٶ� */
	final double v0;
	/** ��ǰ�����ٶ� */
	double speed;
	/** �ƶ����� */
	double s;
	/** ��ķ�Χ, ��ķ�Χ��һ������������, ���ĵ���x,y */
	int size = 40;
	
	public Bird(int n) throws Exception {
		this.g = 4; //�������ٶ�
		this.t = 0.25; //ÿ�μ���ļ��ʱ��
		this.v0 = 20; //��ʼ�����ٶ�
		x = n; //��ĳ�ʼλ��
		y = 275; //��ĳ�ʼλ��
		//���ض���֡
		images = new BufferedImage[8];
		for(int i=0; i<8; i++){
			images[i] = ImageIO.read(getClass().getResource(i+".png"));
		}
		image = images[0];
	}
	
	/** ����һ��  
	 * ��ֱ����λ�Ƽ���
	 *  (1) �����ٶȼ��� V=Vo-gt  
		(2) ���׾������ S=Vot-1/2gt^2
	 * */
	public void step(){
		//V0 �Ǳ���
		double v0 = speed;
		//���㴹ֱ�����˶�, ����ʱ��t�Ժ���ٶ�, 
		double v = v0 - g*t;
		//��Ϊ�´μ���ĳ�ʼ�ٶ�
		speed = v;
		//���㴹ֱ�����˶������о���
		s = v0*t - 0.5 * g * t * t;
		//������ľ��� ����Ϊ y����ı仯
		y = y - (int)s;
		//����С�������, 
		angle = -Math.atan(s/8);
		
	}
	public void fly(){
		//����С��Ķ���֡ͼƬ, ����/4 ��Ϊ�˵����������µ�Ƶ��
		index++;
		image = images[(index/8)%images.length];
	}
	/** С�����Ϸ��� */
	public void flappy(){
		//ÿ��С��������Ծ, ���ǽ�С���ڵ�ǰ�������Գ�ʼ�ٶ� V0 ������
		speed = v0;
	}
	//����ʱ����ִ�е�, Ҫͬ������ ��ת����ϵ���������Ʒ�����Ӱ��
	public synchronized void paint(Graphics g){
		//g.drawRect(x-size/2, y-size/2, size, size);
		Graphics2D g2 = (Graphics2D)g;
		//��ת��ͼ����ϵ, ����
		g2.rotate(angle, this.x, this.y);
		//��x,y Ϊ���Ļ���ͼƬ
		int x = this.x-image.getWidth()/2;
		int y = this.y-image.getHeight()/2;
		g.drawImage(image, x, y, null);
		//��ת���� 
		g2.rotate(-angle, this.x, this.y);
	}

	@Override
	public String toString() {
		return "Bird [x=" + x + ", y=" + y + ", g=" + g + ", t=" + t + ", v0="
				+ v0 + ", speed=" + speed + ",s="+s+"]";
	}
	/** �ж����Ƿ�ͨ������ */
	public boolean pass(Column col1, Column col2) {
		return col1.x==x || col2.x==x;
	}
	/** �ж����Ƿ������Ӻ͵ط�����ײ */
	public boolean hit(Column column1, Column column2, Ground ground) {
		//��������
		if(y-size/2 >= ground.y){
			return true;
		}
		//��������
		return hit(column1) || hit(column2);
	}
	/** ��鵱ǰ���Ƿ��������� */
	public boolean hit(Column col){
		//�������������: ������ĵ�x������ ���ӿ�� + ���һ��
		if( x>col.x-col.width/2-size/2 && x<col.x+col.width/2+size/2){
			if(y>col.y-col.gap/2+size/2 && y<col.y+col.gap/2-size/2 ){
				return false;
			}
			return true;
		}
		return false;
	}
	
}

class Background {
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
