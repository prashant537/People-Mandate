package com.example.prashant_tripathi.onlinevotingapp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Prashant_Tripathi on 26-06-2017.
 */
public class AdminRemoveElection extends AppCompatActivity {
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
        registerForContextMenu(list);
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

    private void DeleteElection(){
       final String name=elec_name.trim();


        class Deleting extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminRemoveElection.this,"Deleting ...","Please Wait ...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                NotificationCompat.Builder builder=new NotificationCompat.Builder(AdminRemoveElection.this);

                if(s.equals("Success")){
                    election_search.setText("");
                    GetJson();
                    builder.setSmallIcon(R.drawable.btn_check_on_holo_light);
                    builder.setContentText("Successfully Deleted ...");
                    NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(1, builder.build());
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


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select An Action");
        menu.add(0, v.getId(), 0, "Edit Details");
        menu.add(0, v.getId(), 0, "Delete Contest");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        elec_name=l1.get((int)info.id);
        if(item.getTitle()=="Edit Details"){
            Intent i =new Intent(getApplicationContext(),AdminEditElection.class);
            i.putExtra("Details",elec_name);
            startActivity(i);
        }
        else if(item.getTitle()=="Delete Contest"){
                    AlertDialog.Builder delete = new AlertDialog.Builder(AdminRemoveElection.this);
                    delete.setTitle("Delete Election");
                    delete.setMessage("Sure Want To Delete ?");
                    delete.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                    DeleteElection();
                        }
                    });
                    delete.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    delete.show();
                    return false;
        }
        else{
            return false;
        }
        return true;
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

        SpecialAdapter adapter=new SpecialAdapter(AdminRemoveElection.this,l,R.layout.admin_remove_election_layout,new String[]{Config.KEY_ELECTION_NAME,Config.KEY_ELECTION_STATE,Config.KEY_ELECTION_DATE},new int[]{R.id.admin_remove_election_name,R.id.admin_remove_election_state,R.id.admin_remove_election_date});
        list.setAdapter(adapter);
    }


    private void GetJson(){
        class ReceiveJson extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(AdminRemoveElection.this,"Loading ...","Please Wait ...",false,false);
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
