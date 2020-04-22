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
        Log.d("uprava", String.valueOf(Prepocitej(DrawView.MujLevHorBody[0])));



        graphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        if (ZaberText == true) {
            for (int i = 0; i < items.size(); i++) {
                TextBlock item = items.valueAt(i);
                if (item != null && item.getValue() != null) {
                    Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());
                    OcrGraphic graphic = new OcrGraphic(graphicOverlay, item);
                    graphicOverlay.add(graphic);
                    if (Prepocitej(DrawView.MujLevHorBody[0]) < OcrGraphic.Zleva && Prepocitej(DrawView.MujPravHorBody[0]) > OcrGraphic.Zprava && Prepocitej(DrawView.MujLevHorBody[1]) < OcrGraphic.Zhora && Prepocitej(DrawView.MujLevDolBody[1]) > OcrGraphic.Zdola)
                    {
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
