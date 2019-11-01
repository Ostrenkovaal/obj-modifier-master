import java.nio.FloatBuffer;

public class ObjModifier {

    public static void centerVertices(FloatBuffer buf) {
        buf.clear();
        Vector pivot = getPivotValue(buf);
        transferFigure(buf, pivot);
        buf.rewind();
    }

    //declare class Vector for convenient interaction with vertex data
    //similar classes exist in external pluggable libraries for 3d rendering
    private static class Vector {
        public float xAxisValue;
        public float yAxisValue;
        public float zAxisValue;

        public Vector() {
        }

        public Vector getPivot(float factor) {
            xAxisValue /= factor;
            yAxisValue /= factor;
            zAxisValue /= factor;
            return this;
        }
    }

    //pivot point search method on a figure through search weight centers for figure
    private static Vector getPivotValue(FloatBuffer buf) {

        //calculate weight points
        Vector weightPolygon = new Vector();
        for (int i = 0; i < buf.limit(); ) {
            weightPolygon.xAxisValue += buf.get(i++);
            weightPolygon.yAxisValue += buf.get(i++);
            weightPolygon.zAxisValue += buf.get(i++);
        }

        //calculate model pivot as center of bounding box
        Vector pivot = weightPolygon.getPivot(buf.limit() / 3);
        return pivot;
    }

    //put in the buffer the values ​​of the points of the transfered figure
    private static void transferFigure(FloatBuffer buffer, Vector pivot) {
        for (int i = 0; i < buffer.limit(); ) {
            buffer.position(i);
            buffer.put(buffer.get(i++) - pivot.xAxisValue);
            buffer.position(i);
            buffer.put(buffer.get(i++) - pivot.yAxisValue);
            buffer.position(i);
            buffer.put(buffer.get(i++) - pivot.zAxisValue);
        }
    }
}
