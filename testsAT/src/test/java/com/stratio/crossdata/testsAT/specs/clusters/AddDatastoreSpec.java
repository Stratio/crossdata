package com.stratio.crossdata.testsAT.specs.clusters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.stratio.crossdata.common.exceptions.ApiException;
import com.stratio.crossdata.common.exceptions.validation.ExistNameException;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.testsAT.specs.BaseSpec;
import com.stratio.crossdata.testsAT.specs.Common;
import com.stratio.tests.utils.ThreadProperty;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AddDatastoreSpec extends BaseSpec {

    Result result;

    public AddDatastoreSpec(Common spec) {
        this.commonspec = spec;
    }

    @Given("^I have to execute the reset metadata query$")
    public void executeResetMetadata() {
        commonspec.getLogger().info("Executing the RESET METADATA query");
        commonspec.getExceptions().clear();
        result = commonspec.getMetaDriver().resetMetadata();
        Assert.assertTrue(Boolean.valueOf(false).equals(result.hasError()),"Error adding a new connector");
        //assertThat("The result is different from expected", result.getResult().toString(), equalTo("Crossdata server reset."));
   }

    @When(value="^I execute a ADD_DATASTORE query with this options: '(.*?)'$", timeout=30000)
    public void executeAddDatastoreQuery(String datastorePath) {
        ThreadProperty.set("datastorePath", datastorePath);
        commonspec.getLogger().info("Clearing exception list");
        commonspec.getExceptions().clear();
        URL url = AddDatastoreSpec.class.getResource("/manifest/" + datastorePath);
        commonspec.getLogger().info("Adding new datastore xml file : " + datastorePath);
        try {
            result = commonspec.getMetaDriver().addDatastore(url.getPath());

        } catch (FileNotFoundException | JAXBException | ExistNameException | ApiException e) {
            commonspec.getExceptions().add(e);
            commonspec.getLogger().error(e.getMessage());
        }
    }
    
    @Then("^The result of add new datastore query is '(.*?)'$")
    public void checkResultOfAddingDatastore(String expected_result){
        Boolean expected = Boolean.valueOf(expected_result);
        commonspec.getLogger().info("Checking the result of adding new datastore");
        assertThat("An exception exists", commonspec.getExceptions(),hasSize(equalTo(0)));
        Assert.assertTrue(Boolean.valueOf(expected).equals(result.hasError()),"Error adding a new datastore");
        commonspec.getExceptions().clear();
    }
    
    @When(value="^I want to know the description of a datastore '(.*?)'", timeout=30000)
    public void executeDescribeDatastore(String datastoreName){
        commonspec.getLogger().info("Executing a describe datastore query");
        commonspec.getMetaDriver().describeDatastore(datastoreName);
    }
    @Then("^I expect to recieve the string: '(.*?)'")
    public void checkResultOfDescribe(String expectedResult){
        CommandResult result = (CommandResult)commonspec.getMetaDriver().getResult();
        String expected_result = result.getResult().toString().toLowerCase();
        commonspec.getLogger().info("Expected result: " + expected_result);
        URL url = AddDatastoreSpec.class.getResource("/manifest/" + expectedResult);
        commonspec.getLogger().info("Adding new datastore xml file : " + expectedResult);
        File file = new File(url.getPath());
        try {
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            d.getDocumentElement().normalize();
            NodeList nodeList = d.getElementsByTagName("*");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String datastoreProperty =  node.getFirstChild().getNodeValue();
                if(!datastoreProperty.matches("\n.*")){
                    assertThat("The text "+ datastoreProperty.toLowerCase() + " is not contained in the describe result" , expected_result, containsString(datastoreProperty.toLowerCase()));
                }
//                String aux_2 = node.getFirstChild().getNodeValue();
//                System.out.println(aux_2);
             
               
            }
            
        } catch (SAXException | IOException | ParserConfigurationException e) {
               Assert.fail();
        }
    }

}
