package com.stratio.meta.core.validator;

import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.grammar.ParsingTest;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.statements.MetaStatement;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class BasicValidatorTest extends BasicCoreCassandraTest {

    protected static MetadataManager _metadataManager = null;

    protected static final ParsingTest _pt = new ParsingTest();

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
        _metadataManager = new MetadataManager(_session);
        _metadataManager.loadMetadata();
    }

    public void validateOk(String inputText, String methodName){
        MetaStatement stmt = _pt.testRegularStatement(inputText, methodName);
        MetaResult result = stmt.validate(_metadataManager, "");
        assertNotNull(result, "Sentence validation not supported - " + methodName);
        assertFalse(result.hasError(), "Cannot validate sentence - " + methodName + ": " + result.getErrorMessage());
    }

    public void validateFail(String inputText, String methodName){
        MetaStatement stmt = _pt.testRegularStatement(inputText, methodName);
        MetaResult result = stmt.validate(_metadataManager, "");
        assertNotNull(result, "Sentence validation not supported - " + methodName);
        assertTrue(result.hasError(), "Cannot validate sentence - " + methodName + ": " + result.getErrorMessage());
    }
}
