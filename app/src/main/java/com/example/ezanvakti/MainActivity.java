package com.example.ezanvakti;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static FileInteraction fi;


    Button button;
    Spinner spinner;



    static TextView imsak;
    static TextView gunes;
    static TextView ogle;
    static TextView ikindi;
    static TextView aksam;
    static TextView yatsi;
    static TextView kalanSure;

    static String CUR_DATE = "01 01 2019";
    static int SELECTED_CITY = 1;
    static ArrayList<String> saatler = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if(!saatler.isEmpty()){
                setClocks();
            }
        }
    };

    @SuppressLint("HandlerLeak")
    static Handler handleLeftTime = new Handler() {
        @Override
        public void handleMessage(Message msg){
            String leftTime = "";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            df = new SimpleDateFormat("HH:mm");
            String currentTime = df.format(c.getTime());
                if(!saatler.isEmpty()){
                    int imsak = Integer.parseInt(saatler.get(0).split(":")[0]);
                    int gunes = Integer.parseInt(saatler.get(1).split(":")[0]);
                    int ogle = Integer.parseInt(saatler.get(2).split(":")[0]);
                    int ikindi = Integer.parseInt(saatler.get(3).split(":")[0]);
                    int aksam = Integer.parseInt(saatler.get(4).split(":")[0]);
                    int yatsi = Integer.parseInt(saatler.get(5).split(":")[0]);

                    int curTime = Integer.parseInt(currentTime.split(":")[0]);
                    if((curTime<imsak)){
                        kalanSure.setText(clockCalcMinus(saatler.get(0),currentTime));
                    } else if (curTime<gunes){
                        kalanSure.setText(clockCalcMinus(saatler.get(1),currentTime));

                    } else if (curTime<ogle) {
                        kalanSure.setText(clockCalcMinus(saatler.get(2),currentTime));

                    } else if (curTime<ikindi) {
                        kalanSure.setText(clockCalcMinus(saatler.get(3),currentTime));

                    } else if (curTime<aksam) {
                        kalanSure.setText(clockCalcMinus(saatler.get(4),currentTime));

                    } else if (curTime<yatsi) {
                        kalanSure.setText(clockCalcMinus(saatler.get(5),currentTime));
                    } else if (curTime>yatsi) {
                        String before24 = clockCalcMinus("24:00",currentTime);
                        String total = clockCalcPlus(before24,saatler.get(0));

                        kalanSure.setText(total);
                    }
                }


        }
    };

    static public void setClocks(){
        if(!saatler.isEmpty()) {
            imsak.setText(saatler.get(0));
            gunes.setText(saatler.get(1));
            ogle.setText(saatler.get(2));
            yatsi.setText(saatler.get(5));
            ikindi.setText(saatler.get(3));
            aksam.setText(saatler.get(4));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fi.fileWrite(saatler,SELECTED_CITY + "", new SimpleDateFormat("dd MM yyyy").format(Calendar.getInstance().getTime()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) this.findViewById(R.id.button);
        spinner = (Spinner) this.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_view,stringBuilder());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        imsak = (TextView) findViewById(R.id.imsak);    imsak.setText("BEKLENİYOR");
        gunes = (TextView) findViewById(R.id.gunes);    gunes.setText("BEKLENİYOR");
        ogle = (TextView) findViewById(R.id.ogle);      ogle.setText("BEKLENİYOR");
        ikindi = (TextView) findViewById(R.id.ikindi);  ikindi.setText("BEKLENİYOR");
        aksam = (TextView) findViewById(R.id.aksam);    aksam.setText("BEKLENİYOR");
        yatsi = (TextView) findViewById(R.id.yatsi);    yatsi.setText("BEKLENİYOR");
        kalanSure = (TextView) findViewById(R.id.kalanSure);
        fi = new FileInteraction(this);
        if(fi.doesExist()){
            try {
                fi.fileRead();
                spinner.setSelection(SELECTED_CITY);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new RetrieveFeedTask().execute("adana");
        }

        timeLeft();


    }

    private String[] stringBuilder(){
        String s = "Adana,Adiyaman,Afyonkarahisar,Agri,Aksaray,Amasya,Ankara,Antalya,Ardahan,Artvin,Aydin,Balikesir,Bartin,Batman,Bayburt,Bilecik,Bingol,Bitlis,Bolu,Burdur,Bursa,Canakkale,Cankiri,Corum,Denizli,Diyarbakir,Duzce,Edirne,Elazig,Erzincan,Erzurum,Eskisehir,Gaziantep,Giresun,Gumushane,Hakkari,Hatay,Igdir,Isparta,Istanbul,Izmir,Kahramanmaras,Karabuk,Karaman,Kars,Kastamonu,Kayseri,Kilis,Kirikkale,Kirklareli,Kirsehir,Kocaeli,Konya,Kutahya,Malatya,Manisa,Mardin,Mersin,Icel,Mugla,Mus,Nevsehir,Nigde,Ordu,Osmaniye,Rize,Sakarya,Samsun,Siirt,Sirnak,Sinop,Sivas,Tekirdag,Tokat,Trabzon,Tunceli,Sanliurfa,Usak,Van,Yalova,Yozgat,Zonguldak";
        s = s.toUpperCase();
        String[] cityList = s.split(",");
        return cityList;
    }

    public void getClock(View V){
        String s = spinner.getSelectedItem().toString().toLowerCase();
        SELECTED_CITY = spinner.getSelectedItemPosition();
        new RetrieveFeedTask().execute(s);


    }


    public void timeLeft(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        kalanSure.setText(formattedDate);
        kalanSure.setGravity(Gravity.CENTER);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handleLeftTime.sendEmptyMessageDelayed(1,1);
                }
            }
        }).start();

    }


    static public String clockCalcMinus(String clock1,String clock2) {
        String result = "";
        String[] temp;
        int result_min;
        int result_hour;

        temp = clock1.split(":");
        int clock1_hour = Integer.parseInt(temp[0]);
        int clock1_mins = Integer.parseInt(temp[1]);

        temp = clock2.split(":");
        int clock2_hour = Integer.parseInt(temp[0]);
        int clock2_mins = Integer.parseInt(temp[1]);

        if (clock1_mins < clock2_mins) {
            clock1_hour--;
            clock1_mins += 60;
        }

        result_min = clock1_mins - clock2_mins;
        result_hour = clock1_hour - clock2_hour;

        if (result_hour < 0) {
            result_hour *= -1;
        }

        if (result_hour < 10 && result_min < 10) {
            result = "0" + result_hour + ":" + result_min + "0";
        } else if (result_hour < 10) {
            result = "0" + result_hour + ":" + result_min;
        } else if (result_min < 10){
            result = result_hour + ":" + result_min + "0";
        } else {
        result = result_hour + ":" + result_min;
        }

        return result;
    }

    static public String clockCalcPlus(String clock1,String clock2){
        String result = "";
        String[] temp;
        int result_min;
        int result_hour;

        temp = clock1.split(":");
        int clock1_hour = Integer.parseInt(temp[0]);
        int clock1_mins = Integer.parseInt(temp[1]);

        temp = clock2.split(":");
        int clock2_hour = Integer.parseInt(temp[0]);
        int clock2_mins = Integer.parseInt(temp[1]);

        result_hour = clock1_hour + clock2_hour;
        result_min = clock1_mins + clock2_mins;

        if(result_min >= 60){
            result_hour += 1;
            result_min -= 60;
        }

        result = result_hour + ":" + result_min;

        return result;
    }


}
