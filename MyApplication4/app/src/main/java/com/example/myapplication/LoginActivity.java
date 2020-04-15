package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends Activity implements HttpResponeCallBack {

    private EditText loginAccount;//账号
    private EditText loginPassword;//密码
    private Button loginBtn;
    private Button registerBtn;
    private final static String SERVER_URL = "http://39.100.73.118/deeplearning_photo/login.php";
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        init();
    }

    /**
     * 初始化数据
     */
    
    
    public String getRegistData(String nickname,String password) 
    {
    	String string = "none";
		int serverResponseCode = 0;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
		//start

        try {
            
            URL url = new URL(SERVER_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);//Allow Inputs
            connection.setDoOutput(true);//Allow Outputs
            connection.setUseCaches(false);//Don't use a cached Copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //connection.setRequestProperty("uploaded_file", nickname);

            //creating new dataoutputstream
            dataOutputStream = new DataOutputStream(connection.getOutputStream());

            //writing bytes to data outputstream
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user_name\";filename=\""
                    + nickname + "_" + password + "\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);

            
            
            
//            
//			dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"passward\";filename=\""
//                    + password + "\"" + lineEnd);
//            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            //Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

            //response code of 200 indicates the server status OK
            dataOutputStream.flush();
            dataOutputStream.close();
            InputStreamReader reader =null;
            BufferedReader reader2 =null;
            
            if (serverResponseCode == 200) {
            	reader = new InputStreamReader(connection.getInputStream(),"utf-8"); 
                reader2 = new BufferedReader(reader);
               string = reader2.readLine();
               if(string.equals("no"))
               {
            	   System.out.println("服务端响应的结果"+string);
               }
               if(string.equals("yes"))
               {
            	   System.out.println("服务端响应的结果"+string);
               }
               System.out.println("服务端响应的结果"+string);
               
            	//Toast.makeText(RegisterActivity.this, "成功111", Toast.LENGTH_SHORT).show();
            }

            //closing the input and output streams 
            
            
            
        }  catch (MalformedURLException e) {
            e.printStackTrace();
            //Toast.makeText(RegisterActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return (string);
    }
    
    
    
    
    
    
    
    
    
    
    private void init() {
        loginAccount = (EditText) findViewById(R.id.login_account);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        registerBtn = (Button) findViewById(R.id.register_btn);
        //点击登录按钮
        loginBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final String account = loginAccount.getText().toString();//账号
                final String password = loginPassword.getText().toString();//密码
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                	new Thread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	String result = "none";
	                        //creating new thread to handle Http Operations
	                    	Looper.prepare();
	                    	result = getRegistData(account, password);
	                    	
	                    	if(result.equals("no"))
	                    	{
	                    		Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
	                    	}
	                    	else if(result.equals("yes"))
	                    	{
	                    		Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
	                    		//ItLanBaoApplication.getInstance().setBaseUser(info);//保存到Application中

	                            //保存到SP中
	                            UserPreference.save(KeyConstance.IS_USER_ID, account);

	                            UserPreference.save(KeyConstance.IS_USER_ACCOUNT, account);
	                            UserPreference.save(KeyConstance.IS_USER_PASSWORD, password);


	                            Intent intent = new Intent();
	                            intent.setClass(LoginActivity.this, MainActivity.class);
	                            startActivity(intent);
	                            overridePendingTransition(android.R.anim.slide_in_left,
	                                    android.R.anim.slide_out_right);
	                            finish();
	                    	}
	                    	else if(result.equals("none"))
	                    	{
	                    		Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
	                    	}
	                    	Looper.loop();	
	                    	//Toast.makeText(RegisterActivity.this, "成功111", Toast.LENGTH_SHORT).show();
	                    	 
//	                    	Map<String, String> params = new HashMap<String, String>(); 
//	                        params.put("user_name", "asd"); 
//	                        params.put("user_name", "123"); 
//	                        String result = null;
//							try {
//								result = sendPostMessage(params,"utf-8");
//							} catch (MalformedURLException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} 
//	                        System.out.println("result->"+result);
	                    }
	                }).start();
                    //RequestApiData.getInstance().getLoginData(account, password, UserBaseInfo.class, LoginActivity.this);
                } else {
                    Toast.makeText(LoginActivity.this, "账号或者密码有误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onResponeStart(String apiName) {
        // TODO Auto-generated method stub

        if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {
            Toast.makeText(LoginActivity.this, "正在加载数据中", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoading(String apiName, long count, long current) {
        // TODO Auto-generated method stub
        Toast.makeText(LoginActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String apiName, Object object) {
        // TODO Auto-generated method stub
        if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {
            //邮箱登录返回数据
            if (object != null && object instanceof UserBaseInfo) {
                UserBaseInfo info = (UserBaseInfo) object;
                if (info.getRet().equals(Constant.KEY_SUCCESS)) {

                    //登录成功，保存登录信息
                    ItLanBaoApplication.getInstance().setBaseUser(info);//保存到Application中

                    //保存到SP中
                    UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));

                    UserPreference.save(KeyConstance.IS_USER_ACCOUNT, info.getEmail());
                    UserPreference.save(KeyConstance.IS_USER_PASSWORD, loginPassword.getText().toString());


                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right);
                    finish();

                } else {
                    Log.e("TAG", "info="+info.toString());
                    if (info.getErrcode().equals(Constant.KEY_NO_REGIST)) {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "info.getMsg()="+info.getMsg());
                    }

                }
            }
        }

    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo,
                          String strMsg) {
        // TODO Auto-generated method stub
        Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();   
    }
}
