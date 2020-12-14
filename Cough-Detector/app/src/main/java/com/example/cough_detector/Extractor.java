package com.example.cough_detector;

//import javax.sound.sampled.UnsupportedAudioFileException;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.mfcc.MFCC;

public class Extractor {

    public final List<float[]> mfccList = new ArrayList<>(200);

    Extractor(String audioFilePath) throws FileNotFoundException {
        int sampleRate = 16000;
        int bufferSize = 512;
        int bufferOverlap = 128;

        InputStream inStream = new FileInputStream(audioFilePath);
        AudioDispatcher dispatcher = new AudioDispatcher(new UniversalAudioInputStream(inStream, new TarsosDSPAudioFormat(sampleRate, bufferSize, 1, true, true)), bufferSize, bufferOverlap);
        final MFCC mfcc = new MFCC(bufferSize, sampleRate, 20, 50, 300, 3000);
        dispatcher.addAudioProcessor(mfcc);
        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public void processingFinished() {
            }

            @Override
            public boolean process(AudioEvent audioEvent) {
                mfccList.add( mfcc.getMFCC());
                Log.i("Extractor","Processing audio with MFCC...");
                return true;
            }
        });
        dispatcher.run();
    }
}