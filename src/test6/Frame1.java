package snake.test6;

import javax.swing.*;

import org.neat.activationFunction.ActivationFunction;
import org.neat.calculate.Calculator;
import org.neat.gene.ConnectionGene;
import org.neat.gene.ConnectionList;
import org.neat.gene.NodeGene;
import org.neat.gene.NodeList;
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
	
	List<Genome> bestGenomes = new ArrayList<>();
	
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
	ShowBestGenomes showBestGenomes;
	
	ArrayList<Snake2> snakes = new ArrayList<>();

	boolean showone = false;
	
	public Frame1() {
		initialize();
		showBestGenomes = new ShowBestGenomes(this);
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
		neat = new NeatX(14+Snake2.memSize, 3+Snake2.memSize, true);
		
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
		
		neat.config.initGenome.meshPercentage = 0.03f;
		
		neat.config.crossover.takeSecondNode = 0.3f;
		neat.config.crossover.takeSecondConnection = 0.3f;
		neat.config.crossover.takeSecondActivation = 0.3f;
		//neat.config.crossover.takeSecondAggregation = 0.3f;
		//neat.config.crossover.thinConnections = true;
		neat.config.crossover.thinProbOrPerc = 0.2f;
		neat.config.crossover.thinViaPercentage = false;
		
		neat.config.species.stagnationDuration = 10;
		
		neat.config.mutate.addExistingNode = true;
		neat.config.mutate.nodeActivation = true;
		neat.config.mutate.genomeActivation = true;
		neat.config.mutate.averageAddNodesPerMutate = 0.6f*mul;
		neat.config.mutate.averageAddExistingNodesPerMutate = 0.075f*mul;
		neat.config.mutate.averageAddConnectionsPerMutate = 0.08f*mul;
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
		
		
		neat.createGenomes(100);
		neat.mutate();
		neat.breedrand(400);
		neat.mutate();
		neat.breedrand(2000);
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
		Thread thread = new Thread(showBestGenomes);
		//thread.setDaemon(true);
		thread.start();
		while (true) {
			seed = random.nextLong();
			int index2 = 0;
			boolean run = true;
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
						for (int k2 = 0; k2 < a.appleX.size(); k2++) {
							g.fillRect(a.appleX.get(k2)*blockSize, a.appleY.get(k2)*blockSize, blockSize, blockSize);
						}
						g.setColor(forgroundColor);
						for (int k2 = 0; k2 < a.posX.size(); k2++) {
							g.setColor(Color.green);
							if (a.lifeLost) {
								g.setColor(Color.orange);
							}
							g.fillRect(a.posX.get(k2)*blockSize, a.posY.get(k2)*blockSize, blockSize, blockSize);
						}
					}
					else {
						g.setColor(appleColor);
						for (Snake2 snake : snakes) {
							if (!snake.alive) {
								continue;
							}
							for (int k2 = 0; k2 < snake.appleX.size(); k2++) {
								g.fillRect(snake.appleX.get(k2)*blockSize, snake.appleY.get(k2)*blockSize, blockSize, blockSize);
							}
						}
						g.setColor(forgroundColor);
						for (Snake2 snake : snakes) {
							if (!snake.alive) {
								continue;
							}
							g.setColor(Color.green);
							if (a.lifeLost) {
								g.setColor(Color.orange);
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
			bestGenomes.add(neat.genomes.getList().get(0).copy());
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
			neat.breed(2250);
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
	
	static class NeatX extends Neat {
		
		public NeatX(int inputCount, int outputCount, boolean addBias) {
			super(inputCount, outputCount, addBias);
		}
		
		@Override
		public void specicateFast(float percentage) {
			float size = genomes.size();
			for (org.neat.libraries.Entry<Integer, Genome> genome : genomes) {
				int id = speciesList.getRandom().id;
				float d = speciesList.get(id).distance(genome.getValue());
				int Count = (int)((float)speciesList.size()*Gaussian()*percentage);
				Count = Math.min(Count, speciesList.size());
				//System.out.print(size/(float)genomes.size()+" ");
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
				size--;
			}
		}
		
		private double nextGaussian() {
			double v1, v2, s;
			do {
				v1 = 2 * Math.random() -1;
				v2 = 2 * Math.random() -1;
				s = Math.pow(v1, 2) + Math.pow(v2, 2);
			} while (s >= 1 || s == 0);
			double multipier = Math.sqrt(-2 * Math.log(s)/s);
			return v1 * multipier;
		}
		private double Gaussian() {
			double rand = nextGaussian();
			return Math.exp(rand);
		}
		@Override
		public void createGenomes(int count) {
			for (int k = 0; k < count; k++) {
				Genome genome = new XGenome(this, inputCount, outputCount, genomes.nextId);
				genome.initialize();
				genomes.add(genome);
			}
		}
	}
	
	private static class XGenome extends Genome {
		public XGenome(Neat neat, int inputCount, int outputCount, int id) {
			super(neat, inputCount, outputCount, id);
		}
		@Override
		public Genome crossover(Genome other) {
			Genome genome = new XGenome(neat, inputCount, outputCount, neat.genomes.nextId);
			if (Math.random()<neat.config.crossover.takeSecondActivation) {
				genome.activation = other.activation;
			}
			else {
				genome.activation = this.activation;
			}
			if (Math.random()<neat.config.crossover.takeSecondAggregation) {
				genome.aggregation = other.aggregation;
			}
			else {
				genome.aggregation = this.aggregation;
			}
			ConnectionList newConnections = new ConnectionList(neat);
			// create copy of this ConnectionList (equal Connections)
			for (Entry<Long, ConnectionGene> connection : connections) {
				newConnections.add(connection.getValue());
			}
			// add copy of other ConnectionList and replace with probability (equal Connections)
			for (Entry<Long, ConnectionGene> connection : other.connections) {
				if (newConnections.contains(connection.getValue())) {
					if (Math.random()<neat.config.crossover.takeSecondConnection) {
						newConnections.set(connection.getValue());
					}
				}
				else {
					newConnections.add(connection.getValue());
				}
			}
			
			// thin out List of Connections
			if (neat.config.crossover.thinConnections) {
				if (neat.config.crossover.thinViaPercentage) {
					int removeCnt = (int) Math.floor(newConnections.size()*neat.config.crossover.thinProbOrPerc);
					for (int k = 0; k < removeCnt; k++) {
						int index = (int) Math.floor(newConnections.size()*Math.random());
						if (newConnections.getList().get(index).fromGene.x==0&&newConnections.getList().get(index).toGene.x==1) {
							newConnections.remove(newConnections.getList().get(index));
						}
					}
				}
				else {
					List<ConnectionGene> removeGenes = new ArrayList<>();
					/*Iterator<Entry<Long, ConnectionGene>> itr = newConnections.iterator();
					while (itr.hasNext()) {
						Entry<Long, ConnectionGene> entry = itr.next();
						if (Math.random()<neat.config.crossover.thinProbOrPerc) {
							itr.remove();
						}
					}*/
					for (Entry<Long,ConnectionGene> entry : newConnections) {
						if (Math.random()<neat.config.crossover.thinProbOrPerc) {
							if (entry.getValue().fromGene.x!=0||entry.getValue().toGene.x!=1) {
								continue;
							}
							removeGenes.add(entry.getValue());
						}
					}
					for (ConnectionGene connectionGene : removeGenes) {
						newConnections.remove(connectionGene);
					}
				}
			}
			
			// Create NodeList of needed Nodes
			NodeList newNodes = new NodeList();
			// NodeList newInputs = new NodeList();
			for (Entry<Long, ConnectionGene> connection : newConnections) {
				if (!newNodes.contains(connection.getValue().fromGene)) {
					if (this.nodes.contains(connection.getValue().fromGene)) {
						if (other.nodes.contains(connection.getValue().fromGene)) {
							if (Math.random()<neat.config.crossover.takeSecondNode) {
								newNodes.add(other.nodes.get(connection.getValue().fromGene));
							}
							else {
								newNodes.add(this.nodes.get(connection.getValue().fromGene));
							}
						}
						else {
							newNodes.add(this.nodes.get(connection.getValue().fromGene));
						}
					}
					else {
						if (other.nodes.contains(connection.getValue().fromGene)) {
							newNodes.add(other.nodes.get(connection.getValue().fromGene));
						}
						else {
							newNodes.add(neat.getNode(connection.getValue().fromGene, genome));
						}
						
					}
				}
				if (this.nodes.contains(connection.getValue().toGene)) {
					if (other.nodes.contains(connection.getValue().toGene)) {
						if (Math.random()<neat.config.crossover.takeSecondNode) {
							newNodes.add(other.nodes.get(connection.getValue().toGene));
						}
						else {
							newNodes.add(this.nodes.get(connection.getValue().toGene));
						}
					}
					else {
						newNodes.add(this.nodes.get(connection.getValue().toGene));
					}
				}
				else {
					if (other.nodes.contains(connection.getValue().toGene)) {
						newNodes.add(other.nodes.get(connection.getValue().toGene));
					}
					else {
						newNodes.add(neat.getNode(connection.getValue().toGene, genome));
					}
				}
			}
			for (Entry<Long, NodeGene> node : newNodes) {
				NodeGene gene = node.getValue().copy(genome);
				if (!gene.activationChanged) {
					gene.activation = genome.activation;
				}
				if (!gene.aggregationChanged) {
					gene.aggregation = genome.aggregation;
				}
				genome.nodes.set(gene);
				if (gene.x <= 0) {
					genome.inputNodes.set(gene);
				}
				if (gene.x >= 1) {
					genome.outputNodes.set(gene);
				}
			}
			for (Entry<Long, NodeGene> node : inputNodes) {
				if (!newNodes.contains(node.getValue())) {
					if (Math.random()<neat.config.crossover.takeSecondNode) {
						genome.nodes.set(other.nodes.get(node.getValue()).copy(genome));
						genome.inputNodes.set(other.nodes.get(node.getValue()).copy(genome));
					}
					else {
						genome.nodes.set(this.nodes.get(node.getValue()).copy(genome));
						genome.inputNodes.set(this.nodes.get(node.getValue()).copy(genome));
					}
				}
			}
			for (Entry<Long, NodeGene> node : outputNodes) {
				if (!newNodes.contains(node.getValue())) {
					if (Math.random()<neat.config.crossover.takeSecondNode) {
						genome.nodes.set(other.nodes.get(node.getValue()).copy(genome));
						genome.outputNodes.set(other.nodes.get(node.getValue()).copy(genome));
					}
					else {
						genome.nodes.set(this.nodes.get(node.getValue()).copy(genome));
						genome.outputNodes.set(this.nodes.get(node.getValue()).copy(genome));
					}
				}
			}
			for (Entry<Long, ConnectionGene> connection : newConnections) {
				ConnectionGene gene = connection.getValue().copy(genome, genome.nodes.get(connection.getValue().fromGene), genome.nodes.get(connection.getValue().toGene));
				genome.connections.add(gene);
			}
			return genome;
		}
	}
	
}
