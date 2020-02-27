package com.example.smedicine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReallyLogin extends AppCompatActivity {

    EditText username;
    EditText password;
    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_really_login);

        //初始化控件
        username = (EditText)findViewById(R.id.username_edit_text);
        password = (EditText)findViewById(R.id.password_edit_text);
        login = (Button)findViewById(R.id.enterButton);
        register = (Button)findViewById(R.id.registerButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReallyLogin.this,"登录成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReallyLogin.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ReallyLogin.this,"登录成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReallyLogin.this,RegisterAcitvity.class);
                startActivity(intent);
            }
        });
    }
}
