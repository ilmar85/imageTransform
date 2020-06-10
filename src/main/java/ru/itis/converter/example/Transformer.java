package ru.itis.converter.example;

import com.nugumanov.wavelettransform.WaveletBufferedImage;
import com.nugumanov.wavelettransform.WaveletImage;
import com.nugumanov.wavelettransform.test.ImageInOut;
import com.nugumanov.wavelettransform.test.ImageOpenSave;
import com.nugumanov.wavelettransform.transforms.TransformType;
import com.nugumanov.wavelettransform.transforms.WaveletType;

import java.awt.image.BufferedImage;
import java.io.File;

import static ru.itis.converter.example.MainView.OUT_PATH;
import static ru.itis.converter.example.MainView.REVERSE_PATH;

public class Transformer {
    public static void reverseTransform(File reverseInputFile) {
        ImageInOut imageInOut = new ImageOpenSave();
        BufferedImage reverseImage = imageInOut.inputImage(reverseInputFile);
        WaveletImage waveletReverseImage = new WaveletBufferedImage(reverseImage, TransformType.REVERSE, WaveletType.HAAR, 2);
        File reverseOutputFile = new File(REVERSE_PATH + File.separator + reverseInputFile.getName());
        imageInOut.outputImage(waveletReverseImage.getTransformedImage(), reverseOutputFile, "png");
    }

    public static void transformImage(File forwardInputFile) {
        ImageInOut imageInOut = new ImageOpenSave();
        BufferedImage forwardImage = imageInOut.inputImage(forwardInputFile);
        WaveletImage waveletForwardImage = new WaveletBufferedImage(forwardImage, TransformType.FORWARD, WaveletType.HAAR, 2);
        File forwardOutputFile = new File(OUT_PATH + File.separator + forwardInputFile.getName());
        imageInOut.outputImage(waveletForwardImage.getTransformedImage(), forwardOutputFile, "png");
    }
}
