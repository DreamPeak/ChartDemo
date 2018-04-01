package com.smartvot.chartdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
   private MyAssistCurveView myAssistCurveView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        myAssistCurveView=findViewById(R.id.st);
        List<Integer > steps=new ArrayList<>();
//        for (int i=0;i<50;i++){
//
//           steps.add(100+i*100) ;
//         //   Log.e("TEST     steps",x+"");
//        }
//        for (int i=0;i<50;i++){
//
//            steps.add(5000-i*80) ;
//          //  Log.e("TEST     steps",x+"");
//        }
//        for (int i=0;i<50;i++){
//
//            steps.add(1080+i*80) ;
//          //  Log.e("TEST     steps",x+"");
//        }
//        for (int i=0;i<50;i++){
//
//            steps.add(5000-i*80) ;
//         //   Log.e("TEST     steps",x+"");
//        }
//        for (int i=0;i<50;i++){
//
//            steps.add(1080+i*80) ;
//        ///    Log.e("TEST     steps",x+"");
//        }
//        for (int i=0;i<38;i++){
//
//            steps.add(5000-i*80) ;
//        //    Log.e("TEST     steps",x+"");
//        }


          steps.add(5000);
          steps.add(100);
          steps.add(3000);
          steps.add(3500);
          steps.add(4000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
          steps.add(10000);
//          steps.add(5000);
//          steps.add(6000);
//          steps.add(7000);
//          steps.add(8000);
//          steps.add(7300);
//          steps.add(3000);
        Log.e("TEST     steps.size",steps.size()+"");
        myAssistCurveView.setSteps(steps);
    }
    public static int getNum(int startNum,int endNum){
        if(endNum > startNum){
            Random random = new Random();
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }
}
