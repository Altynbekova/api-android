package com.example.vk_id.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    public static final String SERVER = "https://jsonplaceholder.typicode.com/";
    public static final String RESOURCE = "users/";

    public static URL generateUrl(String userId){
        Uri uri = Uri.parse(SERVER + RESOURCE)
                .buildUpon()
                .appendPath(userId)
//                .appendQueryParameter("users_id", userId)
                .build();

        URL url;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    public static String getResponse(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {

        httpURLConnection.disconnect();
        }
    }
}
