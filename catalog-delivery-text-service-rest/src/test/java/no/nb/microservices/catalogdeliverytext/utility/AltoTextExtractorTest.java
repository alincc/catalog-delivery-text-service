package no.nb.microservices.catalogdeliverytext.utility;

import no.nb.microservices.catalogdeliverytext.model.alto.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AltoTextExtractorTest {

    @Test
    public void testExtractText() throws Exception {
        Alto alto = new Alto();
        Alto.Layout layout = new Alto.Layout();
        Alto.Layout.Page page = new Alto.Layout.Page();
        PageSpaceType printSpace = new PageSpaceType();
        ComposedBlockType composedBlockType = new ComposedBlockType();
        TextBlockType textBlockType = new TextBlockType();
        TextBlockType.TextLine textLine = new TextBlockType.TextLine();

        StringType s1 = new StringType();
        StringType s2 = new StringType();
        StringType s3 = new StringType();
        StringType s4 = new StringType();
        StringType s5 = new StringType();
        StringType s6 = new StringType();
        s1.setCONTENT("Kaptain");
        s2.setCONTENT("Sabeltann");
        s3.setCONTENT("er");
        s4.setCONTENT("en");
        s5.setCONTENT("farlig");
        s6.setCONTENT("mann");
        textLine.getStringAndSP().add(s1);
        textLine.getStringAndSP().add(s2);
        textLine.getStringAndSP().add(s3);
        textLine.getStringAndSP().add(s4);
        textLine.getStringAndSP().add(s5);
        textLine.getStringAndSP().add(s6);

        textBlockType.getTextLine().add(textLine);
        composedBlockType.getTextBlockOrIllustrationOrGraphicalElement().add(textBlockType);
        printSpace.getTextBlockOrIllustrationOrGraphicalElement().add(composedBlockType);
        page.setPrintSpace(printSpace);
        layout.getPage().add(page);
        alto.setLayout(layout);

        String s = AltoTextExtractor.extractText(alto);
        assertNotNull(s);
        assertEquals("Kaptain Sabeltann er en farlig mann", s);
    }
}
