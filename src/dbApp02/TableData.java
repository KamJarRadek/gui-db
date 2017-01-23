package dbApp02;

import java.util.ArrayList;

public class TableData {

	private static ArrayList<String> colNames; 
	private static ArrayList<String> colTypes; 
	private static String [] tablesNames = {"author", "books", "customer", "item", "rentals"};
	private static String [][] records;
	
	public static int noOfCol(){
		return colNames.size();
	}
	public static String[][] getRecords() {
		return records;
	}
	public static void setRecords(String[][] records) {
		TableData.records = records;
	}
	public static String[] getTablesNames() {
		return tablesNames;
	}
	public  TableData(ArrayList<String> colName, ArrayList<String> colType){
		colNames = colName;
		colTypes = colType;
	}
	public static ArrayList<String> getColNames() {
		return colNames;
	}
	public static void setColNames(ArrayList<String> colNames) {
		TableData.colNames = colNames;
	}
	public static ArrayList<String> getColTypes() {
		return colTypes;
	}
	public static void setColTypes(ArrayList<String> colTypes) {
		TableData.colTypes = colTypes;
	}
	
}
