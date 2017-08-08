        package speciesClasssification;
        import org.deeplearning4j.datasets.iterator.impl.CifarDataSetIterator;
        import org.deeplearning4j.eval.Evaluation;
        import org.deeplearning4j.nn.api.OptimizationAlgorithm;
        import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
        import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
        import org.deeplearning4j.nn.conf.Updater;
        import org.deeplearning4j.nn.conf.inputs.InputType;
        import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
        import org.deeplearning4j.nn.conf.layers.OutputLayer;
        import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
        import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
        import org.deeplearning4j.nn.weights.WeightInit;
        import org.nd4j.linalg.activations.Activation;
        import org.nd4j.linalg.lossfunctions.LossFunctions;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Tiago on 5/1/2017.
 */

@SpringBootApplication
public class App {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(App.class, args);

       /* //obtendo dataset
        CifarDataSetIterator dataSetIterator = new CifarDataSetIterator(2, 5000, true);

        //printando os labels
        System.out.println(dataSetIterator.getLabels());

        //criando as camadas
        ConvolutionLayer layer0 = new ConvolutionLayer.Builder(5,5)
                .nIn(3)
                .nOut(16)
                .stride(1,1)
                .padding(2,2)
                .weightInit(WeightInit.XAVIER)
                .name("First convolution layer")
                .activation(Activation.RELU)
                .build();

        SubsamplingLayer layer1 = new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(2,2)
                .stride(2,2)
                .name("First subsampling layer")
                .build();

        ConvolutionLayer layer2 = new ConvolutionLayer.Builder(5,5)
                .nOut(20)
                .stride(1,1)
                .padding(2,2)
                .weightInit(WeightInit.XAVIER)
                .name("Second convolution layer")
                .activation(Activation.RELU)
                .build();

        SubsamplingLayer layer3 = new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(2,2)
                .stride(2,2)
                .name("Second subsampling layer")
                .build();

        ConvolutionLayer layer4 = new ConvolutionLayer.Builder(5,5)
                .nOut(20)
                .stride(1,1)
                .padding(2,2)
                .weightInit(WeightInit.XAVIER)
                .name("Third convolution layer")
                .activation(Activation.RELU)
                .build();

        SubsamplingLayer layer5 = new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(2,2)
                .stride(2,2)
                .name("Third subsampling layer")
                .build();

        OutputLayer layer6 = new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .activation(Activation.SOFTMAX)
                .weightInit(WeightInit.XAVIER)
                .name("Output")
                .nOut(10)
                .build();

        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .seed(12345)
                .iterations(1)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(0.001)
                .regularization(true)
                .l2(0.0004)
                .updater(Updater.NESTEROVS)
                .momentum(0.9)
                .list()
                .layer(0, layer0)
                .layer(1, layer1)
                .layer(2, layer2)
                .layer(3, layer3)
                .layer(4, layer4)
                .layer(5, layer5)
                .layer(6, layer6)
                .pretrain(false)
                .backprop(true)
                .setInputType(InputType.convolutional(32,32,3))
                .build();

        //iniciando  rede
        MultiLayerNetwork network = new MultiLayerNetwork(configuration);
        network.init();

        //treinando a rede
        network.fit(dataSetIterator);

        //criado o data set para classificacao
        Evaluation evaluation = network.evaluate(new CifarDataSetIterator(2, 500, false));
        System.out.println(evaluation.stats());*/
    }
}

