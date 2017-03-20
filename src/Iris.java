/**
 * Created by aidandoak on 20/03/17.
 */
public class Iris{

    public double sepalLength;
    public double sepalWidth;
    public double petalLength;
    public double petalWidth;

    public String classification;

    public Iris(double sl, double sw, double pl, double pw, String classification){
        sepalLength = sl;
        sepalWidth = sw;
        petalLength = pl;
        petalWidth = pw;

        this.classification = classification;
    }
}
