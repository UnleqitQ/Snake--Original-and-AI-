package snake.test3;

import java.util.*;

import org.neat.calculate.*;
import org.neat.neat.Neat;

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
	public int len = 5;
	int histx;
	int histy;
	int cnt = 0;
	
	public int iteration = 0;
	
	public static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	Direction direction = Direction.LEFT;
	
	public Snake(Calculator calculator, Frame1 frame, Random random, long seed) {
		this.calculator = calculator;
		calculator.genome.fitness = 0;
		this.frame = frame;
		this.random = random;
		//this.random = new Random(seed);
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
		int size = 8;
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
		if (posX.get(0)<0||posX.get(0)>=frame.sizeX) {
			alive = false;
		}
		if (posY.get(0)<0||posY.get(0)>=frame.sizeY) {
			alive = false;
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
		if (cnt == 300) {
			//alive = false;
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
			cnt = 0;
			len++;
			score++;
			appleX = random.nextInt(frame.sizeX);
			appleY = random.nextInt(frame.sizeY);
		}
		calculator.genome.fitness = score+(float)iteration/10000;
		iteration++;
		cnt++;
	}

	/*private Map<Long, Float> getSightMap() {
		Map<Long, Float> sightMap = new HashMap<>();
		for (int k = 0; k < 20; k++) {
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)) {
					if (posY.get(k2)==posY.get(0)+k) {
						brk = true;
						break;
					}
				}
			}
			if (posY.get(0)+k<0||posY.get(0)+k>frame.sizeY) {
				brk = true;
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)&&appleY==posY.get(0)+k) {
				sightMap.put(0L, -2f);
				break;
			}
			sightMap.put(0L, (float)k/20);
		}
		for (int k = 0; k < 20; k++) {
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+k) {
					if (posY.get(k2)==posY.get(0)) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+k<0||posX.get(0)+k>frame.sizeX) {
				brk = true;
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+k&&appleY==posY.get(0)) {
				sightMap.put(1L, -2f);
				break;
			}
			sightMap.put(1L, (float)k/20);
		}
		for (int k = 0; k < 20; k++) {
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)) {
					if (posY.get(k2)==posY.get(0)-k) {
						brk = true;
						break;
					}
				}
			}
			if (posY.get(0)-k<0||posY.get(0)-k>frame.sizeY) {
				brk = true;
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)&&appleY==posY.get(0)-k) {
				sightMap.put(2L, -2f);
				break;
			}
			sightMap.put(2L, (float)k/20);
		}
		for (int k = 0; k < 20; k++) {
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)-k) {
					if (posY.get(k2)==posY.get(0)) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)-k<0||posX.get(0)-k>frame.sizeX) {
				brk = true;
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)-k&&appleY==posY.get(0)) {
				sightMap.put(3L, -2f);
				break;
			}
			sightMap.put(3L, (float)k/20);
		}
		for (int k = 0; k < 20; k++) {
			int sx = 1;
			int sy = 1;
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+sx*k) {
					if (posY.get(k2)==posY.get(0)+sy*k) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+sx*k<0||posX.get(0)+sx*k>frame.sizeX) {
				if (posY.get(0)+sy*k<0||posY.get(0)+sy*k>frame.sizeY) {
					brk = true;
				}
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+sx*k&&appleY==posY.get(0)+sy*k) {
				sightMap.put(4L, -2f);
				break;
			}
			sightMap.put(4L, (float)k/20);
		}
		for (int k = 0; k < 20; k++) {
			int sx = 1;
			int sy = -1;
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+sx*k) {
					if (posY.get(k2)==posY.get(0)+sy*k) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+sx*k<0||posX.get(0)+sx*k>frame.sizeX) {
				if (posY.get(0)+sy*k<0||posY.get(0)+sy*k>frame.sizeY) {
					brk = true;
				}
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+sx*k&&appleY==posY.get(0)+sy*k) {
				sightMap.put(5L, -2f);
				break;
			}
			sightMap.put(5L, (float)k/20);
		}
		for (int k = 0; k < 20; k++) {
			int sx = -1;
			int sy = -1;
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+sx*k) {
					if (posY.get(k2)==posY.get(0)+sy*k) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+sx*k<0||posX.get(0)+sx*k>frame.sizeX) {
				if (posY.get(0)+sy*k<0||posY.get(0)+sy*k>frame.sizeY) {
					brk = true;
				}
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+sx*k&&appleY==posY.get(0)+sy*k) {
				sightMap.put(6L, -2f);
				break;
			}
			sightMap.put(6L, (float)k/20);
		}
		for (int k = 0; k < 20; k++) {
			int sx = -1;
			int sy = 1;
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+sx*k) {
					if (posY.get(k2)==posY.get(0)+sy*k) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+sx*k<0||posX.get(0)+sx*k>frame.sizeX) {
				if (posY.get(0)+sy*k<0||posY.get(0)+sy*k>frame.sizeY) {
					brk = true;
				}
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+sx*k&&appleY==posY.get(0)+sy*k) {
				sightMap.put(7L, -2f);
				break;
			}
			sightMap.put(7L, (float)k/20);
		}
		return sightMap;
	}*/
	
	private Map<Long, Float> getSightMap() {
		float appleInp = 5;
		Map<Long, Float> sightMap = new HashMap<>();
		for (int k = 1; k < 20; k++) {
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)) {
					if (posY.get(k2)==posY.get(0)+k) {
						brk = true;
						break;
					}
				}
			}
			if (posY.get(0)+k<0||posY.get(0)+k>frame.sizeY) {
				brk = true;
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)&&appleY==posY.get(0)+k) {
				sightMap.put(0L, appleInp);
				break;
			}
			sightMap.put(0L, -(float)k/20);
		}
		for (int k = 1; k < 20; k++) {
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+k) {
					if (posY.get(k2)==posY.get(0)) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+k<0||posX.get(0)+k>frame.sizeX) {
				brk = true;
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+k&&appleY==posY.get(0)) {
				sightMap.put(1L, appleInp);
				break;
			}
			sightMap.put(1L, -(float)k/20);
		}
		for (int k = 1; k < 20; k++) {
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)) {
					if (posY.get(k2)==posY.get(0)-k) {
						brk = true;
						break;
					}
				}
			}
			if (posY.get(0)-k<0||posY.get(0)-k>frame.sizeY) {
				brk = true;
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)&&appleY==posY.get(0)-k) {
				sightMap.put(2L, appleInp);
				break;
			}
			sightMap.put(2L, -(float)k/20);
		}
		for (int k = 1; k < 20; k++) {
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)-k) {
					if (posY.get(k2)==posY.get(0)) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)-k<0||posX.get(0)-k>frame.sizeX) {
				brk = true;
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)-k&&appleY==posY.get(0)) {
				sightMap.put(3L, appleInp);
				break;
			}
			sightMap.put(3L, -(float)k/20);
		}
		for (int k = 1; k < 20; k++) {
			int sx = 1;
			int sy = 1;
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+sx*k) {
					if (posY.get(k2)==posY.get(0)+sy*k) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+sx*k<0||posX.get(0)+sx*k>frame.sizeX) {
				if (posY.get(0)+sy*k<0||posY.get(0)+sy*k>frame.sizeY) {
					brk = true;
				}
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+sx*k&&appleY==posY.get(0)+sy*k) {
				sightMap.put(4L, appleInp);
				break;
			}
			sightMap.put(4L, -(float)k/20);
		}
		for (int k = 1; k < 20; k++) {
			int sx = 1;
			int sy = -1;
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+sx*k) {
					if (posY.get(k2)==posY.get(0)+sy*k) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+sx*k<0||posX.get(0)+sx*k>frame.sizeX) {
				if (posY.get(0)+sy*k<0||posY.get(0)+sy*k>frame.sizeY) {
					brk = true;
				}
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+sx*k&&appleY==posY.get(0)+sy*k) {
				sightMap.put(5L, appleInp);
				break;
			}
			sightMap.put(5L, -(float)k/20);
		}
		for (int k = 1; k < 20; k++) {
			int sx = -1;
			int sy = -1;
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+sx*k) {
					if (posY.get(k2)==posY.get(0)+sy*k) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+sx*k<0||posX.get(0)+sx*k>frame.sizeX) {
				if (posY.get(0)+sy*k<0||posY.get(0)+sy*k>frame.sizeY) {
					brk = true;
				}
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+sx*k&&appleY==posY.get(0)+sy*k) {
				sightMap.put(6L, appleInp);
				break;
			}
			sightMap.put(6L, -(float)k/20);
		}
		for (int k = 1; k < 20; k++) {
			int sx = -1;
			int sy = 1;
			boolean brk = false;
			for (int k2 = 1; k2 < posX.size(); k2++) {
				if (posX.get(k2)==posX.get(0)+sx*k) {
					if (posY.get(k2)==posY.get(0)+sy*k) {
						brk = true;
						break;
					}
				}
			}
			if (posX.get(0)+sx*k<0||posX.get(0)+sx*k>frame.sizeX) {
				if (posY.get(0)+sy*k<0||posY.get(0)+sy*k>frame.sizeY) {
					brk = true;
				}
			}
			if (brk) {
				break;
			}
			if (appleX==posX.get(0)+sx*k&&appleY==posY.get(0)+sy*k) {
				sightMap.put(7L, appleInp);
				break;
			}
			sightMap.put(7L, -(float)k/20);
		}
		/*int size = 5;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int px = posX.get(0)+x-2;
				int py = posY.get(0)+y-2;
				//sightMap.put((long)(8+x+size*y), 0f);
				if (px<0||px>=frame.sizeX) {
					//sightMap.put((long) (8+x+size*y), 99f);
				}
				if (py<0||py>=frame.sizeY) {
					//sightMap.put((long) (8+x+size*y), 99f);
				}
				if (px==appleX&&py==appleY) {
					//sightMap.put((long) (8+x+size*y), -appleInp);
				}
				for (int k = 1; k < posX.size(); k++) {
					if (px==posX.get(k)&&py==posY.get(k)) {
						//sightMap.put((long) (8+x+size*y), (float) (posX.size()-k));
					}
				}
			}
		}*/
		return sightMap;
	}
	
}
