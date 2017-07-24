package com.example.prashant_tripathi.onlinevotingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 09-06-2017.
 */
public class RegisterUser extends Activity {
    String State[] = {"UTTAR PRADESH", "DELHI", "BIHAR", "WEST BENGAL"};
    String DD[]={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String MM[]={"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
    ArrayList<String> YYYY=new ArrayList<String>();

    EditText register_user_firstname, register_user_middlename, register_user_lastname, register_user_aadhar, register_user_address1, register_user_address2, register_user_email, register_user_username, register_user_pass, register_user_confirm;
    RadioGroup register_user_gender;
    Button register_user_clear, register_user_register;
    Spinner register_user_date, register_user_month, register_user_year;
    AutoCompleteTextView register_user_state, register_user_district;
    private String JSON_STRING;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        RegisterUserInit();
    }

    public void RegisterUserInit() {
        for(int i=1985;i<=2017;i++){
            YYYY.add(Integer.toString(i));
        }
        register_user_firstname=(EditText)findViewById(R.id.register_user_firstname);

        register_user_middlename=(EditText)findViewById(R.id.register_user_middlename);

        register_user_lastname=(EditText)findViewById(R.id.register_user_lastname);

        register_user_aadhar=(EditText)findViewById(R.id.register_user_aadhar);

        register_user_address1=(EditText)findViewById(R.id.register_user_address1);

        register_user_address2=(EditText)findViewById(R.id.register_user_address2);

        register_user_email=(EditText)findViewById(R.id.register_user_email);

        register_user_username=(EditText)findViewById(R.id.register_user_username);

        register_user_pass=(EditText)findViewById(R.id.register_user_pass);

        register_user_confirm=(EditText)findViewById(R.id.register_user_confirm);

        register_user_gender=(RadioGroup)findViewById(R.id.register_user_gender);

        register_user_clear=(Button)findViewById(R.id.register_user_clear);

        register_user_register=(Button)findViewById(R.id.register_user_register);

        register_user_date=(Spinner)findViewById(R.id.register_user_dd);
        register_user_date.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,DD));

        register_user_month=(Spinner)findViewById(R.id.register_user_mm);
        register_user_month.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,MM));

        register_user_year=(Spinner)findViewById(R.id.register_user_yyyy);
        register_user_year.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,YYYY));

        register_user_state = (AutoCompleteTextView) findViewById(R.id.register_user_state);

        register_user_district = (AutoCompleteTextView) findViewById(R.id.register_user_city);

        register_user_state.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, State));
        register_user_state.setThreshold(1);
        register_user_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state = (String) parent.getItemAtPosition(position);
                GetJson();
            }
        });
    }


    public void ButtonClickedReset(View v){
        register_user_firstname.setText("");
        register_user_middlename.setText("");
        register_user_lastname.setText("");
        register_user_aadhar.setText("");
        register_user_date.setSelection(0);
        register_user_month.setSelection(0);
        register_user_year.setSelection(0);
        register_user_state.setText("");
        register_user_district.setText("");
        register_user_address1.setText("");
        register_user_address2.setText("");
        register_user_gender.check(R.id.register_user_male);
        register_user_email.setText("");
        register_user_username.setText("");
        register_user_pass.setText("");
        register_user_confirm.setText("");
    }

    public void ButtonClickedRegister(View v){
           /* if(CheckedValidation()){

            }*/
        AlertDialog.Builder confirm=new AlertDialog.Builder(RegisterUser.this);
        confirm.setMessage("Confirm Registration ?");
        confirm.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                InsertDetails();
            }
        });
        confirm.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        confirm.show();
    }


    private void InsertDetails(){
        final String fn=register_user_firstname.getText().toString().trim();
        final String mn=register_user_middlename.getText().toString().trim();
        final String ln=register_user_lastname.getText().toString().trim();
        final String aadhar=register_user_aadhar.getText().toString().trim();
        final String dd=register_user_date.getSelectedItem().toString().trim();
        final String mm=register_user_month.getSelectedItem().toString().trim();
        final String yyyy=register_user_year.getSelectedItem().toString().trim();
        final String state=register_user_state.getText().toString().trim();
        final String ditrict=register_user_district.getText().toString().trim();
        final String add1=register_user_address1.getText().toString().trim();
        final String add2=register_user_address2.getText().toString().trim();
        Button b=(Button)findViewById(register_user_gender.getCheckedRadioButtonId());
        final String gender=b.getText().toString().trim();
        final String email=register_user_email.getText().toString().trim();
        final String username=register_user_username.getText().toString().trim();
        final String pwd=register_user_pass.getText().toString().trim();


        class Inserting extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(RegisterUser.this,"Registering User ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                NotificationCompat.Builder builder=new NotificationCompat.Builder(RegisterUser.this);

                if(s.equals("Successfully Registered !")){
                    builder.setSmallIcon(R.drawable.btn_check_on_holo_light);
                    builder.setContentTitle("Congratulations ");
                    builder.setContentText("Successfully Registered ...");
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(1, builder.build());
                    Intent i=new Intent(RegisterUser.this,Login.class);
                    startActivity(i);
                    finish();
                }
                else if(s.equals("Could Not Register")){
                    builder.setSmallIcon(R.drawable.btn_close_selected);
                    builder.setContentTitle("Registration Failed !!!");
                    builder.setContentText("Username Already Exists ...");
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(1, builder.build());
                }
            }



            /*PUSHING DATA INTO HASHMAP AND THEN TRANSPORTING IT*/
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params=new HashMap<>();
                params.put(Config.KEY_FIRST_NAME,fn);
                params.put(Config.KEY_MIDDLE_NAME,mn);
                params.put(Config.KEY_LAST_NAME,ln);
                params.put(Config.KEY_AADHAR,aadhar);
                params.put(Config.KEY_DOB,dd+"-"+mm+"-"+yyyy);
                params.put(Config.KEY_STATE,state);
                params.put(Config.KEY_DISTRICT,ditrict);
                params.put(Config.KEY_ADDRESS1,add1);
                params.put(Config.KEY_ADDRESS2,add2);
                params.put(Config.KEY_GENDER,gender);
                params.put(Config.KEY_EMAIL,email);
                params.put(Config.KEY_USERNAME,username);
                params.put(Config.KEY_PASSWORD,pwd);

                RequestHandler rh=new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_USER_REGISTRATION,params);
                return res;
            }
        }

        Inserting i=new Inserting();
        i.execute();


    }

    private void ShowDistricts(){
        JSONObject jsonObject=null;
        ArrayList<String> list=new ArrayList<String>();
        try{
            jsonObject=new JSONObject(JSON_STRING);
            JSONArray result=jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for(int i=0;i<result.length();i++){
                JSONObject jo=result.getJSONObject(i);
                String s=jo.getString(Config.TAG_STATE);
                String d=jo.getString(Config.TAG_DISTRICT);
                list.add(d);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }


       register_user_district.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list));
        register_user_district.setThreshold(1);
    }


    private void GetJson(){
        class ReceiveJson extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(RegisterUser.this,"Loading ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING=s;
                ShowDistricts();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh=new RequestHandler();
                String s=rh.SendGetRequestParam(Config.URL_LOAD_DISTRICTS,state);
                return s;
            }
        }

        ReceiveJson rj=new ReceiveJson();
        rj.execute();
    }
}
