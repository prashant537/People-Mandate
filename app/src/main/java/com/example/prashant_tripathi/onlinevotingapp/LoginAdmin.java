package com.example.prashant_tripathi.onlinevotingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Prashant_Tripathi on 23-06-2017.
 */
public class LoginAdmin extends AppCompatActivity{
    EditText admin_login_username,admin_login_password,admin_login_empid;
    Button admin_login_login;
    TextView admin_login_forgot;
    CheckBox admin_login_remember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        Admin_Login_Init();
    }

    public void Admin_Login_Init(){
        admin_login_username=(EditText)findViewById(R.id.admin_login_username);
        admin_login_password=(EditText)findViewById(R.id.admin_login_password);
        admin_login_login=(Button)findViewById(R.id.admin_login_login);
        admin_login_forgot=(TextView)findViewById(R.id.admin_login_forgot);
        admin_login_remember=(CheckBox)findViewById(R.id.admin_login_remember);
        admin_login_empid=(EditText)findViewById(R.id.admin_login_empid);
    }

    public void LoginClicked(View v){
        if(v.getId()==R.id.admin_login_login){
            Intent i=new Intent(LoginAdmin.this,AdminHome.class);
            startActivity(i);
            finish();
        }
    }

}
