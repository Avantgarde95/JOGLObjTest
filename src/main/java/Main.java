import io.Logger;
import io.ResLoader;
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
        String fileName = "models/cow.obj"; // any file in src/main/res/models
        ResLoader resLoader = new ResLoader(Main.class);
        List<String> data = resLoader.readFile(fileName);
        final GLModel model = new GLModel(data);
        Logger.log("Main", "Loaded a model from " + fileName + ".");

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
