package wavefront;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate a JOGL model from Wavefront .obj data
 */
public class GLModel {
    private List<Coords> vertices = new ArrayList<>();
    private List<Coords> normals = new ArrayList<>();
    private List<Coords> texCoords = new ArrayList<>();
    private List<Face> faces = new ArrayList<>();

    public GLModel(List<String> data) {
        // split the data into lines
        List<String[]> lines = new ArrayList<>();

        for (String line : data) {
            lines.add(line.trim().split("\\s+"));
        }

        // scan the vertices / normals / texture coordinates
        for (String[] tokens : lines) {
            switch (tokens[0]) {
                case "v":
                    vertices.add(parseVertex(tokens));
                    break;
                case "vn":
                    normals.add(parseNormal(tokens));
                    break;
                case "vt":
                    texCoords.add(parseTexCoord(tokens));
                    break;
            }
        }

        // scan the faces
        for (String[] tokens : lines) {
            if (tokens[0].equals("f")) {
                faces.add(parseFace(tokens));
            }
        }
    }

    public void draw(GL2 gl) {
        for (Face face : faces) {
            face.draw(gl);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int faceCount = faces.size();

        for (int i = 0; i < faceCount - 1; i++) {
            builder.append(faces.get(i)).append("\n");
        }

        if (faceCount > 0) {
            builder.append(faces.get(faceCount - 1));
        }

        return builder.toString();
    }

    private Coords parseVertex(String[] tokens) {
        double x = Double.parseDouble(tokens[1]);
        double y = Double.parseDouble(tokens[2]);
        double z = Double.parseDouble(tokens[3]);

        return new Coords(x, y, z);
    }

    private Coords parseNormal(String[] tokens) {
        double x = Double.parseDouble(tokens[1]);
        double y = Double.parseDouble(tokens[2]);
        double z = Double.parseDouble(tokens[3]);

        return new Coords(x, y, z).normalize();
    }

    private Coords parseTexCoord(String[] tokens) {
        double u = Double.parseDouble(tokens[1]);
        double v = Double.parseDouble(tokens[2]);

        return new Coords(u, v);
    }

    private Face parseFace(String[] tokens) {
        Face face = new Face();
        boolean hasNormal = false;

        for (int i = 1; i < tokens.length; i++) {
            String[] indices = tokens[i].split("/");
            int vIndex, tIndex, nIndex;

            switch (indices.length) {
                // vertices only
                case 1:
                    vIndex = Integer.parseInt(indices[0]) - 1;
                    face.addVertex(vertices.get(vIndex));
                    break;

                // vertices / texture coordinates
                case 2:
                    vIndex = Integer.parseInt(indices[0]) - 1;
                    tIndex = Integer.parseInt(indices[1]) - 1;
                    face.addVertex(vertices.get(vIndex));
                    face.addTexCoord(texCoords.get(tIndex));
                    break;

                // vertices / normals
                case 3:
                    vIndex = Integer.parseInt(indices[0]) - 1;
                    nIndex = Integer.parseInt(indices[2]) - 1;
                    face.addVertex(vertices.get(vIndex));
                    face.addNormal(normals.get(nIndex));

                    hasNormal = true;

                    // and texture coordinates
                    if (!indices[1].isEmpty()) {
                        tIndex = Integer.parseInt(indices[1]) - 1;
                        face.addTexCoord(texCoords.get(tIndex));
                    }

                    break;
            }
        }

        // flat face -> generate a 'global' normal
        if (!hasNormal) {
            face.addGlobalNormal(faceNormal(
                    vertices.get(0),
                    vertices.get(1),
                    vertices.get(2)
            ));
        }

        return face;
    }

    private Coords faceNormal(Coords v1, Coords v2, Coords v3) {
        // a = v2 - v1
        Coords a = Coords.add(v2, Coords.scale(v1, -1));

        // b = v3 - v1
        Coords b = Coords.add(v3, Coords.scale(v1, -1));

        // result = (v2 - v1) X (v3 - v1)
        return Coords.cross(a, b).normalize();
    }
}
