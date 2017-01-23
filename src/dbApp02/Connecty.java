package dbApp02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Connecty {

	private static final String dataBaseName = "library";
	private static String userName = "";
	private static String userPassword = "";
//	private static String answer = "";
	private static boolean in = false;
	private static Connection con = null;


	public static Connection getConnection() {
		return con;
	}

	public Connecty(String name, String password) {
		Connecty.userName = name;
		Connecty.userPassword = password;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dataBaseName + "?useSSL=false", userName,
					userPassword);
			in = true;

		} catch (Exception e) {
			System.out.println("can't connect with DB");
			in = false;
		}
	}

	public String[][] selectDatafrom(String tableName) {
		int noOfCol = TableData.noOfCol();
		int i = 0;
		String[][] records = new String[100][noOfCol];
		System.out.println("selectDatafrom " + tableName + "no of col: " + noOfCol);

		try {
			Connection conn = getConnection();
			System.out.println("connection" + con);
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from " + tableName);
			while (resultSet.next()) {
				for (int j = 0; j < noOfCol; j++) {
					records[i][j] = resultSet.getString(j + 1);
				}
				i++;
			}
		} catch (Exception e) {
			System.out.println("something wrong ");
			e.printStackTrace();
		}
		TableData.setRecords(records);
		return records;
	}

	public static void descTables(String table) {
		ArrayList<String> colNames = new ArrayList<>();
		ArrayList<String> colTypes = new ArrayList<>();

		Connection con = getConnection();
		System.out.println(con);
		Statement st;
		ResultSet resset;

		try {
			st = con.createStatement();
			resset = st.executeQuery("desc " + table);

			while (resset.next()) {
				String field = resset.getString(1);
				String type = resset.getString(2);
				System.out.println(type + "  " + field);
				colNames.add(field);
				colTypes.add(type);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No connection");
		}
		TableData tableData = new TableData(colNames, colTypes);
	}

	public static void setData(String query) {
		Connection con = getConnection();
		Statement st;
		int qerlen = query.length();
		if (query.charAt(qerlen - 4) == ',') {
			query = query.substring(0, qerlen - 4) + query.substring(qerlen - 2);
			System.out.println(query);
		}
		boolean rs;
		try {
			st = con.createStatement();
			rs = st.execute(query);
			JOptionPane.showMessageDialog(null, "Insert successful");
		} catch (SQLException e) {

			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Check data to insert" + "\n " + query);
		}
	}

	public boolean isIn() {
		return in;
	}
}
