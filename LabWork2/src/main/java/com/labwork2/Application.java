package com.labwork2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Application extends javafx.application.Application {
    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("access_key", "854e02f58534616ef44487c12ab2d1cf");
            //connection.setRequestProperty("symbols", "AAPL");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            String inputLine;
            final StringBuilder content = new StringBuilder();
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                //return content.toString();
            } catch (final Exception ex) {
                ex.printStackTrace();
                //return "";
            }
            //Send request
            //DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            //wr.writeBytes(urlParameters);
            //wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
