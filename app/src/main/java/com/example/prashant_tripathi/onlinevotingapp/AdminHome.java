package com.example.prashant_tripathi.onlinevotingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Prashant_Tripathi on 23-06-2017.
 */
public class AdminHome extends AppCompatActivity {
    Button add_election,remove_election,add_candidate,remove_candidate,show_results;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        Admin_Home_Init();
    }

    public void Admin_Home_Init(){
        add_election=(Button)findViewById(R.id.admin_home_add_election);
        remove_election=(Button)findViewById(R.id.admin_home_remove_election);
        add_candidate=(Button)findViewById(R.id.admin_home_add_candidate);
        remove_candidate=(Button)findViewById(R.id.admin_home_remove_candidate);
        show_results=(Button)findViewById(R.id.admin_home_result);
    }

    public void SelectOption(View v){
        switch(v.getId()){
            case R.id.admin_home_add_election:
                Intent i=new Intent(getApplicationContext(),AdminAddElection.class);
                startActivity(i);
                break;
            case R.id.admin_home_remove_election:
                Intent j=new Intent(getApplicationContext(),AdminRemoveElection.class);
                startActivity(j);
                break;
            case R.id.admin_home_add_candidate:
                Intent x=new Intent(getApplicationContext(),AdminAddCandidate.class);
                startActivity(x);
                break;
            case R.id.admin_home_remove_candidate:
                Intent y=new Intent(getApplicationContext(),AdminShowElections.class);
                startActivity(y);
                break;
            case R.id.admin_home_result:
                Intent r=new Intent(getApplicationContext(),AdminViewElections.class);
                startActivity(r);
                break;
        }
    }



}
