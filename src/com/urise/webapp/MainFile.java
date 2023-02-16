package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class MainFile {
    public static void main(String[] args) {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File("./src/ru/javawebinar/basejava");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File catalog = new File("D:\\ProgramFiles\\java\\BaseJava\\basejava\\src\\com\\urise\\webapp");
        printNameFilesInCatalog(catalog, 1);
    }

    public static void printNameFilesInCatalog(File catalog, int offset) {
        File[] files = catalog.listFiles();
        String off = "";
        for (int i = 0; i < offset; i++) {
            off += " ";
        }
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println(off + "Dir:" + file.getName());
                printNameFilesInCatalog(file, offset + 2);
            } else if (file.isFile()) {
                System.out.println(off + "File:" + file.getName());
            }
        }
    }
}
