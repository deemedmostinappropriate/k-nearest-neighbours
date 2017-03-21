/**
 * Created by aidandoak on 20/03/17.
 */

/**
 * An object that holds the data points from the iris classification files.
 * The classification itself can be null if the data point is being tested by
 * the k-nn algorithm.
 */
public class Iris{

    public double sepalLength;
    public double sepalWidth;
    public double petalLength;
    public double petalWidth;

    public String classification;

    /**
     * Creates an object from 4 data points and the classification it has. The
     * classification can be null if the object has yet to be classified.
     * @param sl
     * @param sw
     * @param pl
     * @param pw
     * @param classification
     */
    public Iris(double sl, double sw, double pl, double pw, String classification){
        sepalLength = sl;
        sepalWidth = sw;
        petalLength = pl;
        petalWidth = pw;

        this.classification = classification;
    }

    /**
     * Returns an array of the Iris data.
     * @return
     */
    public double[] getArray(){
        return new double[]{sepalLength, sepalWidth, petalLength, petalWidth};
    }

    @Override
    public String toString(){
        return String.format("%.1f  %.1f  %.1f  %.1f  %s",
                sepalLength,sepalWidth,petalLength,petalWidth,classification);
    }
}
