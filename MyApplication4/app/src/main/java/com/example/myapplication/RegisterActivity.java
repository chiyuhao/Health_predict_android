package com.example.myapplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements HttpResponeCallBack {

    private EditText loginNick;//用户昵称
    
    private EditText password;//注册密码
    private Button registBtn;//注册
    
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = SystemClock.uptimeMillis(); // �˷���������Android
        if (time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }
    
    private final static String SERVER_URL = "http://39.100.73.118/deeplearning_photo/register.php";
	private static RequestApiData instance = null;
	private HttpResponeCallBack mCallBack = null;
	private static URL url;
	
    
	public static String sendPostMessage(Map<String, String> params,String encode) throws MalformedURLException{ 
		url = new URL(SERVER_URL);
        StringBuffer buffer = new StringBuffer(); 
        try {//把请求的主体写入正文！！ 
        	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
             if(params != null&&!params.isEmpty()){ 
                //迭代器 
            	 for(Map.Entry<String, String> entry : params.entrySet()){ 
                  buffer.append(entry.getKey()).append("="). 
                  append(URLEncoder.encode(entry.getValue(),encode)). 
                  append("&"); 
              } 
            } 
//            System.out.println(buffer.toString()); 
              //删除最后一个字符&，多了一个;主体设置完毕 
              buffer.deleteCharAt(buffer.length()-1); 
              byte[] mydata = buffer.toString().getBytes();  
              HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
              connection.setConnectTimeout(3000); 
              connection.setDoInput(true);//表示从服务器获取数据 
              connection.setDoOutput(true);//表示向服务器写数据 
           
             connection.setRequestMethod("POST"); 
              //是否使用缓存 
              connection.setUseCaches(false); 
              //表示设置请求体的类型是文本类型 
              connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
                 
              connection.setRequestProperty("Content-Length", String.valueOf(mydata.length)); 
              connection.connect();   //连接，不写也可以。。？？有待了解 
              
              //获得输出流，向服务器输出数据 
              OutputStream outputStream = connection.getOutputStream(); 
                   outputStream.write(mydata,0,mydata.length); 
                   //获得服务器响应的结果和状态码 
              int responseCode = connection.getResponseCode();  
              if(responseCode == HttpURLConnection.HTTP_OK){ 
                 return changeInputeStream(connection.getInputStream(),encode); 
                     
              } 
        } catch (UnsupportedEncodingException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (IOException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
           
        return ""; 
    } 
    
	private static String changeInputeStream(InputStream inputStream,String encode) { 
        //通常叫做内存流，写在内存中的 
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
        byte[] data = new byte[1024]; 
        int len = 0; 
        String result = ""; 
        if(inputStream != null){ 
            try { 
                while((len = inputStream.read(data))!=-1){ 
                    data.toString(); 
                       
                    outputStream.write(data, 0, len); 
                } 
                //result是在服务器端设置的doPost函数中的 
                result = new String(outputStream.toByteArray(),encode); 
                outputStream.flush(); 
            } catch (IOException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
            } 
        } 
        return result; 
    } 
	
	
	
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
            String androidid = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user_name\";filename=\""
                    + nickname + "_" + password + "_" + androidid + "\"" + lineEnd);
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
    
    
    
    

    private void initView() {
        loginNick = (EditText) findViewById(R.id.regist_nick);
        password = (EditText) findViewById(R.id.regist_password);
        registBtn = (Button) findViewById(R.id.regist_btn);

        registBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //获得用户输入的信息
                final String nick = loginNick.getText().toString();
                final String passwordStr = password.getText().toString();
                if(isFastDoubleClick() == false)
                {
                if (!TextUtils.isEmpty(nick)
                        && !TextUtils.isEmpty(passwordStr)) {

                	new Thread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	String result = "none";
	                        //creating new thread to handle Http Operations
	                    	Looper.prepare();
	                    	result = getRegistData(nick, passwordStr);
	                    	
	                    	if(result.equals("no"))
	                    	{
	                    		Toast.makeText(RegisterActivity.this, "用户名已注册", Toast.LENGTH_SHORT).show();
	                    	}
	                    	else if(result.equals("yes"))
	                    	{
	                    		Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
	                    	}
	                    	else if(result.equals("none"))
	                    	{
	                    		Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
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
                	
                	//Toast.makeText(RegisterActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    //RequestApiData.getInstance().getRegistData(nick, passwordStr,
                    //            AnalyticalRegistInfo.class, RegisterActivity.this);
                    
                } else {
                	Toast.makeText(RegisterActivity.this, "输入信息未完全", Toast.LENGTH_SHORT).show();
                }
                }
            }
        });
    }

    @Override
    public void onResponeStart(String apiName) {
        // TODO Auto-generated method stub
        Toast.makeText(RegisterActivity.this, "正在请求数据...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading(String apiName, long count, long current) {
        Toast.makeText(RegisterActivity.this, "Loading...", Toast.LENGTH_SHORT).show(); 
    }

    @Override
    public void onSuccess(String apiName, Object object) {
        // TODO Auto-generated method stub
        //注册接口
        if (UrlConstance.KEY_REGIST_INFO.equals(apiName)) {
            if (object != null && object instanceof AnalyticalRegistInfo) {
                AnalyticalRegistInfo info = (AnalyticalRegistInfo) object;
                String successCode = info.getRet();
                //请求成功
                if (successCode.equals(Constant.KEY_SUCCESS)) {
                    UserBaseInfo baseUser = new UserBaseInfo();
                    baseUser.setEmail(info.getEmail());
                    baseUser.setNickname(info.getNickname());
                    baseUser.setUserhead(info.getUserhead());
                    baseUser.setUserid(String.valueOf(info.getUserid()));
                    ItLanBaoApplication.getInstance().setBaseUser(baseUser);
                    UserPreference.save(KeyConstance.IS_USER_ID, String.valueOf(info.getUserid()));
                    UserPreference.save(KeyConstance.IS_USER_ACCOUNT, info.getEmail());
                    UserPreference.save(KeyConstance.IS_USER_PASSWORD, password.getText().toString());


                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    RegisterActivity.this.startActivity(intent);
                    Toast.makeText(RegisterActivity.this, "注册成功...", Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();

                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onFailure(String apiName, Throwable t, int errorNo, String strMsg) {
        Toast.makeText(RegisterActivity.this, "Failure", Toast.LENGTH_SHORT).show();
    }
}
