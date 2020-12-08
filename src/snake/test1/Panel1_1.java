package snake.test1;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Panel1_1 extends JPanel {
	
	Canvas1_1 canvas;
	Graphics graphics;
	Frame1 frame;
	
	public Panel1_1(Frame1 frame) {
		super();
		initialize();
		this.frame = frame;
		addCanvas();
	}
	
	private void initialize() {
		setVisible(true);
		setBounds(200, 200, 600, 600);
	}
	
	public void addCanvas(GraphicsConfiguration config) {
		//canvas = new Canvas1_1_1(frame, config);
		canvas.setVisible(true);
		canvas.setSize(300, 450);
		add(canvas);
	}
	public void addCanvas() {
		canvas = new Canvas1_1(frame);
		add(canvas);
		canvas.setVisible(true);
		canvas.setSize(300, 450);
	}
	
}
