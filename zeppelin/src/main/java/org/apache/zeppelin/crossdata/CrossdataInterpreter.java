/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zeppelin.crossdata;

import akka.util.Timeout;
import com.stratio.crossdata.common.SQLCommand;
import com.stratio.crossdata.common.SQLResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.SuccessfulQueryResult;
import com.stratio.crossdata.driver.JavaDriver;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterPropertyBuilder;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class CrossdataInterpreter extends Interpreter {

    private JavaDriver driver;

    private static final String CROSSDATA_SEEDS_PROPERTY = "crossdata.seeds";
    private static final String CROSSDATA_DEFAULT_LIMIT = "crossdata.defaultLimit";
    private static final String CROSSDATA_RETRIES = "crossdata.retries";
    private static final String CROSSDATA_TIMEOUT_SEC = "crossdata.tiemoutSeconds";


    static {
        Interpreter.register(
                "crossdata",
                "crossdata",
                CrossdataInterpreter.class.getName(),
                new InterpreterPropertyBuilder()
                        .add(CROSSDATA_SEEDS_PROPERTY, "127.0.0.1:13420", "The list of seeds.")
                        .add(CROSSDATA_DEFAULT_LIMIT, "1000", "Default limit")
                                //.add(CROSSDATA_RETRIES, "1", "Number of retries.")
                        .add(CROSSDATA_TIMEOUT_SEC, "100", "Query tiemout in seconds").build());
    }

    public CrossdataInterpreter(Properties property) {
        super(property);
    }


    @Override
    public void open() {
        List<String> seeds = Arrays.asList(getProperty(CROSSDATA_SEEDS_PROPERTY).split(","));
        driver = new JavaDriver(seeds, false);
    }

    @Override
    public void close() {
        // TODO add JavaDriver close => driver.close()

    }


    @Override
    public InterpreterResult interpret(String sql, InterpreterContext context) {

        SQLResult sqlResult = driver.syncQuery(new SQLCommand(sql, UUID.randomUUID(), false),
                new Timeout(1000*Long.parseLong(getProperty(CROSSDATA_TIMEOUT_SEC))),
                1); //Integer.parseInt(getProperty(CROSSDATA_RETRIES))

        if (sqlResult.hasError() && ErrorResult.class.isInstance(sqlResult)) {
            return new InterpreterResult(InterpreterResult.Code.ERROR, ErrorResult.class.cast(sqlResult).message());
        } else if (SuccessfulQueryResult.class.isInstance(sqlResult)) {
            StructType schema = SuccessfulQueryResult.class.cast(sqlResult).schema();
            Row[] resultSet = sqlResult.resultSet();
            if (resultSet.length <= 0) {
                return new InterpreterResult(InterpreterResult.Code.SUCCESS, "%text EMPTY result");
            } else {
                return new InterpreterResult(InterpreterResult.Code.SUCCESS, resultToZeppelinMsg(resultSet, schema));
            }

        } else {
            return new InterpreterResult(InterpreterResult.Code.ERROR, "Unexpected result: " + sqlResult.toString());
        }

    }

    private String resultToZeppelinMsg(Row[] resultSet, StructType schema) {

        int defaultLimit = Integer.parseInt(getProperty(CROSSDATA_DEFAULT_LIMIT));

        StringBuilder msg = new StringBuilder();

        // Add columns names
        String resultsHeader = "";
        for (String colName : schema.fieldNames()) {
            if (resultsHeader.isEmpty()) {
                resultsHeader = colName;
            } else {
                resultsHeader += "\t" + colName;
            }
        }

        msg.append(resultsHeader).append(System.lineSeparator());


        //Add rows
        // ArrayType, BinaryType, BooleanType, ByteType, DecimalType, DoubleType, DynamicType,
        // FloatType, FractionalType, IntegerType, IntegralType, LongType, MapType, NativeType,
        // NullType, NumericType, ShortType, StringType, StructType

        int resultLength = Math.min(defaultLimit, resultSet.length);
        int numFields = schema.fieldNames().length;

        for (int r = 0; r < resultLength; r++) {
            Row row = resultSet[r];


            for (int i = 0; i < numFields; i++) {
                if (!row.isNullAt(i)) {
                    msg.append(row.apply(i).toString());
                } else {
                    msg.append("null");
                }
                if (i != numFields - 1) {
                    msg.append("\t");
                }
            }
            msg.append(System.lineSeparator());
        }

        if (resultSet.length > defaultLimit) {
            // TODO use default limit -> Improve driver API
            msg.append(System.lineSeparator()).append("<font color=red>Results are limited by ").append(defaultLimit).append(".</font>");
        }


        msg.append(System.lineSeparator());
        return "%table " + msg.toString();

    }

    @Override
    public void cancel(InterpreterContext context) {
        // TODO do nothing
    }

    @Override
    public FormType getFormType() {
        return FormType.SIMPLE;
    }

    @Override
    public int getProgress(InterpreterContext context) {
        return 0;
    }

    @Override
    public List<String> completion(String buf, int cursor) {
        return null;
    }

    @Override
    public Scheduler getScheduler() {
        return SchedulerFactory.singleton().createOrGetParallelScheduler("interpreter_" + this.hashCode(), 10);
    }
}
