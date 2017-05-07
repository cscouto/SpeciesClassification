package speciesClasssification;

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
    Random randNumGen;
    int batchSize = 1; //numero de imagens por vez
    int outputNum = 2; //numero de possiveis solucoes
    int numEpochs = 30; //quantidade de treinamento
    File trainData;
    File testData;
    FileSplit train;
    FileSplit test;
    ParentPathLabelGenerator labelMaker;
    ImageRecordReader recordReader;
    DataSetIterator dataIter;
    DataNormalization scaler;
    File LOCATION_TO_SAVE = new File("src/main/bd/trained_network.zip"); // onde salvar a base
    MultiLayerNetwork model; //modelo da rede

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
        trainData = new File("src/main/data");
        //imagens para teste
        testData = new File("src/main/data");

        // Define the FileSplit(PATH, ALLOWED FORMATS,random)
        randNumGen = new Random(rngseed);

        //divide imagens para treino e teste
        train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);

        //usa label das pastas para criar classes
        labelMaker = new ParentPathLabelGenerator();

        //le as imagens
        recordReader = new ImageRecordReader(height, width, channels, labelMaker);
        recordReader.initialize(train);

        // DataSet Iterator
        dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);

        // normaliza valores dos pixel entre 0-1
        scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);

        reconhecerEspecieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                System.out.println("Selecionar Especie");

                // pop up file chooser
                String filechose = fileChose().toString();

                File file = new File(filechose);

                // Use NativeImageLoader to convert to numerical matrix

                NativeImageLoader loader = new NativeImageLoader(height, width, channels);

                // Get the image into an INDarray

                INDArray image = null;
                try {
                    image = loader.asMatrix(file);
                    scaler.transform(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Pass through to neural Net

                INDArray output = model.output(image);
                System.out.println("## The FILE CHOSEN WAS " + filechose);
                System.out.println("## The Neural Nets Pediction ##");
                System.out.println("## list of probabilities per label ##");
                //System.out.println("## List of Labels in Order## ");
                // In new versions labels are always in order
                System.out.println(output.toString());
                System.out.println(dataIter.getLabels());
            }
        });
        listarEspeciesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println(dataIter.getLabels());
            }
        });
        treinarRedeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    treinarModelo();
                    testaModelo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        main.carregaBaseDados();
        frame.setVisible(true);
    }

    public void carregaBaseDados() throws IOException {

        if(LOCATION_TO_SAVE.exists()){

            model = ModelSerializer.restoreMultiLayerNetwork(LOCATION_TO_SAVE);
            model.getLabels();
            testaModelo();

        }else{
            criaNovoModelo();

            treinarModelo();

            recordReader.reset();

            testaModelo();

            salvaBase();
        }
    }


    public void salvaBase() throws IOException {
        System.out.println("******SAVE TRAINED MODEL******");

        // boolean save Updater
        boolean saveUpdater = false;

        // ModelSerializer needs modelname, saveUpdater, Location
        ModelSerializer.writeModel(model,LOCATION_TO_SAVE,saveUpdater);
    }
    public void criaNovoModelo(){
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

        model = new MultiLayerNetwork(conf);
        model.init();

        model.setListeners(new ScoreIterationListener(10));
    }
    public void treinarModelo() throws IOException {
        for(int i = 0; i<numEpochs; i++){
            model.fit(dataIter);
        }
        salvaBase();
    }

    public void  testaModelo() throws IOException {
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

        System.out.println(eval.stats());
    }

    public static String fileChose(){
        JFileChooser fc = new JFileChooser();
        int ret = fc.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            return filename;
        }
        else {
            return null;
        }
    }
}
