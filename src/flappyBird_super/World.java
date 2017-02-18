package flappyBird_super;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * 鸟飞行的世界
 * @author Robin
 */
public class World extends JPanel {
	
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

	public World() throws IOException {
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
					score=score+100;
				}
				if(bird.hit(column1, column2, ground)){
					start = false;
					gameOver = true;
				}
				if(bird2.pass(column1, column2)){
					score=score+100;
				}
				if(bird2.hit(column1, column2, ground)){
					start = false;
					gameOver = true;
				}
			}
			if(! gameOver) bird.fly();
			if(! gameOver) bird2.fly();
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
		World world = new World();
		frame.add(world);
		frame.setSize(432, 644+30);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		world.action();
	}
}
