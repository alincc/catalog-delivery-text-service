package no.nb.microservices.catalogdeliverytext.utility;

import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ZipUtilsTest {

    @Test
    public void zipTest() throws Exception {
        File f1 = new File(Paths.get(getClass().getResource("/alto/urn:nbn:no-nb_digibok_2014062307158/digibok_2014062307158_0001.xml").toURI()).toString());
        File f2 = new File(Paths.get(getClass().getResource("/alto/urn:nbn:no-nb_digibok_2014062307158/digibok_2014062307158_0002.xml").toURI()).toString());
        List<File> files = Arrays.asList(f1, f2);

        File zippedFile = ZipUtils.zip(files, "target");
        assertNotNull(zippedFile);

        ZipFile zipFile = new ZipFile(zippedFile.getAbsolutePath());
        assertEquals(2, zipFile.size());

        zipFile.close();

    }

    @Test
    public void zipTextTest() throws Exception {
        String text = "Bananer i pyjamas";

        File zippedFile = ZipUtils.zipText(text, "target");
        assertNotNull(zippedFile);

        ZipFile zipFile = new ZipFile(zippedFile.getAbsolutePath());
        assertEquals(1, zipFile.size());

        zipFile.close();
    }
}
