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

public class User_page extends Activity implements View.OnClickListener {


    private Button loginBtn;
    private Button cancelBtn;
    private TextView textview111;
    private Button mMain;
    private Button hHelp;
    private Button uUser;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_user);
        loginBtn = (Button) findViewById(R.id.lLogin);
        cancelBtn = (Button) findViewById(R.id.cCancel);
        textview111 = (TextView) findViewById(R.id.tvAccount);
        mMain = (Button) findViewById(R.id.m_Main);
        hHelp = (Button) findViewById(R.id.h_Help);
        uUser = (Button) findViewById(R.id.u_User);
        mMain.setOnClickListener(this);
        hHelp.setOnClickListener(this);
        uUser.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
//        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.tvAccount);
//        TextView textview=new TextView(this);
        //init();
        String userAccount = UserPreference.read(KeyConstance.IS_USER_ACCOUNT, null);
        if(userAccount == null)
        {
        	textview111.setText("无登录账号");
        	
        }
        else
        {
        	textview111.setText(userAccount);
        	
        }
    }

    /**
     * 初始化数据
     */
    
    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
        	Intent intent = new Intent();
            intent.setClass(User_page.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            
            
        }
        if (v == cancelBtn) {
        	SharedPreferences sp;

	        sp = getSharedPreferences("user_preference",0);
	        sp.edit().clear().commit();
	        Intent intent = new Intent();
            intent.setClass(User_page.this, User_page.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            finish();

           
        }
        if (v == mMain) {

            //on attachment icon click
        	Intent intent = new Intent();
            intent.setClass(User_page.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        }
        if (v == uUser) {

            //on attachment icon click
        	Intent intent = new Intent();
            intent.setClass(User_page.this, User_page.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            
        }
        if (v == hHelp) {

            //on attachment icon click
        	Intent intent = new Intent();
            intent.setClass(User_page.this, Help_page.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            
        }
       

    }

    

    private void init() {
        
        //点击登录按钮
        loginBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
            	
            	Intent intent = new Intent();
                intent.setClass(User_page.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                finish();
            	
            	
                
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	SharedPreferences sp;

		        sp = getSharedPreferences("user_preference",0);
		        sp.edit().clear().commit();
	          
            }
        });
    }


}
