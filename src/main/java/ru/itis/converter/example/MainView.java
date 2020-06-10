package ru.itis.converter.example;

import com.nugumanov.wavelettransform.WaveletBufferedImage;
import com.nugumanov.wavelettransform.WaveletImage;
import com.nugumanov.wavelettransform.test.ImageInOut;
import com.nugumanov.wavelettransform.test.ImageOpenSave;
import com.nugumanov.wavelettransform.transforms.TransformType;
import com.nugumanov.wavelettransform.transforms.WaveletType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static ru.itis.converter.example.Transformer.transformImage;


/**
 * The main view contains a button and a click listener.
 */
@Route
@PWA(name = "imageTransform", shortName = "imageTransform")
public class MainView extends VerticalLayout {
    final static String IN_PATH = "C:\\work\\imageconverter\\ImagesRes\\in";
    public final static String OUT_PATH = "C:\\work\\imageconverter\\ImagesRes\\out";
    public final static String REVERSE_PATH = "C:\\work\\imageconverter\\ImagesRes\\reverse";
    protected  Image image = new Image();
    public MainView() {
        Div page = new Div();
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
            Component component = createComponent(event.getMIMEType(),
                    event.getFileName(), buffer.getInputStream(event.getFileName()));
            page.add(component);
        });
        image.addClickListener(event -> {
        });

        final Button imageButton = new Button("Загруженные изображения");
        imageButton.addClickListener(event -> UI.getCurrent().navigate("image_page"));



        add(  imageButton, page, upload);
    }


    private Component createComponent(String mimeType, String fileName,
                                      InputStream stream) {
        if (mimeType.startsWith("image")) {
            try {

                byte[] bytes = IOUtils.toByteArray(stream);
                image.getElement().setAttribute("src", new StreamResource(
                        fileName, () -> new ByteArrayInputStream(bytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(
                        new ByteArrayInputStream(bytes))) {
                    final Iterator<ImageReader> readers = ImageIO
                            .getImageReaders(in);
                    if (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(in);
                            image.setWidth(reader.getWidth(0) + "px");
                            image.setHeight(reader.getHeight(0) + "px");
                        } finally {
                            reader.dispose();
                        }
                    }
                    File file = new File(String.format("%s%s%s", IN_PATH, File.separator, fileName));
                    FileUtils.copyInputStreamToFile(new ByteArrayInputStream(bytes), file);
                    transformImage(file);
                }

                Notification.show("Uploaded!");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return image;
        }
        Div content = new Div();
        String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'",
                mimeType, MessageDigestUtil.sha256(stream.toString()));
        content.setText(text);
        return content;

    }
}
