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

import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.widget.EditText;

import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import android.widget.EditText;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

import customview.ColorBall;
import customview.DrawView;

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */


public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OcrGraphic> graphicOverlay;
    public static String GlobalUlohaText;



    public  static  boolean ZaberText = false;
    public ArrayList<Float> srovnano = new ArrayList<Float>();
    public ArrayList<Integer> srovnanoPodruhe = new ArrayList<Integer>();
    public RectF NasCtverecicek;

    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        graphicOverlay = ocrGraphicOverlay;
    }




    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */

    double MaxHod1 = 0;
    double MaxHod2 = 0;

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
       // EditText simpleEditText = (EditText) findViewById(R.id.UlohaField);
       MaxHod1 = OcrCaptureActivity.width * 1.115; // rozsah Fňjů
       MaxHod2 = OcrCaptureActivity.width * 0.655; // rozsah Bžjů

        //float outgoing = (float) (0 + (MaxHod2 - 0) * ((1100 - 0) / (MaxHod1 - 0)));

       Log.d("wert", String.valueOf(OcrGraphic.ctverce));

       if (OcrGraphic.ctverce.size() > 0) {

           //Log.d("wert", String.valueOf(OcrGraphic.ctverce.get(0).left) + " " + String.valueOf(OcrGraphic.ctverce.get(0).right) + " " + String.valueOf(OcrGraphic.ctverce.get(0).top));
           for (int qw = 0; qw <= OcrGraphic.ctverce.size(); qw++) {
             if (srovnano.size() < OcrGraphic.ctverce.size() ) {
                 srovnano.add(OcrGraphic.ctverce.get(qw).left - DrawView.MujLevHorBody[0]);
             }
           }
           Log.d("srovnano", "neusporadano : " + String.valueOf(srovnano));
           for (int bz = 0; bz <= srovnano.size(); bz ++) {
               if (srovnanoPodruhe.size() < OcrGraphic.ctverce.size()) {
                   int mezi = Math.round(srovnano.get(bz));
                   srovnanoPodruhe.add(mezi * mezi);
               }
           }
               Collections.sort(srovnanoPodruhe);
               Log.d("srovnano", "usporadano : " + String.valueOf(srovnanoPodruhe));
               double odmocninka =  Math.sqrt(srovnanoPodruhe.get(0));
               double KporovnaniPlus = odmocninka + DrawView.MujLevHorBody[0];
               double KporovnaniMinus = odmocninka * -1 + DrawView.MujLevHorBody[0];
                //furt facha
               double NasCtverecek = 0;

               for (int qrt = 0; qrt <= OcrGraphic.ctverce.size(); qrt++) {
                   if (KporovnaniPlus == OcrGraphic.ctverce.get(qrt).left || KporovnaniMinus == OcrGraphic.ctverce.get(qrt).left) {
                        NasCtverecek = qrt;
                        Log.d("tady", "shoda");
                        break;
                   }
               }
               int poziceVarray = (int)NasCtverecek;
               NasCtverecicek = OcrGraphic.ctverce.get(poziceVarray);

           Log.d("ulohicka", "Nas X leva : " +  Prepocitej(DrawView.MujLevHorBody[0]) + " x prava : " + Prepocitej(DrawView.MujPravHorBody[0]));
           Log.d("ulohicka", "Auto X leva : " + NasCtverecicek.left + " x prava " + NasCtverecicek.right);
       }



        graphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        if (ZaberText == true) {
            for (int i = 0; i < items.size(); i++) {
                TextBlock item = items.valueAt(i);
                if (item != null && item.getValue() != null) {
                    Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());
                    OcrGraphic graphic = new OcrGraphic(graphicOverlay, item);
                    graphicOverlay.add(graphic);

                    if(Prepocitej(DrawView.MujLevHorBody[0]) <= NasCtverecicek.left && Prepocitej(DrawView.MujPravHorBody[0]) >= NasCtverecicek.right) {
                        Log.d("ulohicka", "hotovo");
                        GlobalUlohaText += item.getValue();
                    }
                    //OcrCaptureActivity.PreviewPole.setText(GlobalUlohaText);
                }

            }
            ZaberText = false;
        }



    }

    float Prepocitej(int vstup) {
        return  (float) (0 + (MaxHod2 - 0) * ((vstup - 0) / (MaxHod1 - 0)));
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        graphicOverlay.clear();
    }
}
