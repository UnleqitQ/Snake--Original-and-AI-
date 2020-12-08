package snake.test4;

import java.util.*;

import org.neat.calculate.*;
import org.neat.neat.Neat;

public class Snake2 {
	
	Calculator calculator;
	Frame1 frame;
	Random random;
	
	boolean alive = true;
	
	public ArrayList<Integer> posX;
	public ArrayList<Integer> posY;
	public ArrayList<Integer> appleX;
	public ArrayList<Integer> appleY;
	public int score = 0;
	public int len = 5;
	public int lifes = 2;
	public boolean lifeLost = false;
	int histx;
	int histy;
	int cnt = 0;
	
	public int iteration = 0;
	
	public static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	Direction direction = Direction.LEFT;
	
	public Snake2(Calculator calculator, Frame1 frame, Random random, long seed) {
		this.calculator = calculator;
		calculator.genome.fitness = 0;
		this.frame = frame;
		this.random = random;
		this.random = new Random(seed);
		posX = new ArrayList<>();
		posY = new ArrayList<>();
	}
	/*private float[][] getSight() {
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
	}*/
	/*private Map<Long, Float> getSightMap() {
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
	}*/
	
	private Direction getOutput() {
		/*int size = frame.sightField*2+1;
		size = size*size;*/
		int size = 16;
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
		appleX = new ArrayList<>();
		appleY = new ArrayList<>();
		for (int k = 0; k < 3; k++) {
			appleX.add(random.nextInt(frame.sizeX));
			appleY.add(random.nextInt(frame.sizeY));
		}
	}
	
	public void run() {
		if (!alive) {
			return;
		}
		lifeLost = false;
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
		if (posX.get(0)<0||posX.get(0)>=frame.sizeX) {
			//alive = false;
			lifes--;
			lifeLost = true;
			posX.set(0, Math.floorMod(posX.get(0)+frame.sizeX, frame.sizeX));
		}
		if (posY.get(0)<0||posY.get(0)>=frame.sizeY) {
			//alive = false;
			lifes--;
			lifeLost = true;
			posY.set(0, Math.floorMod(posY.get(0)+frame.sizeY, frame.sizeY));
		}
		if (posX.get(0)!=histx) {
			//cnt = 0;
		}
		if (posY.get(0)!=histy) {
			//cnt = 0;
		}
		if (posX.size()>len) {
			histx = posX.remove(len);
		}
		if (posY.size()>len) {
			histy = posY.remove(len);
		}
		if (cnt >= Math.pow(frame.sizeX*frame.sizeY, 1)) {
			alive = false;
		}
		for (int k2 = 1; k2 < posX.size(); k2++) {
			if (posX.get(0)==posX.get(k2)) {
				if (posY.get(0)==posY.get(k2)) {
					//alive = false;
					lifes--;
					lifeLost = true;
					break;
				}
			}
		}
		if (lifes<=0) {
			alive = false;
		}
		cnt++;
		for (int k2 = 0; k2 < appleX.size(); k2++) {
			if (posX.get(0) == appleX.get(k2) && posY.get(0) == appleY.get(k2)) {
				cnt = 0;
				len++;
				score++;
				appleX.set(k2, random.nextInt(frame.sizeX));
				appleY.set(k2, random.nextInt(frame.sizeY));
			}
		}
		calculator.genome.fitness = score+(float)iteration/1000;
		iteration++;
	}
	
	private Map<Long, Float> getSightMap() {
		Map<Long, Float> sightMap = new HashMap<>();
		sightMap.put(0L, getAppleDist(0, 1));
		sightMap.put(1L, getAppleDist(1, 1));
		sightMap.put(2L, getAppleDist(1, 0));
		sightMap.put(3L, getAppleDist(1, -1));
		sightMap.put(4L, getAppleDist(0, -1));
		sightMap.put(5L, getAppleDist(-1, -1));
		sightMap.put(6L, getAppleDist(-1, 0));
		sightMap.put(7L, getAppleDist(-1, 1));
		
		/*sightMap.put(8L, getTailDist(0, 1));
		sightMap.put(9L, getTailDist(1, 1));
		sightMap.put(10L, getTailDist(1, 0));
		sightMap.put(11L, getTailDist(1, -1));
		sightMap.put(12L, getTailDist(0, -1));
		sightMap.put(13L, getTailDist(-1, -1));
		sightMap.put(14L, getTailDist(-1, 0));
		sightMap.put(15L, getTailDist(-1, 1));*/
		
		/*sightMap.put(16L, getWallDist(0, 1));
		sightMap.put(17L, getWallDist(1, 1));
		sightMap.put(18L, getWallDist(1, 0));
		sightMap.put(19L, getWallDist(1, -1));
		sightMap.put(20L, getWallDist(0, -1));
		sightMap.put(21L, getWallDist(-1, -1));
		sightMap.put(22L, getWallDist(-1, 0));
		sightMap.put(23L, getWallDist(-1, 1));*/
		
		sightMap.put(8L, Math.min(getTailDist(0, 1), getWallDist(0, 1)));
		sightMap.put(9L, Math.min(getTailDist(1, 1), getWallDist(1, 1)));
		sightMap.put(10L, Math.min(getTailDist(1, 0), getWallDist(1, 0)));
		sightMap.put(11L, Math.min(getTailDist(1, -1), getWallDist(1, -1)));
		sightMap.put(12L, Math.min(getTailDist(0, -1), getWallDist(0, -1)));
		sightMap.put(13L, Math.min(getTailDist(-1, -1), getWallDist(-1, -1)));
		sightMap.put(14L, Math.min(getTailDist(-1, 0), getWallDist(-1, 0)));
		sightMap.put(15L, Math.min(getTailDist(-1, 1), getWallDist(-1, 1)));
		return sightMap;
	}
	
	public float getAppleDist(int sx, int sy) {
		int dist = 100;
		for (int k = 0; k < Math.max(frame.sizeX, frame.sizeY); k++) {
			for (int k2 = 0; k2 < appleX.size(); k2++) {
				if (posX.get(0)+sx*k==appleX.get(k2)) {
					if (posY.get(0)+sy*k==appleY.get(k2)) {
						dist = Math.min(dist, k);
					}
				}
			}
		}
		return dist;
	}
	public float getTailDist(int sx, int sy) {
		for (int k = 1; k < Math.max(frame.sizeX, frame.sizeY); k++) {
			for (int i = 1; i < posX.size(); i++) {
				if (posX.get(0)+sx*k==posX.get(i)) {
					if (posY.get(0)+sy*k==posY.get(i)) {
						return k;
					}
				}
			}
		}
		return 100;
	}
	public float getWallDist(int sx, int sy) {
		int dx = 100;
		int dy = 100;
		if (sx>0) {
			dx = frame.sizeX-posX.get(0);
		}
		else if (sx<0) {
			dx = posX.get(0);
		}
		if (sy>0) {
			dy = frame.sizeY-posY.get(0);
		}
		else if (sy<0) {
			dy = posY.get(0);
		}
		return Math.min(dx, dy);
	}
	
}
