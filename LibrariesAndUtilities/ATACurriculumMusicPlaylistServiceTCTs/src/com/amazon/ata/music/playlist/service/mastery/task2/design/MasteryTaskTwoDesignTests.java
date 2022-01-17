package com.amazon.ata.music.playlist.service.mastery.task2.design;

import com.amazon.ata.music.playlist.service.helpers.TctIntrospectionTest;

import com.amazonaws.services.atacurriculummusicplaylistservice.model.TctResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class MasteryTaskTwoDesignTests extends TctIntrospectionTest {

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
    }

    @Override
    protected String getTestSuiteId() {
        return "MT2-Design";
    }

    @Test(dataProvider = "TctResults")
    public void masteryTask2Design_runIntrospectionSuite_reportResults(TctResult result) {
        assertTrue(result.isPassed(), result.getErrorMessage() + "\n");
    }
}
