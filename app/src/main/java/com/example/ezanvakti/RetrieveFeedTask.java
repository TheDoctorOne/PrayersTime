package com.example.ezanvakti;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

public class RetrieveFeedTask extends AsyncTask<String, Void, ArrayList<String>> {

    private ArrayList<String> urlList = new ArrayList<>();


    @Override
    protected ArrayList<String> doInBackground(String... s) {
        MainActivity.saatler = new ArrayList<>();
        URL url = null;
        try {
            url = new URL("http://www.hurriyet.com.tr/" + s[0] + "-namaz-vakitleri/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        try {
        while ((inputLine = in.readLine()) != null)
            if(inputLine.contains("<span class=\"pray-time\">")) {
                String[] temp = inputLine.split("<span class=\"pray-time\">");
                temp = temp[1].split("</span>");
                MainActivity.saatler.add(temp[0]);

            }
            MainActivity.handler.sendEmptyMessageDelayed(1,1);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }




}
