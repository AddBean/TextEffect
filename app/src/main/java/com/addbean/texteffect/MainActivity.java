package com.addbean.texteffect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.addbean.effect.TextEffectView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextEffectView text = (TextEffectView) findViewById(R.id.text_view);
        final EditText editText= (EditText) findViewById(R.id.edit_text);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.startAnim(editText.getText().toString().trim());
            }
        });

    }
}
