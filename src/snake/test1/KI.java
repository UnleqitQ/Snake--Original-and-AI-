package snake.test1;

import snake.test1.Frame1.Direction;

public class KI {
	
	Frame1 frame;
	
	public float gamma = 0.5f;
	public float randomness = 0.0f;
	public int depth;
	
	private float[][][] quality;
	
	int[][] inputs;
	
	public KI(Frame1 frame) {
		this.frame = frame;
	}
	
	public void setInputs(int[][] inputs) {
		this.inputs = inputs;
	}
	
	public void resetQuality() {
		quality = new float[frame.sightField*2+1][frame.sightField*2+1][depth+1];
		for (int x = 0; x < frame.sightField*2+1; x++) {
			for (int y = 0; y < frame.sightField*2+1; y++) {
				for (int k = 0; k < depth+1; k++) {
					quality[x][y][k] = 100;
				}
			}
		}
	}
	
	public float getQuality(int depth, int pX, int pY) {
		if (quality[pX][pY][depth]<50) {
			return quality[pX][pY][depth];
		}
		if (inputs[pX][pY]<0) {
			quality[pX][pY][depth] = inputs[pX][pY];
			return inputs[pX][pY];
		}
		if (depth==0) {
			if (inputs[pX][pY]<0) {
				quality[pX][pY][depth] = inputs[pX][pY];
				return inputs[pX][pY];
			}
			quality[pX][pY][depth] = inputs[pX][pY];
			return inputs[pX][pY];
		}
		float qualityUp = getQuality(depth-1, pX, Math.min(2*frame.sightField, Math.max(0, pY-1)))*gamma;
		float qualityDown = getQuality(depth-1, pX, Math.min(2*frame.sightField, Math.max(0, pY+1)))*gamma;
		float qualityLeft = getQuality(depth-1, Math.min(2*frame.sightField, Math.max(0, pX-1)), pY)*gamma;
		float qualityRight = getQuality(depth-1, Math.min(2*frame.sightField, Math.max(0, pX+1)), pY)*gamma;
		float valueUp = qualityUp*(1-randomness)+qualityLeft*randomness/2+qualityRight*randomness/2;
		float valueDown = qualityDown*(1-randomness)+qualityLeft*randomness/2+qualityRight*randomness/2;
		float valueLeft = qualityLeft*(1-randomness)+qualityUp*randomness/2+qualityDown*randomness/2;
		float valueRight = qualityRight*(1-randomness)+qualityUp*randomness/2+qualityDown*randomness/2;
		float quality = Math.max(Math.max(valueLeft, valueRight), Math.max(valueUp, valueDown));
		if (this.inputs[pX][pY]<=0) {
			quality-=1/6;
		}
		this.quality[pX][pY][depth] = quality;//+inputs[pX][pY];
		return quality+inputs[pX][pY];
	}
	
