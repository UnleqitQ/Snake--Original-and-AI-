package snake.test0;

import javax.swing.*;
import java.util.*;
import java.awt.*;

public class ApplicationSnake1 {
	
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
		ApplicationSnake1 window = new ApplicationSnake1();
	}
	
	public ApplicationSnake1() {
		initialize();
		frame1.runGame();
		System.exit(0);
	}
	
	private void initialize() {
		this.frame1 = new Frame1();
	}
	
}
