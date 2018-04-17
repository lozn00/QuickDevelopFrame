package cn.qssq666.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.qssq666.rapiddevelopframe.global.SuperAppContext;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MyApp.getInstance()
        SuperAppContext.showToast("xxxx");
    }

}