	public Direction getBestAction() {
		float qualityUp = getQuality(depth, frame.sightField, frame.sightField-1);
		float qualityDown = getQuality(depth, frame.sightField, frame.sightField+1);
		float qualityLeft = getQuality(depth, frame.sightField-1, frame.sightField);
		float qualityRight = getQuality(depth, frame.sightField+1, frame.sightField);
		if (qualityUp>qualityDown) {
			if (qualityLeft>qualityRight) {
				if (qualityLeft>qualityUp) {
					return Direction.LEFT;
				}
				else if (qualityLeft<qualityRight) {
					return Direction.UP;
				}
				else {
					if (Math.random()<0.5) {
						return Direction.LEFT;
					}
					else {
						return Direction.UP;
					}
				}
			}
			else if (qualityLeft<qualityRight) {
				if (qualityRight>qualityUp) {
					return Direction.RIGHT;
				}
				else if (qualityLeft<qualityRight) {
					return Direction.UP;
				}
				else {
					if (Math.random()<0.5) {
						return Direction.RIGHT;
					}
					else {
						return Direction.UP;
					}
				}
			}
			else {
				if (Math.random()<0.5) {
					if (qualityLeft>qualityUp) {
						return Direction.LEFT;
					}
					else if (qualityLeft<qualityRight) {
						return Direction.UP;
					}
					else {
						if (Math.random()<0.5) {
							return Direction.LEFT;
						}
						else {
							return Direction.UP;
						}
					}
				}
				else {
					if (qualityRight>qualityUp) {
						return Direction.RIGHT;
					}
					else if (qualityLeft<qualityRight) {
						return Direction.UP;
					}
					else {
						if (Math.random()<0.5) {
							return Direction.RIGHT;
						}
						else {
							return Direction.UP;
						}
					}
				}
			}
		}
		else if (qualityUp<qualityDown) {
			if (qualityLeft>qualityRight) {
				if (qualityLeft>qualityUp) {
					return Direction.LEFT;
				}
				else if (qualityLeft<qualityRight) {
					return Direction.DOWN;
				}
				else {
					if (Math.random()<0.5) {
						return Direction.LEFT;
					}
					else {
						return Direction.DOWN;
					}
				}
			}
			else if (qualityLeft<qualityRight) {
				if (qualityRight>qualityUp) {
					return Direction.RIGHT;
				}
				else if (qualityLeft<qualityRight) {
					return Direction.DOWN;
				}
				else {
					if (Math.random()<0.5) {
						return Direction.RIGHT;
					}
					else {
						return Direction.DOWN;
					}
				}
			}
			else {
				if (Math.random()<0.5) {
					if (qualityLeft>qualityUp) {
						return Direction.LEFT;
					}
					else if (qualityLeft<qualityRight) {
						return Direction.DOWN;
					}
					else {
						if (Math.random()<0.5) {
							return Direction.LEFT;
						}
						else {
							return Direction.DOWN;
						}
					}
				}
				else {
					if (qualityRight>qualityUp) {
						return Direction.RIGHT;
					}
					else if (qualityLeft<qualityRight) {
						return Direction.DOWN;
					}
					else {
						if (Math.random()<0.5) {
							return Direction.RIGHT;
						}
						else {
							return Direction.DOWN;
						}
					}
				}
			}
		}
		else {
			if (Math.random()<0.6) {
				if (qualityLeft>qualityRight) {
					if (qualityLeft>qualityUp) {
						return Direction.LEFT;
					}
					else if (qualityLeft<qualityRight) {
						return Direction.UP;
					}
					else {
						if (Math.random()<0.5) {
							return Direction.LEFT;
						}
						else {
							return Direction.UP;
						}
					}
				}
				else if (qualityLeft<qualityRight) {
					if (qualityRight>qualityUp) {
						return Direction.RIGHT;
					}
					else if (qualityLeft<qualityRight) {
						return Direction.UP;
					}
					else {
						if (Math.random()<0.5) {
							return Direction.RIGHT;
						}
						else {
							return Direction.UP;
						}
					}
				}
				else {
					if (Math.random()<0.5) {
						if (qualityLeft>qualityUp) {
							return Direction.LEFT;
						}
						else if (qualityLeft<qualityRight) {
							return Direction.UP;
						}
						else {
							if (Math.random()<0.5) {
								return Direction.LEFT;
							}
							else {
								return Direction.UP;
							}
						}
					}
					else {
						if (qualityRight>qualityUp) {
							return Direction.RIGHT;
						}
						else if (qualityLeft<qualityRight) {
							return Direction.UP;
						}
						else {
							if (Math.random()<0.5) {
								return Direction.RIGHT;
							}
							else {
								return Direction.UP;
							}
						}
					}
				}
			}
			else {
				if (qualityLeft>qualityRight) {
					if (qualityLeft>qualityUp) {
						return Direction.LEFT;
					}
					else if (qualityLeft<qualityRight) {
						return Direction.DOWN;
					}
					else {
						if (Math.random()<0.5) {
							return Direction.LEFT;
						}
						else {
							return Direction.DOWN;
						}
					}
				}
				else if (qualityLeft<qualityRight) {
					if (qualityRight>qualityUp) {
						return Direction.RIGHT;
					}
					else if (qualityLeft<qualityRight) {
						return Direction.DOWN;
					}
					else {
						if (Math.random()<0.5) {
							return Direction.RIGHT;
						}
						else {
							return Direction.DOWN;
						}
					}
				}
				else {
					if (Math.random()<0.5) {
						if (qualityLeft>qualityUp) {
							return Direction.LEFT;
						}
						else if (qualityLeft<qualityRight) {
							return Direction.DOWN;
						}
						else {
							if (Math.random()<0.5) {
								return Direction.LEFT;
							}
							else {
								return Direction.DOWN;
							}
						}
					}
					else {
						if (qualityRight>qualityUp) {
							return Direction.RIGHT;
						}
						else if (qualityLeft<qualityRight) {
							return Direction.DOWN;
						}
						else {
							if (Math.random()<0.5) {
								return Direction.RIGHT;
							}
							else {
								return Direction.DOWN;
							}
						}
					}
				}
			}
		}
	}
	
}
