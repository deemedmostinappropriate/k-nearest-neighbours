/**
 * Created by aidandoak on 20/03/17.
 */
import java.io.*;
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
        testSet = new HashSet<Iris>();
    }

    /**
     * Reads data from the given Iris training data file and adds it to the set of
     * training data.
     * @param fileName
     */
    public void readTrainingData(String fileName) throws IOException{
        readIrisData(fileName, trainingSet);
    }

    /**
     * Reads data from the given Iris testing data file and adds it to the set of
     * testing data.
     * @param fileName
     */
    public void readTestData(String fileName) throws IOException{
        readIrisData(fileName, testSet);
    }

    /**
     * Reads the data of Iris classifications from the given file and places it in the given set.
     * @param fileName
     * @param to
     * @throws IOException
     */
    private void readIrisData(String fileName, Set<Iris> to) throws IOException{
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

        // Close the reader:
        r.close();
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

    public Iris[] getNeighbours(Iris testData, int k) throws Error{
        if(k % 2 == 0)
            throw new Error("k-value must be odd numbered.");

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
        }

        // Return only as many result as asked for by the k value:
        return sortedResult;
    }

    public Map<String, Double> getCertainty(Iris testSample, Iris[] neighbours){
        // Order relates to the classification strings: {s, vc, vg}
        int[] irisClassCount = {0, 0, 0};

        // Increase classification counts:
        for(Iris i : neighbours){
            if(i.classification.equals(s)) {
                irisClassCount[0] = irisClassCount[0]+1;
            } else if(i.classification.equals(vc)) {
                irisClassCount[1] = irisClassCount[1]+1;
            } else {
                irisClassCount[2] = irisClassCount[2]+1;
            }
        }
        // Map for putting the predictions certainty into:
        Map<String, Double> certainty = new HashMap<String, Double>();

        // Put the classifications and their likelihood into the map
        certainty.put(s, (double)irisClassCount[0]/neighbours.length);
        certainty.put(vc, (double)irisClassCount[1]/neighbours.length);
        certainty.put(vg, (double)irisClassCount[2]/neighbours.length);

        // Return the certainty map:
        return certainty;
    }

    public Set<Iris> getTestSet(){
        return testSet;
    }


    /* Main Method */
    public static void main(String[] args){
        IrisClassifier main = new IrisClassifier();
        int k = 0;

        // Read the given arguments as training and testing data:
        try {
            main.readTrainingData(args[0]);
            main.readTestData(args[1]);
            k = Integer.parseInt(args[2]);
        }catch(Exception e){ // If the file names are wrong/file doesnt exist:
            if(e instanceof NumberFormatException){
                System.out.println("k value must be a number.");
            }
            System.out.println("Error with reading file.");
            System.out.println("Useage:\n\t IrisClassifier <trainingDataFile> <testDataFile> <k-value>");
            System.exit(1);
        }

        if(k % 2 == 0 || k < 1){
            System.out.println("k value must be positive and odd (1,3,5,7,...)");
            System.exit(1);
        }

        double correctInstances = 0;
        double instances = 0;

        File output = new File("predictions.csv");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(output));

            writer.println("sl,sw,pl,pw,observed,prediction,likelihood");
            // Run the neighbours calculation on the test set:
            for(Iris i : main.getTestSet()){
                Iris[] neighbours = main.getNeighbours(i, k);

                // Retrieve the certainty of each possible classification:
                Map<String, Double> certainty = main.getCertainty(i, neighbours);

                String prediction = "";
                double likelihood = 0.0;

                // Find highest predicted value:
                if(certainty.get(s) > certainty.get(vc) && certainty.get(s) > certainty.get(vg)){
                    // Iris-setosa classification:
                    prediction = s;
                    likelihood = certainty.get(s);
                } else if(certainty.get(vc) > certainty.get(s) && certainty.get(vc) > certainty.get(vg)){
                    // Iris-versicolor classification:
                    prediction = vc;
                    likelihood = certainty.get(vc);
                } else if(certainty.get(vg) > certainty.get(vc) && certainty.get(vg) > certainty.get(s)){
                    // Iris-virginica classification:
                    prediction = vg;
                    likelihood = certainty.get(vg);
                } else{
                    prediction = "try higher k-value";
                }

                // Write Iris Sample, Predicted Result and Likelihood to results file:
                writer.printf("%s,%s,%.2f\n", i.toString(), prediction, likelihood);

                if(i.toString().split(",")[4].equals(prediction)){
                    ++correctInstances;
                }
                ++instances;
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing results to file: " + output.getName());
        }

        // Calculate Accuracy:
        System.out.printf("\nTest Size: %1.0f\tk-value: %d\tTotal Accuracy: %.1f%%\n", instances, k, (correctInstances/instances) * 100);

        // Success exit:
        System.exit(0);
    }
}
