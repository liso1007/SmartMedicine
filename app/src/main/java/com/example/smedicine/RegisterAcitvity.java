package com.example.smedicine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterAcitvity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText passwordRepeat;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitvity);

        username = (EditText)findViewById(R.id.password_edit_text);
        password = (EditText)findViewById(R.id.password_edit_text_register);
        passwordRepeat = (EditText)findViewById(R.id.password_edit_text_register_repeat);
        register = (Button)findViewById(R.id.registerButton_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterAcitvity.this,"注册成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterAcitvity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
