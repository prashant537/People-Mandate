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

import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 23-06-2017.
 */
public class AdminAddElection extends AppCompatActivity {
    String State[] = {"UTTAR PRADESH", "DELHI", "BIHAR", "WEST BENGAL"};
    EditText election_name,election_date;
    AutoCompleteTextView election_state;
    Button back,next;
    private String st;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_election);
        Admin_Election_Init();
    }

    public void Admin_Election_Init(){
        election_name=(EditText)findViewById(R.id.admin_add_election_name);
        election_state=(AutoCompleteTextView)findViewById(R.id.admin_add_election_state);
        election_date=(EditText)findViewById(R.id.admin_add_election_date);
        back=(Button)findViewById(R.id.admin_add_election_back);
        next=(Button)findViewById(R.id.admin_add_election_next);

        election_state.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, State));
        election_state.setThreshold(1);
        election_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                st = (String) parent.getItemAtPosition(position);
            }
        });
    }

    public void AddElection(View v){
        switch(v.getId()){
            case R.id.admin_add_election_back:
                Intent i=new Intent(getApplicationContext(),AdminHome.class);
                startActivity(i);
                finish();
                break;
            case R.id.admin_add_election_next:
                InsertElectionDetails();
                break;
        }
    }

    private void InsertElectionDetails(){
        final String name=election_name.getText().toString().trim();
        final String state=st;
        final String date=election_date.getText().toString().trim();

        class Inserting extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminAddElection.this,"Adding Details ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                NotificationCompat.Builder builder=new NotificationCompat.Builder(AdminAddElection.this);

                if(s.equals("Success")) {
                    class Creating extends AsyncTask<Void, Void, String> {
                        ProgressDialog loading;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            loading = ProgressDialog.show(AdminAddElection.this, "Creating ...", "Please Wait ...", false, false);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            loading.dismiss();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(AdminAddElection.this);

                            if (s.equals("Success")) {
                                builder.setSmallIcon(R.drawable.btn_check_on_holo_light);
                                builder.setContentText("Election Contest Registered ...");
                                NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notif.notify(1, builder.build());
                                Intent i = new Intent(AdminAddElection.this,AdminAddCandidate.class);
                                startActivity(i);
                                finish();
                            } else if (s.equals("Failed")) {
                                builder.setSmallIcon(R.drawable.btn_close_selected);
                                builder.setContentText("Some Error Occured ...");
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
                String res=rh.SendPostRequest(Config.URL_ADD_ELECTION,params);
                return res;
            }
        }

        Inserting i=new Inserting();
        i.execute();
    }
    }

