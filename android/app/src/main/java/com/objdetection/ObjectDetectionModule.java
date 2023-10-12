package com.objdetection;

import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.defaults.PredefinedCategory;

import java.io.IOException;

public class ObjectDetectionModule extends ReactContextBaseJavaModule {
    @Override
    public String getName() {
        return "MyObjectDetection";
    }

    private final ReactApplicationContext reactContext;
    private ObjectDetector objectDetector;

    public ObjectDetectionModule(ReactApplicationContext context) {
        super(context);
        this.reactContext = context;
        configureObjectDetector();
    }

    private void configureObjectDetector() {
        // Configure object detector for your use case
        ObjectDetectorOptions options =
                new ObjectDetectorOptions.Builder()
                        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()  // Optional
                        .build();

        // Get an instance of ObjectDetector:
        objectDetector = ObjectDetection.getClient(options);
    }


    @ReactMethod
    public void startObjectDetection(String imagePath) {
        Log.d("MyObjectDetectionModule", "Received imagePath: " + imagePath);
        InputImage image = null;
        try {
            image = InputImage.fromFilePath(reactContext, android.net.Uri.parse(imagePath));
            Log.d(image.toString(), "image: ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Process the image using the ObjectDetector
        objectDetector.process(image)
                .addOnSuccessListener(detectedObjects -> {
                    for (DetectedObject detectedObject : detectedObjects) {
                        Rect boundingBox = detectedObject.getBoundingBox();
                        Integer trackingId = detectedObject.getTrackingId();
                        Log.d("out_loop", detectedObject.toString());

                        for (DetectedObject.Label label : detectedObject.getLabels()) {
                            Log.d("DetectedObject", detectedObject.toString());
                            String ObjectLabel = label.getText();
                            Log.d("Detected Text", ObjectLabel);
                            if (PredefinedCategory.FOOD.equals(ObjectLabel)) {
                                // When a "FOOD" object is detected, you can perform specific actions.
                            }
                            if (PredefinedCategory.FASHION_GOOD.equals(ObjectLabel)) {
                                // When a "FASHION_GOOD" object is detected, you can perform specific actions.
                            }
                            int index = label.getIndex();
                            float confidence = label.getConfidence();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("onFailure", "onFailure:" + e);
                        e.printStackTrace();
                    }
                });

    }
}
