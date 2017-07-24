package com.example.prashant_tripathi.onlinevotingapp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
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
 * Created by Prashant_Tripathi on 02-07-2017.
 */
public class AdminShowResults extends AppCompatActivity {
    private ListView list;
    private String JSON_STRING;
    String elec_name;
    AutoCompleteTextView candidate_name_search;
    private ArrayList<String> name=new ArrayList<String>();
    private ArrayList<String> party=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=getIntent().getExtras();
        elec_name=b.getString("Election");
        setContentView(R.layout.admin_remove_candidate);
        list=(ListView)findViewById(R.id.admin_remove_candidate_list);
        candidate_name_search=(AutoCompleteTextView)findViewById(R.id.admin_remove_candidate_candidate_search);
        GetJsonAgain();
    }

    public class SpecialAdapter extends SimpleAdapter {
        private int[] colors = new int[]{0x30FF0000, 0x300000FF};

        public SpecialAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
            super(context, items, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if(position==0){
                view.setBackgroundColor(Color.parseColor("#32CD32"));
            }
            else{
            int colorPos = position % colors.length;
            view.setBackgroundColor(colors[colorPos]);}
            return view;
        }

    }

    private void ShowCandidates(){
        JSONObject jsonObject=null;
        ArrayList<HashMap<String,String>> l=new ArrayList<HashMap<String, String>>();
        ArrayList<String> l1=new ArrayList<String>();
        ArrayList<String> l2=new ArrayList<String>();
        try{
            jsonObject=new JSONObject(JSON_STRING);
            JSONArray result=jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for(int i=0;i<result.length();i++){
                JSONObject jo=result.getJSONObject(i);
                String candi_name=jo.getString(Config.KEY_CANDIDATE_NAME);
                String candi_party=jo.getString(Config.KEY_CANDIDATE_PARTY);
                String candi_votes=jo.getString(Config.KEY_CANDIDATE_VOTES);
                HashMap<String,String> all=new HashMap<>();
                all.put(Config.KEY_CANDIDATE_NAME,candi_name);
                all.put(Config.KEY_CANDIDATE_PARTY, candi_party);
                all.put(Config.KEY_CANDIDATE_VOTES,candi_votes);
                l.add(all);
                l1.add(candi_name);
                l2.add(candi_party);
                name=l1;
                party=l2;
                System.out.println("Name "+name);
                System.out.println("Party "+party);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        candidate_name_search.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,l1));
        candidate_name_search.setThreshold(1);

        SpecialAdapter adapter=new SpecialAdapter(AdminShowResults.this,l,R.layout.admin_result_layout,new String[]{Config.KEY_CANDIDATE_NAME,Config.KEY_CANDIDATE_PARTY,Config.KEY_CANDIDATE_VOTES},new int[]{R.id.admin_result_candidate_name,R.id.admin_result_candidate_party,R.id.admin_result_candidate_votes});
        list.setAdapter(adapter);
    }




    private void GetJsonAgain(){
        class ReceiveJsonAgain extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminShowResults.this,"Loading ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING=s;
                System.out.println("3. "+JSON_STRING);
                if(JSON_STRING!=null){
                    ShowCandidates();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh=new RequestHandler();
                String s=rh.SendGetRequestParam(Config.URL_SHOW_RESULTS,elec_name);
                return s;
            }
        }

        ReceiveJsonAgain rj=new ReceiveJsonAgain();
        rj.execute();
    }
}
