package snake.test2;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Canvas1_1 extends Canvas {
	
	Frame1 panel;
	
	Graphics graphics;
	
	/*public Canvas1_1_1(Panel1_1 panel) {
		super();
		this.panel = panel;
	}
	public Canvas1_1_1(Panel1_1 panel, GraphicsConfiguration graphicsConfiguration) {
		super(graphicsConfiguration);
		this.panel = panel;
	}*/
	public Canvas1_1(Frame1 frame) {
		super();
		this.panel = frame;
	}
	
}
