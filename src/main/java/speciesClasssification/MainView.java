package speciesClasssification;

import dataexamples.MnistImagePipelineExampleLoad;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Tiago on 5/7/2017.
 */
public class MainView {
    /*
        image information
        28 * 28 grayscale
        grayscale implies single channel
        */

    //variaveis
    int height = 28;
    int width = 28;
    int channels = 1; //apenas 1 canal pois eh cinza
    int rngseed = 10;
    Random randNumGen = new Random(rngseed);
    int batchSize = 1; //numero de imagens por vez
    int outputNum = 2; //numero de possiveis solucoes
    int numEpochs = 1; //quantidade de treinamento
    File trainData;
    File testData;
    FileSplit train;
    FileSplit test;
    ParentPathLabelGenerator labelMaker;
    ImageRecordReader recordReader;
    DataSetIterator dataIter;
    DataNormalization scaler;
    File locationToSave;
    private static Logger log = LoggerFactory.getLogger(MainView.class);

    //UI Componentes
    private JLabel reconhecimentoDeEspeciesFlorestaisLabel;
    private JButton listarEspeciesButton;
    private JButton reconhecerEspecieButton;
    private JButton treinarRedeButton;
    private JButton cadastrarEspeciesButton;
    private JButton adcionarFotosButton;
    private JPanel panelMain;


    public MainView() throws IOException {

        //imagens para treino
        trainData = new File("C:\\Users\\Tiago\\Desktop\\TCC\\SpeciesClassification\\src\\main\\data");
        //imagens para teste
        testData = new File("/mnist_png/testing");

        // Define the FileSplit(PATH, ALLOWED FORMATS,random)

        //divide imagens para treino e teste
        train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);

        //usa label das pastas
        labelMaker = new ParentPathLabelGenerator();

        //le a imagem
        recordReader = new ImageRecordReader(height, width, channels, labelMaker);
        recordReader.initialize(train);

        // DataSet Iterator
        dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);

        // Scale pixel values to 0-1
        scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);

        // In production you would loop through all the data
        // in this example the loop is just through 3
        while (dataIter.hasNext()) {
            org.nd4j.linalg.dataset.DataSet ds = dataIter.next();
            System.out.println(ds);
            System.out.println(dataIter.getLabels());

        }

        reconhecerEspecieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        listarEspeciesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        treinarRedeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        cadastrarEspeciesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        adcionarFotosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    public static void main (String args[]) throws IOException {
        MainView main = new MainView();
        JFrame frame = new JFrame("ManView");
        frame.setContentPane(main.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        main.carregaBaseDados();
    }

    public void carregaBaseDados() throws IOException {

        locationToSave = new File("trained_mnist_model.zip");

        if(locationToSave.exists()){

            MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

            model.getLabels();

            //Test the Loaded Model with the test data

            recordReader.initialize(test);
            DataSetIterator testIter = new RecordReaderDataSetIterator(recordReader,batchSize,1,outputNum);
            scaler.fit(testIter);
            testIter.setPreProcessor(scaler);

            // Create Eval object with 10 possible classes
            Evaluation eval = new Evaluation(outputNum);

            while(testIter.hasNext()){
                DataSet next = testIter.next();
                INDArray output = model.output(next.getFeatures());
                eval.eval(next.getLabels(),output);

            }

            log.info(eval.stats());
        }else{

            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                    .seed(rngseed)
                    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                    .iterations(1)
                    .learningRate(0.006)
                    .updater(Updater.NESTEROVS).momentum(0.9)
                    .regularization(true).l2(1e-4)
                    .list()
                    .layer(0, new DenseLayer.Builder()
                            .nIn(height * width)
                            .nOut(100)//numero de neuronios na proxima camada
                            .activation(Activation.RELU)
                            .weightInit(WeightInit.XAVIER)
                            .build())
                    .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                            .nIn(100)//igual o numero de saidas da camada anterior
                            .nOut(outputNum)
                            .activation(Activation.SOFTMAX)
                            .weightInit(WeightInit.XAVIER)
                            .build())
                    .pretrain(false).backprop(true)
                    .setInputType(InputType.convolutional(height,width,channels))
                    .build();

            MultiLayerNetwork model = new MultiLayerNetwork(conf);
            model.init();

            model.setListeners(new ScoreIterationListener(10));

            log.info("*****TRAIN MODEL********");
            for(int i = 0; i<numEpochs; i++){
                model.fit(dataIter);
            }

            log.info("******EVALUATE MODEL******");

            recordReader.reset();

            // The model trained on the training dataset split
            // now that it has trained we evaluate against the
            // test data of images the network has not seen

            recordReader.initialize(test);
            DataSetIterator testIter = new RecordReaderDataSetIterator(recordReader,batchSize,1,outputNum);
            scaler.fit(testIter);
            testIter.setPreProcessor(scaler);

        /*
        log the order of the labels for later use
        In previous versions the label order was consistent, but random
        In current verions label order is lexicographic
        preserving the RecordReader Labels order is no
        longer needed left in for demonstration
        purposes
        */
            log.info(recordReader.getLabels().toString());

            // Create Eval object with 10 possible classes
            Evaluation eval = new Evaluation(outputNum);


            // Evaluate the network
            while(testIter.hasNext()){
                org.nd4j.linalg.dataset.DataSet next = testIter.next();
                INDArray output = model.output(next.getFeatureMatrix());
                // Compare the Feature Matrix from the model
                // with the labels from the RecordReader
                eval.eval(next.getLabels(),output);

            }

            log.info(eval.stats());

            log.info("******SAVE TRAINED MODEL******");
            // Details

            // Where to save model
            locationToSave = new File("trained_mnist_model.zip");

            // boolean save Updater
            boolean saveUpdater = false;

            // ModelSerializer needs modelname, saveUpdater, Location

            ModelSerializer.writeModel(model,locationToSave,saveUpdater);
        }
    }
}
