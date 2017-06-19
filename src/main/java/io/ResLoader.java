package io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Util for reading a resource file (src/main/res).
 */
public class ResLoader {
    private ClassLoader classLoader;

    public ResLoader(Class klass) {
        classLoader = klass.getClassLoader();
    }

    public List<String> readFile(String fileName) throws IOException {
        List<String> data = new ArrayList<>();
        URL url = classLoader.getResource(fileName);

        if (url == null) {
            throw new IOException(fileName);
        }

        File file = new File(url.getFile());
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }

        scanner.close();
        return data;
    }
}
