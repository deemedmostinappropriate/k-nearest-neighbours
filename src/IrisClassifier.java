/**
 * Created by aidandoak on 20/03/17.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class IrisClassifier {

    public Set<Iris> trainingSet;

    public IrisClassifier(){
        trainingSet = new HashSet<Iris>();
    }

    public void readTrainingData(String fileName){
        try {
            File f = new File(fileName);
            BufferedReader r = new BufferedReader(new FileReader(f));

            String line = r.readLine();
            while(line != null){
                // Split the line given line by commas:
                String[] split = line.split(",");

                double sl = Double.valueOf(split[0]);
                double sw = Double.valueOf(split[1]);
                double pl = Double.valueOf(split[2]);
                double pw = Double.valueOf(split[3]);
                String classification = split[4];

                // Create a new Iris data object from the line:
                Iris data = new Iris(sl,sw,pl,pw,classification);
                // Add the new data point to the set of training data:
                trainingSet.add(data);
            }

            r.close();
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Calculates the distance between 2 given vectors based on their corresponding ranges.
     * @param from
     * @param to
     * @param ranges
     * @return
     * @throws Error
     */
    public double getEuclideanDist(double[] from, double[] to, double[] ranges) throws Error{
        double distance = 0.0;

        if(from.length != to.length && to.length != ranges.length)
            throw new Error("All vectors must equal in length.");

        for(int i = 0; i < ranges.length; ++i){
            double calc = Math.pow(from[i] - to[i], 2) / Math.pow(ranges[i], 2);
            distance += calc;
        }

        return Math.sqrt(distance);
    }
}
