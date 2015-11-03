package com.stratio.crossdata.testsAT.specs.utils;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

public class OperationsUtils {
	public OperationsUtils() {

	}

	private boolean supporInsert = false;

	public ArrayList<String> getFeatures(NodeList node) {
		ArrayList<String> tests = new ArrayList<String>();
	
		for (int i = 0; i < node.getLength(); i++) {
			switch (node.item(i).getTextContent()) {
			case "SELECT_OPERATOR":
				tests.add("/SelectQueries/BasicSelect");
				break;
//			case "PAGINATION":
//				tests.add("/SelectQueries/Pagination");
//				break;
//			case "SELECT_LIMIT":
//				tests.add("/SelectQueries/SelectLimit");
//				break;
//			case "SELECT_ORDER_BY":
//				tests.add("/SelectQueries/SelectOrderBy");
//				break;
//			case "FILTER_PK_EQ":
//				tests.add("/SelectQueries/Filter_PK_EQ");
//				break;
//			case "FILTER_INDEXED_EQ":
//				tests.add("/SelectQueries/Filter_INDEXDED_EQ");
//				break;
//			case "FILTER_INDEXED_MATCH":
//				tests.add("/SelectQueries/Filter_INDEXED_MATCH");
//				break;
//			case "FILTER_PK_MATCH":
//				tests.add("/SelectQueries/Filter_PK_MATCH");
//				break;
//			case "FILTER_PK_GT":
//				tests.add("/SelectQueries/Filter_PK_GT");
//				break;
//			case "FILTER_NON_INDEXED_EQ":
//				tests.add("/SelectQueries/Filter_NON_INDEXED_EQ");
//				break;
//			case "FILTER_NON_INDEXED_GT":
//				tests.add("/SelectQueries/Filter_NON_INDEXED_GT");
//				break;
//			case "FILTER_NON_INDEXED_GET":
//				tests.add("/SelectQueries/Filter_NON_INDEXED_GET");
//				break;
//			case "FILTER_NON_INDEXED_LT":
//				tests.add("/SelectQueries/Filter_NON_INDEXED_LT");
//				break;
//			case "FILTER_NON_INDEXED_LET":
//				tests.add("/SelectQueries/Filter_NON_INDEXED_LET");
//				break;
//			// case "CREATE_INDEX":
//			// tests.add("/Index/CreateIndex");
//			// break;
//			// case "DROP_INDEX":
//			// tests.add("/Index/DropIndex");
//			// break;
//			case "CREATE_TABLE":
//				tests.add("/Tables/CreateTable");
//				break;
//			case "DROP_TABLE":
//				tests.add("/Tables/DropTable");
//				break;
//			case "ALTER_TABLE":
//				tests.add("/Tables/AlterTable");
//				break;
//			case "DELETE_PK_EQ":
//				tests.add("/DeleteQueries/Delete_PK_EQ");
//				break;
//			case "TRUNCATE_TABLE":
//				tests.add("/DeleteQueries/TruncateTable");
//				break;
//			case "INSERT_IF_NOT_EXISTS":
//				tests.add("/Tables/InsertIntoIfNotExists");
//				break;
//			case "INSERT":
//				tests.add("/Tables/InsertInto");
//				supporInsert = true;
//				break;
//			case "SELECT_INNER_JOIN":
//				tests.add("/SelectQueries/SelectInnerJoin");
//				break;
			}
		}
//		if (supporInsert) {
//			tests.addAll(getInsertIntoFromSelect(node));
//			return tests;
//		}
//		tests.addAll(getFunctions(node));
		return tests;
	}

	private ArrayList<String> getInsertIntoFromSelect(NodeList node) {
		ArrayList<String> tests = new ArrayList<String>();
		for (int i = 0; i < node.getLength(); i++) {
			switch (node.item(i).getTextContent()) {
			case "SELECT_OPERATOR":
				tests.add("Tables/InsertIntoFromSelect");
				break;
			case "FILTER_PK_EQ":
				tests.add("Tables/InsertIntoFromSelectFilterPKEQ");
				break;
			case "FILTER_PK_GT":
				tests.add("Tables/InsertIntoFromSelectFilterPKGT");
				break;
			case "FILTER_PK_LT":
				tests.add("Tables/InsertIntoFromSelectFilterPKLT");
				break;
			case "FILTER_PK_LET":
				tests.add("Tables/InsertIntoFromSelectFilterPKLET");
				break;
			case "FILTER_PK_GET":
				tests.add("Tables/InsertIntoFromSelectFilterPKGET");
				break;
			case "FILTER_PK_MATCH":
				tests.add("Tables/InsertIntoFromSelectFilterPKMATCH");
				break;
			}
		}
		return tests;
	}

	public ArrayList<String> getFunctions(NodeList node) {
		ArrayList<String> tests = new ArrayList<String>();
		for (int i = 0; i < node.getLength(); i++) {
			switch (node.item(i).getTextContent()) {
			case "concat":
				tests.add("/Functions/Concat");
				break;
			case "count":
				tests.add("/Functions/Count");
				break;
			case "now":
				tests.add("/Functions/Now");
				break;
			case "range":
				tests.add("/Functions/Range");
				break;
			}
		}
		return tests;
	}

}
