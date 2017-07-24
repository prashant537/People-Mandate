package com.example.prashant_tripathi.onlinevotingapp;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 27-06-2017.
 */
public class AdminEditElection extends AppCompatActivity {

    String State[] = {"UTTAR PRADESH", "DELHI", "BIHAR", "WEST BENGAL"};
    Button admin_edit_election_back,admin_edit_election_save;
    EditText admin_edit_election_name,admin_edit_election_date;
    private String JSON_STRING,elec_name;
    AutoCompleteTextView admin_edit_election_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_election);
        Bundle b=getIntent().getExtras();
        elec_name=b.getString("Details");
        AdminEditElectionInit();
    }

    public void AdminEditElectionInit(){
        admin_edit_election_back=(Button)findViewById(R.id.admin_edit_election_back);
        admin_edit_election_save=(Button)findViewById(R.id.admin_edit_election_save);
        admin_edit_election_name=(EditText)findViewById(R.id.admin_edit_election_name);
        admin_edit_election_state=(AutoCompleteTextView)findViewById(R.id.admin_edit_election_state);
        admin_edit_election_state.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, State));
        admin_edit_election_state.setThreshold(1);
        admin_edit_election_date=(EditText)findViewById(R.id.admin_edit_election_date);
        GetJson();
    }



    private void ShowElectionDetails(){
        JSONObject jsonObject=null;
        ArrayList<String> list=new ArrayList<String>();
        try{
            jsonObject=new JSONObject(JSON_STRING);
            JSONArray result=jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for(int i=0;i<result.length();i++){
                JSONObject jo=result.getJSONObject(i);
                String elec_name=jo.getString(Config.KEY_ELECTION_NAME);

                admin_edit_election_name.setText(elec_name);

                String elec_state=jo.getString(Config.KEY_ELECTION_STATE);
                admin_edit_election_state.setText(elec_state);

                String elec_date=jo.getString(Config.KEY_ELECTION_DATE);
                admin_edit_election_date.setText(elec_date);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }


    private void GetJson(){
        class ReceiveJson extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminEditElection.this,"Loading ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING=s;
                ShowElectionDetails();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh=new RequestHandler();
                String s=rh.SendGetRequestParam(Config.URL_ELECTION_DETAILS,elec_name);
                return s;
            }
        }

        ReceiveJson rj=new ReceiveJson();
        rj.execute();
    }

    private void InsertElectionDetails(){
        final String name=admin_edit_election_name.getText().toString().trim();
        final String state=admin_edit_election_state.getText().toString();
        final String date=admin_edit_election_date.getText().toString().trim();

        class Inserting extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminEditElection.this,"Updating ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                NotificationCompat.Builder builder=new NotificationCompat.Builder(AdminEditElection.this);

                if(s.equals("Success")) {
                    class Creating extends AsyncTask<Void, Void, String> {
                        ProgressDialog loading;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(AdminEditElection.this, "Updating Necessary Details ...", "Please Wait ...", false, false);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            loading.dismiss();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(AdminEditElection.this);

                            if (s.equals("Success")) {
                                builder.setSmallIcon(R.drawable.btn_check_on_holo_light);
                                builder.setContentText("Updated Successfully ...");
                                NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notif.notify(1, builder.build());
                                Intent i = new Intent(AdminEditElection.this,AdminRemoveElection.class);
                                startActivity(i);
                                finish();
                            } else if (s.equals("Failed")) {
                                builder.setSmallIcon(R.drawable.btn_close_selected);
                                builder.setContentText("Updation Failed ...");
                                NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notif.notify(1, builder.build());
                            }
                        }


                        /*PUSHING DATA INTO HASHMAP AND THEN TRANSPORTING IT*/
                        @Override
                        protected String doInBackground(Void... v) {
                            HashMap<String, String> para = new HashMap<>();
                            para.put(Config.KEY_ELECTION_NAME, name);


                            RequestHandler rh = new RequestHandler();
                            String res = rh.SendPostRequest(Config.URL_CREATE_ELECTION, para);
                            return res;
                        }
                    }

                    Creating c = new Creating();
                    c.execute();
                }
                else if(s.equals("Failed")){
                    builder.setSmallIcon(R.drawable.btn_close_selected);
                    builder.setContentText("Some Error Occured ...");
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(1, builder.build());
                }
            }



            /*PUSHING DATA INTO HASHMAP AND THEN TRANSPORTING IT*/
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params=new HashMap<>();
                params.put(Config.KEY_ELECTION_NAME,name);
                params.put(Config.KEY_ELECTION_STATE,state);
                params.put(Config.KEY_ELECTION_DATE,date);


                RequestHandler rh=new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_ADD_ELECTION, params);
                return res;
            }
        }

        Inserting i=new Inserting();
        i.execute();
    }



    private void DeleteElection(){
        final String name=elec_name.trim();


        class Deleting extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminEditElection.this,"Updating ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                NotificationCompat.Builder builder=new NotificationCompat.Builder(AdminEditElection.this);

                if(s.equals("Success")){
                   InsertElectionDetails();
                }
                else if(s.equals("Failed")){
                    builder.setSmallIcon(R.drawable.btn_close_selected);
                    builder.setContentText("Some Error Occured ...");
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(1, builder.build());
                }
            }



            /*PUSHING DATA INTO HASHMAP AND THEN TRANSPORTING IT*/
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params=new HashMap<>();
                params.put(Config.KEY_ELECTION_NAME, name);
                RequestHandler rh=new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_DELETE_ELECTION,params);
                if(res.equals("Success")){
                    res=rh.SendPostRequest(Config.URL_DROP_ELECTION,params);
                }
                return res;
            }
        }

        Deleting d=new Deleting();
        d.execute();
    }


    public void OptionSelected(View v) {
        switch (v.getId()) {
            case R.id.admin_edit_election_save:
                DeleteElection();
                break;
            case R.id.admin_edit_election_back:
                Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}