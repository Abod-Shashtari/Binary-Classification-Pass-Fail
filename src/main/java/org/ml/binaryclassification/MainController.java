package org.ml.binaryclassification;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;

public class MainController {
    @FXML
    private Button btnGo;

    @FXML
    private Button btnTrain;

    @FXML
    private Button btnUpload;

    @FXML
    private Label lblAccuracy;

    @FXML
    private Label lblResult;

    @FXML
    private TextField txtEnglishMark;

    @FXML
    private TextField txtErrorGoal;

    @FXML
    private TextField txtLearningRate;

    @FXML
    private TextField txtMathMark;

    @FXML
    private TextField txtMaxEpochs;

    @FXML
    private TextField txtScienceMark;

    @FXML
    private Label lblEpochs;
    //model-evaluation-page
    @FXML
    private Label lblFF;

    @FXML
    private Label lblFP;

    @FXML
    private Label lblNumPassSamples;

    @FXML
    private Label lblNumSamples;

    @FXML
    private Label lblTF;

    @FXML
    private Label lblTP;

    @FXML
    private Label lnlNumFailSamples;
    //
    ReadData rd;
    ArrayList<ArrayList<Integer>> dataSet;
    ArrayList<ArrayList<Integer>> trainingDataSet;
    ArrayList<ArrayList<Integer>> testingDataSet;
    Perceptron perceptron;
    boolean trainFlag=false;

    @FXML
    public void initialize() {
        rd = new ReadData();
        dataSet=new ArrayList<>();
        trainingDataSet=new ArrayList<>();
        testingDataSet=new ArrayList<>();
    }
    @FXML
    protected void uploadFileBtn(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILES","*.csv"));
        File f = fileChooser.showOpenDialog(null);

        if(f!=null){
            rd.openFile(f.getAbsolutePath());
            dataSet=rd.readCSV();
            int numOfElements=(int)Math.floor(dataSet.size()*0.7);
            copySlice(dataSet,trainingDataSet,0,numOfElements);
            copySlice(dataSet,testingDataSet,numOfElements,0);
        }
    }
    @FXML
    protected void trainBtn(){
        if(dataSet.isEmpty()){
            showMSG("Upload Dataset!");
            return;
        }
        String learningRateString=txtLearningRate.getText();
        String maxEpochsString=txtMaxEpochs.getText();
        String errorGoalString=txtErrorGoal.getText();
        if(learningRateString.isEmpty() || maxEpochsString.isEmpty() || errorGoalString.isEmpty()){
            showMSG("Make sure to fill all the Blanks\nIn Training Side");
        }else{
            try {
                double learningRate=Double.parseDouble(learningRateString);
                int maxEpochs=Integer.parseInt(maxEpochsString);
                double errorGoal=Double.parseDouble(errorGoalString);
                perceptron = new Perceptron(learningRate,maxEpochs,errorGoal);
                System.out.println("w0: "+perceptron.weights[0]);
                System.out.println("w1: "+perceptron.weights[1]);
                System.out.println("w2: "+perceptron.weights[2]);
                perceptron.training(trainingDataSet);
                System.out.println("----------------");
                System.out.println("w0: "+perceptron.weights[0]);
                System.out.println("w1: "+perceptron.weights[1]);
                System.out.println("w2: "+perceptron.weights[2]);
                double accuracy=perceptron.testing(testingDataSet);
                System.out.println("accuracy: "+accuracy);
                lblAccuracy.setText("Accuracy: "+accuracy);
                lblEpochs.setText("Epochs: "+perceptron.countEpochs);
            }catch (NumberFormatException e){
                showMSG("The input takes Numbers only!");
            }
        }
        trainFlag=true;
    }
    @FXML
    protected void goBtn(){
        if(!trainFlag){showMSG("Train the data first!"); return;}
        String mathString=txtMathMark.getText();
        String scienceString=txtScienceMark.getText();
        String englishString=txtEnglishMark.getText();
        if(mathString.isEmpty() || scienceString.isEmpty() || englishString.isEmpty()){
            showMSG("Make sure to fill all the Blanks\nIn Testing Side");
        }else{
            try {
                int mathMark=Integer.parseInt(mathString);
                int scienceMark=Integer.parseInt(scienceString);
                int englishMark=Integer.parseInt(englishString);
                ArrayList<Integer> inputs=new ArrayList<>();
                inputs.add(mathMark);
                inputs.add(scienceMark);
                inputs.add(englishMark);
                int value=perceptron.predict(inputs);
                String output = (value==1)?"Pass":"Fail";
                System.out.println(value);
                lblResult.setText("Result: "+output);
            }catch (NumberFormatException e){
                showMSG("The input takes Numbers only!");
            }
        }
    }
    private void copySlice(ArrayList<ArrayList<Integer>> src,ArrayList<ArrayList<Integer>> dist,int start,int numberOfElements){
        if(start<0) return;
        for (int i = start; (((numberOfElements==0) && (i < src.size()))||(i < numberOfElements) && (i < src.size())); i++) {
            dist.add(src.get(i));
        }
    }
    private void showMSG(String str){
        Dialog<String> dialog=new Dialog<>();
        dialog.setHeaderText(str);
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.show();
    }

}