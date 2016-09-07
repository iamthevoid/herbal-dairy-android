package com.iam.herbaldairy.util;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class WikiSearch {

    public static String wikipediaRuSearch = "https://ru.wikipedia.org/w/api.php?action=opensearch&format=json&formatversion=2&search=%searchString%&namespace=0&limit=10&suggest=true";
    public static String wikipediaTemplate = "%searchString%";

    private String name;
    private String description;
    private String url;

    public WikiSearch(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String url() {
        return url;
    }

    public static String search(String searchString) {
        try {
            return Web.GET(wikipediaRuSearch.replace(wikipediaTemplate, URLEncoder.encode(searchString, "UTF-8")), null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Search string must not be empty");
    }

    public static ArrayList<WikiSearch> results(String s) {
        if (s.length() > 0) {
            try {
                JSONArray j = new JSONArray(s);

                JSONArray jNames = j.getJSONArray(1);
                System.out.println(jNames);
                JSONArray jDescriptions = j.getJSONArray(2);
                JSONArray jURLs = j.getJSONArray(3);
                final int searchResultsCount = jNames.length();
                if (searchResultsCount == 0) {
                    return new ArrayList<>();
                } else if (searchResultsCount == jDescriptions.length() && searchResultsCount == jURLs.length()) {
                    ArrayList<WikiSearch> result = new ArrayList<>();
                    for (int i = 0; i < searchResultsCount; i++) {
                        String description = jDescriptions.getString(i);
                        if (description.contains("растен") || description.contains("трав")) {
                            System.out.println("gay");
                            result.add(new WikiSearch(jNames.getString(i), description, jURLs.getString(i)));
                        }
                    }
                    return result;
                } else {
                    return new ArrayList<>();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return new ArrayList<>();
        }
        throw new RuntimeException("Wikipedia returns bad search result. JSON parse exception. String: " + s);
    }
}