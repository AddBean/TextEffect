package com.addbean.texteffect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.addbean.effect.TextEffectView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextEffectView text = (TextEffectView) findViewById(R.id.text_view);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.startShow("微信\n Q Q",2000,16,10,false);
                text.setOnDotLifeListener(new TextEffectView.OnDotLifeListener() {
                    @Override
                    public void onAnimFinish() {
                        text.startShow("一网\n打尽",2000,20,10,false);
                        text.setOnDotLifeListener(new TextEffectView.OnDotLifeListener() {
                            @Override
                            public void onAnimFinish() {
//                                text.startShow(" ",2000,10,12,true);
//                                text.setOnDotLifeListener(null);
                            }
                        });
                    }
                });
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.startShow(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher),2000,10,16,true);
            }
        });

    }
}
