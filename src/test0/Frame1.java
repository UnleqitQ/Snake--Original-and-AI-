package snake.test0;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Frame1 extends JFrame {
	
	Random random = new Random();
	
	Canvas1_1 canvas;
	
	public static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	Direction direction = Direction.RIGHT;
	ArrayList<Integer> posX = new ArrayList<>();
	ArrayList<Integer> posY = new ArrayList<>();
	int len = 3;
	
	int appleX;
	int appleY;
	
	int sizeX = 40;
	int sizeY = 30;
	int blockSizeX = 30;
	int blockSizeY = 30;
	Color appleColor = Color.red;
	Color backgroundColor = Color.black;
	Color snakeColor = Color.green;
	Color labelColor = new Color(0, 0, 195);
	Color lineColor = Color.white;
	float ticksPerSecond = 10f;
	
	public Frame1() {
		initialize();
		canvas = new Canvas1_1(this);
		add(canvas);
		canvas.setBackground(backgroundColor);
		canvas.setVisible(true);
		canvas.setSize(getWidth(), getHeight());
	}
	
	private void initialize() {
		setUndecorated(true);
		setBounds(0, 0, sizeX*blockSizeX, sizeY*blockSizeY);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	
	public void runGame() {
		int score = 0;
		Font scoreFont = new Font("Times New Roman", Font.BOLD, 30);
		canvas.setFont(scoreFont);
		boolean run = true;
		Graphics g = canvas.getGraphics();
		posX.add((int)(sizeX/2));
		posY.add((int)(sizeY/2));
		appleX = random.nextInt(sizeX);
		appleY = random.nextInt(sizeY);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					if (direction==Direction.DOWN) {
						break;
					}
					direction = Direction.UP;
					break;
				case KeyEvent.VK_DOWN:
					if (direction==Direction.UP) {
						break;
					}
					direction = Direction.DOWN;
					break;
				case KeyEvent.VK_LEFT:
					if (direction==Direction.RIGHT) {
						break;
					}
					direction = Direction.LEFT;
					break;
				case KeyEvent.VK_RIGHT:
					if (direction==Direction.LEFT) {
						break;
					}
					direction = Direction.RIGHT;
					break;
				case KeyEvent.VK_PLUS:
					if (ticksPerSecond<100) {
						ticksPerSecond*=2;
					}
					break;
				case KeyEvent.VK_MINUS:
					if (ticksPerSecond>0.01) {
						ticksPerSecond/=2;
					}
					break;
				default:
					break;
				}
			}
		});
		while (run) {
			switch (direction) {
			case UP:
				posX.add(0, posX.get(0));
				posY.add(0, posY.get(0)-1);
				break;
			case DOWN:
				posX.add(0, posX.get(0));
				posY.add(0, posY.get(0)+1);
				break;
			case LEFT:
				posX.add(0, posX.get(0)-1);
				posY.add(0, posY.get(0));
				break;
			case RIGHT:
				posX.add(0, posX.get(0)+1);
				posY.add(0, posY.get(0));
				break;
			default:
				break;
			}
			if (posX.get(0)<0||posX.get(0)>=sizeX) {
				run = false;
				break;
			}
			if (posY.get(0)<0||posY.get(0)>=sizeY) {
				run = false;
				break;
			}
			if (posX.size()>len) {
				posX.remove(len);
			}
			if (posY.size()>len) {
				posY.remove(len);
			}
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(0)==posX.get(k2)) {
					if (posY.get(0)==posY.get(k2)) {
						run = false;
						break;
					}
				}
			}
			g.setColor(appleColor);
			g.fillOval(appleX*blockSizeX, appleY*blockSizeY, blockSizeX, blockSizeY);
			g.setColor(snakeColor);
			for (int k2 = 0; k2 < posX.size()-1; k2++) {
				g.fillRect((posX.get(k2)*blockSizeX+posX.get(k2+1)*blockSizeX)/2, (posY.get(k2)*blockSizeY+posY.get(k2+1)*blockSizeY)/2, blockSizeX, blockSizeY);
			}
			for (int k2 = 0; k2 < posX.size(); k2++) {
				g.fillOval(posX.get(k2)*blockSizeX, posY.get(k2)*blockSizeY, blockSizeX, blockSizeY);
			}
			g.setColor(lineColor);
			for (int x = 0; x < sizeX; x++) {
				g.drawLine(x*blockSizeX, 0, x*blockSizeX, getHeight());
			}
			for (int y = 0; y < sizeX; y++) {
				g.drawLine(0, y*blockSizeY, getWidth(), y*blockSizeY);
			}
			g.setColor(backgroundColor);
			g.drawLine(blockSizeX, 0, blockSizeX, blockSizeY*2);
			g.drawLine(blockSizeX*2, 0, blockSizeX*2, blockSizeY*2);
			g.drawLine(blockSizeX*3, 0, blockSizeX*3, blockSizeY*2);
			g.drawLine(blockSizeX*4, 0, blockSizeX*4, blockSizeY*2);
			g.drawLine(0, blockSizeY, blockSizeX*5, blockSizeY);
			g.setColor(labelColor);
			g.drawString("Score: "+score, 20, 40);
			try {
				for (int k = 0; k < 100/ticksPerSecond; k++) {
					Thread.sleep(10);
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!run) {
				break;
			}
			g.clearRect(0, 0, getWidth(), getHeight());
			if (posX.get(0) == appleX && posY.get(0) == appleY) {
				len++;
				score++;
				appleX = random.nextInt(sizeX);
				appleY = random.nextInt(sizeY);
			}
		}
		System.out.println(score);
		setVisible(false);
	}
	
}
