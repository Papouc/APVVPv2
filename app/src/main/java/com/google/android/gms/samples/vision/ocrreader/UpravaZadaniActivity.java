package com.google.android.gms.samples.vision.ocrreader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Calendar;

public class UpravaZadaniActivity extends AppCompatActivity {


    public  static EditText PoleProUlohu;
    public  static Button OdstranitTextBut;
    public String NaskanovanaUloha;
    final Handler handler = new Handler();
    public static ArrayList<String> ProPridaniDoHistorie = new ArrayList<>();
    public static ContentValues Dodatabaze = new ContentValues();
    public static Button VyresBut;
    public int random;
    DatabaseHandler databaseHandler;
    char[] abeceda = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    public ImageButton infotlac ;
    public static boolean HistoriePodleNastaveni = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uprava_zadani);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        VyresBut = (Button) findViewById(R.id.ReseniBut);
        databaseHandler = new DatabaseHandler(this);
        OcrCaptureActivity.mContext = this;

        Nastav.pref = getApplicationContext().getSharedPreferences("Nastavko", 0);
        final boolean MamHistorii = Nastav.pref.getBoolean("MamZapisovatDoHistorie", true);
        HistoriePodleNastaveni = MamHistorii;

        PoleProUlohu = (EditText) findViewById(R.id.PoleProUpravu);
        Button ButtonPridat = findViewById(R.id.ReseniBut);
        Intent intent3 = getIntent();
        NaskanovanaUloha = intent3.getStringExtra(OcrCaptureActivity.EXTRA_TEXT);
        UpravTextHackyCarky();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PoleProUlohu.setText(NaskanovanaUloha);
                OdstranitTextBut = (Button) findViewById(R.id.DeleteBut);
                OdstranitTextBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VymazatZadani();
                    }
                });

            }
        }, 300);

        infotlac = (ImageButton)findViewById(R.id.InfoBut);
        infotlac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog().show();
            }
        });


        VyresBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NaskanovanaUloha != null && checkString(PoleProUlohu.getText().toString()) == true) {
                    if (HistoriePodleNastaveni == true) {
                        ZapisDoGarbage(PoleProUlohu.getText().toString());
                    }
                } else if (PoleProUlohu.getText().toString() != null) {
                   if (checkString(PoleProUlohu.getText().toString()) == true) {
                       if (HistoriePodleNastaveni == true) {
                           ZapisDoGarbage(PoleProUlohu.getText().toString());
                       }
                   } else {
                       Log.d("format", "spatny format");
                   }
                } else {
                    Log.d("format", "spatny format");
                }

                OcrCaptureActivity.RozdelNaSlova(PoleProUlohu.getText().toString());

            }
        });

    }

    public Dialog onCreateDialog() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Úloha by neměla obsahovat žádnou diakritiku, speciální znaky ani smajlíky :-).")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }


    private static boolean checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
            else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if(numberFlag && capitalFlag && lowerCaseFlag)
                return true;
        }
        return false;
    }



    public void ZapisDoGarbage(String TaUloha) {

        Date currentTime = Calendar.getInstance().getTime();
        boolean isIserted = databaseHandler.insertData(TaUloha,String.valueOf(currentTime));
        if (isIserted == true)
            //Toast.makeText(UpravaZadaniActivity.this,"Data Inserted", Toast.LENGTH_LONG).show();
            Log.d("Data", "Data inserted");
        else
            //Toast.makeText(UpravaZadaniActivity.this,"Data not Inserted", Toast.LENGTH_LONG).show();
        Log.d("Data", "Data not inserted");

       /* if (OcrCaptureActivity.DoGarbage == true) {
            OcrCaptureActivity.garbageDatabase = FirebaseDatabase.getInstance("https://apvvp-garbage.firebaseio.com/").getReference(String.valueOf(random));
            OcrCaptureActivity.garbageDatabase.setValue(TaUloha);
        }*/

    }


    public  void VymazatZadani() {
        if (NaskanovanaUloha != null) {
            OcrDetectorProcessor.GlobalUlohaText = null;
            OcrCaptureActivity.TextProUpravu = null;
            NaskanovanaUloha = null;
            PoleProUlohu.setText(null);
        } else if (PoleProUlohu.getText() != null) {
            OcrDetectorProcessor.GlobalUlohaText = null;
            OcrCaptureActivity.TextProUpravu = null;
            NaskanovanaUloha = null;
            PoleProUlohu.setText(null);
        }

    }




    public  void UpravTextHackyCarky() {

        Random rand = new Random();
        random = rand.nextInt(32000);

        if (NaskanovanaUloha != null) {

            // Ucka
            NaskanovanaUloha = NaskanovanaUloha.replace("Ù", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ú", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Û", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ü", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ư", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ů", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ū", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ů", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ų", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ű", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("ů", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ú", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ü", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ū", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ų", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ű", "u");

            // Acka
            NaskanovanaUloha = NaskanovanaUloha.replace("À", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Á", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Â", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ã", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ä", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Å", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("à", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("á", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("â", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("ã", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("ä", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("å", "a");

            // Icka
            NaskanovanaUloha = NaskanovanaUloha.replace("Ī", "I");
            NaskanovanaUloha = NaskanovanaUloha.replace("Į", "I");
            NaskanovanaUloha = NaskanovanaUloha.replace("İ", "I");
            NaskanovanaUloha = NaskanovanaUloha.replace("Í", "I");
            NaskanovanaUloha = NaskanovanaUloha.replace("ī", "i");
            NaskanovanaUloha = NaskanovanaUloha.replace("į", "i");
            NaskanovanaUloha = NaskanovanaUloha.replace("ı", "i");
            NaskanovanaUloha = NaskanovanaUloha.replace("ί", "i");
            NaskanovanaUloha = NaskanovanaUloha.replace("í", "i");

            // Ecka
            NaskanovanaUloha = NaskanovanaUloha.replace("Ē", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("ē", "e");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ė", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("ė", "e");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ę", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("ę", "e");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ě", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("ě", "e");
            NaskanovanaUloha = NaskanovanaUloha.replace("É", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("é", "e");

            // Ocka
            NaskanovanaUloha = NaskanovanaUloha.replace("Ō", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("ō", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ő", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("ő", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ó", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("ó", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("ό", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("Θ", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("ò", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("ó", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("ô", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("õ", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("ö", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ò", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ó", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ô", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("Õ", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ö", "O");

            // to co nejsou souhlasky
            NaskanovanaUloha = NaskanovanaUloha.replace("Ž", "Z");
            NaskanovanaUloha = NaskanovanaUloha.replace("ž", "z");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ť", "T");
            NaskanovanaUloha = NaskanovanaUloha.replace("ť", "t");
            NaskanovanaUloha = NaskanovanaUloha.replace("Š", "S");
            NaskanovanaUloha = NaskanovanaUloha.replace("š", "s");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ř", "R");
            NaskanovanaUloha = NaskanovanaUloha.replace("ř", "r");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ň", "N");
            NaskanovanaUloha = NaskanovanaUloha.replace("ň", "n");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ď", "D");
            NaskanovanaUloha = NaskanovanaUloha.replace("ď", "d");
            NaskanovanaUloha = NaskanovanaUloha.replace("Č", "C");
            NaskanovanaUloha = NaskanovanaUloha.replace("č", "c");
            NaskanovanaUloha = NaskanovanaUloha.replace("ý", "y");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ý", "Y");
            //NaskanovanaUloha = NaskanovanaUloha.toLowerCase();





            if (NaskanovanaUloha.charAt(0) == 'n' && NaskanovanaUloha.charAt(1) == 'u' && NaskanovanaUloha.charAt(2) == 'l' && NaskanovanaUloha.charAt(3) == 'l') {
               // Log.d("testicek",String.valueOf(NaskanovanaUloha.charAt(0)) + String.valueOf(NaskanovanaUloha.charAt(1)) + String.valueOf(NaskanovanaUloha.charAt(2)) + String.valueOf(NaskanovanaUloha.charAt(3)) );
               // Log.d("testicek",NaskanovanaUloha.substring(0,4));
                 NaskanovanaUloha = NaskanovanaUloha.replace(NaskanovanaUloha.substring(0,4),"");

            }






        }



        }

    }





