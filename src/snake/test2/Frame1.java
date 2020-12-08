package snake.test2;

import javax.swing.*;

import org.neat.calculate.Calculator;
import org.neat.neat.Neat;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame1 extends JFrame {
	
	Neat neat;
	
	Random random = new Random();
	
	Canvas1_1 canvas;
	
	
	int sizeX = 40;
	int sizeY = 40;
	int blockSize = 20;
	Color appleColor = Color.red;
	Color backgroundColor = Color.black;
	Color forgroundColor = Color.white;
	Color invColor = Color.gray;
	Color invAppleColor = new Color(127, 0, 0);
	int sightField = 18;
	float ticksPerSecond = 30f;
	
	ArrayList<Snake> snakes = new ArrayList<>();
	
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
		setBounds(200, 200, sizeX*blockSize, sizeY*blockSize);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		getGraphics();
	}
	
	
	public void runGame() {
		neat = new Neat(4*(sightField+1)*sightField+1, 4, true);
		neat.initialize();
		neat.createGenomes(50);
		neat.mutate();
		neat.breedrand(200);
		neat.mutate();
		//neat.initSpecicate();
		Font scoreFont = new Font("Times New Roman", Font.PLAIN, 30);
		canvas.setFont(scoreFont);
		int index = 45;
		while (true) {
			int index2 = index;
			boolean run = true;
			Graphics g = canvas.getGraphics();
			List<Calculator> calculators = neat.createCalculators();
			for (Calculator calculator : calculators) {
				Snake snake = new Snake(calculator, this, random);
				snake.init();
				snakes.add(snake);
			}
			while (run) {
				run  = false;
				for (Snake snake : snakes) {
					if (snake.alive) {
						run = true;
					}
				}
				if (!run) {
					break;
				}
				for (Snake snake : snakes) {
					if (!snake.alive) {
						continue;
					}
					snake.run();
				}
				g.setColor(appleColor);
				for (Snake snake : snakes) {
					if (!snake.alive) {
						continue;
					}
					g.fillRect(snake.appleX*blockSize, snake.appleY*blockSize, blockSize, blockSize);
				}
				g.setColor(forgroundColor);
				for (int x = 0; x < sizeX; x++) {
					g.drawLine(x*blockSize, 0, x*blockSize, getHeight());
				}
				for (int y = 0; y < sizeX; y++) {
					g.drawLine(0, y*blockSize, getWidth(), y*blockSize);
				}
				for (Snake snake : snakes) {
					if (!snake.alive) {
						continue;
					}
					for (int k2 = 0; k2 < snake.posX.size(); k2++) {
						g.fillRect(snake.posX.get(k2)*blockSize, snake.posY.get(k2)*blockSize, blockSize, blockSize);
					}
				}
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
				index2--;
				if (index2==0) {
					run = false;
				}
			}
			snakes.clear();
			/*neat.specicate();
			neat.realignSpeciesRepresentative();
			neat.specicate();
			neat.realignSpeciesRepresentative();
			neat.removeEmptySpecies();
			neat.cutGenomesInSpecies(0.75f);
			neat.breedSpeciesUpTo(50);*/
			neat.cutToBestGenomes(25);
			neat.breed(225);
			index+=5;
		}
	}
	
}
