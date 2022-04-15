import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;


public class Main {
    private static final String targetUrl = "https://api.nasa.gov/planetary/apod?api_key=";
    private static final String key = "8Fh0r3dmQQUnatKFabMwXGhDOlm2qIjcRw7h92bN";
    private static final String uri = targetUrl + key;

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        //Создание метода в который добавим и настроим класс CloseableHttpClient например с помощью builder
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        //Добавьте объект запроса HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=ВАШ_КЛЮЧ")
        // и вызовите удаленный сервис CloseableHttpResponse response = httpClient.execute(request);
        HttpGet request = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(request);

//        Создадим в классе Main.java, json mapper: А в методе main добавим код для преобразования json в java:
//        Выведем список постов на экран:
        //Это хорошо известная проблема со стиранием типов в Java: T — это просто переменная типа, и вы должны указать
        // фактический класс, обычно в качестве аргумента класса. Без такой информации лучшее, что можно сделать, — это
        // использовать границы; и простой T примерно такой же, как «T extends Object». Затем Джексон свяжет объекты
        // JSON как карты.
        //В этом случае метод тестера должен иметь доступ к классу, и вы можете построить
//        JavaType type = mapper.getTypeFactory().
//                constructCollectionType(List.class, Cat.class);
//либо же вместо new TypeReference<List<Cat>>() {} ===== type (из строки 48)

        //Преобразование json в java-объект;
        NodeNasa posts = mapper.readValue(response.getEntity().getContent(), new TypeReference<NodeNasa>() {
        });
//        System.out.println(posts);

        //В java-объекте найдите поле url и сделайте с ним еще один http-запрос с помощью уже созданного httpClient;

        //url address imagine

        HttpGet req = new HttpGet(posts.getUrl());
        //ответ json формата
        response = httpClient.execute(req);

        //абстрактный класс , представляющий входной поток байтов.
        InputStream asd = response.getEntity().getContent();
        //создание файла для записи
        FileOutputStream fos = new FileOutputStream("PIA25175_1053.jpg");
        byte[] buffer = new byte[1];
        //Считывает некоторое количество байтов из входного потока и сохраняет их в буферном массиве buffer
        int c = asd.read(buffer);
        while (c > 0) {
            fos.write(buffer, 0, c);
            c = asd.read(buffer);
        }
        fos.flush();
        fos.close();
    }
}
