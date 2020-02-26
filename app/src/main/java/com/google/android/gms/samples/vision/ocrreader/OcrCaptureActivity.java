/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.ocrreader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.CameraSource;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.CameraSourcePreview;
import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.os.Handler;


/**
 * Activity for the Ocr Detecting app.  This app detects text and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and contents of each TextBlock.
 */
public final class OcrCaptureActivity extends AppCompatActivity {
    private static final String TAG = "OcrCaptureActivity";

    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    // Constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    public static final String TextBlockObject = "String";

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay<OcrGraphic> graphicOverlay;

    // Helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    // A TextToSpeech engine for speaking a String value.
    private TextToSpeech tts;

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    final Handler handler = new Handler();
    public static Handler handler2 = new Handler();

    private ImageButton NastavBut;
   // public static EditText PreviewPole; // Zpřístupnit z OCRprocessor
    public static FloatingActionButton SnimaciBut;
    public static  String TextProUpravu;
    public static  final  String EXTRA_TEXT = "TextID"; //com.example.application.example.EXTRA_TEXT
    public static  ImageButton HistoryBut;
    public static  FloatingActionButton ZoomButPlus;
    public static  FloatingActionButton ZoomButMinus;
    public static ConstraintLayout CaptureLayout;

    private DatabaseReference mainDatabase;
    public static DatabaseReference garbageDatabase;


    int height;
    int width;
    int realHeight;

    public static boolean autoFocus;
    public static boolean useFlash;

    public static ArrayList<String> baf = new ArrayList<String>();
    public static int testint = 0;

    public static int Poc = 0;
    public static String string1;

