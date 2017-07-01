package ui;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import io.Logger;
import wavefront.GLModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Extension of GLJPanel which handles all kinds of (JO)GL stuffs.
 */
public class Renderer extends GLJPanel implements GLEventListener {
    private double posX, posY, posZ;
    private double angleX, angleY, angleZ;
    private double scale;

    private double dPos = 0.2;
    private double dAngle = 4;
    private double dScale = 0.01;

    private GLModel model;
    private int modelID;
    private boolean firstFrame;

    public Renderer(GLCapabilities caps) {
        super(caps);

        // transformations / key bindings
        initTransformations();
        bindTransformations();

        // callbacks for rendering
        addGLEventListener(this);

        // automate buffer swapping
        setAutoSwapBufferMode(true);

        // no model at first
        setModel(null);

        Logger.log("Renderer", "Initialized the renderer.");
    }

    public void setModel(GLModel model) {
        this.model = model;
        firstFrame = true;
        initTransformations();

        Logger.log("Renderer", "Changed the model to the new one.");
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // depth test
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);

        // perspective-correct interpolation
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

        // back-face culling
        gl.glDisable(GL.GL_CULL_FACE);

        // background color
        gl.glClearColor(0, 0, 0, 1);

        // lighting
        float[] lightPos = {5, 5, -1, 0};

        gl.glEnable(GLLightingFunc.GL_LIGHTING);
        gl.glEnable(GLLightingFunc.GL_LIGHT0);
        gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, lightPos, 0);

        // shading
        float[] ambientColor = {0.4f, 0.4f, 0.8f, 1.0f};
        float[] diffuseColor = {0.3f, 0.3f, 1.0f, 1.0f};
        float[] specColor = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] shininess = {100.0f};

        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_AMBIENT, ambientColor, 0);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_DIFFUSE, diffuseColor, 0);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_SPECULAR, specColor, 0);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_SHININESS, shininess, 0);

        Logger.log("Renderer", "Initialized GL.");
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // do nothing
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        if (model == null) {
            return;
        }

        GL2 gl = drawable.getGL().getGL2();

        // clear the buffers
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // generate the display list for the new model
        if (firstFrame) {
            firstFrame = false;
            modelID = gl.glGenLists(1);
            gl.glNewList(modelID, GL2.GL_COMPILE);
            model.draw(gl);
            gl.glEndList();

            Logger.log("Renderer", "Generated a new display list.");
        }

        // apply the transformations
        gl.glLoadIdentity();
        gl.glTranslated(posX, posY, posZ);
        gl.glRotated(angleX, 1, 0, 0);
        gl.glRotated(angleY, 0, 1, 0);
        gl.glRotated(angleZ, 0, 0, 1);
        gl.glScaled(scale, scale, scale);

        // draw the model
        gl.glCallList(modelID);
        gl.glFlush();
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL2 gl = drawable.getGL().getGL2();

        // viewport
        gl.glViewport(0, 0, w, h);

        // projection matrix
        float ratio = w / (float) h;
        float left = -ratio;
        float right = ratio;
        float bottom = -1.0f;
        float top = 1.0f;
        float zNear = 1.0f;
        float zFar = 100.0f;

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustum(left, right, bottom, top, zNear, zFar);

        // modelview matrix
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();

        Logger.log("Renderer", String.format("Resized the window -" +
                " Coor: (%d, %d), Size: (%d, %d)", x, y, w, h));
    }

    private void initTransformations() {
        posX = 0;
        posY = 0;
        posZ = 0;

        angleX = 0;
        angleY = 0;
        angleZ = 0;

        scale = 1;
    }

    private void bindTransformations() {
        // scale
        bindKey('+', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scale += dScale;
                Logger.log("Renderer", "Transformation - Scale up.");
            }
        });

        bindKey('-', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scale > dScale) {
                    scale -= dScale;
                    Logger.log("Renderer", "Transformation - Scale down.");
                }
            }
        });

        // translation
        // ...x direction
        bindKey('d', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                posX += dPos;
                Logger.log("Renderer", "Transformation - Move right.");
            }
        });

        bindKey('a', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                posX -= dPos;
                Logger.log("Renderer", "Transformation - Move left.");
            }
        });

        // ...y direction
        bindKey('w', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                posY += dPos;
                Logger.log("Renderer", "Transformation - Move up.");
            }
        });

        bindKey('s', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                posY -= dPos;
                Logger.log("Renderer", "Transformation - Move down.");
            }
        });

        // ...z direction
        bindKey('q', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                posZ += dPos;
                Logger.log("Renderer", "Transformation - Move back.");
            }
        });

        bindKey('e', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                posZ -= dPos;
                Logger.log("Renderer", "Transformation - Move front.");
            }
        });

        // rotation
        // ...x direction
        bindKey('i', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angleX += dAngle;
                Logger.log("Renderer", "Transformation - Rotate X+.");
            }
        });

        bindKey('k', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angleX -= dAngle;
                Logger.log("Renderer", "Transformation - Rotate X-.");
            }
        });

        // ...y direction
        bindKey('l', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angleY += dAngle;
                Logger.log("Renderer", "Transformation - Rotate Y+.");
            }
        });

        bindKey('j', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angleY -= dAngle;
                Logger.log("Renderer", "Transformation - Rotate Y-.");
            }
        });

        // ...z direction
        bindKey('u', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angleZ += dAngle;
                Logger.log("Renderer", "Transformation - Rotate Z+.");
            }
        });

        bindKey('o', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angleZ -= dAngle;
                Logger.log("Renderer", "Transformation - Rotate Z-.");
            }
        });

        // restore
        bindKey('r', new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initTransformations();
                Logger.log("Renderer", "Restored the model to the original state.");
            }
        });
    }

    private void bindKey(char key, AbstractAction action) {
        String actionName = "action-" + key;
        getInputMap().put(KeyStroke.getKeyStroke(key), actionName);
        getActionMap().put(actionName, action);
    }
}
