package io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Util for reading a file.
 */
public class FileLoader {
    public static List<String> readResFile(Class<?> klass, String fileName) throws IOException {
        InputStream stream = klass.getResourceAsStream(fileName);
        return readLines(stream);
    }

    public static List<String> readFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream stream = new FileInputStream(file);
        return readLines(stream);
    }

    private static List<String> readLines(InputStream stream) throws IOException {
        List<String> data = new ArrayList<>();
        String line;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        }

        return data;
    }
}
