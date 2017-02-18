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
 * 鸟飞行的世界
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
		//主循环, 时间间隔是 1/60 秒
		while(true){
			if(start && !gameOver){
				bird.step();
				bird2.step();
				column1.step();
				column2.step();
				backgrounds1.step();
				backgrounds2.step();
				//检查是否通过柱子了
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
		//抗锯齿代码
//		Graphics2D g2 = (Graphics2D)g;
//		RenderingHints qualityHints = new RenderingHints(
//				RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//		qualityHints.put(RenderingHints.KEY_RENDERING,
//				RenderingHints.VALUE_RENDER_QUALITY);
//		g2.setRenderingHints(qualityHints);
		//绘制背景
//		g.drawImage(background, 0, 0, null);//不动
		backgrounds1.paint(g);
		backgrounds2.paint(g);
		//绘制柱子
		column1.paint(g);
		column2.paint(g); 
		//绘制地面
		ground.paint(g);
		//绘制分数
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 45);
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(score+"", 30, 50);
		//绘制小鸟
		bird.paint(g);
		bird2.paint(g);
		//绘制结束状态
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
		JFrame frame = new JFrame("飞扬小鸟--变态版");
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
	//背景
	BufferedImage image;
	
	//顶点坐标
	int x;
	int y;
	
	//大小
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
	//以柱子的中间作为柱子的位置
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
	/** 鸟飞行位置, 这个位置是鸟的中心位置 */
	int x;	int y;
	/** 飞行角度 */
	double angle;
	/** 动画帧 */
	BufferedImage[] images;
	/** 当前图片 */
	BufferedImage image;
	/** 当前图片序号 */
	int index = 0;
	/** 重力加速度 */
	final double g; 
	/** 时间间隔秒 */
	final double t;
	/** 初始上抛速度 */
	final double v0;
	/** 当前上抛速度 */
	double speed;
	/** 移动距离 */
	double s;
	/** 鸟的范围, 鸟的范围是一个正方形区域, 中心点是x,y */
	int size = 40;
	
	public Bird(int n) throws Exception {
		this.g = 4; //重力加速度
		this.t = 0.25; //每次计算的间隔时间
		this.v0 = 20; //初始上抛速度
		x = n; //鸟的初始位置
		y = 275; //鸟的初始位置
		//加载动画帧
		images = new BufferedImage[8];
		for(int i=0; i<8; i++){
			images[i] = ImageIO.read(getClass().getResource(i+".png"));
		}
		image = images[0];
	}
	
	/** 飞行一步  
	 * 竖直上抛位移计算
	 *  (1) 上抛速度计算 V=Vo-gt  
		(2) 上抛距离计算 S=Vot-1/2gt^2
	 * */
	public void step(){
		//V0 是本次
		double v0 = speed;
		//计算垂直上抛运动, 经过时间t以后的速度, 
		double v = v0 - g*t;
		//作为下次计算的初始速度
		speed = v;
		//计算垂直上抛运动的运行距离
		s = v0*t - 0.5 * g * t * t;
		//将计算的距离 换算为 y坐标的变化
		y = y - (int)s;
		//计算小鸟的仰角, 
		angle = -Math.atan(s/8);
		
	}
	public void fly(){
		//更换小鸟的动画帧图片, 其中/4 是为了调整动画更新的频率
		index++;
		image = images[(index/8)%images.length];
	}
	/** 小鸟向上飞扬 */
	public void flappy(){
		//每次小鸟上抛跳跃, 就是将小鸟在当前点重新以初始速度 V0 向上抛
		speed = v0;
	}
	//绘制时并发执行的, 要同步避免 旋转坐标系对其他绘制方法的影响
	public synchronized void paint(Graphics g){
		//g.drawRect(x-size/2, y-size/2, size, size);
		Graphics2D g2 = (Graphics2D)g;
		//旋转绘图坐标系, 绘制
		g2.rotate(angle, this.x, this.y);
		//以x,y 为中心绘制图片
		int x = this.x-image.getWidth()/2;
		int y = this.y-image.getHeight()/2;
		g.drawImage(image, x, y, null);
		//旋转回来 
		g2.rotate(-angle, this.x, this.y);
	}

	@Override
	public String toString() {
		return "Bird [x=" + x + ", y=" + y + ", g=" + g + ", t=" + t + ", v0="
				+ v0 + ", speed=" + speed + ",s="+s+"]";
	}
	/** 判断鸟是否通过柱子 */
	public boolean pass(Column col1, Column col2) {
		return col1.x==x || col2.x==x;
	}
	/** 判断鸟是否与柱子和地发生碰撞 */
	public boolean hit(Column column1, Column column2, Ground ground) {
		//碰到地面
		if(y-size/2 >= ground.y){
			return true;
		}
		//碰到柱子
		return hit(column1) || hit(column2);
	}
	/** 检查当前鸟是否碰到柱子 */
	public boolean hit(Column col){
		//如果鸟碰到柱子: 鸟的中心点x坐标在 柱子宽度 + 鸟的一半
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
