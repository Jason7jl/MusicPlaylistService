package com.amazon.ata.music.playlist.service.preparedness.task3;

import com.amazon.ata.music.playlist.service.helpers.TctIntrospectionTest;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.TctResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class PreparednessTaskThreeIntrospectionTests extends TctIntrospectionTest {

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @Override
    protected String getTestSuiteId() {
        return "PPT03";
    }

    @Test(dataProvider = "TctResults")
    public void preparednessTask3_runIntrospectionSuite_reportResults(TctResult result) {
        assertTrue(result.isPassed(), result.getErrorMessage() + "\n");
    }
}
