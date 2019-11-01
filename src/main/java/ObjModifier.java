import java.nio.FloatBuffer;

public class ObjModifier {

    public static void centerVertices(FloatBuffer buf) {
        buf.clear();
        Vector pivotValue = getPivotValue(buf);
        transferFigure(buf, pivotValue);
        buf.rewind();
    }

    //declare class Vector for convenient interaction with point data
    //similar classes exist in external pluggable libraries for 3d rendering
    private static class Vector {
        public float xAxisValue;
        public float yAxisValue;
        public float zAxisValue;

        public Vector() {
        }

        public Vector(float xAxisValue, float yAxisValue, float zAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yAxisValue = yAxisValue;
            this.zAxisValue = zAxisValue;
        }

        public Vector addEndPoint(Vector vector) {
            xAxisValue += vector.xAxisValue;
            yAxisValue += vector.yAxisValue;
            zAxisValue += vector.zAxisValue;
            return this;
        }

        public Vector multiply(float factor) {
            xAxisValue *= factor;
            yAxisValue *= factor;
            zAxisValue *= factor;
            return this;
        }

    }

    //pivot point search method on a figure
    private static Vector getPivotValue(FloatBuffer buf){

        Vector minValue = new Vector(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        Vector maxValue = new Vector(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        Vector bufValue = new Vector();

        for (int i = 0; i < buf.limit(); ) {
            bufValue.xAxisValue = buf.get(i++);
            bufValue.yAxisValue = buf.get(i++);
            bufValue.zAxisValue = buf.get(i++);
            findExtremes(minValue, maxValue, bufValue);
        }

        //calculate model pivot as center of bounding box
        Vector vertex = minValue.addEndPoint(maxValue);
        Vector pivot = vertex.multiply(0.5f);

        return pivot;
    }

    //search for extrema on coordinate axes
    private static void findExtremes(Vector minValue, Vector maxValue, Vector bufValue) {
        if (bufValue.xAxisValue < minValue.xAxisValue) {
            minValue.xAxisValue = bufValue.xAxisValue;
        }
        if (bufValue.xAxisValue > maxValue.xAxisValue) {
            maxValue.xAxisValue = bufValue.xAxisValue;
        }
        if (bufValue.yAxisValue < minValue.yAxisValue) {
            minValue.yAxisValue = bufValue.yAxisValue;
        }
        if (bufValue.yAxisValue > maxValue.yAxisValue) {
            maxValue.yAxisValue = bufValue.yAxisValue;
        }
        if (bufValue.zAxisValue < minValue.zAxisValue) {
            minValue.zAxisValue = bufValue.zAxisValue;
        }
        if (bufValue.zAxisValue > maxValue.zAxisValue) {
            maxValue.zAxisValue = bufValue.zAxisValue;
        }
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

