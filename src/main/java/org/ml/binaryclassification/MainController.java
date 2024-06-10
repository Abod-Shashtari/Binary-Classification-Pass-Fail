package org.ml.binaryclassification;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

    ReadData rd;
    ArrayList<ArrayList<Integer>> dataSet;
    ArrayList<ArrayList<Integer>> trainingDataSet;
    ArrayList<ArrayList<Integer>> testingDataSet;
    Perceptron perceptron;
    DataEvaluation dataEvaluation;
    boolean trainFlag=false;

    @FXML
    public void initialize() {
        rd = new ReadData();
        dataSet=new ArrayList<>();
        dataEvaluation=new DataEvaluation();
        trainingDataSet=new ArrayList<>();
        testingDataSet=new ArrayList<>();
    }
    private int[] countPassFailSamples(ArrayList<ArrayList<Integer>> dataSet){
        int countPass=0;
        int countFail=0;
        for (ArrayList a : dataSet) {
            if ((int) a.get(3) == 1) {
                countPass++;
            } else {
                countFail++;
            }
        }
        return new int[]{countPass,countFail};
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
            btnUpload.setText("Uploaded 📄");
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
                loading(true);
                double learningRate=Double.parseDouble(learningRateString);
                int maxEpochs=Integer.parseInt(maxEpochsString);
                double errorGoal=Double.parseDouble(errorGoalString);
                perceptron = new Perceptron(learningRate,maxEpochs,errorGoal);
                loading(true);
                new Thread(()->{
                    System.out.println("w0: "+perceptron.weights[0]);
                    System.out.println("w1: "+perceptron.weights[1]);
                    System.out.println("w2: "+perceptron.weights[2]);
                    perceptron.training(trainingDataSet);
                    loading(false);
                    System.out.println("----------------");
                    System.out.println("w0: "+perceptron.weights[0]);
                    System.out.println("w1: "+perceptron.weights[1]);
                    System.out.println("w2: "+perceptron.weights[2]);
                    double accuracy=perceptron.testing(testingDataSet)[2];
                    System.out.println("accuracy: "+accuracy);
                    dataEvaluation.accuracy=accuracy;
                    dataEvaluation.epochs=perceptron.countEpochs;
                    dataEvaluation.numPassSamples=countPassFailSamples(testingDataSet)[0];
                    dataEvaluation.numFailSamples=countPassFailSamples(testingDataSet)[1];
                    dataEvaluation.numActualPassOfDesiered =(int)perceptron.testing(testingDataSet)[0];
                    dataEvaluation.numActualFailOfDesiered =(int)perceptron.testing(testingDataSet)[1];
                    dataEvaluation.numSamples=testingDataSet.size();
                    Platform.runLater(()->{
                        loading(false);
                    });
                }).start();
            }catch (NumberFormatException e){
                showMSG("The input takes Numbers only!");
            }
        }
        trainFlag=true;
    }

    private void loading(boolean flag) {
        Platform.runLater(() -> {
            if (flag) {
                btnTrain.setText("Loading...");
            } else {
                btnTrain.setText("TRAIN");
            }
        });
    }

    @FXML
    protected void openEvaluationWindow(){
        if(!trainFlag){showMSG("Train the data first!"); return;}
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("model-evaluation.fxml"));
            Parent rootE = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Model Evaluation");
            stage.setScene(new Scene(rootE));
            ModelEvaluationController controller = fxmlLoader.getController();
            controller.myInitialize(dataEvaluation);
            stage.show();
        }catch (Exception e){
            e.printStackTrace(System.out);
            System.out.println("Error: Can't load the window!");
        }
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