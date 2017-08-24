package speciesClasssification;

/**
 * Created by Tiago on 8/23/2017.
 */
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;


public class Test {
    // Compulsory
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        Mat img = Imgcodecs.imread("path/to/img", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    }
}
