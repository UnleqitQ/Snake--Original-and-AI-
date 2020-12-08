package snake.test2;

import javax.swing.*;
import java.util.*;
import java.awt.*;

public class ApplicationSnake3 {
	
	Frame1 frame1;
	
	public static void main(String[] args) {
		/*EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					Application window = new Application();
					System.out.print("");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
		ApplicationSnake3 window = new ApplicationSnake3();
	}
	
	public ApplicationSnake3() {
		initialize();
		frame1.runGame();
		System.exit(0);
	}
	
	private void initialize() {
		this.frame1 = new Frame1();
	}
	
}
