package dbApp02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DynamicTable extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable editTable;
	private ArrayList<JButton> buttons = new ArrayList<>();
	private ArrayList<JLabel> jLabels = new ArrayList<>();
	private ArrayList<JTextField> jTextFields = new ArrayList<>();
	private JPanel panelChoose, panelltb, panelTable;
	private int logCounter = 0;
	private Connecty connecty;
	private JButton btnAuthor, btnBooks, btnCustomer, btnItem, btnRentals;
	private String currentTable = "";
	private JTextField txFind;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DynamicTable frame = new DynamicTable();
					frame.setVisible(true);
					frame.letsLog();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DynamicTable() {
		getContentPane().setBackground(new Color(0, 102, 102));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 500);
		getContentPane().setLayout(new FlowLayout());

	}

	private void initialize() {

		panelChoose = new JPanel();
		this.getContentPane().add(panelChoose, BorderLayout.NORTH);
		panelChoose.setLayout(new BorderLayout(0, 0));

		btnAuthor = new JButton(TableData.getTablesNames()[0].toUpperCase());
		buttons.add(btnAuthor);
		btnAuthor.addActionListener(this);
		panelChoose.add(btnAuthor, BorderLayout.LINE_START);

		btnBooks = new JButton(TableData.getTablesNames()[1].toUpperCase());
		buttons.add(btnBooks);
		btnBooks.addActionListener(this);
		panelChoose.add(btnBooks, BorderLayout.AFTER_LAST_LINE);

		btnCustomer = new JButton(TableData.getTablesNames()[2].toUpperCase());
		buttons.add(btnCustomer);
		btnCustomer.addActionListener(this);
		panelChoose.add(btnCustomer, BorderLayout.AFTER_LINE_ENDS);

		btnItem = new JButton(TableData.getTablesNames()[3].toUpperCase());
		buttons.add(btnItem);
		btnItem.addActionListener(this);
		panelChoose.add(btnItem, BorderLayout.BEFORE_FIRST_LINE);

		btnRentals = new JButton(TableData.getTablesNames()[4].toUpperCase());
		buttons.add(btnRentals);
		btnRentals.addActionListener(this);
		panelChoose.add(btnRentals, BorderLayout.CENTER);

		panelltb = new JPanel();
		this.getContentPane().add(panelltb);
		panelltb.setLayout(new GridBagLayout());
		panelltb.setBackground(new Color(0, 102, 102));

		panelTable = new JPanel();
		this.getContentPane().add(panelTable);
		panelTable.setLayout(new GridBagLayout());
		Connecty.descTables("author");
	}

	public void createComponents(int colname) {
		panelltb.removeAll();
		jTextFields.removeAll(jTextFields);
		buttons.removeAll(buttons);
		ArrayList<String> colnames = TableData.getColNames();
		System.out.println("createComponents");
		GridBagConstraints constraints = new GridBagConstraints();
		for (int i = 0; i < colnames.size(); i++) {

			JLabel label = new JLabel(colnames.get(i));

			JTextField field = new JTextField(TableData.getRecords()[0][i], 14);
			field.addActionListener(this);
			field.setActionCommand("" + i);

			constraints.gridx = 0;
			constraints.gridy = i;
			panelltb.add(label, constraints);
			constraints.gridx = 1;
			constraints.gridy = i;
			panelltb.add(field, constraints);

			jLabels.add(label);
			jTextFields.add(field);
		}
		constraints.gridx = 3;
		constraints.gridy = 0;
		JButton insert = new JButton("Insert");
		insert.addActionListener(this);
		insert.setActionCommand("insert");
		panelltb.add(insert, constraints);
		buttons.add(insert);

		constraints.gridx = 3;
		constraints.gridy = 1;
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(this);
		btnUpdate.setActionCommand("update");
		if (currentTable.equals("rentals")) {
			btnUpdate.setName("return");
			btnUpdate.setActionCommand("return");
		}
		panelltb.add(btnUpdate, constraints);
		buttons.add(btnUpdate);

		constraints.gridx = 2;
		constraints.gridy = 2;
		txFind = new JTextField(10);
		panelltb.add(txFind, constraints);

		constraints.gridx = 3;
		constraints.gridy = 2;
		JButton find = new JButton("Find");
		find.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				findRecord();

			}
		});
		find.setActionCommand("find");
		panelltb.add(find, constraints);
		buttons.add(find);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnAuthor)) {
			currentTable = "author";
		} else if (e.getSource().equals(btnBooks)) {
			currentTable = "books";
		} else if (e.getSource().equals(btnCustomer)) {
			currentTable = "customer";
		} else if (e.getSource().equals(btnItem)) {
			currentTable = "item";
		} else if (e.getSource().equals(btnRentals)) {
			currentTable = "rentals";
		} else if (e.getActionCommand().equals("insert")) {
			if (currentTable.equals("rentals")) {
				modifyRentals("insert");
			} else {
				insert(currentTable);
			}
		} else if (e.getActionCommand().equals("update")) {
			update(currentTable);
		} else if (e.getActionCommand().equals("return")) {
			modifyRentals("return");
		}
		Connecty.descTables(currentTable);
		connecty.selectDatafrom(currentTable);
		createComponents(TableData.getColNames().size());
		fillAuthorTable(TableData.getRecords());

	}

	private void modifyRentals(String type) {
		String itemId = "", sql = "";
		if (type.equals("insert")) {

			String customerName = "", customerSurname = "", customerId = "";

			for (int i = 0; i < TableData.noOfCol(); i++) {
				if (TableData.getColNames().get(i).equalsIgnoreCase("item_id")) {
					itemId = jTextFields.get(i).getText();
				} else if (TableData.getColNames().get(i).equalsIgnoreCase("customer_name")) {
					customerName = jTextFields.get(i).getText();
				} else if (TableData.getColNames().get(i).equalsIgnoreCase("customer_surname")) {
					customerSurname = jTextFields.get(i).getText();
				}
			}
			customerId = findCustomerId(customerName, customerSurname);
			sql = "insert into rental_b ( item_id,customer_id) values( " + itemId + " , " + customerId + " ) ";
		} else if (type.equals("return")) {
			sql = "update  rental_b  set return_date = '" + jTextFields.get(3).getText()
					+ "' where rental_id = " + jTextFields.get(0).getText();
		}
		System.out.println(sql);
		Connecty.setData(sql);
		
	}

	private String findCustomerId(String customerName, String customerSurname) {
		String customerId = "";
		Connecty.descTables("customer");
		connecty.selectDatafrom("customer");
		for (int i = 0; i < TableData.getRecords().length; i++) {

			String tempName = TableData.getRecords()[i][1];
			String tempSurname = TableData.getRecords()[i][2];
			if (tempName != null && tempSurname != null && tempName.equalsIgnoreCase(customerName)
					&& tempSurname.equalsIgnoreCase(customerSurname)) {
				customerId = TableData.getRecords()[i][0];
			}
		}
		return customerId;
	}

	private void findRecord() {
		String[][] recordsToDisplay = new String[100][TableData.noOfCol()];
		int counter = 0;
		for (int i = 0; i < TableData.getRecords().length; i++) {
			for (int j = 0; j < TableData.noOfCol(); j++) {
				if (TableData.getRecords()[i][j] != null
						&& (TableData.getRecords()[i][j].equalsIgnoreCase(txFind.getText())
								|| TableData.getRecords()[i][j].contains(txFind.getText()))) {
					for (int k = 0; k < TableData.noOfCol(); k++) {
						recordsToDisplay[counter][k] = TableData.getRecords()[i][k];
					}
					counter++;
				}
			}
		}
		fillAuthorTable(recordsToDisplay);
	}

	private void update(String currentTable) {
		String sqUpdate = "update " + currentTable + " set ";
		for (int i = 1; i < TableData.getColNames().size(); i++) {
			String type = TableData.getColTypes().get(i);
			if (!jTextFields.get(i).getText().isEmpty()) {
				if (type.contains("int")) {
					sqUpdate += TableData.getColNames().get(i) + " = " + jTextFields.get(i).getText() + " , ";
				} else {
					sqUpdate += TableData.getColNames().get(i) + " = '" + jTextFields.get(i).getText() + "' , ";
				}
			}
		}
		if (sqUpdate.endsWith(", ")) {
			sqUpdate = sqUpdate.substring(0, sqUpdate.length() - 2);
		}
		sqUpdate += " where " + TableData.getColNames().get(0) + " = " + jTextFields.get(0).getText();
		Connecty.setData(sqUpdate);
		System.out.println(sqUpdate);

	}

	private void insert(String currentTable) {
		String sqInsert = "insert into " + currentTable + " ( ";
		for (int i = 1; i < TableData.getColNames().size(); i++) {
			if (jTextFields.size() - 1 == i) {
				if (!jTextFields.get(i).getText().isEmpty()) {
					sqInsert += TableData.getColNames().get(i);
				} else if (sqInsert.endsWith(", ")) {
					sqInsert = sqInsert.substring(0, sqInsert.length() - 2);
				}
				sqInsert += " ) values ( ";
			} else {
				if (!jTextFields.get(i).getText().isEmpty())
					sqInsert += TableData.getColNames().get(i) + " , ";
			}
		}
		for (int i = 1; i < jTextFields.size(); i++) {
			String type = TableData.getColTypes().get(i);

			if (jTextFields.size() - 1 == i && !jTextFields.get(i).getText().isEmpty()) {
				if (type.contains("int")) {
					sqInsert += " " + jTextFields.get(i).getText() + "  ";
				} else {
					sqInsert += " '" + jTextFields.get(i).getText() + "'  ";
				}
			} else if (!jTextFields.get(i).getText().isEmpty()) {
				if (type.contains("int")) {
					sqInsert += " " + jTextFields.get(i).getText() + " , ";
				} else {
					sqInsert += " '" + jTextFields.get(i).getText() + "' , ";
				}
			}

		}
		sqInsert += " )";
		System.out.println(sqInsert);
		Connecty.setData(sqInsert);
	}

	public void fillAuthorTable(String[][] recordsToTable) {
		panelTable.removeAll();
		editTable = new JTable(new DefaultTableModel(new Object[][] {},
				TableData.getColNames().toArray(new String[TableData.getColNames().size()])));

		editTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index = editTable.getSelectedRow();
				updateTextFields(index);
			}
		});
		editTable.setPreferredScrollableViewportSize(new Dimension(413, 400));
		editTable.setAutoCreateRowSorter(true);
		editTable.setAutoscrolls(true);
		editTable.setFillsViewportHeight(true);
		editTable.setCellSelectionEnabled(true);
		editTable.setColumnSelectionAllowed(true);
		editTable.setForeground(new Color(0, 0, 0));
		editTable.setBackground(new Color(51, 204, 255));
		editTable.setBounds(370, 26, 225, 0);
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridx = 0;
		constraints.gridy = 0;
		panelTable.add(editTable.getTableHeader(), constraints);
		constraints.gridy = 1;
		panelTable.add(editTable, constraints);

		DefaultTableModel model = (DefaultTableModel) editTable.getModel();
		model.setRowCount(0);

		Object[] row = new Object[TableData.noOfCol()];
		for (int i = 0; i < recordsToTable.length; i++) {
			if (recordsToTable[i][0] != null) {
				for (int j = 0; j < TableData.noOfCol(); j++) {
					row[j] = recordsToTable[i][j];
				}
				model.addRow(row);
			}
		}
	}

	protected void updateTextFields(int index) {
		for (int i = 0; i < jTextFields.size(); i++) {
			jTextFields.get(i).setText(TableData.getRecords()[index][i]);
		}

	}

	public void letsLog() {

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
			letsLog();
		} else if (logCounter > 4) {
			System.exit(0);
		} else if (connecty.isIn()) {
			System.out.println("run initialize");
			initialize();
		}
	}
}
