package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;


public class Uploaded extends Activity implements View.OnClickListener {
	



    

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    public String selectedFilePath;
    private String SERVER_URL = "http://39.100.73.118/deeplearning_photo/draw_photo_new.php";
    Button ivAttachment;
    ImageView imgView;
    ImageView imgView2;
    ImageView img_show;
    Button bUpload;
    Button d_download;
    Button cstartCamera;
    Button mMain;
    Button hHelp;
    Button uUser;
    TextView tvFileName;
    ProgressDialog dialog;
    private String mImagePath;
    private String mFileName; //ͼƬ����
    private Uri mImageUri; //ͼƬ·��Uri
    public static final int TAKE_PHOTO = 2;
    public static final int CROP_PHOTO = 3;
    private final int PICK = 4;
    private String imgString = "";

    
    


    
    
    public String[] checkPermission(String[] rawPermission) {
        List<String> requestList = new ArrayList();
        for (String permission : rawPermission) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestList.add(permission);
            }
        }
        return requestList.toArray(new String[requestList.size()]);// return the permission needed to be requested
    }


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded);
        //requestPermission();

        imgView = (ImageView) findViewById(R.id.imgView);
        imgView2 = (ImageView) findViewById(R.id.imgView2);
        mMain = (Button) findViewById(R.id.m_Main);
        hHelp = (Button) findViewById(R.id.h_Help);
        uUser = (Button) findViewById(R.id.u_User);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);

        mMain.setOnClickListener(this);
        hHelp.setOnClickListener(this);
        uUser.setOnClickListener(this);
        uploadFile();
        //d_download.setOnClickListener(this);
    }
    
    
//    @Override
//    public void onBackPressed() {
//    	Intent intent = new Intent();
//      intent.setClass(Uploaded.this, MainActivity.class);
//      startActivity(intent);
//    }

    

    public void  download_headportrait_new(String user_name){
        final String url_head="http://39.100.73.118/deeplearning_photo/result_photo/" + user_name + "/";
        final String pic_name="dotplot.png";
        final String pic_name2="heatmap.png";
                try {
                	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                	
                    //�����url������Ҫ���ص��ļ���·��
                    URL url = new URL(url_head+pic_name);
                    //����һ��HttpURLConnection�����Ӷ���
                    HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                    //���÷�ʽΪGET ��ʽ ���� ���������Ҳ�� ��ΪHttpURLConnection Ĭ��ΪGET��ʽ
                    httpURLConnection.setRequestMethod("GET");
                    //��ȡ��Ӧ��
                    int statusCode = httpURLConnection.getResponseCode();
                    //��ȡ�������ļ���InputStream����
                    if (statusCode == 200) {
                        InputStream inputStream=httpURLConnection.getInputStream();
                        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        //dialog.dismiss();
                        imgView.setImageBitmap(bitmap);
                        //���������ļ���InputStream����
                        //�����bitmap��������Ҫ���ص�ͼƬ��Ȼ���ڽ��к�������
                    }
                    URL url2 = new URL(url_head+pic_name2);
                    //����һ��HttpURLConnection�����Ӷ���
                    HttpURLConnection httpURLConnection2 =(HttpURLConnection)url2.openConnection();
                    //���÷�ʽΪGET ��ʽ ���� ���������Ҳ�� ��ΪHttpURLConnection Ĭ��ΪGET��ʽ
                    httpURLConnection2.setRequestMethod("GET");
                    //��ȡ��Ӧ��
                    int statusCode2 = httpURLConnection2.getResponseCode();
                    //��ȡ�������ļ���InputStream����
                    if (statusCode == 200) {
                        InputStream inputStream2=httpURLConnection2.getInputStream();
                        Bitmap bitmap2= BitmapFactory.decodeStream(inputStream2);
                        //dialog.dismiss();
                        imgView2.setImageBitmap(bitmap2);
                        //���������ļ���InputStream����
                        //�����bitmap��������Ҫ���ص�ͼƬ��Ȼ���ڽ��к�������
                    }
 
                }catch (Exception e){
                    Log.getStackTraceString(e);
                }
        
    }
    
    
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = SystemClock.uptimeMillis(); // �˷���������Android
        if (time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    
    

    
    private Intent intent;
    
    @Override
    public void onClick(View v) {

        if (v == mMain) {

            //on attachment icon click
        	Intent intent = new Intent();
            intent.setClass(Uploaded.this, MainActivity.class);
            startActivity(intent);
        }
        if (v == uUser) {

            //on attachment icon click
        	Intent intent = new Intent();
            intent.setClass(Uploaded.this, User_page.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            finish();
        }
        if (v == hHelp) {

            //on attachment icon click
        	Intent intent = new Intent();
            intent.setClass(Uploaded.this, Help_page.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            finish();
        }
//        if (v == d_download) {
//        	//dialog = ProgressDialog.show(MainActivity.this, "", "Uploading File...", true);
//            //on upload button Click
//        	new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //creating new thread to handle Http Operations
//                	download_headportrait();
//                	
//                }
//            }).start();
//        	
//        	
//
//        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }
    
  //*********************************  
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * ����api19����(������api19),����uri��ȡͼƬ�ľ���·��
     *
     * @param context �����Ķ���
     * @param uri     ͼƬ��Uri
     * @return ���Uri��Ӧ��ͼƬ����, ��ô���ظ�ͼƬ�ľ���·��, ���򷵻�null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * ����api19������,����uri��ȡͼƬ�ľ���·��
     *
     * @param context �����Ķ���
     * @param uri     ͼƬ��Uri
     * @return ���Uri��Ӧ��ͼƬ����, ��ô���ظ�ͼƬ�ľ���·��, ���򷵻�null
     */

    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // �����document���͵� uri, ��ͨ��document id�����д���
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // ʹ��':'�ָ�
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            // ����� content ���͵� Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // ����� file ���͵� Uri,ֱ�ӻ�ȡͼƬ��Ӧ��·��
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * ��ȡ���ݿ���е� _data �У�������Uri��Ӧ���ļ�·��
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    
    
    
    
    
    
    
    
    
    
    
//****************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }
  
                
                
                Uri selectedFileUri = data.getData();
                
                
                //selectedFilePath = FilePath.getPath(this, selectedFileUri);
                
                selectedFilePath = getRealPathFromUri(this, selectedFileUri);
                Log.i(TAG, "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    tvFileName.setText(selectedFilePath);
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
                
                
                if (selectedFilePath != null) 
                {
                	Bitmap bm = BitmapFactory.decodeFile(selectedFilePath);
                    img_show.setImageBitmap(bm);
                }
            }
            
            
            
            if (requestCode == PICK) {
            	new DateFormat();
            	String name = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";	
            
            	Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                FileOutputStream b = null;		   
                String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
                File dirFile = new File(sdCardDir);  //Ŀ¼ת�����ļ���
                if (!dirFile.exists()) {              //��������ڣ��Ǿͽ�������ļ���
                    dirFile.mkdirs();
                } 
                String fileName = sdCardDir+name;
                try {
    				b = new FileOutputStream(fileName);
    				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			} finally {
    				try {
    					b.flush();
    					b.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}


            }
        }
        
        
    }



    
    
  //android upload file to server
    public void uploadFile() {
    	final String androidid = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                
                download_headportrait_new(androidid);
                //tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/" + fileName);
            }
        });
        

    }
    
   

}













