package snake.test2;

import java.util.*;

import org.neat.calculate.*;

public class Snake {
	
	Calculator calculator;
	Frame1 frame;
	Random random;
	
	boolean alive = true;
	
	public ArrayList<Integer> posX;
	public ArrayList<Integer> posY;
	public int appleX;
	public int appleY;
	public int score = 0;
	public int len = 3;
	
	public int iteration = 0;
	
	public static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	Direction direction = Direction.LEFT;
	
	public Snake(Calculator calculator, Frame1 frame, Random random) {
		this.calculator = calculator;
		this.frame = frame;
		this.random = random;
		posX = new ArrayList<>();
		posY = new ArrayList<>();
	}
	private float[][] getSight() {
		float[][] inputs = new float[frame.sightField*2+1][frame.sightField*2+1];
		for (int k1 = 0; k1 < inputs.length; k1++) {
			for (int k2 = 0; k2 < inputs.length; k2++) {
				if ((posX.get(0)+k1-frame.sightField)<0||(posX.get(0)+k1-frame.sightField)>frame.sizeX) {
					inputs[k1][k2] = -len-3;
					continue;
				}
				if ((posY.get(0)+k2-frame.sightField)<0||(posY.get(0)+k2-frame.sightField)>frame.sizeY) {
					inputs[k1][k2] = -len-3;
					continue;
				}
				inputs[k1][k2] = 0;
			}
		}
		for (int k = 0; k < posX.size(); k++) {
			if (Math.abs(posX.get(k)-posX.get(0))>frame.sightField) {
				continue;
			}
			if (Math.abs(posY.get(k)-posY.get(0))>frame.sightField) {
				continue;
			}
			inputs[posX.get(k)-posX.get(0)+frame.sightField][posY.get(k)-posY.get(0)+frame.sightField] = -len+k/2-2;
		}
		if (Math.abs(appleX-posX.get(0))<=frame.sightField) {
			if (Math.abs(appleY-posY.get(0))<=frame.sightField) {
				inputs[appleX-posX.get(0)+frame.sightField][appleY-posY.get(0)+frame.sightField] += 2;
			}
		}
		return inputs;
	}
	private Map<Long, Float> getSightMap() {
		int size = frame.sightField*2+1;
		Map<Long, Float> inputs = new HashMap<>();
		for (int k1 = 0; k1 < size; k1++) {
			for (int k2 = 0; k2 < size; k2++) {
				if ((posX.get(0)+k1-frame.sightField)<0||(posX.get(0)+k1-frame.sightField)>frame.sizeX) {
					inputs.put((long)(k1+size*k2), (float)(-len-3));
					continue;
				}
				if ((posY.get(0)+k2-frame.sightField)<0||(posY.get(0)+k2-frame.sightField)>frame.sizeY) {
					inputs.put((long)(k1+size*k2), (float)(-len-3));
					continue;
				}
				inputs.put((long)(k1+size*k2), (float)(0));
			}
		}
		for (int k = 0; k < posX.size(); k++) {
			if (Math.abs(posX.get(k)-posX.get(0))>frame.sightField) {
				continue;
			}
			if (Math.abs(posY.get(k)-posY.get(0))>frame.sightField) {
				continue;
			}
			inputs.put((long)((posX.get(k)-posX.get(0)+frame.sightField)+size*(posY.get(k)-posY.get(0)+frame.sightField)), (float)(-len+k/2-2));
			//inputs[posX.get(k)-posX.get(0)+frame.sightField][posY.get(k)-posY.get(0)+frame.sightField] = -len+k/2-2;
		}
		if (Math.abs(appleX-posX.get(0))<=frame.sightField) {
			if (Math.abs(appleY-posY.get(0))<=frame.sightField) {
				//inputs[appleX-posX.get(0)+frame.sightField][appleY-posY.get(0)+frame.sightField] += 2;
				inputs.put((long)(appleX-posX.get(0)+frame.sightField)+size*(appleY-posY.get(0)+frame.sightField), inputs.get((long)((appleX-posX.get(0)+frame.sightField)+size*(appleY-posY.get(0)+frame.sightField)))+2);
			}
		}
		return inputs;
	}
	
	private Direction getOutput() {
		int size = frame.sightField*2+1;
		size = size*size;
		Map<Long, Float> outputs = calculator.getOutputs();
		float max = -1000;
		for (Long key : outputs.keySet()) {
			if (outputs.get(key)>max) {
				max = outputs.get(key);
			}
		}
		Set<Long> remove = new HashSet();
		for (Long key : outputs.keySet()) {
			if (outputs.get(key)<max) {
				remove.add(key);
			}
		}
		for (Long key : remove) {
			outputs.remove(key);
		}
		int k = random.nextInt(outputs.size());
		for (Long key : outputs.keySet()) {
			if (k==0) {
				if (key == size) {
					return Direction.UP;
				}
				if (key == size+1) {
					return Direction.LEFT;
				}
				if (key == size+2) {
					return Direction.DOWN;
				}
				if (key == size+3) {
					return Direction.RIGHT;
				}
			}
			k--;
		}
		return Direction.UP;
	}
	
	public void init() {
		posX.add((int)(frame.sizeX/2));
		posY.add((int)(frame.sizeY/2));
		appleX = random.nextInt(frame.sizeX);
		appleY = random.nextInt(frame.sizeY);
	}
	
	public void run() {
		if (!alive) {
			return;
		}
		calculator.setInputValues(getSightMap());
		direction = getOutput();
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
		if (posX.get(0)<0||posX.get(0)>frame.sizeX) {
			alive = false;
		}
		if (posY.get(0)<0||posY.get(0)>frame.sizeY) {
			alive = false;
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
					alive = false;
					break;
				}
			}
		}
		if (posX.get(0) == appleX && posY.get(0) == appleY) {
			len++;
			score++;
			appleX = random.nextInt(frame.sizeX);
			appleY = random.nextInt(frame.sizeY);
		}
		if (!alive) {
			calculator.genome.fitness = score+iteration/10;
		}
		iteration++;
	}
	
}
