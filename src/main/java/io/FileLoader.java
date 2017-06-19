package io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Util for reading a file.
 */
public class FileLoader {
    public static List<String> readResFile(Class klass, String fileName) throws IOException {
        URL url = klass.getClassLoader().getResource(fileName);

        if (url == null) {
            throw new IOException(fileName);
        } else {
            String realName = url.getFile();
            return readFile(realName);
        }
    }

    public static List<String> readFile(String fileName) throws IOException {
        List<String> data = new ArrayList<>();

        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        scanner.close();
        Logger.log("FileLoader", "Loaded the data from \"" + fileName + "\".");
        return data;
    }
}
