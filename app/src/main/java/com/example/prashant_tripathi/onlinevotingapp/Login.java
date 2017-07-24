package com.example.prashant_tripathi.onlinevotingapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    EditText login_username,login_password;
    Button login_login,login_register;
    TextView login_forgot;
    CheckBox login_remember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Login_Init();
    }

    public void Login_Init(){
        login_username=(EditText)findViewById(R.id.login_username);
        login_password=(EditText)findViewById(R.id.login_password);
        login_login=(Button)findViewById(R.id.login_login);
        login_register=(Button)findViewById(R.id.login_register);
        login_forgot=(TextView)findViewById(R.id.login_forgot);
        login_remember=(CheckBox)findViewById(R.id.login_remember);
    }

    public void UserLogin(View v){

    }

    public void UserRegister(View v){
        if(v.getId()==R.id.login_register){
            Intent i=new Intent(Login.this,RegisterUser.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_admin_signup:
                Intent i=new Intent(getApplicationContext(),RegisterAdmin.class);
                startActivity(i);
                break;
            case R.id.menu_admin_login:
                Intent j=new Intent(getApplicationContext(),LoginAdmin.class);
                startActivity(j);
                break;
        }
        return true;
    }
}

