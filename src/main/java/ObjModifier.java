import de.javagl.obj.Obj;

import java.nio.FloatBuffer;

public class ObjModifier {

    public static void centerVertices(Obj obj, FloatBuffer buf) {
        buf.clear();
        Vector pivot = getPivotValue(obj, buf);
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

        public Vector sumCenterWeight(Vector vec, float factor) {
            xAxisValue += vec.xAxisValue / factor;
            yAxisValue += vec.yAxisValue / factor;
            zAxisValue += vec.zAxisValue / factor;
            return this;
        }

        public Vector getPivot(float factor) {
            xAxisValue /= factor;
            yAxisValue /= factor;
            zAxisValue /= factor;
            return this;
        }
    }

    //pivot point search method on a figure through search weight centers for polygons
    private static Vector getPivotValue(Obj obj, FloatBuffer buf) {

        Vector sumCenterWeightFigure = new Vector();
        Vector polygons = new Vector();

        for (int i = 0; i < obj.getNumFaces(); i++) {

            Vector weightPolygon = new Vector();
            int lengthFace = obj.getFace(i).getNumVertices();

            //calculate the weight of each polygon
            for (int j = 0; j < lengthFace; j++) {
                weightPolygon.xAxisValue += buf.get(obj.getFace(i).getVertexIndex(j) * lengthFace);
                weightPolygon.yAxisValue += buf.get(obj.getFace(i).getVertexIndex(j) * lengthFace + 1);
                weightPolygon.zAxisValue += buf.get(obj.getFace(i).getVertexIndex(j) * lengthFace + 2);
            }
            //calculate the weight all polygons
            sumCenterWeightFigure = polygons.sumCenterWeight(weightPolygon, 3.0f);
        }

        //calculate model pivot as center of bounding box
        Vector pivot = sumCenterWeightFigure.getPivot(obj.getNumFaces());
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
