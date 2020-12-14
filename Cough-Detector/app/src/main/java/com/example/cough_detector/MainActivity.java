package com.example.cough_detector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import weka.classifiers.Classifier;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.SparseInstance;
import weka.classifiers.meta.FilteredClassifier;
import static weka.core.SerializationHelper.read;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.mfcc.*;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private static String unlabeledCSVName = null;

    private MediaRecorder recorder = null;
    private MediaPlayer   player = null;

    private CSVLoader loader;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    Classifier cls;
    Instances unlabeled;
    ArffSaver saver;


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            try {
                stopRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() throws IOException {
        recorder.stop();
        recorder.release();
        recorder = null;
        Extractor ext = null;

//        new AndroidFFMPEGLocator(this);

        try {
            ext = new Extractor(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Instance i = (Instance) ext.mfccList.get(0);
//        cls.classifyInstance()
//        List<float[]> lst = ext.mfccList;
//        for (int i = 0; i < lst.size(); i++) {
//                float[] arr = lst.get(i);
////            Log.i("stopRecording", lst.get(i));
//            for (int j = 0; j < arr.length; j++) {
//                Log.i("stopRecording","" + arr[j]);
//            }
//        }

        unlabeledCSVName = getExternalCacheDir().getAbsolutePath();
        unlabeledCSVName += "/unlabeled.csv";
        Log.i("stopRecording","Before writing to unlabeled.csv");
        try {
            FileWriter myWriter = new FileWriter(unlabeledCSVName);
            for (int i = 1; i < 21; i++)
                myWriter.write("mfcc"+ i +",");
            myWriter.write("Class");
            myWriter.write("\n");
            List<float[]> lst = ext.mfccList;
            for (int i = 0; i < lst.size(); i++) {
                float[] arr = lst.get(i);
                for (int j = 0; j < arr.length; j++) {
                    myWriter.write(arr[j]+",");
                }
//                myWriter.write("Coughing");
                myWriter.write("\n");
            }
            myWriter.close();
            Log.i("stopRecording","Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Log.i("stopRecording","After writing to unlabeled.csv");

        loader.setSource(new File(unlabeledCSVName));
        Instances data = loader.getDataSet();
        if (data.classIndex() == -1) {
            System.out.println("reset index...");
            data.setClassIndex(0);
        }
        System.out.println("numAttr: "+ data.numAttributes());
//        String arff = getExternalCacheDir().getAbsolutePath() + "/unlabeled.arff";
//        saver.setFile(new File(arff));
//        saver.setInstances(data);
//        saver.writeBatch();
        double[] predictions = new double[10];
        int i = 0;
        for (Instance ins : data) {
            try {
                 predictions[i] = cls.classifyInstance(ins);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("blah", "" + predictions[i]);
            i++;
        }
//        Instances arffInstances = saver.getInstances();
//        try {
//            unlabeled = new Instances(
//                    new BufferedReader(
//                            new FileReader(arff)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // create copy
//        Instances labeled = new Instances(unlabeled);
//
        // label instances
//        for (int i = 0; i < data.numInstances(); i++) {
////            double clsLabel = 21;
//            try {
////                clsLabel = cls.classifyInstance(data.instance(i));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            data.instance(i).setClassValue(clsLabel);
//        }
        // save labeled data
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(unlabeledCSVName));
        writer.write(data.toString());


    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        loader = new CSVLoader();
        saver = new ArffSaver();
        setContentView(R.layout.activity_main);

        // import pre-trained weka model

        try {
            cls = (Classifier) read(getAssets().open("Trained_model3.model"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("HEW", "after Classifier set");
        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        final Button recordButton = (Button) findViewById(R.id.start_collecting_data);
        final Button playButton = (Button) findViewById(R.id.start_or_stop_playing);
        final Button detailButton = (Button) findViewById(R.id.show_details_of_data);
        final TextView modelAccuracy = (TextView) findViewById(R.id.Model_Accuracy);
        final TextView modelConfidence = (TextView) findViewById(R.id.Model_Confidence);
        final TextView detectCough = (TextView) findViewById(R.id.Detected_a_Cough);
//        AssetManager  a = getApplicationContext().getAssets();
//        String[] lst = a.getLocales();
//        for (int i = 0; i < lst.length; i++) {
//            Log.i("checkAssets", lst[i]);
//        }

        recordButton.setOnClickListener(new View.OnClickListener() {
            boolean mStartRecording = true;
            public void onClick (View v) {
                onRecord(mStartRecording);
                new CountDownTimer(10000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish(){
                        mStartRecording = false;
                        recordButton.setText("Start recording");
                    }
                }.start();

                if (mStartRecording) {
                    recordButton.setText("Stop recording");
                }
                mStartRecording = !mStartRecording;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            boolean mStartPlaying = true;
            public void onClick (View v) {
                onPlay(mStartPlaying);
                new CountDownTimer(10000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish(){
                        mStartPlaying = false;
                        recordButton.setText("Start recording");
                    }
                }.start();

                if (mStartPlaying) {
                    recordButton.setText("Stop recording");
                }
                mStartPlaying = !mStartPlaying;
            }
        });

        detailButton.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v) {
                modelAccuracy.setText("0%");
                modelConfidence.setText("0%");
                detectCough.setText("Nothing");
            }
        });

//        try {
//            unlabeled = new Instances(
//                    new BufferedReader(
//                            new FileReader("../../../assets/unlabeled.csv")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }


}