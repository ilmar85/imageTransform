package ru.itis.converter.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.util.Iterator;


@Route(ImagesPage.ROUTE)
public class ImagesPage extends VerticalLayout {
    static final String ROUTE = "image_page";

    public ImagesPage() {
        final Div div = new Div();
        Button button = new Button("back");
        button.addClickListener(event -> {
            UI.getCurrent().navigate("");
        });
        div.add(button);
        final File dir = new File(MainView.OUT_PATH);
        try {
            if (dir.isDirectory()) {
                final File[] files = dir.listFiles();
                for (File file : files) {
                    final Image image = new Image();
                    byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
                    image.getElement().setAttribute("src", new StreamResource(
                            file.getName(), () -> new ByteArrayInputStream(bytes)));
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
                                image.addClickListener(event ->
                                        UI.getCurrent().navigate(
                                                String.format("%s%s%s", ImageView.ROUTE, File.separator, file.getName())));
                            } finally {
                                reader.dispose();
                            }
                        }
                    }
                    div.add(image);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(div);
    }
}
