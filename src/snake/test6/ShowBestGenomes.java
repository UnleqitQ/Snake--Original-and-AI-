package snake.test6;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;

import org.neat.calculate.Calculator;

public class ShowBestGenomes implements Runnable {
	
	JFrame frame = new JFrame(); 
	Frame1 frame1;
	Canvas canvas;
	int generation = 0;
	boolean restart = false;
	float ticksPerSecond = 100f;
	
	public ShowBestGenomes(Frame1 frame1) {
		this.frame1 = frame1;
		frame.setBounds(200, 50, frame1.getWidth(), frame1.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		canvas = new Canvas();
		canvas.setSize(frame.getSize());
		canvas.setBackground(frame1.backgroundColor);
		frame.add(canvas);
		frame.setVisible(true);
		canvas.setVisible(true);
		KeyListener keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
					generation++;
					restart = true;
				}
				if (e.getKeyCode()==KeyEvent.VK_LEFT) {
					generation--;
					restart = true;
				}
				if (e.getKeyCode()==KeyEvent.VK_PLUS) {
					if (ticksPerSecond>1000) {
						return;
					}
					ticksPerSecond *= 2;
				}
				if (e.getKeyCode()==KeyEvent.VK_MINUS) {
					if (ticksPerSecond<2) {
						return;
					}
					ticksPerSecond /= 2;
				}
			}
		};
		frame.addKeyListener(keyListener);
		canvas.addKeyListener(keyListener);
	}
	
	@Override
	public void run() {
		while (frame1.bestGenomes.size()<1) {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		Graphics2D g = (Graphics2D) canvas.getGraphics();
		while (true) {
			if (generation<0) {
				generation = 0;
			}
			if (generation>=frame1.bestGenomes.size()) {
				generation = frame1.bestGenomes.size()-1;
			}
			Calculator calculator = new Calculator(frame1.bestGenomes.get(generation));
			calculator.initialize();
			Snake2 snake = new Snake2(calculator, frame1, new Random(), new Random().nextLong());
			snake.init();
			while (!restart) {
				try {
					Thread.sleep((int)(1000/ticksPerSecond));
				}
				catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				drawGenome(g, snake);
				snake.run();
				if (!snake.alive) {
					break;
				}
			}
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			restart = false;
		}
	}
	
	private void drawGenome(Graphics2D g, Snake2 snake) {
		g.clearRect(0, 0, frame1.getWidth(), frame1.getHeight());
		g.setColor(frame1.forgroundColor);
		for (int x = 0; x < frame1.sizeX; x++) {
			g.drawLine(x*frame1.blockSize, 0, x*frame1.blockSize, frame1.getHeight());
		}
		for (int y = 0; y < frame1.sizeX; y++) {
			g.drawLine(0, y*frame1.blockSize, frame1.getWidth(), y*frame1.blockSize);
		}
		g.setColor(frame1.appleColor);
		for (int k2 = 0; k2 < snake.appleX.size(); k2++) {
			g.fillRect(snake.appleX.get(k2)*frame1.blockSize, snake.appleY.get(k2)*frame1.blockSize, frame1.blockSize, frame1.blockSize);
		}
		g.setColor(frame1.forgroundColor);
		for (int k2 = 0; k2 < snake.posX.size(); k2++) {
			g.setColor(Color.green);
			if (snake.lifeLost) {
				g.setColor(Color.orange);
			}
			g.fillRect(snake.posX.get(k2)*frame1.blockSize, snake.posY.get(k2)*frame1.blockSize, frame1.blockSize, frame1.blockSize);
		}
		g.setColor(Color.blue);
		Font font = new Font("Times New Roman", Font.BOLD, 30);
		g.setFont(font);
		g.drawString("Generation: "+generation, frame.getWidth()-200, 70);
	}
	
	
}
