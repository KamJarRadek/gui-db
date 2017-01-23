package dbApp02;

import java.awt.EventQueue;

public class AppLauncher {
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Logger();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
