package ui;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import io.Logger;
import wavefront.GLModel;

import javax.swing.*;
import java.awt.*;

/**
 * UI of the main window.
 */
public class App extends JFrame {
    Dimension size;
    Renderer renderer;

    public App(String title, int width, int height) {
        super(title);

        size = new Dimension(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // OpenGL profile
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCaps = new GLCapabilities(glProfile);

        // panel for rendering
        renderer = new Renderer(glCaps);
        renderer.setMinimumSize(size);
        renderer.setPreferredSize(size);
        getContentPane().add(renderer);

        // animator
        FPSAnimator animator = new FPSAnimator(renderer, 60, true);
        animator.start();

        Logger.log("App", "Initialized the app.");
    }

    public void setModel(GLModel model) {
        renderer.setModel(model);
    }
}
