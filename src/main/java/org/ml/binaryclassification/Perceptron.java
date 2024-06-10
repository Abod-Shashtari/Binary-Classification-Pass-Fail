package org.ml.binaryclassification;

import java.util.ArrayList;
import java.util.Random;

public class Perceptron {
    double[] weights=new double[3];
    double threshold=1;
    double learningRate;
    int maxEpochs;
    double errorGoal;
    int countEpochs;
    Random random=new Random();
    public Perceptron(double learningRate, int maxEpochs,double errorGoal){
        for (int i=0;i<3;++i){
            weights[i]=randomWeight();
        }
        threshold=1;
        this.learningRate=learningRate;
        this.maxEpochs=maxEpochs;
        this.errorGoal=errorGoal;
        this.countEpochs=0;
    }
    public int predict(ArrayList<Integer> input){
        double X=threshold;
        for(int i=0;i<3;++i){
            X+=input.get(i)*this.weights[i];
        }
        return (X>0)?1:0;
    }
    public void training(ArrayList<ArrayList<Integer>> trainDataSet){
        ArrayList<Integer> errors=new ArrayList<>();
        for (int i=0;i<maxEpochs;++i){
            for (ArrayList<Integer> row : trainDataSet) {
                int yActual = predict(row);
                int yDesired = row.getLast();
                int error = yDesired - yActual;
                errors.add(error);
                calcNewWeights(row, error);
            }
            countEpochs++;
            if(mse(errors)<=errorGoal) return;
        }
    }
    public double[] testing(ArrayList<ArrayList<Integer>> testingDataSet){
        int countE0=0;
        int countPass=0;
        int countFail=0;
        ArrayList<Integer> errors=new ArrayList<>();
        for (ArrayList<Integer> row : testingDataSet) {
            int yActual = predict(row);
            int yDesired = row.getLast();
            int error = yDesired - yActual;
            if(error==0) countE0++;
            if(yActual==yDesired && yActual==1) countPass++;
            if(yActual==yDesired && yActual==0) countFail++;
            errors.add(error);
        }
        return new double[]{countPass,countFail,((double) countE0 /testingDataSet.size())};
    }
    private double mse(ArrayList<Integer> errors){
        double sum=0;
        for (Integer e : errors){
            sum+=Math.pow(e,2);
        }
        return sum/ errors.size();
    }
    public void calcNewWeights(ArrayList<Integer> inputs,int error){
        for (int i = 0; i < 3; i++) {
            weights[i]+=learningRate*inputs.get(i)*error;
        }
        threshold+=learningRate*error*1;
    }

    private double randomWeight(){
        double range=2.4/3;
        return random.nextDouble(range*2)-range;
    }
}
