package no.nb.microservices.catalogdeliverytext.utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by andreasb on 03.09.15.
 */
public class ZipUtils {

    public static File zip(List<File> files, String path) throws IOException {
        File zipFile = new File(path + "/" + UUID.randomUUID().toString() + ".zip");
        FileOutputStream fos = new FileOutputStream(zipFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ZipOutputStream zos = new ZipOutputStream(bos);
        for (File file : files) {
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            zos.write(Files.readAllBytes(file.toPath()));
            zos.closeEntry();
        }
        zos.close();

        return zipFile;
    }
}
