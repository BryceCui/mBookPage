package com.cuipengyu.emingren.mbookpage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    PageTrunView pageTrunView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pageTrunView = (PageTrunView) findViewById(R.id.Page);
        List<Bitmap> bitmaps = new ArrayList<>();
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg1);
        bitmaps.add(bitmap);

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg2);
        bitmaps.add(bitmap);

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg3);
        bitmaps.add(bitmap);

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg4);
        bitmaps.add(bitmap);

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg5);
        bitmaps.add(bitmap);

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg6);
        bitmaps.add(bitmap);

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg7);
        bitmaps.add(bitmap);
        pageTrunView.setBitmaps(bitmaps);
    }
}
