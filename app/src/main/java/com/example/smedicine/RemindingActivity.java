package com.example.smedicine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RemindingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminding);
        Button button = (Button) findViewById(R.id.btn_quit_reminding);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
