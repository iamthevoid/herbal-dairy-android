package com.iam.herbaldairy.util;

import android.icu.text.StringSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Web {

    public static class url {
        public static String ruslatGoogleTranslate = "https://translate.google.ru/#ru/la/";
        public static String wikipediaRu = "https://ru.wikipedia.org/wiki/";
    }

    public static String GET(String s, SocketAddress socketAddress) {
        try {
            StringBuilder content = new StringBuilder();

            URL url = new URL(s);

            Proxy proxy = socketAddress == null ? Proxy.NO_PROXY : new Proxy(Proxy.Type.HTTP, socketAddress);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(proxy);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
            bufferedReader.close();
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
