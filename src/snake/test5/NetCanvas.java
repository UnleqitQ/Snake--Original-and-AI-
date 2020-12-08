package snake.test5;

import java.awt.Canvas;

public class NetCanvas extends Canvas {
	
	Frame1 application;
	
	public NetCanvas(Frame1 application) {
		this.application = application;
		setSize(application.netFrame1.getWidth(), application.netFrame1.getHeight());
		setVisible(true);
	}
	
}
