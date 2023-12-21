package com.exlabwork2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NasdaqApiClient {
    static String apiKey = "UQCTUiCi3sUyrZzXV7BK"; // UQCTUiCi3sUyrZzXV7BK
    String baseUrl = "https://data.nasdaq.com/api/v3/";


    public static List<NasdaqDatabase> getDataBases() throws IOException {
        List<NasdaqDatabase> databases = null;
        try {
            // Указываем URL для запроса
            String url = "https://data.nasdaq.com/api/v3/databases" + "?api_key=" + apiKey;
            // Создаем объект URL
            URL apiUrl = new URL(url);

            // Открываем соединение
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Устанавливаем метод запроса
            connection.setRequestMethod("GET");

            // Получаем ответ от сервера
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Если ответ успешный, читаем данные
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(apiUrl);
                JsonNode databasesNode = jsonResponse.get("databases");

                // Десериализация в список объектов Database
                databases = objectMapper.readValue(databasesNode.toString(), new TypeReference<List<NasdaqDatabase>>() {
                });

                // Теперь у вас есть список объектов Database, с которым можно работать
                for (NasdaqDatabase database : databases) {
                    System.out.println(database.getName());
                }
            } else {
                System.out.println("Ошибка. Код ответа: " + responseCode);
            }

            // Закрываем соединение
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return databases;
    }

    public static List<NasdaqDataset> getDataSets(NasdaqDatabase db) {
        if(db == null){
            return null;
        }
        return getDataSets(db.getDatabaseCode());
    }
    public static List<NasdaqDataset> getDataSets(String db) {
        List<NasdaqDataset> datasets = null;
        try {
            // Указываем URL для запроса
            String url = "https://data.nasdaq.com/api/v3/datasets?database_code=" + db + "&api_key=" + apiKey;
            // Создаем объект URL
            URL apiUrl = new URL(url);

            // Открываем соединение
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Устанавливаем метод запроса
            connection.setRequestMethod("GET");

            // Получаем ответ от сервера
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Если ответ успешный, читаем данные
                ObjectMapper objectMapper = new ObjectMapper();

                int totalPages = 0;
                int currentPage = 0;

                JsonNode jsonResponse = objectMapper.readTree(apiUrl);
                // Извлекаем данные из "meta"
                JsonNode metaNode = jsonResponse.get("meta");
                if (metaNode != null) {
                    totalPages = metaNode.get("total_pages").asInt();
                    currentPage = metaNode.get("current_page").asInt();
                }

                JsonNode databasesNode = jsonResponse.get("datasets");
                // Десериализация в список объектов Database
                datasets = objectMapper.readValue(databasesNode.toString(), new TypeReference<List<NasdaqDataset>>() {
                });
                if (currentPage == totalPages) {
                    // Закрываем соединение
                    connection.disconnect();
                    return datasets;
                } else {
                    for (int i = 2; i < totalPages+1; i++) {
                        var newUrl = url + "&page=" + i;
                        apiUrl = new URL(newUrl);
                        connection = (HttpURLConnection) apiUrl.openConnection();
                        connection.setRequestMethod("GET");
                        responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            jsonResponse = objectMapper.readTree(apiUrl);
                            databasesNode = jsonResponse.get("datasets");
                            var tempDataset = objectMapper.readValue(databasesNode.toString(), new TypeReference<List<NasdaqDataset>>() {
                            });
                            datasets.addAll(tempDataset);
                        }
                        // Закрываем соединение
                        connection.disconnect();
                    }
                }

            } else {
                System.out.println("Ошибка. Код ответа: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return datasets;
    }

    public static List<NasdaqData> getData(NasdaqDataset ds) {
        List<NasdaqData> databases = null;
        try {
            // Указываем URL для запроса
            String url = "https://data.nasdaq.com/api/v3/datasets/" + ds.getDatasetCode() + "?api_key=" + apiKey;
            // Создаем объект URL
            URL apiUrl = new URL(url);

            // Открываем соединение
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Устанавливаем метод запроса
            connection.setRequestMethod("GET");

            // Получаем ответ от сервера
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Если ответ успешный, читаем данные
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(apiUrl);
                JsonNode databasesNode = jsonResponse.get("databases");

                // Десериализация в список объектов Database
                databases = objectMapper.readValue(databasesNode.toString(), new TypeReference<List<NasdaqData>>() {
                });

                // Теперь у вас есть список объектов Database, с которым можно работать
                for (NasdaqData database : databases) {
                    System.out.println(database.getName());
                }
            } else {
                System.out.println("Ошибка. Код ответа: " + responseCode);
            }

            // Закрываем соединение
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return databases;
    }

    public static NasdaqData getData(NasdaqDataset ds, String startDate, String endDate) {
        NasdaqData databases = null;
        try {
            String formattedStartDate = convertDateFormat(startDate);
            String formattedEndDate = convertDateFormat(endDate);
            // Указываем URL для запроса
            String url = "https://data.nasdaq.com/api/v3/datasets/" + ds.getDatabaseCode()+"/"+ds.getDatasetCode() +
                    "?start_date=" + formattedStartDate +
                    "&end_date=" + formattedEndDate +
                    "&api_key=" + apiKey;
            // Создаем объект URL
            URL apiUrl = new URL(url);

            // Открываем соединение
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Устанавливаем метод запроса
            connection.setRequestMethod("GET");

            // Получаем ответ от сервера
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Если ответ успешный, читаем данные
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(apiUrl);
                //JsonNode databasesNode = jsonResponse.get("databases");
                JsonNode databasesNode = jsonResponse.get("dataset");
                if(databasesNode == null){
                    return null;
                }
                // Десериализация в список объектов Database
                databases = objectMapper.readValue(databasesNode.toString(), new TypeReference<NasdaqData>() {
                });

            } else {
                System.out.println("Ошибка. Код ответа: " + responseCode);
            }

            // Закрываем соединение
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return databases;
    }

    private static String convertDateFormat(String input) throws ParseException {
        SimpleDateFormat sdfInput = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdfInput.parse(input);
        return sdfOutput.format(date);
    }
}
