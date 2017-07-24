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
 * Created by Prashant_Tripathi on 25-06-2017.
 */
public class AdminAddCandidate extends AppCompatActivity {
    AutoCompleteTextView admin_add_candidate_election_name;
    EditText admin_add_candidate_election_state,admin_add_candidate_candidate_name,admin_add_candidate_candidate_party;
    Button admin_add_candidate_add,admin_add_candidate_exit;
    private String JSON_STRING,elec_name;
    private HashMap<String,String> details=new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_candidate);
        AdminAddInit();
    }

    public void AdminAddInit(){
        admin_add_candidate_election_name=(AutoCompleteTextView)findViewById(R.id.admin_add_candidate_election_name);
        admin_add_candidate_election_state=(EditText)findViewById(R.id.admin_add_candidate_election_state);
        admin_add_candidate_candidate_name=(EditText)findViewById(R.id.admin_add_candidate_candidate_name);
        admin_add_candidate_candidate_party=(EditText)findViewById(R.id.admin_add_candidate_candidate_party);
        admin_add_candidate_add=(Button)findViewById(R.id.admin_add_candidate_add);
        admin_add_candidate_exit=(Button)findViewById(R.id.admin_add_candidate_exit);

        GetJson();

        admin_add_candidate_election_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                elec_name = (String) parent.getItemAtPosition(position);
                admin_add_candidate_election_state.setText(details.get(elec_name));
                admin_add_candidate_election_state.setEnabled(false);
            }
        });

    }



    private void ShowElectionDetails(){
        JSONObject jsonObject=null;
        ArrayList<HashMap<String,String>> l=new ArrayList<HashMap<String, String>>();
        ArrayList<String> l2=new ArrayList<String>();
        try{
            jsonObject=new JSONObject(JSON_STRING);
            JSONArray result=jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for(int i=0;i<result.length();i++){
                JSONObject jo=result.getJSONObject(i);
                String name=jo.getString(Config.KEY_ELECTION_NAME);
                String state=jo.getString(Config.KEY_ELECTION_STATE);
                System.out.println(name+" "+state);
                HashMap<String,String> all=new HashMap<>();
                all.put(Config.KEY_ELECTION_NAME,name);
                all.put(Config.KEY_ELECTION_STATE, state);
                details.put(name,state);
                l.add(all);
                l2.add(name);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        admin_add_candidate_election_name.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l2));
        admin_add_candidate_election_name.setThreshold(1);
    }


    private void GetJson(){
        class ReceiveJson extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminAddCandidate.this,"Loading ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING=s;
                if(JSON_STRING!=null){
                    ShowElectionDetails();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh=new RequestHandler();
                String s=rh.SendGetRequestParam(Config.URL_ALL_ELECTIONS,"xyz");
                return s;
            }
        }

        ReceiveJson rj=new ReceiveJson();
        rj.execute();
    }



    private void InsertCandidateDetails(){
        final String name=admin_add_candidate_candidate_name.getText().toString().trim();
        final String party=admin_add_candidate_candidate_party.getText().toString().trim();
        final String election_name=elec_name;


        class Inserting extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminAddCandidate.this,"Adding Details ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                NotificationCompat.Builder builder=new NotificationCompat.Builder(AdminAddCandidate.this);

                if(s.equals("Success")){
                    builder.setSmallIcon(R.drawable.btn_check_on_holo_light);
                    builder.setContentText("Added Successfully ...");
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(1, builder.build());
                    Intent i=new Intent(AdminAddCandidate.this,AdminHome.class);
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
                params.put(Config.KEY_ELECTION_NAME,election_name);


                RequestHandler rh=new RequestHandler();
                String res=rh.SendPostRequest(Config.URL_CANDIDATE_REGISTRATION,params);
                return res;
            }
        }

        Inserting i=new Inserting();
        i.execute();
    }



    public void MakeChoice(View v){
        switch (v.getId()) {
            case R.id.admin_add_candidate_add:
                InsertCandidateDetails();
                break;
            case R.id.admin_add_candidate_exit:
                ActivityCompat.finishAffinity(AdminAddCandidate.this);
                break;
        }
    }


}
