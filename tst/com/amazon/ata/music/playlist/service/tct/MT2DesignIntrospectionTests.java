package com.amazon.ata.music.playlist.service.tct;

import com.amazon.ata.test.assertions.PlantUmlClassDiagramAssertions;
import com.amazon.ata.test.helper.AtaTestHelper;
import com.amazon.ata.test.helper.PlantUmlClassDiagramHelper;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static com.amazon.ata.test.helper.PlantUmlClassDiagramHelper.classDiagramIncludesExtendsRelationship;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Tag("MT2-Design")
public class MT2DesignIntrospectionTests {
    private static final String CLASS_DIAGRAM_PATH = "mastery-task1-music-playlist-CD.puml";

    private String content;

    @BeforeEach
    public void setup() {
        content = AtaTestHelper.getFileContentFromResources(CLASS_DIAGRAM_PATH);
    }

    @ParameterizedTest
    @ValueSource(strings = {"InvalidAttributeChangeException", "AlbumTrackNotFoundException",
            "InvalidAttributeValueException", "PlaylistNotFoundException"})
    void mt2Design_getClassDiagram_containsExceptionClasses(String packagingClass) {
        PlantUmlClassDiagramAssertions.assertClassDiagramContainsClass(content, packagingClass);
    }

    @Test
    public void mt2Design_getClassDiagram_containsExpectedExceptionHierarchy() {
        String invalidValueException = "InvalidAttributeValueException";
        String invalidChangeException = "InvalidAttributeChangeException";

        Set<String> relatedTypesToValueException = PlantUmlClassDiagramHelper.getClassDiagramRelatedTypes(content,
                invalidValueException);
        Set<String> relatedTypesToChangeException = PlantUmlClassDiagramHelper.getClassDiagramRelatedTypes(content,
                invalidChangeException);

        Set<String> commonRelatedTypes = Sets.intersection(relatedTypesToValueException, relatedTypesToChangeException);

        assertFalse(commonRelatedTypes.isEmpty(),
                String.format("Expected %s and %s to have at least one related class in common.",
                        invalidChangeException, invalidValueException));

        int numberOfCommonParentClasses = 0;
        for (String relatedType : commonRelatedTypes) {
            if (classDiagramIncludesExtendsRelationship(content, invalidChangeException, relatedType) &&
                    classDiagramIncludesExtendsRelationship(content, invalidValueException, relatedType)) {
                numberOfCommonParentClasses++;
            }
        }
        assertEquals(1, numberOfCommonParentClasses,
                String.format("Expected %s and %s to have a common parent class.",
                        invalidChangeException, invalidValueException));
    }
}
