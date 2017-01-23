package dbApp02;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Logger {
	private static Connecty connecty;
	private DynamicTable dynamicTable;
	private int logCounter;

	public Logger() {
		run();
	}

	protected static Connecty getConnecty() {
		return connecty;
	}

	private void run() {
		JLabel title = new JLabel("Login Username and Password");
		JTextField username = new JTextField("root");
		JPasswordField password = new JPasswordField("root");
		final JComponent[] inputs = new JComponent[] { title, new JLabel("Username"), username, new JLabel("Password"),
				password };

		JOptionPane.showMessageDialog(null, inputs, "Login", JOptionPane.PLAIN_MESSAGE);

		String user = username.getText();
		String userPassword = new String(password.getPassword());

		connecty = new Connecty(user, userPassword);
		if (!connecty.isIn() && logCounter < 5) {
			logCounter++;
			run();
		} else if (logCounter > 4) {
			System.exit(0);
		} else if (connecty.isIn()) {
			System.out.println("run initialize");
			dynamicTable = new DynamicTable();
		}

	}

}
