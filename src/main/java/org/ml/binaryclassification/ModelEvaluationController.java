package org.ml.binaryclassification;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class ModelEvaluationController {
    @FXML
    private Label lblAccuracy;

    @FXML
    private Label lblEpochs;

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
    private Label lblNumFailSamples;

    @FXML
    private Label lblSumPredictedFail;

    @FXML
    private Label lblSumPredictedPass;

    DataEvaluation dataEvaluation;

    public void myInitialize(DataEvaluation dataEvaluation){
        this.dataEvaluation=dataEvaluation;
        lblAccuracy.setText("Accuracy: "+dataEvaluation.accuracy);
        lblEpochs.setText("Epochs: "+dataEvaluation.epochs);
        lblNumSamples.setText("n = "+dataEvaluation.numSamples);
        lblNumPassSamples.setText(dataEvaluation.numPassSamples+"");
        lblNumFailSamples.setText(dataEvaluation.numFailSamples+"");
        //TODO:Fix the this.
        lblTP.setText("TP = "+dataEvaluation.numActualPassOfDesiered);
        lblTF.setText("TF = "+dataEvaluation.numActualFailOfDesiered);
        lblFP.setText("FP = "+(dataEvaluation.numFailSamples-dataEvaluation.numActualFailOfDesiered));
        lblFF.setText("FF = "+(dataEvaluation.numPassSamples-dataEvaluation.numActualPassOfDesiered));
        lblSumPredictedPass.setText((dataEvaluation.numActualPassOfDesiered+(dataEvaluation.numFailSamples-dataEvaluation.numActualFailOfDesiered))+"");
        lblSumPredictedFail.setText((dataEvaluation.numActualFailOfDesiered+(dataEvaluation.numPassSamples-dataEvaluation.numActualPassOfDesiered))+"");
    }
}
