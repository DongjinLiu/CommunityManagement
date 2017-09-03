package com.example.jin.communitymanagement;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.jar.Manifest;


public class EditAssociationActivityActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    //控件属性
    private Toolbar toolbar;

    private CardView cardViewEditStartTime;
    private CardView cardViewEditEndTime;
    private  CardView cardViewAddImage;

    private TextView textViewEditStartTime;
    private TextView textViewEditEndTime;
    private TextView textEditTimeSV;
    private TextView textEditTimeEV;
    private ImageView picture;


    private TimePickerView timePickerStartView;

    private TimePickerView timePickerEndView;

    private Uri imageUri;

    ;private NiftyDialogBuilder dialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_association_activity);
        initToolBar();
        initView();
        initCardView();


    }

    private void initView() {
        picture=(ImageView)findViewById(R.id.imageView_edit_ac_img);
        dialogBuilder=NiftyDialogBuilder.getInstance(this);




    }

    private void initCardView() {
        initEditTimeCardView();
        initAddImageView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    try
                    {
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);

                    }catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    if(Build.VERSION.SDK_INT>=19)
                    {
                        handleImageOnKitKat(data);
                    }
                    else
                    {
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if(imagePath!=null)
        {
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else
        {
            Toast.makeText(this, "打开图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path=null;
        Cursor  cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return  path;
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme()))
            {
                imagePath=getImagePath(uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme()))
            {
                imagePath=uri.getPath();
            }


        displayImage(imagePath);
    }

    private void initAddImageView() {
        cardViewAddImage=(CardView)findViewById(R.id.cardView_edit_ac_add_photo);
        cardViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder
                        .withTitle("打开方式")                                  //.withTitle(null)  no title
                        .withTitleColor("#FFcc00")                                  //def
                        .withDividerColor("#11000000")                              //def
                        .withMessage("请选择照片路径")                     //.withMessage(null)  no Msg
                        .withMessageColor("#ffcc00")                              //def  | withMessageColor(int resid)
                        .withDialogColor("#333333")                               //def  | withDialogColor(int resid)
                        // .withIcon(getResources().getDrawable(R.drawable.icon))
                        .withEffect(Effectstype.RotateLeft)
                        .withDuration(700)                                          //def//def Effectstype.Slidetop
                        .withButton1Text("拍照")                                      //def gone
                        .withButton2Text("相册")                                  //def gone
                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                        // .setCustomView(R.layout.custom_view,v.getContext())         //.setCustomView(View or ResId,context)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.cancel();
                              pickPictureFromFileCamera();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.cancel();
                            pickPictureFromFile();
                            }
                        }).show();
            }
        });
    }

    private void pickPictureFromFile()
    {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);

        }else
        {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 100:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    openAlbum();
                }else
                {
                    Toast.makeText(this, "对不起，您拒绝了请求", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void pickPictureFromFileCamera()
    {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate =new Date(System.currentTimeMillis());//获取当前时间       
        String str=formatter.format(curDate);
        File outputImage=new File(getExternalCacheDir(),str+".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24)
        {
            imageUri= FileProvider.getUriForFile(EditAssociationActivityActivity.this,"com.example.jin.communitymanagement.fileprovider",outputImage);

        }else
        {
            imageUri=Uri.fromFile(outputImage);
        }
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }
    private void initEditTimeCardView() {
        initTimePickerStartView();
        initTimePickerEndView();
        cardViewEditStartTime=(CardView)findViewById(R.id.cardView_edit_ac_start_time);
        cardViewEditEndTime=(CardView)findViewById(R.id.cardView_edit_ac_end_time);
        textViewEditStartTime=(TextView)findViewById(R.id.text_edit_asso_ac_start_time);
        textViewEditEndTime=(TextView)findViewById(R.id.text_edit_asso_ac_end_time);
        textEditTimeSV=(TextView)findViewById(R.id.text_edit_asso_ac_start_time_v);
        textEditTimeEV=(TextView)findViewById(R.id.text_edit_asso_ac_end_time_v);
        cardViewEditStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerStartView.show();

            }
        });
        cardViewEditEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerEndView.show();
            }
        });
    }

    private void initTimePickerStartView() {
        timePickerStartView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = new Date(date.getTime());
                String times = sm.format(d);
                textViewEditStartTime.setText("开始时间："+times);
                textEditTimeSV.setVisibility(View.GONE );
            }
        })
                .build();
        timePickerStartView.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        timePickerStartView.setDialogOutSideCancelable(true);
    }
    private void initTimePickerEndView()
    {
       timePickerEndView= new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = new Date(date.getTime());
                String times = sm.format(d);
                textViewEditEndTime.setText("结束时间："+times);
                textEditTimeEV.setVisibility(View.GONE );
            }
        })
                .build();
        timePickerEndView.setDate(Calendar.getInstance());
        timePickerEndView.setDialogOutSideCancelable(true);
    }

    private void initToolBar() {
        toolbar=(Toolbar)findViewById(R.id.toolbar_edit_association_activity);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_edit_association_activity,menu );
        return true;
    }


}