package com.cuipengyu.emingren.mbookpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    PageTrunView pageTrunView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       pageTrunView =(PageTrunView)findViewById(R.id.Page);

    }
}
