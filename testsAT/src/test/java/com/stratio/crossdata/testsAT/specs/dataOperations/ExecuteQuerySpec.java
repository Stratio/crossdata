package com.stratio.crossdata.testsAT.specs.dataOperations;

import static com.stratio.tests.utils.ResultSetMatcher.EqualsRecordSet;
import static com.stratio.tests.utils.ContainsRowsMatcher.ContainsRows;
import static com.stratio.tests.utils.EqualsResultSet.EqualsResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.sql.SQLException;

import org.testng.Assert;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.specs.BaseSpec;
import com.stratio.crossdata.specs.Common;
import com.stratio.tests.utils.DriverResultHandler;
import com.stratio.tests.utils.ThreadProperty;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ExecuteQuerySpec extends BaseSpec {

	public DriverResultHandler driverResultHandler;

	public ExecuteQuerySpec(Common spec) {
		this.commonspec = spec;
	}

	@When(value = "^I execute a query: '(.*?)'$", timeout = 30000)
	public void execute_simple_query(String statement) {
		commonspec.getExceptions().clear();

		if (ThreadProperty.get("ExecutionType").equals("Crossdata")) {
			commonspec.getLogger().info("Query : " + statement);
			try {
				commonspec.getMetaDriver().executeQuery(statement);
			} catch (ParsingException | ValidationException
					| ExecutionException | UnsupportedException
					| ConnectionException e) {
				commonspec.getLogger().error(e.toString());
				commonspec.getExceptions().add(e);
			}
		} else {
			commonspec.getLogger().info("Executin query over JDBC");
			try {
				commonspec.getJDBCDriver().executeQueryJDBC(
						commonspec.getJDBCDriver().getConnectionJDBC(),
						statement);
			} catch (SQLException e) {
				commonspec.getLogger().error(e.toString());
				commonspec.getExceptions().add(e);
			}
		}
	}

	@Then("^the result has to be:$")
	public void assertResultSetAreEquals(DataTable tab) {
		if (ThreadProperty.get("ExecutionType").equals("Crossdata")) {
			QueryResult queryResult = (QueryResult) commonspec.getMetaDriver()
					.getResult();
			assertThat("The result expected is different",
					queryResult.getResultSet(), EqualsRecordSet(tab));
		} else {
			assertThat("The JDBC result expected is different", commonspec
					.getJDBCDriver().getResult(), EqualsRecordSet(tab));
		}
	}

	@Then("^the result has to be contained:$")
	public void assertContainedResult(DataTable tab) {
		if (ThreadProperty.get("ExecutionType").equals("Crossdata")) {
			QueryResult queryResult = (QueryResult) commonspec.getMetaDriver()
					.getResult();
			assertThat("The result expected is different",
					queryResult.getResultSet(), ContainsRows(tab));
		} else {
			assertThat("The JDBC result expected is different", commonspec
					.getJDBCDriver().getResult(), ContainsRows(tab));
		}
	}

	@Then("^the result has to be exactly:$")
	public void assertEqualsResult(DataTable tab) {
		if (ThreadProperty.get("ExecutionType").equals("Crossdata")) {
			QueryResult queryResult = (QueryResult) commonspec.getMetaDriver()
					.getResult();
			assertThat("The result expected is different",
					queryResult.getResultSet(), EqualsResult(tab));
		} else {
			assertThat("The JDBC result expected is different", commonspec
					.getJDBCDriver().getResult(), EqualsResult(tab));
		}
	}

	@Then("^the result has not errors: '(.*?)'$")
	public void assert_result(String result_expected) {
		Boolean expected = Boolean.valueOf(result_expected);
		commonspec.getLogger().info(
				"Checking the result of adding new connector");
		assertThat("An exception exists", commonspec.getExceptions(),
				hasSize(equalTo(0)));
		if (ThreadProperty.get("ExecutionType").equals("Crossdata")) {
			Assert.assertTrue(
					Boolean.valueOf(expected).equals(
							commonspec.getMetaDriver().getResult().hasError()),
					"An error exists");
		}
		commonspec.getExceptions().clear();
	}

	@Then("^the table '(.*?)' has to exists: '(.*?)'")
	public void checkIfATableExists(String tableName, String value) {
		String[] table = tableName.split("[.]");
		if (ThreadProperty.get("ExecutionType").equals("Crossdata")) {

			assertThat("The table does" + table[1]
					+ "not exists in the catalog" + table[0], commonspec
					.getMetaDriver().existsTable(table[0], table[1]),
					equalTo(Boolean.valueOf(value)));
		} else {
			assertThat("The table does" + table[1]
					+ "not exists in the catalog" + table[0], commonspec
					.getJDBCDriver().existsTable(table[0], table[1]),
					equalTo(Boolean.valueOf(value)));
		}
	}

	@Then("^the column '(.*?)' of type '(.*?)' has to exists: '(.*?)'$")
	public void check_if_a_column_exists(String column, String type,
			String value) {
		String[] column_aux = column.split("[.]");
		boolean aux = Boolean.valueOf(value);
		if (ThreadProperty.get("ExecutionType").equals("Crossdata")) {
			assertThat(
					"The column " + column + "not exists",
					aux,
					equalTo(commonspec.getMetaDriver().existsColumn(
							column_aux[0], column_aux[1], column_aux[2], type)));
		} else {
			assertThat(
					"The column " + column + "not exists",
					Boolean.valueOf(value),
					equalTo(commonspec.getJDBCDriver().existsColumn(
							column_aux[0], column_aux[1], column_aux[2])));
		}
	}

	@Then("^the number of rows of the table '(.*?)' has to be: '(.*?)'")
	public void checkNumberOfRows(String table, String number) {
		String[] tableName = table.split("[.]");
		if (ThreadProperty.get("ExecutionType").equals("Crossdata")) {
			assertThat(
					"The number of rows of the table " + table
							+ "are not equals (" + number + ")",
					Integer.parseInt(number),
					equalTo(commonspec.getMetaDriver().getNumberOfRows(
							tableName[0], tableName[1])));
		} else {
			assertThat("The number of rows of the table " + table
					+ "are not equals (" + number + ")",
					Integer.parseInt(number), equalTo(commonspec
							.getJDBCDriver()
							.getRows(tableName[0], tableName[1])));
		}
	}

	@When(value = "^I execute a async query: '(.*?)'$", timeout = 30000)
	public void execute_async_query(String statement) {
		driverResultHandler = new DriverResultHandler(this.commonspec);
		commonspec.getExceptions().clear();
		commonspec.getMetaDriver().clearAsyncResults();
		commonspec.getLogger().info("Query : " + statement);
		try {
			commonspec.getMetaDriver().executeAsyng(statement,
					driverResultHandler);
		} catch (ParsingException e) {
			commonspec.getLogger().error(e.toString());
			commonspec.getExceptions().add(e);
		}

	}

	@When(value = "^I have to recieve '(.*?)' results", timeout = 30000)
	public void checkAsyncResult(String pag_num) {
		int pag = Integer.parseInt(pag_num);
		commonspec.getLogger().info("Comienza el while");

		while (commonspec.getMetaDriver().getAsyncResults().size() < pag) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		commonspec.getLogger().info("Finaliza el while");

		assertThat("The number of pages are not equals", pag,
				equalTo(commonspec.getMetaDriver().getAsyncResults().size()));
		for (int i = 0; i < commonspec.getMetaDriver().getAsyncResults().size(); i++) {
			int pag_number = ((QueryResult) commonspec.getMetaDriver()
					.getAsyncResults().get(i)).getResultPage();
			assertThat("The last result is not the last result", pag_number,
					equalTo(i));
			assertThat("The query id of the results are diferent", commonspec
					.getMetaDriver().getAsyncResults().get(0).getQueryId(),
					equalTo(commonspec.getMetaDriver().getAsyncResults().get(i)
							.getQueryId()));
		}
		boolean last = ((QueryResult) commonspec.getMetaDriver()
				.getAsyncResults()
				.get(commonspec.getMetaDriver().getAsyncResults().size() - 1))
				.isLastResultSet();
		// commonspec.getMetaDriver().removeResultsHandler(commonspec.getMetaDriver().getAsyncResults().get(0).getQueryId());
		assertThat("The last result is not the last result", last,
				equalTo(true));

	}
}
