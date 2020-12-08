package snake.test5;

import javax.swing.*;

import org.neat.activationFunction.ActivationFunction;
import org.neat.calculate.Calculator;
import org.neat.gene.ConnectionGene;
import org.neat.gene.ConnectionList;
import org.neat.gene.NodeGene;
import org.neat.genome.Genome;
import org.neat.neat.Neat;
import org.neat.species.Species;

import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	int sightField = 6;
	float ticksPerSecond = 100f;
	JFrame netFrame1;
	NetCanvas netCanvas1;
	JFrame netFrame2;
	NetCanvas netCanvas2;
	Map<String, Image> activationIcons;
	public int sizeNode = 40;
	public int generation = 0;
	boolean show = true;
	boolean jump = false;
	boolean showCurr = false;
	
	
	ArrayList<Snake2> snakes = new ArrayList<>();

	boolean showone = false;
	
	public Frame1() {
		initialize();
		initIcons();
		canvas = new Canvas1_1(this);
		add(canvas);
		canvas.setBackground(backgroundColor);
		canvas.setVisible(true);
		canvas.setSize(getWidth(), getHeight());
	}
	private void initIcons() {
		activationIcons = new HashMap<>();
		addToMap("Abs");
		addToMap("ArcTan");
		addToMap("Clamped");
		addToMap("Cube");
		addToMap("ELU");
		addToMap("Exp");
		addToMap("Gauss");
		addToMap("Hat");
		addToMap("Identity");
		addToMap("Inverted");
		addToMap("LeakyReLU");
		addToMap("Log");
		addToMap("ReLU");
		addToMap("Sigmoid");
		addToMap("SiLU");
		addToMap("Sin");
		addToMap("Softplus");
		addToMap("Square");
		addToMap("Step");
		addToMap("Tanh");
	}
	private void addToMap(String name) {
		activationIcons.put("Activation"+name, (new ImageIcon("resources/Activations/"+name+".png")).getImage().getScaledInstance(sizeNode, sizeNode, Image.SCALE_SMOOTH));
	}
	
	
	private void initialize() {
		setUndecorated(true);
		setBounds(200, 50, sizeX*blockSize, sizeY*blockSize);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		getGraphics();
		
		netFrame1 = new JFrame("Last best Genome");
		netFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//netFrame.setBounds(width+50, 0, 1100, 1000);
		netFrame1.setBounds(0, 0, 1920, 1080);
		netFrame1.setUndecorated(true);
		//netFrame.setAlwaysOnTop(true);
		netFrame1.setVisible(true);
		netCanvas1 = new NetCanvas(this);
		netFrame1.add(netCanvas1);
		
		netFrame2 = new JFrame("Current best Genome");
		netFrame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//netFrame.setBounds(width+50, 0, 1100, 1000);
		netFrame2.setBounds(0, 0, 1920, 1080);
		netFrame2.setUndecorated(true);
		//netFrame.setAlwaysOnTop(true);
		netFrame2.setVisible(true);
		netCanvas2 = new NetCanvas(this);
		netFrame2.add(netCanvas2);
		netFrame2.setVisible(false);
	}
	
	
	public void runGame() {
		//neat = new Neat(4*(sightField+1)*sightField+1, 4, true);
		neat = new ONeat((sightField*2+1)*(sightField*2+1), 4, true);
		
		boolean run = true;
		KeyListener keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
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
				if (e.getKeyCode()==KeyEvent.VK_P) {
					canvas.repaint();
					show = !show;
				}
				if (e.getKeyCode()==KeyEvent.VK_SPACE) {
					showone = !showone;
				}
				if (e.getKeyCode()==KeyEvent.VK_X) {
					jump = true;
				}
				if (e.getKeyCode()==KeyEvent.VK_G) {
					showCurr = !showCurr;
				}
			}
		};
		canvas.addKeyListener(keyListener);
		netCanvas1.addKeyListener(keyListener);
		netFrame1.addKeyListener(keyListener);
		netCanvas2.addKeyListener(keyListener);
		netFrame2.addKeyListener(keyListener);
		addKeyListener(keyListener);
		
		float mul = 0.2f;
		
		neat.config.initGenome.meshPercentage = 0.01f;
		
		neat.config.crossover.takeSecondNode = 0.3f;
		neat.config.crossover.takeSecondConnection = 0.3f;
		neat.config.crossover.takeSecondActivation = 0.3f;
		//neat.config.crossover.takeSecondAggregation = 0.3f;
		//neat.config.crossover.thinConnections = true;
		neat.config.crossover.thinProbOrPerc = 0.9f;
		neat.config.crossover.thinViaPercentage = false;
		
		neat.config.species.stagnationDuration = 10;
		
		neat.config.mutate.addExistingNode = true;
		neat.config.mutate.nodeActivation = true;
		neat.config.mutate.genomeActivation = true;
		neat.config.mutate.averageAddNodesPerMutate = 0.6f*mul;
		neat.config.mutate.averageAddExistingNodesPerMutate = 0.075f*mul;
		neat.config.mutate.averageAddConnectionsPerMutate = 0.04f*mul;
		neat.config.mutate.averageNewNodesActivationPerMutate = 0.005f*mul;
		neat.config.mutate.averageToggleNodesPerMutate = 0.0375f*mul;
		neat.config.mutate.averageToggleConnectionsPerMutate = 0.04f*mul;
		neat.config.mutate.averageCombineInConnectionsPerMutate = 0.09f*mul;
		neat.config.mutate.averageCombineOutConnectionsPerMutate = 0.09f*mul;
		neat.config.mutate.genomeActivationProb = 0.005f*mul;
		
		neat.activationFunctions.add(ActivationFunction.Step);
		neat.activationFunctions.add(ActivationFunction.Clamped);
		neat.activationFunctions.add(ActivationFunction.Gauss);
		neat.activationFunctions.add(ActivationFunction.Hat);
		//neat.activationFunctions.add(ActivationFunction.Sin);
		//neat.activationFunctions.add(ActivationFunction.Tanh);
		//neat.activationFunctions.add(ActivationFunction.ReLU);
		//neat.activationFunctions.add(ActivationFunction.SiLU);
		
		neat.initialize();
		
		
		neat.createGenomes(250);
		neat.mutate();
		neat.breedrand(1250);
		neat.mutate();
		neat.breedrand(7500);
		neat.mutate();
		//System.out.println("Init Specicate");
		
		/*neat.initSpecicateRand();
		System.out.println("Specicate");
		neat.specicateFast(0.25f);
		neat.realignSpeciesRepresentative();
		System.out.println("Done");*/
		
		Font scoreFont = new Font("Times New Roman", Font.PLAIN, 30);
		canvas.setFont(scoreFont);
		//int index = 800;
		long seed = random.nextLong();
		while (true) {
			//seed = random.nextLong();
			int index2 = 0;
			run = true;
			Graphics g = canvas.getGraphics();
			List<Calculator> calculators = neat.createCalculators();
			for (Calculator calculator : calculators) {
				Snake2 snake = new Snake2(calculator, this, random, seed);
				snake.init();
				snakes.add(snake);
			}
			while (run) {
				run  = false;
				for (Snake2 snake : snakes) {
					if (snake.alive) {
						run = true;
					}
				}
				if (!run) {
					break;
				}
				for (Snake2 snake : snakes) {
					if (!snake.alive) {
						continue;
					}
					snake.run();
				}
				if ((showone&show)|showCurr) {
					neat.sortGenomes();
				}
				int index1 = 10000;
				Snake2 a = snakes.get(0);
				if ((showone&show)|showCurr) {
					snakes.sort(new Comparator<Snake2>() {
						@Override
						public int compare(Snake2 o1, Snake2 o2) {
							return -Float.compare(o1.calculator.genome.fitness, o2.calculator.genome.fitness);
						}
					});
					a = snakes.get(0);
					for (Snake2 snake : snakes) {
						int i = neat.genomes.getList().indexOf(snake.calculator.genome);
						if (!snake.alive||i>index1) {
							continue;
						}
						//index = i;
						a = snake;
					}
					for (int k = 0; k < snakes.size(); k++) {
						if (snakes.get(k).alive) {
							a = snakes.get(k);
							break;
						}
					}
				}
				if (showCurr&&Math.floorMod(index2, 50)==0) {
					drawNet(a.calculator.genome, netCanvas2, netFrame2);
				}
				if (show) {
					g.clearRect(0, 0, getWidth(), getHeight());
					g.setColor(forgroundColor);
					for (int x = 0; x < sizeX; x++) {
						g.drawLine(x*blockSize, 0, x*blockSize, getHeight());
					}
					for (int y = 0; y < sizeX; y++) {
						g.drawLine(0, y*blockSize, getWidth(), y*blockSize);
					}
					if (showone) {
						g.setColor(appleColor);
						g.fillRect(a.appleX*blockSize, a.appleY*blockSize, blockSize, blockSize);
						g.setColor(forgroundColor);
						for (int k2 = 0; k2 < a.posX.size(); k2++) {
							g.fillRect(a.posX.get(k2)*blockSize, a.posY.get(k2)*blockSize, blockSize, blockSize);
						}
					}
					else {
						g.setColor(appleColor);
						for (Snake2 snake : snakes) {
							if (!snake.alive) {
								continue;
							}
							g.fillRect(snake.appleX*blockSize, snake.appleY*blockSize, blockSize, blockSize);
						}
						g.setColor(forgroundColor);
						for (Snake2 snake : snakes) {
							if (!snake.alive) {
								continue;
							}
							for (int k2 = 0; k2 < snake.posX.size(); k2++) {
								g.fillRect(snake.posX.get(k2)*blockSize, snake.posY.get(k2)*blockSize, blockSize, blockSize);
							}
						}
					}
				}
				try {
					if (show) {
						Thread.sleep((int)(1000/ticksPerSecond));
					}
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!run) {
					break;
				}
				/*if (index2==0) {
					run = false;
				}*/
				index2++;
				if (jump) {
					break;
				}
			}
			
			canvas.repaint();
			
			jump = false;
			
			neat.sortGenomes();
			//neat.calcSpeciesFitness();
			//neat.sortSpecies();
			//System.out.println(neat.speciesList.getList().get(0).fitness);
			System.out.println(neat.genomes.getList().get(0).fitness);
			drawNet(neat.genomes.getList().get(0), netCanvas1, netFrame1);
			//neat.stagnate(0.2f, 0.2f);
			//neat.addSpeciesFitnessToHist();
			snakes.clear();
			
			//neat.cutGenomesInSpecies(0.75f);
			//neat.cutGenomesInSpecies(0.8f);
			//neat.breedSpeciesUpTo(5000);
			neat.cutToBestGenomes(250);
			neat.breed(7750);
			neat.mutate();
			//neat.clearGenomesOfSpecies();
			//neat.specicateFast(0.1f);
			//neat.removeEmptySpecies();
			generation++;
			//index+=50;
			for (Genome genome : neat.genomes.getList()) {
				for (int k = 0; k < 10; k++) {
					NodeGene node = genome.nodes.getRandom();
					if (node.x<=0) {
						continue;
					}
					if (node.x>=1) {
						continue;
					}
					genome.nodes.remove(node);
				}
				ConnectionList remove = new ConnectionList(neat);
				for (ConnectionGene connection : genome.connections.getList()) {
					if (!genome.nodes.contains(connection.fromGene)) {
						remove.add(connection);
						continue;
					}
					if (!genome.nodes.contains(connection.toGene)) {
						remove.add(connection);
						continue;
					}
				}
				for (ConnectionGene connection : remove.getList()) {
					genome.connections.remove(connection);
				}
			}
		}
	}
	private void drawNet(Genome genome, NetCanvas netCanvas, JFrame netFrame) {
		//netCanvas.repaint();
		int dist = 200;
		//System.out.print(ActivationArcTan.class.getSimpleName());
		Graphics2D g = (Graphics2D) netCanvas.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, netCanvas.getWidth(), netCanvas.getHeight());
		if (genome == null) {
			return;
		}
		Map<Float, List<NodeGene>> nodesByX = genome.nodes.getNodesByX();
		List<ConnectionGene> connections = genome.connections.getList();
		g.setStroke(new BasicStroke(4));
		/*for (Entry<Float, List<NodeGene>> layerEntry : nodesByX.entrySet()) {
			for (NodeGene node : layerEntry.getValue()) {
				if (node.enabled) {
					g.setColor(Color.black);
				}
				else {
					g.setColor(Color.gray);
				}
				Image icon = activationIcons.get(node.activation.getClass().getSimpleName());
				//icon = icon.getScaledInstance(sizeNode, sizeNode, Image.SCALE_SMOOTH);
				int posX = (int) ((float)netCanvas.getWidth()*((float)layerEntry.getKey()*0.9+0.05))+0;
				int posY = (int) ((float)netCanvas.getHeight()*((float)layerEntry.getValue().indexOf(node)/(float)layerEntry.getValue().size()*0.9+0.05))+0;
				//g.fillOval(posX-10, posY-10, 20, 20);
				g.drawImage(icon, posX-sizeNode/2, (int)(posY-(sizeNode/2-dist*Math.pow(layerEntry.getKey(), 2))), null);
				g.drawImage(icon, posX-sizeNode/2, (int)(posY-(sizeNode/2-dist*Math.pow(layerEntry.getKey(), 2))), null);
				g.drawOval(posX-sizeNode/2, (int)(posY-sizeNode/2+dist*Math.pow(layerEntry.getKey(), 2)), sizeNode, sizeNode);
				//System.out.println(node);
			}
		}*/
		for (ConnectionGene connectionGene : connections) {
			if (connectionGene.weight>0) {
				g.setColor(Color.blue);
				if (!connectionGene.enabled) {
					g.setColor(new Color(0, 255, 255));
				}
			}
			else {
				g.setColor(Color.red);
				if (!connectionGene.enabled) {
					g.setColor(new Color(255, 255, 0));
				}
			}
			g.setStroke(new BasicStroke(2*(float) Math.ceil(10*Math.abs(connectionGene.weight*4))/10));
			if (connectionGene.weight==0) {
				if (connectionGene.enabled) {
					g.setColor(Color.black);
				}
				else {
					g.setColor(Color.gray);
				}
				g.setStroke(new BasicStroke(1));
			}
			NodeGene from = connectionGene.fromGene;
			NodeGene to = connectionGene.toGene;
			List<NodeGene> fromLayer = nodesByX.get(from.x);
			List<NodeGene> toLayer = nodesByX.get(to.x);
			int posX1 = (int) ((float)netCanvas.getWidth()*(from.x*0.9+0.05))+0;
			int posX2 = (int) ((float)netCanvas.getWidth()*(to.x*0.9+0.05))+0;
			int posY1 = (int) ((float)netCanvas.getHeight()*((float)fromLayer.indexOf(from)/(float)fromLayer.size()*0.9+0.05))+0;
			int posY2 = (int) ((float)netCanvas.getHeight()*((float)toLayer.indexOf(to)/(float)toLayer.size()*0.9+0.05))+0;
			g.drawLine(posX1+sizeNode/2, (int)(posY1+dist*Math.pow(from.x, 2)), posX2-sizeNode/2, (int)(posY2+dist*Math.pow(to.x, 2)));
			//System.out.println(connectionGene);
		}
		g.setStroke(new BasicStroke(4));
		for (Entry<Float, List<NodeGene>> layerEntry : nodesByX.entrySet()) {
			if (layerEntry.getKey()==0) {
				continue;
			}
			for (NodeGene node : layerEntry.getValue()) {
				if (node.enabled) {
					g.setColor(Color.black);
				}
				else {
					g.setColor(Color.gray);
				}
				Image icon = activationIcons.get(node.activation.getClass().getSimpleName());
				//icon = icon.getScaledInstance(sizeNode, sizeNode, Image.SCALE_SMOOTH);
				int posX = (int) ((float)netCanvas.getWidth()*((float)layerEntry.getKey()*0.9+0.05))+0;
				int posY = (int) ((float)netCanvas.getHeight()*((float)layerEntry.getValue().indexOf(node)/(float)layerEntry.getValue().size()*0.9+0.05))+0;
				//g.fillOval(posX-10, posY-10, 20, 20);
				g.drawImage(icon, posX-sizeNode/2, (int)(posY-(sizeNode/2-dist*Math.pow(layerEntry.getKey(), 2))), null);
				g.drawImage(icon, posX-sizeNode/2, (int)(posY-(sizeNode/2-dist*Math.pow(layerEntry.getKey(), 2))), null);
				g.drawOval(posX-sizeNode/2, (int)(posY-sizeNode/2+dist*Math.pow(layerEntry.getKey(), 2)), sizeNode, sizeNode);
				//System.out.println(node);
			}
		}
		g.setColor(Color.red);
		Font font = new Font("Times New Roman", Font.BOLD, 30);
		g.setFont(font);
		g.drawString("Generation: "+generation, 1920-200, 70);
	}
	
	public static class ONeat extends Neat {
		
		public ONeat(int inputCount, int outputCount, boolean addBias) {
			super(inputCount, outputCount, addBias);
		}
		
		@Override
		public void specicateFast(float percentage) {
			int index = 0;
			for (org.neat.libraries.Entry<Integer, Genome> genome : genomes) {
				if (Math.floorMod(index+1, 200)==0) {
					System.out.print(index+1+" ");
				}
				index++;
				int id = speciesList.getRandom().id;
				float d = speciesList.get(id).distance(genome.getValue());
				int Count = (int)((float)speciesList.size()*Gaussian()*percentage);
				Count = Math.min(Count, speciesList.size());
				for (int k = 0; k < Math.max(1, Count); k++) {
					Species species = speciesList.getRandom();
					float d0 = species.distance(genome.getValue());
					if (d0<d) {
						d = d0;
						id = species.id;
					}
				}
				if (d<config.species.compatibilityThreshold) {
					speciesList.get(id).add(genome.getValue());
				}
				else {
					Species species0 = new Species(this, speciesList.nextId(), genome.getValue());
					speciesList.add(species0);
					species0.add(genome.getValue());
				}
			}
			System.out.println();
		}
	}
	private static double nextGaussian() {
		double v1, v2, s;
		do {
			v1 = 2 * Math.random() -1;
			v2 = 2 * Math.random() -1;
			s = Math.pow(v1, 2) + Math.pow(v2, 2);
		} while (s >= 1 || s == 0);
		double multipier = Math.sqrt(-2 * Math.log(s)/s);
		return v1 * multipier;
	}
	private static double Gaussian() {
		double rand = nextGaussian();
		return Math.exp(rand);
	}
	
}
