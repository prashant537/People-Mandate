package com.example.prashant_tripathi.onlinevotingapp;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Prashant_Tripathi on 05-07-2017.
 */
public class AdminEditCandidate extends AppCompatActivity {
    Button admin_edit_candidate_exit,admin_edit_candidate_save;
    EditText admin_edit_candidate_name,admin_edit_candidate_party;
    private String candi_name,candi_party,elec_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_edit_candidate);
        Bundle b=getIntent().getExtras();
        candi_name=b.getString("Name");
        candi_party=b.getString("Party");
        elec_name=b.getString("Election");
        System.out.println(candi_name+" "+candi_party+" "+elec_name);

        AdminEditCandidateInit();
    }

    public void AdminEditCandidateInit(){
        admin_edit_candidate_name=(EditText)findViewById(R.id.admin_edit_candidate_candidate_name);
        admin_edit_candidate_party=(EditText)findViewById(R.id.admin_edit_candidate_candidate_party);
        admin_edit_candidate_save=(Button)findViewById(R.id.admin_edit_candidate_save);
        admin_edit_candidate_exit=(Button)findViewById(R.id.admin_edit_candidate_exit);
        admin_edit_candidate_name.setText(candi_name);
        admin_edit_candidate_party.setText(candi_party);
    }


    private void InsertCandidateDetails(){
        final String name=admin_edit_candidate_name.getText().toString().trim();
        final String party=admin_edit_candidate_party.getText().toString();

        class Inserting extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminEditCandidate.this,"Updating ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                NotificationCompat.Builder builder=new NotificationCompat.Builder(AdminEditCandidate.this);

                if(s.equals("Success")) {
                    builder.setSmallIcon(R.drawable.btn_check_on_holo_light);
                    builder.setContentText("Updated Successfully ...");
                    NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(1, builder.build());
                    Intent i = new Intent(getApplicationContext(),AdminRemoveCandidate.class);
                    i.putExtra("Election",elec_name);
                    startActivity(i);
                    finish();
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
                params.put(Config.KEY_CANDIDATE_NAME,name);
                params.put(Config.KEY_CANDIDATE_PARTY,party);
                params.put(Config.KEY_ELECTION_NAME,elec_name);

                RequestHandler rh=new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_CANDIDATE_REGISTRATION, params);
                return res;
            }
        }

        Inserting i=new Inserting();
        i.execute();
    }



    private void DeleteCandidate(){

        class Deleting extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminEditCandidate.this,"Updating ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                NotificationCompat.Builder builder=new NotificationCompat.Builder(AdminEditCandidate.this);

                if(s.equals("Success")){
                    InsertCandidateDetails();
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
                params.put(Config.KEY_ELECTION_NAME,elec_name);
                params.put(Config.KEY_CANDIDATE_NAME,candi_name);
                RequestHandler rh=new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_DELETE_CANDIDATE,params);
                return res;
            }
        }

        Deleting d=new Deleting();
        d.execute();
    }


    public void ClickedButton(View v) {
        switch (v.getId()) {
            case R.id.admin_edit_candidate_save:
                DeleteCandidate();
                break;
            case R.id.admin_edit_candidate_exit:
                ActivityCompat.finishAffinity(AdminEditCandidate.this);
                break;
        }
    }


}
