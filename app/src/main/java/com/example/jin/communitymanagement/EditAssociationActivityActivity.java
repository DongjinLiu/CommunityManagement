package com.example.jin.communitymanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.UiThread;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditAssociationActivityActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO=1;
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
            default:
                break;
        }
    }

    private void initAddImageView() {
        cardViewAddImage=(CardView)findViewById(R.id.cardView_edit_ac_add_photo);
        cardViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
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
