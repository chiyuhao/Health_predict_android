package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 欢迎界面
 */
public class WelcomeActiviy extends Activity implements HttpResponeCallBack {

    private ImageView iv;
    private final static String SERVER_URL = "http://39.100.73.118/deeplearning_photo/login.php";
    
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activiy);

        iv = (ImageView) this.findViewById(R.id.logo);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(2000);
        iv.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            /**
             * 动画结束时判断是否保存有登录的信息
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                //暂时用用户名密码登录
                String userAccount = UserPreference.read(KeyConstance.IS_USER_ACCOUNT, null);//软件还没有保持账号
                String userPassword = UserPreference.read(KeyConstance.IS_USER_PASSWORD, null);
                String userid = UserPreference.read(KeyConstance.IS_USER_ID, null);
                final String account = userAccount;//账号
                final String password = userPassword;//密码
                if (TextUtils.isEmpty(userAccount)) {//没有保存的登录信息跳转到登录界面
                    //空的，表示没有注册，或者清除数据
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActiviy.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else {
                	new Thread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	String result = "none";
	                        //creating new thread to handle Http Operations
	                    	Looper.prepare();
	                    	result = getRegistData(account, password);
	                    	
	                    	if(result.equals("no"))
	                    	{
	                    		Toast.makeText(WelcomeActiviy.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
	                    	}
	                    	else if(result.equals("yes"))
	                    	{
	                    		Toast.makeText(WelcomeActiviy.this, "登录成功", Toast.LENGTH_SHORT).show();
	                    		//ItLanBaoApplication.getInstance().setBaseUser(info);//保存到Application中

	                            //保存到SP中
	                            UserPreference.save(KeyConstance.IS_USER_ID, account);

	                            UserPreference.save(KeyConstance.IS_USER_ACCOUNT, account);
	                            UserPreference.save(KeyConstance.IS_USER_PASSWORD, password);


	                            Intent intent = new Intent();
	                            intent.setClass(WelcomeActiviy.this, MainActivity.class);
	                            startActivity(intent);
	                            overridePendingTransition(android.R.anim.slide_in_left,
	                                    android.R.anim.slide_out_right);
	                            finish();
	                    	}
	                    	else if(result.equals("none"))
	                    	{
	                    		Toast.makeText(WelcomeActiviy.this, "网络错误", Toast.LENGTH_SHORT).show();
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
                    //用保存的信息直接登录
//                    RequestApiData.getInstance().getLoginData(userAccount, userPassword,
//                            UserBaseInfo.class, WelcomeActiviy.this);

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onResponeStart(String apiName) {

    }

    @Override
    public void onLoading(String apiName, long count, long current) {
        Toast.makeText(WelcomeActiviy.this, "Loading...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String apiName, Object object) {
        //当前接口是否是获取用户的基本信息的接口
        if (UrlConstance.KEY_USER_BASE_INFO.equals(apiName)) {
            if (object != null && object instanceof UserBaseInfo) {
                UserBaseInfo info = (UserBaseInfo) object;
                ItLanBaoApplication.getInstance().setBaseUser(info);//把数据放入到Application里面，全局
                UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));

                Intent intent = new Intent();
                intent.setClass(WelcomeActiviy.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
                finish();

            } else {
                Toast.makeText(WelcomeActiviy.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        } else if (UrlConstance.KEY_LOGIN_INFO.equals(apiName)) {//当前接口是登录的接口
            //登录返回数据
            if (object != null && object instanceof UserBaseInfo) {
                UserBaseInfo info = (UserBaseInfo) object;
                if (Constant.KEY_SUCCESS.equals(info.getRet())) {

                    ItLanBaoApplication.getInstance().setBaseUser(info);//将用户信息保存在Application中
                    UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));

                    Intent intent = new Intent();
                    intent.setClass(WelcomeActiviy.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right);
                    finish();

                } else {
                    Toast.makeText(WelcomeActiviy.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(WelcomeActiviy.this, "Failure", Toast.LENGTH_SHORT).show();
    }
}
