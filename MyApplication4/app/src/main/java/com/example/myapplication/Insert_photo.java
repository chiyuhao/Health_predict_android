package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

//import android.icu.util.Calendar;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;


public class Insert_photo extends Activity implements View.OnClickListener{

    private EditText loginAccount;//账号
    private EditText loginPassword;//密码
    private Button Uupload;
    private Button Sselect;
    private Button select_time;
    private TextView textView2;
    private Calendar calendar;
    private ImageView img_show;
    private final static String SERVER_URL = "http://39.100.73.118/deeplearning_photo/insert_data.php";
    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    public String selectedFilePath;
    public static String total_time;
    public static final int TAKE_PHOTO = 2;
    public static final int CROP_PHOTO = 3;
    private final int PICK = 4;
    private String imgString = "";
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_insert);
        calendar = Calendar.getInstance();
        textView2 = (TextView) findViewById(R.id.tv2);
        select_time = (Button) findViewById(R.id.select_time);
        Sselect = (Button) findViewById(R.id.login_btn);
        Uupload= (Button) findViewById(R.id.register_btn);
        img_show = (ImageView) findViewById(R.id.imgView3);
        select_time.setOnClickListener(this);
        Sselect.setOnClickListener(this);
        Uupload.setOnClickListener(this);
        
        
    }

    /**
     * 初始化数据
     */
    
    
         
    public static void showTimePickerDialog(Activity activity, final TextView tv, Calendar calendar) {
    	total_time = "";
    	// Calendar c = Calendar.getInstance();  
    	            // 创建一个TimePickerDialog实例，并把它显示出来  
    	           // 解释一哈，Activity是context的子类
    	            new TimePickerDialog( activity,  
    	            // 绑定监听器  
    	                    new OnTimeSetListener() {
    	                        @Override  
    	                        public void onTimeSet(TimePicker view,  
    	                                int hourOfDay, int minute) {  
    	                            tv.setText("您选择了：" + hourOfDay + "时" + minute  
    	                                    + "分");  
    	                            total_time = total_time + Integer.toString(hourOfDay) + "_" + Integer.toString(minute);
    	                        }  
    	                    }  
    	                    // 设置初始时间  
    	                    , calendar.get(Calendar.HOUR_OF_DAY)
    	                    , calendar.get(Calendar.MINUTE)
    	                    // true表示采用24小时制  
    	                    ,true).show();  
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
    
   

    /**
     * 初始化数据
     */
    @Override
    public void onClick(View v) {
        if (v == select_time) {

            //on attachment icon click
        	showTimePickerDialog(this, textView2, calendar);
        }
        if (v == Uupload) {

        	if(isFastDoubleClick() == false)
        	{
	            if (selectedFilePath != null) {
	            	
	                dialog = ProgressDialog.show(Insert_photo.this, "", "请等待返回结果...", true);
	            	//startActivity(new Intent(this,SimplePerActivity.class));
	                new Thread(new Runnable() {
	                    @Override
	                    public void run() {
	                        //creating new thread to handle Http Operations
	                        uploadFile(selectedFilePath);
	                        
	                    }
	                }).start();
	            } else {
	                Toast.makeText(Insert_photo.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
	            }
        	}
            
        }
        if (v == Sselect) {
        	requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
            //on attachment icon click
            showFileChooser();
//            finish();
        }
        
        
       

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
                    //tvFileName.setText(selectedFilePath);
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

    
    
    
    
    
    
    
    
    
    
    
    public static Bitmap compressImage(Bitmap image) {    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();    
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��    
        int options = 90;    
        while (baos.toByteArray().length / 1024 > 500) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��    
            baos.reset(); // ����baos�����baos    
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��    
            options -= 10;// ÿ�ζ�����10    
        }    
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��    
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ    
        return bitmap;    
    }
    
    
    
    
    
    
    FileOutputStream out;
    String bitmapName = "zipped.jpg";
    File file;
    String QQFilePath;
    String total_path = Environment.getExternalStorageDirectory() + "/zip/zipped.jpg";
     
    
    public void saveBitmap(Bitmap bmp) {

        try { // ��ȡSDCardָ��Ŀ¼��
            String sdCardDir = Environment.getExternalStorageDirectory() + "/zip/";
            File dirFile = new File(sdCardDir);  //Ŀ¼ת�����ļ���
            if (!dirFile.exists()) {              //��������ڣ��Ǿͽ�������ļ���
                dirFile.mkdirs();
            }                          //�ļ����������Ϳ��Ա���ͼƬ��
            File file = new File(sdCardDir, bitmapName);// ��SDcard��Ŀ¼�´���ͼƬ��,�Ե�ǰʱ��Ϊ������
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            System.out.println("_________���浽____sd______ָ��Ŀ¼�ļ�����____________________");
            Log.e("saveBitMap", "saveBitmap: ͼƬ���浽" + Environment.getExternalStorageDirectory() + "/zip/" + bitmapName);
            QQFilePath = Environment.getExternalStorageDirectory() + "/zip/" + "zipped.jpg";
//            showShare(QQFilePath);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(HahItemActivity.this,"�����Ѿ���"+Environment.getExternalStorageDirectory()+"/CoolImage/"+"Ŀ¼�ļ�����", Toast.LENGTH_SHORT).show();
    }
    
    
    public int uploadFile(final String selectedFilePath) {
        int serverResponseCode = 0;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);
        
        BitmapFactory.Options newOpts = new BitmapFactory.Options();    
        // ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��    
        newOpts.inJustDecodeBounds = true;    
        Bitmap bitmap = BitmapFactory.decodeFile(selectedFilePath, newOpts);// ��ʱ����bmΪ��    
        newOpts.inJustDecodeBounds = false;    
        int w = newOpts.outWidth;    
        int h = newOpts.outHeight;    
        // ���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ    
        float hh = 512;// �������ø߶�Ϊ800f    
        float ww = 512f;// �������ÿ��Ϊ480f    
        // ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��    
        int be = 1;// be=1��ʾ������    
        if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����    
            be = (int) (newOpts.outWidth / ww);    
        } else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����    
            be = (int) (newOpts.outHeight / hh);    
        }    
        if (be <= 0)    
            be = 1;    
        newOpts.inSampleSize = be;// �������ű���    
        // ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��    
        bitmap = BitmapFactory.decodeFile(selectedFilePath, newOpts);    
        bitmap = compressImage(bitmap);// ѹ���ñ�����С���ٽ�������ѹ�� 
        saveBitmap(bitmap);

        String[] parts = total_path.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            //dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + total_path);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(total_path);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", total_path);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + total_path + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                //String androidid = "unknown";
                final String androidid = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);

                
                

                
                
				Calendar calendar = Calendar.getInstance();
				//��ȡϵͳ������
				//��
				int year = calendar.get(Calendar.YEAR);
				//��
				int month = calendar.get(Calendar.MONTH)+1;
				//��
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				//��ȡϵͳʱ��
				//Сʱ
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				//����
				int minute = calendar.get(Calendar.MINUTE);
				//��
				int second = calendar.get(Calendar.SECOND);
				String time_string;
				time_string = year + "." + month + "." + day + "-" + hour + ":" + minute + ":" + second; 

	              
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"usr_name\";filename=\""
                        + androidid + "_" + time_string + "_" + total_time + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);
                
                
                
                
                
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();
                	Intent intent = new Intent();
                    intent.setClass(Insert_photo.this, Uploaded.class);
                    startActivity(intent);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            
//                            download_headportrait_new(androidid);
//                            //tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/" + fileName);
//                        }
//                    });
                }

                //closing the input and output streams 
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Insert_photo.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(Insert_photo.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Insert_photo.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            //dialog.dismiss();
            return serverResponseCode;
        }

    }
    
    
    
    
}
