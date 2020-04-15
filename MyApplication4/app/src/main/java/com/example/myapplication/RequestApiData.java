package com.example.myapplication;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.io.*;
import java.io.File;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;



import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
/*
 * 缂冩垹绮堕幒銉ュ經 wjl
 */

/**
 * @author Administrator
 *
 */
public class RequestApiData {
	private String SERVER_URL = "http://39.100.73.118/deeplearning_photo/register.php";
	private static RequestApiData instance = null;
	private HttpResponeCallBack mCallBack = null;

	//閸掓稑缂撻幒銉ュ經鐎电钖�
	public static RequestApiData getInstance() {
		if (instance == null) {
			instance = new RequestApiData();
		}
		return instance;
	}

	/**
	 * 4.6濞夈劌鍞介悽銊﹀煕閹恒儱褰�
	 * @param nickname  閺勭數袨
	 * @param email 闁喚顔�
	 * @param password 鐎靛棛鐖�
	 * @param clazz  閺佺増宓佹潻鏂挎礀閻ㄥ嫯袙閺嬫劕顕挒锟�
	 * @param callback 閸ョ偠鐨�
	 * 閻楃懓鍩嗙憰浣规暈閹板繐寮弫棰佺秴缂冾喕绗夐懗钘夊綁鐟曚焦鐗撮幑顔芥瀮濡楋絾娼�
	 * 鐠囬攱鐪伴弬鐟扮础閿涙瓍OST
	 */
	public void getRegistData(String nickname
			,String password, Class<AnalyticalRegistInfo> clazz,
		   HttpResponeCallBack callback) {
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
                    + nickname + "\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);

            
            
            
            
			dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"passward\";filename=\""
                    + password + "\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();
            //Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

            //response code of 200 indicates the server status OK
            if (serverResponseCode == 200) {
            	//tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/" + fileName);
                
            }

            //closing the input and output streams 
            dataOutputStream.flush();
            dataOutputStream.close();


        }  catch (MalformedURLException e) {
            e.printStackTrace();
            //Toast.makeText(RegisterActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		//end
		
		
		
		
		
		
		
		
		
		
		
//		 mCallBack = callback;
//		 //鏉╂瑦妲稿В蹇庣娑擃亝甯撮崣锝囨畱閸烆垯绔撮弽鍥┿仛
//		 String tagUrl = UrlConstance.KEY_REGIST_INFO;//濞夈劌鍞介幒銉ュ經
//		 //鐏忓棙鏁為崘宀�娈戞穱鈩冧紖娣囨繂鐡ㄩ崷鈺゛p娑擃叏绱欐い璇叉嫲閺堝秴濮熼崳銊ь伂娑擄拷閼疯揪绱�
//		 HashMap<String, String> parameter = new HashMap<String, String>();
//		 parameter.put("nickname", nickname);
//		 parameter.put("password",password);
//
//		//閹峰吋甯撮崣鍌涙殶娣団剝浼呴敍灞炬█缁夊府绱濋柇顔绢唸閿涘苯鐦戦惍渚婄礉閸忣剟鎸滈敍灞借嫙閻⑩暕d5鏉╂稖顢戦崝鐘茬槕
//        StringBuilder builder = new StringBuilder();
//        builder.append(nickname);
//        builder.append(password);
//        builder.append(UrlConstance.PUBLIC_KEY);
//
//        parameter.put(UrlConstance.ACCESSTOKEN_KEY,MD5Util.getMD5Str(builder.toString()));
//
//		 //鐠囬攱鐪伴弫鐗堝祦閹恒儱褰�
//		 RequestManager.post(UrlConstance.APP_URL,tagUrl, parameter, clazz, callback);

	}


	/**
	 * 4.8閻ц缍嶉悽銊﹀煕閹恒儱褰�
	 * @param email 闁喚顔�
	 * @param password 鐎靛棛鐖�
	 * @param clazz 閺佺増宓佹潻鏂挎礀閻ㄥ嫯袙閺嬫劕顕挒锟�
	 * @param callback 閸ョ偠鐨�
	 * 閻楃懓鍩嗙憰浣规暈閹板繐寮弫棰佺秴缂冾喕绗夐懗钘夊綁鐟曚焦鐗撮幑顔芥瀮濡楋絾娼�
	 * 鐠囬攱鐪伴弬鐟扮础閿涙瓍OST
	 */
	public void getLoginData(String nickname ,String password,
			Class<UserBaseInfo> clazz,
		   HttpResponeCallBack callback) {
		 mCallBack = callback;
		 //鏉╂瑦妲稿В蹇庣娑擃亝甯撮崣锝囨畱閸烆垯绔撮弽鍥┿仛
		 String tagUrl = UrlConstance.KEY_LOGIN_INFO;//閻ц缍嶉幒銉ュ經
		 HashMap<String, String> parameter = new HashMap<String, String>();
		 parameter.put("nickname", nickname);
		 parameter.put("password", password);

			//閹峰吋甯撮崣鍌涙殶娣団剝浼呴敍宀勫仏缁犳唻绱濈�靛棛鐖滈敍灞藉彆闁姐儻绱濋獮鍓佹暏md5鏉╂稖顢戦崝鐘茬槕
			StringBuilder builder = new StringBuilder();
			builder.append(nickname);
			builder.append(password);
			builder.append(UrlConstance.PUBLIC_KEY);

		 parameter.put(UrlConstance.ACCESSTOKEN_KEY,MD5Util.getMD5Str(builder.toString()));

		 //鐠囬攱鐪伴弫鐗堝祦閹恒儱褰�
		 RequestManager.post(UrlConstance.APP_URL,tagUrl, parameter, clazz, callback);

	}

}
