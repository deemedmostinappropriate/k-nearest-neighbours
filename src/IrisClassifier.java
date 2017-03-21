/**
 * Created by aidandoak on 20/03/17.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IrisClassifier {

    public Set<Iris> trainingSet;
    public Set<Iris> testSet;

    public static final double[] ranges = {3.6, 2.4, 5.9, 2.4};
    public static final String s = "Iris-setosa";
    public static final String vc = "Iris-versicolor";
    public static final String vg = "Iris-virginica";


    public IrisClassifier(){
        trainingSet = new HashSet<Iris>();
    }

    /**
     * Reads data from the given Iris training data file and adds it to the set of
     * training data.
     * @param fileName
     */
    public void readTrainingData(String fileName){
        readIrisData(fileName, trainingSet);
    }

    /**
     * Reads data from the given Iris testing data file and adds it to the set of
     * testing data.
     * @param fileName
     */
    public void readTestData(String fileName){
        readIrisData(fileName, testSet);
    }

    /**
     * Reads the data of Iris classifications from the given file and places it in the given set.
     * @param fileName
     * @param to
     */
    private void readIrisData(String fileName, Set<Iris> to){
        try {
            File f = new File(fileName);
            BufferedReader r = new BufferedReader(new FileReader(f));

            String line = r.readLine();
            while(line != null){
                // Split the line given line by commas:
                String[] split = line.split("  ");

                double sl = Double.valueOf(split[0]);
                double sw = Double.valueOf(split[1]);
                double pl = Double.valueOf(split[2]);
                double pw = Double.valueOf(split[3]);
                String classification = split[4];

                // Create a new Iris data object from the line:
                Iris data = new Iris(sl,sw,pl,pw,classification);
                // Add the new data point to the set of training data:
                to.add(data);

                // Read the next line:
                line = r.readLine();
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

    public Iris[] getNeighbours(Iris testData, int k){
        List<Double> distances = new ArrayList<Double>();
        Map<Double, Iris> distancesToData = new HashMap<Double, Iris>();

        // Place the distances in a list and have the data objects mapped to them separately:
        for(Iris i : trainingSet){
            double distance = getEuclideanDist(testData.getArray(), i.getArray(), ranges);

            // Add to list of distances:
            distances.add(distance);
            // Map the distance to the current data point:
            distancesToData.put(distance, i);
        }

        // Sort the list for distances:
        Collections.sort(distances);

        Iris[] sortedResult = new Iris[k];

        // Use the sorted distances to add the corresponding data point to the return array,
        // only returning as much as asked for by k:
        for(int i = 0; i < k; ++i){
            sortedResult[i] = distancesToData.get(distances.get(i));
            System.out.println(distances.get(i));
        }

        // Return only as many result as asked for by the k value:
        return sortedResult;
    }

    private void printTrainingData(){
        for(Iris i : trainingSet){
            System.out.println(i.toString());
        }
    }

    public static void main(String[] args){
        IrisClassifier main = new IrisClassifier();

        main.readTrainingData("iris-training.txt");
        main.readTestData("iris-test.txt");
    }
}
