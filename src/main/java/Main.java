import de.javagl.obj.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.out.println("The number of arguments should be two: input_path and output_path");
            System.exit(1);
        }

        var inputPath = Path.of(args[0]);
        var outputPath = Path.of(args[1]);

        Obj obj;
        try (var inputStream = Files.newInputStream(inputPath)) {
            obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
        }

        var vertices = ObjData.getVertices(obj);
        //We modified this method for transfer figure through polygons
        ObjModifier.centerVertices(obj,vertices);

        var newObj = Objs.createFromIndexedTriangleData(ObjData.getFaceVertexIndices(obj),
                vertices, ObjData.getTexCoords(obj, 2), ObjData.getNormals(obj));
        try (var outputStream = Files.newOutputStream(outputPath)) {
            ObjWriter.write(newObj, outputStream);
        }
    }
}
