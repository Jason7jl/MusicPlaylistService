package com.amazon.ata.music.playlist.service.mastery.task2;

import com.amazon.ata.music.playlist.service.helpers.TctIntrospectionTest;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.TctResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class MasteryTaskTwoIntrospectionTests extends TctIntrospectionTest {
    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @Override
    protected String getTestSuiteId() {
        return "MT02";
    }

    @Test(dataProvider = "TctResults")
    public void masteryTask2_runIntrospectionSuite_reportResults(TctResult result) {
        assertTrue(result.isPassed(), result.getErrorMessage() + "\n");
    }
}
