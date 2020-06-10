package ru.itis.converter.example;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static ru.itis.converter.example.MainView.OUT_PATH;
import static ru.itis.converter.example.MainView.REVERSE_PATH;
import static ru.itis.converter.example.Transformer.reverseTransform;

@Route(ImageView.ROUTE)
public class ImageView extends VerticalLayout implements HasUrlParameter<String> {
    String fileName;
    static final String ROUTE = "image_view";
    Image before = new Image();
    Image after = new Image();

    public ImageView() {

        Button backButton = new Button("Назад");
        backButton.addClickListener(event ->
                UI.getCurrent().navigate(ImagesPage.ROUTE));

        final Button homeButton = new Button("Главная");
        homeButton.addClickListener(event ->
                UI.getCurrent().navigate(""));
        add(backButton, homeButton);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        initImage(MainView.IN_PATH, before);
        initImage(OUT_PATH, after);



        Dialog dialog = new Dialog();
        final Button close = new Button("Close");
        close.addClickListener(event -> dialog.close());
        final Image image = new Image();
        final VerticalLayout verticalLayout = new VerticalLayout(image, close);
        dialog.add(verticalLayout);
        dialog.setWidth("90%");
        dialog.setHeight("90%");

        after.addClickListener(event -> {
            Notification.show("Начинаем  реверс преобразование!");
            reverseTransform( new File(String.format("%s%s%s", OUT_PATH, File.separator, fileName)));
            Notification.show("готово");
            Notification.show("Загружаем");
            initImage(REVERSE_PATH,image);
            dialog.open();
        });
        add(before, new Label("До преобразования "), after, new Label("после преобразования"));
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        fileName = parameter;
    }

    private void initImage(String path, Image image) {
        try {
            File file = new File(String.format("%s%s%s", path, File.separator, fileName));
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
            image.getElement().setAttribute("src", new StreamResource(
                    file.getName(), () -> new ByteArrayInputStream(bytes)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}