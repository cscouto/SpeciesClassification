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
        import org.springframework.context.annotation.Bean;
        import org.springframework.web.servlet.config.annotation.CorsRegistry;
        import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
        import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

        /**
 * Created by Tiago on 5/1/2017.
 */

@SpringBootApplication
public class App {

    public static void main(String[] args) throws Exception {
            SpringApplication.run(App.class, args);
    }
                @Bean
                public WebMvcConfigurer corsConfigurer() {
                        return new WebMvcConfigurerAdapter() {
                                @Override
                                public void addCorsMappings(CorsRegistry registry) {
                                        registry.addMapping("/**");
                                }
                        };
                }
}

