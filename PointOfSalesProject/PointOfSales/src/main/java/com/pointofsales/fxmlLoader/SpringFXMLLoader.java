package com.pointofsales.fxmlLoader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Getter;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class SpringFXMLLoader {
    private final ConfigurableApplicationContext context;

    @Getter
    private FXMLLoader loader;

    public SpringFXMLLoader(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public Parent load(String fxmlPath) throws IOException {
        loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(context::getBean);  // ✅ Let Spring handle it
        return loader.load();
    }

}
