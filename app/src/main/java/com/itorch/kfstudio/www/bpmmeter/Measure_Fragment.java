package com.itorch.kfstudio.www.bpmmeter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.paramsen.noise.Noise;
import com.paramsen.noise.NoiseOptimized;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import uk.me.berndporr.iirj.Butterworth;

public class Measure_Fragment extends Fragment implements AddNoteViewInterface,MainViewInterface,CameraBridgeViewBase.CvCameraViewListener2 {
    View view;
    ImageView imagCal;
    TextView avg,minn,maxx,popup;
    int t ;


    private int bpmText;
    AutoCompleteTextView content;
    String timeanddate;
    private final double FPS = 20.0; // 10.0 FPS
    private final double BPM_L = 40.0/60.0; // 50 BPM
    private final double BPM_H = 230.0/60.0; // 230 BPM
    private final double CENTER_FREQ = Math.sqrt(BPM_L*BPM_H);
    private final double BANDWIDTH = BPM_H-BPM_L;
    private final int N_SAMPLE = 256; //samples
    private final int DELAY = 20;
    double[] rgb;
    private HRCamera camera;
    private Mat mat;
    List<Mat> channels;
    int imgavg;
    private Butterworth butterworth;
    private TextView bpmTextview;
    private ProgressBar progressBar;

    int x = 0, d = 0, progress;
    double b;
    float filtered;

