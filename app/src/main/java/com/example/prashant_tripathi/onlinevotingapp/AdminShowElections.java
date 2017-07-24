package com.example.prashant_tripathi.onlinevotingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Prashant_Tripathi on 05-07-2017.
 */
public class AdminShowElections extends AppCompatActivity {
    private ListView list;
    private String JSON_STRING;
    String elec_name;
    AutoCompleteTextView election_search;
    private ArrayList<String> l1=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_remove_election);
        list=(ListView)findViewById(R.id.admin_remove_election_list);
        election_search=(AutoCompleteTextView)findViewById(R.id.admin_remove_election_search);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(AdminShowElections.this,AdminRemoveCandidate.class);
                HashMap<String,String> map=(HashMap)parent.getItemAtPosition(position);
                elec_name=map.get(Config.KEY_ELECTION_NAME).toString();
                i.putExtra("Election",elec_name);
                startActivity(i);
            }
        });

        GetJson();

    }

    public class SpecialAdapter extends SimpleAdapter {
        private int[] colors = new int[]{0x30FF0000, 0x300000FF};

        public SpecialAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
            super(context, items, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
                int colorPos = position % colors.length;
                view.setBackgroundColor(colors[colorPos]);
            return view;
        }

    }

    private void ShowElections(){
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
                String date=jo.getString(Config.KEY_ELECTION_DATE);
                HashMap<String,String> all=new HashMap<>();
                all.put(Config.KEY_ELECTION_NAME,name);
                all.put(Config.KEY_ELECTION_STATE, state);
                all.put(Config.KEY_ELECTION_DATE,date);
                l.add(all);
                l2.add(name);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        l1=l2;

        election_search.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l2));
        election_search.setThreshold(1);

        SpecialAdapter adapter=new SpecialAdapter(AdminShowElections.this,l,R.layout.admin_remove_election_layout,new String[]{Config.KEY_ELECTION_NAME,Config.KEY_ELECTION_STATE,Config.KEY_ELECTION_DATE},new int[]{R.id.admin_remove_election_name,R.id.admin_remove_election_state,R.id.admin_remove_election_date});
        list.setAdapter(adapter);
    }



    private void GetJson(){
        class ReceiveJson extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminShowElections.this,"Loading ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING=s;
                if(JSON_STRING!=null){
                    ShowElections();
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
}
