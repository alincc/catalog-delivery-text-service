package no.nb.microservices.catalogdeliverytext.utility;

import no.nb.microservices.catalogdeliverytext.model.alto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AltoTextExtractor {

    private AltoTextExtractor() {}

    public static String extractText(Alto alto) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Alto.Layout.Page page : alto.getLayout().getPage()) {
            List<TextBlockType> textBlocks = getTextBlocks(page.getPrintSpace().getTextBlockOrIllustrationOrGraphicalElement());
            List<TextBlockType.TextLine> textLines = getTextLines(textBlocks);
            for (TextBlockType.TextLine textLine : textLines) {
                stringBuilder.append(getTextFromTextLine(textLine));
                stringBuilder.append(System.lineSeparator());
            }
        }
        return stringBuilder.toString().trim();
    }

    private static List<TextBlockType> getTextBlocks(List<BlockType> blocks) {
        List<TextBlockType> textBlocks = new ArrayList<>();
        for (BlockType blockType : blocks) {
            if (blockType instanceof ComposedBlockType) {
                ComposedBlockType composedBlock = (ComposedBlockType) blockType;
                textBlocks = composedBlock.getTextBlockOrIllustrationOrGraphicalElement().stream()
                        .filter(block -> block instanceof TextBlockType)
                        .map(block -> (TextBlockType) block).collect(Collectors.toList());
            }
        }
        return textBlocks;
    }

    private static List<TextBlockType.TextLine> getTextLines(List<TextBlockType> textBlocks) {
        List<TextBlockType.TextLine> textLines = new ArrayList<>();
        for (TextBlockType textBlock : textBlocks) {
            textLines.addAll(textBlock.getTextLine().stream().collect(Collectors.toList()));
        }
        return textLines;
    }

    private static String getTextFromTextLine(TextBlockType.TextLine textLine) {
        StringBuilder stringBuilder = new StringBuilder();
        List<StringType> stringTypes = textLine.getStringAndSP().stream()
                .filter(o -> o instanceof StringType)
                .map(o -> (StringType) o).collect(Collectors.toList());
        for (StringType stringType : stringTypes) {
            stringBuilder.append(stringType.getCONTENT());
            stringBuilder.append(" ");
        }
        stringBuilder.trimToSize();
        return stringBuilder.toString();

    }
}
