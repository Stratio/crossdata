package com.stratio.qa.utils;

import java.util.List;

import org.assertj.core.api.AbstractAssert;

import com.stratio.crossdata.driver.metadata.FieldMetadata;
import cucumber.api.DataTable;

/**
 * Created by hdominguez on 19/10/15.
 */
public class FlattenerMetadataAssert extends AbstractAssert<FlattenerMetadataAssert, List<FieldMetadata>>{

    /**
     * Generic constructor.
     *
     * @param actual
     */
    public FlattenerMetadataAssert( List<FieldMetadata> actual) {
        super(actual, FlattenerMetadataAssert.class);
    }

    /**
     * Checks the "DataFrame".
     *
     * @param actual
     * @return DataFrameAssert
     */
    public static FlattenerMetadataAssert asserThat( List<FieldMetadata> actual){
        return new FlattenerMetadataAssert(actual);
    }

    public FlattenerMetadataAssert hasLength(int length){
        if(actual.size() != length){
            failWithMessage("Expected number of columns to be <%s> but was <%s>", length, actual.size());
        }
        return this;
    }

    public FlattenerMetadataAssert checkMetadata(DataTable table){
            for (int i = 0; i < actual.size(); i++) {
                FieldMetadata field = actual.get(i);
                if (!field.name().equals(table.raw().get(i).get(0))){
                    failWithMessage("Expected column name to be <%s> but was <%s>", table.raw().get(i).get(0), field
                            .name());
                }
                if(!field._type().typeName().equals(table.raw().get(i).get(1))){
                    failWithMessage("Expected type for column <%s> to be <%s> but was <%s>", field.name(), table.raw().get(i).get
                            (1), field._type().typeName());
                }
        }
        return this;
    }

}
