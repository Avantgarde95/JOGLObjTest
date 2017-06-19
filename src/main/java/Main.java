import io.FileLoader;
import io.Logger;
import ui.App;
import wavefront.GLModel;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * Main routine.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        List<String> data;

        if (args.length == 0) {
            data = FileLoader.readResFile(Main.class, "example.obj");
        } else {
            data = FileLoader.readFile(args[0]);
        }

        final GLModel model = new GLModel(data);
        Logger.log("Main", "Parsed the data and generated a GLModel instance.");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App app = new App("OBJTest", 400, 300);
                app.setModel(model);
                app.setVisible(true);
                app.pack();
            }
        });
    }
}
