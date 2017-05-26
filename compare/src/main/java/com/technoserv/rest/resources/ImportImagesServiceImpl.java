package com.technoserv.rest.resources;

import com.technoserv.rest.model.StopListElement;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * Created by Andrey on 18.12.2016.
 */
@Service
public class ImportImagesServiceImpl implements ImportImagesService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_SSS");

    @Autowired
    private CompareResource compareResource;

    public void importImage(Long stopListId, InputStream uploadedInputStream, String fileName) {
        Path uploadedFileLocation = null;
        try {
            uploadedFileLocation = Files.createTempDirectory(DATE_FORMAT.format(new Date()) + "_");

            saveToFile(uploadedInputStream,
                    uploadedFileLocation.toAbsolutePath().toString() + File.separator + fileName);

            UploadImagesFileVisitor fileVisitor = new UploadImagesFileVisitor(
                    p -> {
                        String file = encodeFile(p);
                        if (file != null) {
                            StopListElement stopListElement = new StopListElement();
                            stopListElement.setPhoto(file);
                            compareResource.add(stopListId, stopListElement);
                            return true;
                        }
                        return false;
                    });

            Path path = Files.walkFileTree(uploadedFileLocation, fileVisitor);
            FileUtils.deleteDirectory(new File(uploadedFileLocation.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Map<String, Boolean>> importImageZip(Long stopListId, InputStream uploadedInputStream, String fileName) {
        Path uploadedFileLocation = null;
        try {
            uploadedFileLocation = Files.createTempDirectory(DATE_FORMAT.format(new Date()) + "_");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Boolean> unzipReport = unZip(uploadedInputStream, uploadedFileLocation);

        UploadImagesFileVisitor fileVisitor = new UploadImagesFileVisitor(
                p -> {
                    System.out.println("Upload file: " + p.toAbsolutePath().toString());
                    String file = encodeFile(p);
                    if (file != null) {
                        StopListElement stopListElement = new StopListElement();
                        stopListElement.setPhoto(file);
                        compareResource.add(stopListId, stopListElement);
                        return true;
                    }
                    return false;
                });
        try {
            Path path = Files.walkFileTree(uploadedFileLocation, fileVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.deleteDirectory(new File(uploadedFileLocation.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Boolean> uploadReport = fileVisitor.getUploadReport();
        Map<String, Map<String, Boolean>> report = new HashMap<>();
        report.put("unzipReport", unzipReport);
        report.put("uploadReport", uploadReport);
        return report;
    }

    private Map<String, Boolean> unZip(InputStream inputStream, Path uploadedFileLocation) {
        Map<String, Boolean> report = new HashMap<>();
        try (ZipInputStream zis = new ZipInputStream(inputStream, Charset.forName("CP866"));) {
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                boolean res = false;
                try {
                    if (ze.isDirectory()) {
                        Files.createDirectory(uploadedFileLocation.resolve(Paths.get(ze.getName())));
                        res = true;
                    } else {
                        String filePath = Paths.get(uploadedFileLocation.toAbsolutePath().toString(), ze.getName())
                                .toAbsolutePath().toString();
                        res = saveToFile(zis, filePath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    res = false;
                    return report;
                }
                report.put(ze.getName(), res);
                ze = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return report;
    }

    private boolean saveToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        try (OutputStream out = new FileOutputStream(uploadedFileLocation)) {
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        }
        return true;
    }

    public static class UploadImagesFileVisitor extends SimpleFileVisitor<Path> {
        Map<String, Boolean> uploadReport = new HashMap<>();

        private Function<Path, Boolean> function;

        public UploadImagesFileVisitor(Function<Path, Boolean> function) {
            this.function = function;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            boolean res = function.apply(file);
            uploadReport.put(file.getFileName().toString(), res);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            uploadReport.put(file.getFileName().toString(), false);
            return CONTINUE;
        }

        public Map<String, Boolean> getUploadReport() {
            return uploadReport;
        }
    }

    private String encodeFile(Path path) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
            return bytes != null ? new String(Base64.getEncoder().encode(bytes)) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}