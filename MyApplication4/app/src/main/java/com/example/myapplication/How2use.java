package com.example.myapplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class How2use extends Activity implements View.OnClickListener{

	private Button mMain;
    private Button hHelp;
    private Button uUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how2use);
        mMain = (Button) findViewById(R.id.m_Main);
        hHelp = (Button) findViewById(R.id.h_Help);
        uUser = (Button) findViewById(R.id.u_User);
        mMain.setOnClickListener(this);
        hHelp.setOnClickListener(this);
        uUser.setOnClickListener(this);
        
    }

    /**
     * 初始化数据
     */
    @Override
    public void onClick(View v) {
        if (v == mMain) {

            //on attachment icon click
        	Intent intent = new Intent();
            intent.setClass(How2use.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        }
        if (v == uUser) {

            //on attachment icon click
        	Intent intent = new Intent();
            intent.setClass(How2use.this, User_page.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            
        }
        if (v == hHelp) {

            //on attachment icon click
        	Intent intent = new Intent();
            intent.setClass(How2use.this, Help_page.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
//            finish();
        }
       

    }
//    @Override
//    public void onBackPressed() {
//    	Intent intent = new Intent();
//      intent.setClass(Help_page.this, MainActivity.class);
//      startActivity(intent);
//    }
    

    

    


}
