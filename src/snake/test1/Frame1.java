package snake.test1;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame1 extends JFrame {
	
	Random random = new Random();
	
	Canvas1_1 canvas;
	
	public static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	Direction direction = Direction.LEFT;
	ArrayList<Integer> posX = new ArrayList<>();
	ArrayList<Integer> posY = new ArrayList<>();
	int len = 2;
	
	int appleX;
	int appleY;
	
	int sizeX = 40;
	int sizeY = 40;
	int blockSize = 20;
	Color appleColor = Color.red;
	Color backgroundColor = Color.black;
	Color forgroundColor = Color.white;
	Color invColor = Color.gray;
	Color invAppleColor = new Color(127, 0, 0);
	int sightField = 18;
	float ticksPerSecond = 40f;
	
	public Frame1() {
		initialize();
		canvas = new Canvas1_1(this);
		add(canvas);
		canvas.setBackground(invColor);
		canvas.setVisible(true);
		canvas.setSize(getWidth(), getHeight());
	}
	
	private void initialize() {
		setUndecorated(true);
		setBounds(200, 200, sizeX*blockSize, sizeY*blockSize);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		getGraphics();
	}
	
	private int[][] getSight() {
		int[][] inputs = new int[sightField*2+1][sightField*2+1];
		for (int k1 = 0; k1 < inputs.length; k1++) {
			for (int k2 = 0; k2 < inputs.length; k2++) {
				if ((posX.get(0)+k1-sightField)<0||(posX.get(0)+k1-sightField)>sizeX) {
					inputs[k1][k2] = -len-3;
					continue;
				}
				if ((posY.get(0)+k2-sightField)<0||(posY.get(0)+k2-sightField)>sizeY) {
					inputs[k1][k2] = -len-3;
					continue;
				}
				inputs[k1][k2] = 0;
			}
		}
		for (int k = 0; k < posX.size(); k++) {
			if (Math.abs(posX.get(k)-posX.get(0))>sightField) {
				continue;
			}
			if (Math.abs(posY.get(k)-posY.get(0))>sightField) {
				continue;
			}
			inputs[posX.get(k)-posX.get(0)+sightField][posY.get(k)-posY.get(0)+sightField] = -len+k/2-2;
		}
		if (Math.abs(appleX-posX.get(0))<=sightField) {
			if (Math.abs(appleY-posY.get(0))<=sightField) {
				inputs[appleX-posX.get(0)+sightField][appleY-posY.get(0)+sightField] += 2;
			}
		}
		return inputs;
	}
	
	public void runGame() {
		int score = 0;
		KI ki = new KI(this);
		ki.depth = sightField+sightField*2/3;
		Font scoreFont = new Font("Times New Roman", Font.PLAIN, 30);
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
				/*if (e.getKeyCode() == KeyEvent.VK_UP) {
					direction = Direction.UP;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					direction = Direction.DOWN;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					direction = Direction.LEFT;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					direction = Direction.RIGHT;
				}*/
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
				default:
					break;
				}
			}
		});
		while (run) {
			ki.resetQuality();
			if ((posX.get(0))>sizeX) {
				System.out.print("");
			}
			int[][] inputs = getSight();
			ki.setInputs(inputs);
			direction = ki.getBestAction();
			if (Math.random()<ki.randomness/2) {
				if (direction==Direction.UP||direction==Direction.DOWN) {
					if (Math.random()<0.5) {
						direction = Direction.LEFT;
					}
					else {
						direction = Direction.RIGHT;
					}
				}
				else {
					if (Math.random()<0.5) {
						direction = Direction.UP;
					}
					else {
						direction = Direction.DOWN;
					}
				}
			}
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
			/*if (direction==Direction.UP) {
				posY.add(0, posY.get(0)-1);
				posX.add(0, posX.get(0));
			}
			if (direction==Direction.DOWN) {
				posY.add(0, posY.get(0)+1);
				posX.add(0, posX.get(0));
			}
			if (direction==Direction.LEFT) {
				posX.add(0, posX.get(0)-1);
				posY.add(0, posY.get(0));
			}
			if (direction==Direction.RIGHT) {
				posX.add(0, posX.get(0)+1);
				posY.add(0, posY.get(0));
			}*/
			if (posX.get(0)<0||posX.get(0)>sizeX+1) {
				run = false;
				break;
			}
			if (posY.get(0)<0||posY.get(0)>sizeY+1) {
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
			g.setColor(backgroundColor);
			g.fillRect((posX.get(0)-sightField)*blockSize, (posY.get(0)-sightField)*blockSize, (sightField*2+1)*blockSize, (sightField*2+1)*blockSize);
			if (Math.abs(appleX-posX.get(0))<sightField&&Math.abs(appleY-posY.get(0))<sightField) {
				g.setColor(appleColor);
			}
			else {
				g.setColor(invAppleColor);
			}
			g.fillRect(appleX*blockSize, appleY*blockSize, blockSize, blockSize);
			g.setColor(forgroundColor);
			for (int x = 0; x < sizeX; x++) {
				g.drawLine(x*blockSize, 0, x*blockSize, getHeight());
			}
			for (int y = 0; y < sizeX; y++) {
				g.drawLine(0, y*blockSize, getWidth(), y*blockSize);
			}
			for (int k2 = 0; k2 < posX.size(); k2++) {
				g.fillRect(posX.get(k2)*blockSize, posY.get(k2)*blockSize, blockSize, blockSize);
			}
			g.setColor(Color.green);
			g.drawString("Score: "+score, 20, 40);
			try {
				Thread.sleep((int)(1000/ticksPerSecond));
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