    public static Context mContext;
    public static String TenhleVysledekFaktPlati;
    public boolean MamInternet;
    public static boolean MuzuResit;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ocr_capture);


        //PreviewPole = (EditText) findViewById(R.id.UlohaField)
        SnimaciBut = (FloatingActionButton) findViewById(R.id.CaptureButFloat);
        SnimaciBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snimej();
            }
        });

        HistoryBut = (ImageButton) findViewById(R.id.HistoryTlac);
        HistoryBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistory();
            }
        });

        NastavBut = (ImageButton) findViewById(R.id.SettingsTlac);
        NastavBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNastaveni();
            }
        });
        ZoomButPlus = (FloatingActionButton) findViewById(R.id.ZoomPlusBut);
        ZoomButPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSource.doZoom(1.01f);
            }
        });
        ZoomButMinus = (FloatingActionButton) findViewById(R.id.ZoomMinusBut);
        ZoomButMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSource.doZoom(0.4f);
            }
        });




        CaptureLayout = (ConstraintLayout) findViewById(R.id.topLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(CaptureLayout);

      /*  Resources r = getResources();
        float dip = 400f;
        float px400 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        Log.d("testik", String.valueOf(width));*/




        if (width == 0 && height == 0) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
            realHeight = height + getNavigationBarHeight();
        }

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Landscape
            constraintSet.setMargin(SnimaciBut.getId(),ConstraintSet.TOP, Math.round(realHeight/ 2f));
            constraintSet.setMargin(SnimaciBut.getId(),ConstraintSet.BOTTOM, Math.round(180));
            constraintSet.setMargin(ZoomButPlus.getId(),ConstraintSet.TOP, Math.round(width/2f));
            constraintSet.setMargin(ZoomButMinus.getId(),ConstraintSet.TOP, Math.round(width/2f));
            constraintSet.setMargin(ZoomButPlus.getId(),ConstraintSet.START, Math.round(0));
            constraintSet.setMargin(ZoomButPlus.getId(),ConstraintSet.END, Math.round(realHeight/2.5f));
            constraintSet.setMargin(ZoomButMinus.getId(),ConstraintSet.START, Math.round(realHeight/2.5f));
            constraintSet.setMargin(ZoomButMinus.getId(),ConstraintSet.END, Math.round(0));
            constraintSet.applyTo(CaptureLayout);
        } else {
            //Portrait
            constraintSet.setMargin(SnimaciBut.getId(),ConstraintSet.TOP, Math.round(realHeight/1.7f));
            constraintSet.setMargin(ZoomButPlus.getId(),ConstraintSet.TOP, Math.round(realHeight/200f));
            constraintSet.setMargin(ZoomButMinus.getId(),ConstraintSet.TOP, Math.round(realHeight/5f));
            constraintSet.setMargin(ZoomButPlus.getId(),ConstraintSet.END, Math.round(width/8f));
            constraintSet.setMargin(ZoomButMinus.getId(),ConstraintSet.END, Math.round(width/8f));
            constraintSet.applyTo(CaptureLayout);
        }

        preview = (CameraSourcePreview) findViewById(R.id.preview);
        graphicOverlay = (GraphicOverlay<OcrGraphic>) findViewById(R.id.graphicOverlay);

        // Set good defaults for capturing text.
        autoFocus = true;
        useFlash = false;

        //DatabaseHandler databaseHandler = new DatabaseHandler(this);





        // Tohle je starej lehce funkcni model
        /*for (int fn = 0; fn <= 577; fn ++) {
            mainDatabase = FirebaseDatabase.getInstance().getReference(String.valueOf(fn));
            DatabaseReference mainDatabase1 = mainDatabase.child("Zadani");

            // Read from the database
            mainDatabase1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    Log.v("Cteni", "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.v("Cteni", "Failed to read value.", error.toException());
                }
            });

        }*/




        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }



        gestureDetector = new GestureDetector(this, new CaptureGestureListener());
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


       /* Snackbar.make(graphicOverlay, "Tap to Speak. Pinch/Stretch to zoom",
                Snackbar.LENGTH_LONG)
                .show();*/

        // Set up the Text To Speech engine.
        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("OnInitListener", "Text to speech engine started successfully.");
                            tts.setLanguage(Locale.US);
                        } else {
                            Log.d("OnInitListener", "Error starting the text to speech engine.");
                        }
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(), listener);
        Log.d("internet",String.valueOf(isOnline()));

        if (isOnline() == true) {
            MuzuResit = true;
            CtiZdatabaze();
        }  else  {
            MuzuResit = false;
            Toast.makeText(this, "Internetové připojení nebylo nalezeno. Připojte se prosím k internetu a restartujte aplikaci.", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static void CtiZdatabaze() {

        final DatabaseReference HlavniDatabaze = FirebaseDatabase.getInstance().getReference();

        final ArrayList<String> local = new ArrayList<String>();

        HlavniDatabaze.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String value = String.valueOf(dataSnapshot1.child("Zadani").getValue());


                    local.add(value);

                    Ukradni(local);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private static int countWords(String str){

        int wordCount = 0;

        //if string is null, word count is 0
        if(str == null)
            return wordCount;

        //remove leading and trailing white spaces first
        str = str.trim();

        //if string contained all the spaces, word count is 0
        if(str.equals(""))
            return wordCount;

        //split the string by one or more space
        String[] temp = str.split("\\s+");

        //number of array elements is equal to the words
        return temp.length;
    }

    public static void Ukradni(ArrayList<String> Ojebavac) {
       baf = Ojebavac;
    }


    public static void RozdelNaSlova(String ToCoChciRozdelit) {

        if (MuzuResit == true) {

            ArrayList<Double> podobnList = new ArrayList<Double>();

            for (int qu = 0; qu <= baf.size() - 1; qu++) {
                podobnList.add(cosineSimilarity(baf.get(qu), ToCoChciRozdelit));
            }

            double Max = Collections.max(podobnList);
            Log.d("test", "Nasel jsem max shodu : " + String.valueOf(Max));

            if (Max < 0.7) {
                Log.d("bohuzel", "toto nejde vyresit"); // stranka strasne nas to mrzi, work in progress
                Intent ZamerMrziNasTo = new Intent(mContext, MocNasToMrzi.class);
                mContext.startActivity(ZamerMrziNasTo);
            } else {
                Log.d("skvele", "toto jde vyresit"); // jdeme na to

                int indexik = 0;

                for (int psat = 0; psat <= podobnList.size() - 1; psat++) {
                    if (podobnList.get(psat) == Max) {
                        indexik = psat;
                        Log.d("test", "nasel jsem na miste : " + String.valueOf(indexik));
                        break;
                    }
                }

                NajdiVysledek(indexik);
            }
        } else {
            Intent Zamer2 = new Intent(mContext, MocNasToMrzi.class);
            mContext.startActivity(Zamer2);
        }



    }

   public static void NajdiVysledek(int Indexicek) {

        int id = Indexicek;
       DatabaseReference VysledekDatabaze;
       VysledekDatabaze = FirebaseDatabase.getInstance().getReference().child(String.valueOf(id));

       VysledekDatabaze.child("Vysledek").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String TenVysledek = String.valueOf(dataSnapshot.getValue());
               Log.d("VasVysledek", "Vas vysledek je : " + TenVysledek);
               Finale(TenVysledek);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });






    }

    public static void Finale(String UzKonecneVysledek) {
        TenhleVysledekFaktPlati = UzKonecneVysledek;
        Intent Zamer = new Intent(mContext, ShowVysledek.class);
        //Zamer.putExtra("Vysledecek", UzKonecneVysledek);
        mContext.startActivity(Zamer);

    }



    public static Map<String, Integer> getTermFrequencyMap(String[] terms) {
        Map<String, Integer> termFrequencyMap = new HashMap<>();
        for (String term : terms) {
            Integer n = termFrequencyMap.get(term);
            n = (n == null) ? 1 : ++n;
            termFrequencyMap.put(term, n);
        }
        return termFrequencyMap;
    }

    /**
     * @param text1
     * @param text2
     * @return cosine similarity of text1 and text2
     */
    public static double cosineSimilarity(String text1, String text2) {
        //Get vectors
        Map<String, Integer> a = getTermFrequencyMap(text1.split("\\W+"));
        Map<String, Integer> b = getTermFrequencyMap(text2.split("\\W+"));

        //Get unique words from both sequences
        HashSet<String> intersection = new HashSet<>(a.keySet());
        intersection.retainAll(b.keySet());

        double dotProduct = 0, magnitudeA = 0, magnitudeB = 0;

        //Calculate dot product
        for (String item : intersection) {
            dotProduct += a.get(item) * b.get(item);
        }

        //Calculate magnitude a
        for (String k : a.keySet()) {
            magnitudeA += Math.pow(a.get(k), 2);
        }

        //Calculate magnitude b
        for (String k : b.keySet()) {
            magnitudeB += Math.pow(b.get(k), 2);
        }

        //return cosine similarity
        return dotProduct / Math.sqrt(magnitudeA * magnitudeB);
    }






    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
        }
    }

    public void openHistory() {
        Intent intent = new Intent(this, HistorieActivity.class);
        startActivity(intent);
    }


    public void openNastaveni() {
       Intent intent = new Intent(this, Nastav.class);
       startActivity(intent);

    }

    public  void Snimej() {
        OcrDetectorProcessor.ZaberText = true;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextProUpravu = OcrDetectorProcessor.GlobalUlohaText;
                OcrDetectorProcessor.ZaberText = false;

                NastartujProces();
            }
        },450);

    }

    public  void NastartujProces() {
            Intent intent2 = new Intent(this, UpravaZadaniActivity.class);
            intent2.putExtra(EXTRA_TEXT, TextProUpravu);
            startActivity(intent2);
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(graphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean b = scaleGestureDetector.onTouchEvent(e);

        boolean c = gestureDetector.onTouchEvent(e);

        return b || c || super.onTouchEvent(e);
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // A text recognizer is created to find text.  An associated multi-processor instance
        // is set to receive the text recognition results, track the text, and maintain
        // graphics for each text block on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each text block.
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        textRecognizer.setProcessor(new OcrDetectorProcessor(graphicOverlay));

        if (!textRecognizer.isOperational()) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the text recognizer to detect small pieces of text.
        cameraSource =
                new CameraSource.Builder(getApplicationContext(), textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024) // rozliseni (puvodni 1280 * 1024)
                .setRequestedFps(2.0f)
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO : null)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (preview != null) {
            preview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preview != null) {
            preview.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus,true);
            boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (cameraSource != null) {
            try {
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    /**
     * onTap is called to speak the tapped TextBlock, if any, out loud.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the tap was on a TextBlock
     */
    private boolean onTap(float rawX, float rawY) {
        OcrGraphic graphic = graphicOverlay.getGraphicAtLocation(rawX, rawY);
        TextBlock text = null;
        if (graphic != null) {
            text = graphic.getTextBlock();
            if (text != null && text.getValue() != null) {
                Log.d(TAG, "text data is being spoken! " + text.getValue());
                // Speak the string.
                tts.speak(text.getValue(), TextToSpeech.QUEUE_ADD, null, "DEFAULT");
            }
            else {
                Log.d(TAG, "text data is null");
            }
        }
        else {
            Log.d(TAG,"no text detected");
        }
        return text != null;
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         * <p/>
         * Once a scale has ended, {@link ScaleGestureDetector#getFocusX()}
         * and {@link ScaleGestureDetector#getFocusY()} will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         *                 retrieve extended info about event state.
         */
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.d("testik",String.valueOf(detector.getScaleFactor()));
            if (cameraSource != null) {
                cameraSource.doZoom(1.0f);
            }
        }
    }
}
