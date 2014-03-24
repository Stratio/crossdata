package com.stratio.meta.core.metadata;

import com.datastax.driver.core.ColumnMetadata;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.structures.IndexType;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


public class LuceneIndexHelperTest extends BasicCoreCassandraTest {

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    }

    @Test
    public void processLuceneOptions(){
        String options = "{\"schema\":\"{default_analyzer:\\\"org.apache.lucene.analysis.standard.StandardAnalyzer\\\",fields:{name:{type:\\\"string\\\"}, gender:{type:\\\"string\\\"}, email:{type:\\\"string\\\"}, age:{type:\\\"integer\\\"}, bool:{type:\\\"boolean\\\"}, phrase:{type:\\\"text\\\", analyzer:\\\"org.apache.lucene.analysis.en.EnglishAnalyzer\\\"}}}\",\"refresh_seconds\":\"1\",\"class_name\":\"org.apache.cassandra.db.index.stratio.RowIndex\"}";

        int numColumns = 6;
        LuceneIndexHelper lih = new LuceneIndexHelper(_session);
        Map<String, List<CustomIndexMetadata>> indexes = lih.processLuceneOptions(null, options);
        assertEquals("Invalid number of indexes", numColumns, indexes.size());

        for(Map.Entry<String, List<CustomIndexMetadata>> entry : indexes.entrySet()){
            assertEquals("Column has several indexes", 1, entry.getValue().size());
            Assert.assertEquals("Invalid type of index", IndexType.LUCENE, entry.getValue().get(0).getIndexType());
        }
    }

    @Test
    public void processLuceneFields(){

        String options = "{name:{type:\"string\"}, gender:{type:\"string\"}, email:{type:\"string\"}, age:{type:\"integer\"}, bool:{type:\"boolean\"}, phrase:{type:\"text\", analyzer:\"org.apache.lucene.analysis.en.EnglishAnalyzer\"}}";
        int numColumns = 6;

        LuceneIndexHelper lih = new LuceneIndexHelper(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JsonFactory factory = mapper.getJsonFactory();
        JsonParser jp = null;
        JsonNode root = null;
        try {
            jp = factory.createJsonParser(options);
            root = mapper.readTree(jp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jp != null) {
                try {
                    jp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        assertNotNull("Cannot index options.", root);

        Map<String, List<CustomIndexMetadata>> indexes = lih.processLuceneFields(null, root);
        assertNotNull("Cannot retrieve mapped columns", indexes);
        assertEquals("Invalid number of indexes", numColumns, indexes.size());

        for(Map.Entry<String, List<CustomIndexMetadata>> entry : indexes.entrySet()){
            assertEquals("Column has several indexes", 1, entry.getValue().size());
            Assert.assertEquals("Invalid type of index", IndexType.LUCENE, entry.getValue().get(0).getIndexType());
        }
    }

    @Test
    public void getIndexedColumns(){
        String keyspace = "demo";
        String table = "users";
        String column = "lucene_index_1";
        int numIndexedColumns = 6;

        ColumnMetadata cm = _session.getCluster().getMetadata()
                .getKeyspace(keyspace)
                .getTable(table)
                .getColumn(column);
        assertNotNull("Cannot retrieve test column", cm);
        LuceneIndexHelper lih = new LuceneIndexHelper(_session);
        Map<String, List<CustomIndexMetadata>> indexedColumns = lih.getIndexedColumns(cm);
        assertEquals("Invalid number of indexes", numIndexedColumns, indexedColumns.size());
        for(Map.Entry<String, List<CustomIndexMetadata>> entry : indexedColumns.entrySet()){
            System.out.println(entry.getKey() + " - " + entry.getValue().size());
            for(CustomIndexMetadata cim : entry.getValue()){
                System.out.println("-> " + cim.getIndexType() + " - " + cim.getIndexOptions());
            }
        }

    }

}