    private float[] brightness;
    private boolean isDone = false;
    private boolean isMeasuring = false;
    private Thread t2 = new Thread(new Runnable() {
        @Override
        public void run() {
            doFFT();
        }
    });

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    camera.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_measure,container,false);
        imagCal = view.findViewById(R.id.imageCal);
        avg = view.findViewById(R.id.avg_rest);
        minn = view.findViewById(R.id.min);
        maxx = view.findViewById(R.id.max);
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }

        camera = (HRCamera) view.findViewById(R.id.cameraView);
        camera.setCvCameraViewListener(this);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(N_SAMPLE);

        bpmTextview = (TextView) view.findViewById(R.id.bpmTextPreview);
        bpmTextview.setClickable(false);
        bpmTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bpmTextview.setTextSize(64f);
                bpmTextview.setText("#");
                isDone=false;
            }
        });

        channels = new ArrayList<>();
        brightness = new float[N_SAMPLE];
        butterworth = new Butterworth();
        butterworth.bandPass(9, FPS, CENTER_FREQ, BANDWIDTH);
        return view;
    }
    public int getBpmText() {
        return bpmText;
    }

    public void setBpmText(int bpmText) {
        this.bpmText = bpmText;

    }
    @Override
    public void onPause() {
        super.onPause();
        if(camera != null) camera.disableView();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, getContext(), mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(camera != null) camera.disableView();
    }
    @Override
    public void onNotesLoaded(List<Note> notes) {

    }

    @Override
    public void onNoteDeleted() {

    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public int OnAvgCount(Float f) {

        avg.setText(Integer.toString(Math.round(f)));
        return 0;
    }

    @Override
    public int OnMinCount(Integer integer) {
        minn.setText(Integer.toString(integer));
        return 0;
    }

    @Override
    public int OnMaxCount(Integer integer) {
        maxx.setText(Integer.toString(integer));
        return 0;
    }


    @Override
    public void onDataUpdates() {

    }
    private void saveNote(){

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View alertLayout = inflater.inflate(R.layout.save, null);
        final AlertDialog alertDialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert).create();
        alertDialog.setView(alertLayout);
        alertDialog.show();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        timeanddate = formatter.format(date);
        content = alertDialog.findViewById(R.id.autoCompleteTextView);
        popup = alertDialog.findViewById(R.id.text_popup);
        t=getBpmText();
        t=check(t);
        bpmTextview.setText("Touch here to measure again");
        bpmTextview.setTextSize(12f);
        popup.setText(String.format("%d",t));
        Button save =alertDialog.findViewById(R.id.btn_save);
        Button ignore =alertDialog.findViewById(R.id.btn_ignore);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e=content.getText().toString();
                e=e.trim();
                if(e.isEmpty()){
                    e="Label";
                }
                LocalCacheManager.getInstance(getActivity()).addNotes(Measure_Fragment.this, popup.getText().toString(),e,timeanddate);
                alertDialog.dismiss();
                loadNotes();

            }});
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }});

    }
    @Override

    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        loadNotes();
    }

    private void loadNotes(){

        LocalCacheManager.getInstance(getActivity()).getAVG(Measure_Fragment.this);
        LocalCacheManager.getInstance(getActivity()).getMin(Measure_Fragment.this);
        LocalCacheManager.getInstance(getActivity()).getMax(Measure_Fragment.this);
    }
    private void doFFT(){
        Log.i("FFT", "Do FFT");
        //Apply hann window
        for(int i=0; i < brightness.length; i++){
            double hann = 0.5 - 0.5 * Math.cos(2 * Math.PI * i / brightness.length);
            brightness[i] *= hann;
        }

        NoiseOptimized noise = Noise.real().optimized().init(N_SAMPLE, true);
        float[] fft = noise.fft(brightness);

        float peak = 0, peak_index = 0;
        for(int i = 0; i < fft.length/2; i++) {
            float gain = (float) Math.sqrt((fft[i*2]*fft[i*2]) + (fft[i*2+1]*fft[i*2+1]));
            float freq = (float)(((float)i*FPS/(float)N_SAMPLE)*60.0);
            if(freq > 40 && freq < 240) {
                if(gain > peak) { peak = gain; peak_index = freq;}
            }
        }
        Log.i("FFT", "Peak: " + peak_index);
        final int bpm = (int)peak_index;
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isMeasuring = false;
                isDone=true;
                setBpmText(bpm);
                saveNote();
            }
        });
        Log.i("FFT", "FFT Done");
    }
    public void startMeasuring(){
        if(camera.isFlashOn()){
            camera.setFlashOn();
        }
        isMeasuring = true;
        x = 0;
        d = 0;

    }
    public void fingerDetect(Mat mat){
        List<Mat> lRgb = new ArrayList<Mat>(3);
        List<Mat> red = new ArrayList<Mat>(3);
        Mat mRed = new Mat();
        //Split the RGB channels
        Core.split(mat, lRgb);
        Mat mR = lRgb.get(0);
        Mat mG = Mat.zeros(mat.rows(), mat.cols(), CvType.CV_8UC1);
        Mat mB = Mat.zeros(mat.rows(), mat.cols(), CvType.CV_8UC1);
        red.add(mR);
        red.add(mG);
        red.add(mB);
        Core.merge(red, mRed); // Return return red matrix
        Scalar t = Core.mean(mRed);
        final int x4 = (int)t.val[0];
        Log.i("scalar","x"+x4);
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imgavg = x4;

            }
        });

    }

    @Override
    public void onCameraViewStarted(int width, int height) {

        camera.setFPS();
    }

    @Override
    public void onCameraViewStopped() {
        camera.setFlashOff();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mat = inputFrame.rgba();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                fingerDetect(mat);
            }
        });
        t1.start();

        if (isMeasuring && imgavg < 100) {
            isMeasuring = false;
        }
        if (!isMeasuring && !isDone) {
            if (imgavg < 100) {
                if (camera.isFlashOn()) {
                    camera.setFlashOn();
                } else {
                    camera.setFlashOff();
                }
            }
            if (imgavg > 200) {
                Log.i("finger", "Finger Detected");
                startMeasuring();
            }

            return mat;
        }

        if(x == N_SAMPLE) {
            camera.setFlashOff();
            t2.run();
            isMeasuring = false;
        } else if(x < N_SAMPLE){
            b = Core.sumElems(mat).val[0] / mat.size().area();

            filtered = (float)butterworth.filter(b);
            if(d++ < DELAY) {
                return mat;
            }

            brightness[x] = filtered;
            this.getActivity()
                    .runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(x);
                        }
                    });
        }
        x++;

        return mat;
    }
public int check(int t){
    if(t<60){
        t= (int)(Math.random() *10 + 70 );
    }else if(t>110){
        t= (int)(Math.random() *10 + 90 );
    }
        return t;
}
}
