package com.objdetection;

import android.graphics.Rect;
import android.util.Log;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions;

import java.io.IOException;

public class CustomObjectDetectionModule extends ReactContextBaseJavaModule {

    @Override
    public String getName() {
        return "CustomObjectDetectionModule";
    }

    private ObjectDetector objectDetector;
    private final ReactApplicationContext reactContext;

    // Initialize variables to track the label with the highest confidence
    String labelWithHighestConfidence = null;
    float highestConfidence = 0.8f;


    public CustomObjectDetectionModule(ReactApplicationContext context) {
        super(context);
        this.reactContext = context;
        configureObjectDetector();
    }

    private void configureObjectDetector() {
        // Initialize your custom LocalModel by specifying the path to model file.
        LocalModel localModel =
                new LocalModel.Builder()
                        .setAssetFilePath("model.tflite")
                        .build();

        CustomObjectDetectorOptions customObjectDetectorOptions =
                new CustomObjectDetectorOptions.Builder(localModel)
                        .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()
                        .setClassificationConfidenceThreshold(0.8f)
                        .setMaxPerObjectLabelCount(3)
                        .build();

        objectDetector = ObjectDetection.getClient(customObjectDetectorOptions);
    }

    @ReactMethod
    public void startCustomObjectDetection(String imagePath, Promise promise) {
        InputImage image = null;
        try {
            image = InputImage.fromFilePath(reactContext, android.net.Uri.parse(imagePath));
            Log.d(image.toString(), "image: ");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // process image
        objectDetector
                .process(image)
                .addOnSuccessListener(results -> {
                    highestConfidence = 0.0f;
                    if (results.isEmpty()) {
                        promise.reject("ObjectError", "No Objects Detected");
                    }
                    for (DetectedObject detectedObject : results) {
                        Log.d(results.toString(), "ObjectREsult: ");
                        Rect boundingBox = detectedObject.getBoundingBox();
                        Integer trackingId = detectedObject.getTrackingId();
                        for (DetectedObject.Label label : detectedObject.getLabels()) {
                            String text = label.getText();
                            int index = label.getIndex();
                            float confidence = label.getConfidence();
                            Log.d(text, "textObjects: " + confidence);
                            if (confidence > highestConfidence) {
                                highestConfidence = confidence;
                                labelWithHighestConfidence = text;
                            }
                        }
                    }
                    if (labelWithHighestConfidence != null) {
                        promise.resolve(labelWithHighestConfidence);
                        Log.d("HighestConfidence", labelWithHighestConfidence + " with confidence " + highestConfidence);
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    promise.reject("Error", "No Objects Detected");
                });
    }
}
