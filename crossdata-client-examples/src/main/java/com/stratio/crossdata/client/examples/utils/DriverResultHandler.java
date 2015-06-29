package com.stratio.crossdata.client.examples.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.result.IDriverResultHandler;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.result.Result;

public class DriverResultHandler implements IDriverResultHandler {

	private int num_results = 0;
	private String queryID = null;
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());

	public DriverResultHandler() {
		this.num_results = 0;
		this.queryID = null;
	}

	public int getNumResults() {
		return this.num_results;
	}

	public void setNumResults() {
		this.num_results = this.num_results + 1;
	}

	public String getQueryID() {
		return this.queryID;
	}

	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}

	@Override
	public void processAck(String queryId, QueryStatus status) {

	}

	@Override
	public void processError(Result errorResult) {
		logger.error("");
		logger.error("ERROR ASYNC RESULT: " + errorResult.toString());
		logger.error("");
	}

	@Override
	public void processResult(Result result) {
		logger.info("PROCESS ASYNC RESULT");
		setQueryID(result.getQueryId());
		stringResult(((QueryResult) result).getResultSet());
	}

	public static String stringResult(
			com.stratio.crossdata.common.data.ResultSet ResultSet) {
		if (ResultSet.isEmpty()) {
			return "OK";
		}

		com.stratio.crossdata.common.data.ResultSet resultSet = ResultSet;

		Map<String, Integer> colWidths = calculateColWidths(resultSet);

		String bar = StringUtils.repeat('-', getTotalWidth(colWidths)
				+ (colWidths.values().size() * 3) + 1);

		StringBuilder sb = new StringBuilder(System.lineSeparator());
		sb.append(bar).append(System.lineSeparator());
		boolean firstRow = true;
		for (com.stratio.crossdata.common.data.Row row : resultSet) {
			sb.append("| ");

			if (firstRow) {
				for (ColumnMetadata columnMetadata : resultSet
						.getColumnMetadata()) {
					sb.append(
							StringUtils.rightPad(columnMetadata.getName()
									.getColumnNameToShow(), colWidths
									.get(columnMetadata.getName()
											.getColumnNameToShow()) + 1))
							.append("| ");
				}
				sb.append(System.lineSeparator());
				sb.append(bar);
				sb.append(System.lineSeparator());
				sb.append("| ");
				firstRow = false;
			}
			for (Map.Entry<String, Cell> entry : row.getCells().entrySet()) {
				String str = String.valueOf(entry.getValue().getValue());
				sb.append(StringUtils.rightPad(str,
						colWidths.get(entry.getKey())));
				sb.append(" | ");
			}
			sb.append(System.lineSeparator());
		}
		sb.append(bar).append(System.lineSeparator());
		return sb.toString();
	}

	private static Map<String, Integer> calculateColWidths(
			com.stratio.crossdata.common.data.ResultSet resultSet) {
		Map<String, Integer> colWidths = new HashMap<>();
		// Get column names
		com.stratio.crossdata.common.data.Row firstRow = resultSet.iterator()
				.next();
		for (String key : firstRow.getCells().keySet()) {
			colWidths.put(key, key.length());
		}
		// Find widest cell content of every column
		for (com.stratio.crossdata.common.data.Row row : resultSet) {
			for (String key : row.getCells().keySet()) {
				String cellContent = String
						.valueOf(row.getCell(key).getValue());
				int currentWidth = colWidths.get(key);
				if (cellContent.length() > currentWidth) {
					colWidths.put(key, cellContent.length());
				}
			}
		}
		return colWidths;
	}

	private static int getTotalWidth(Map<String, Integer> colWidths) {
		int totalWidth = 0;
		for (int width : colWidths.values()) {
			totalWidth += width;
		}
		return totalWidth;
	}

}
