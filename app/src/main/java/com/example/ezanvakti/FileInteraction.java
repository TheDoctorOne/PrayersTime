package com.example.ezanvakti;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileInteraction {

    Context ctx;

    String fileName = "data";

    FileInteraction(Context ctx){
        this.ctx = ctx;
    }

    public void fileWrite(ArrayList<String> clocks, String cityName, String curDate) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(ctx.openFileOutput(fileName,Context.MODE_PRIVATE));
        String s = cityName + "\n" + curDate + "\n" + clocks.get(0) + "\n" + clocks.get(1) + "\n" + clocks.get(2) + "\n" + clocks.get(3) + "\n" + clocks.get(4) + "\n" + clocks.get(5);
        osw.write(s);

        osw.close();
    }


    public void fileRead() throws IOException {
        InputStream fis = ctx.openFileInput(fileName);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String toIn = br.readLine();
        MainActivity.SELECTED_CITY = Integer.parseInt(toIn);
        String curDat = br.readLine();
        if(!MainActivity.CUR_DATE.equals(curDat)){
            MainActivity.CUR_DATE = curDat;
            MainActivity.setClocks();
        }

        while((toIn = br.readLine()) != null){
            MainActivity.saatler.add(toIn);
        }

        if(MainActivity.saatler.size() < 6){
            MainActivity.setClocks();
            fileRead();
        } else
            MainActivity.handler.sendEmptyMessageDelayed(1,1);


    }

    public boolean doesExist(){
        File file = ctx.getFileStreamPath(fileName);
        System.out.println("VAR MIYIM ? : : "+file.exists());
        if (file.exists())
            return true;

        return false;
    }




}
