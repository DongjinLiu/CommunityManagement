package com.example.jin.communitymanagement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    private MyDBHelper dbHelper;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

        dbHelper = new MyDBHelper(this, "UserStore.db", null,BaseActivity.DATABASE_VERSION);
    }

    public boolean login(View view) {
        //SQLiteDatabase db=dbHelper.getWritableDatabase();

        EditText editText3 = (EditText) findViewById(R.id.editText3);
        EditText editText4 = (EditText) findViewById(R.id.editText4);
        EditText editText5 = (EditText) findViewById(R.id.editText5);
        String newname = editText3.getText().toString();
        String password = editText4.getText().toString();
        String passwordCer = editText5.getText().toString();

        if (newname.equals("")) {
            Toast.makeText(this, "请输入您的账号", Toast.LENGTH_SHORT).show();
            editText3.requestFocus();
            return false;
        }

        if (!checkName(newname)) {
            Toast.makeText(this, "账号格式应为字母开头可包含数字但不得超过16位", Toast.LENGTH_SHORT).show();
            editText3.requestFocus();
            return false;
        }
        if (CheckIsDataAlreadyInDBorNot(newname)) {
            Toast.makeText(this, "账号已存在", Toast.LENGTH_SHORT).show();
            editText3.requestFocus();
            return false;
        }
        if (password.equals("")) {
            Toast.makeText(this, "请输入您的密码", Toast.LENGTH_SHORT).show();
            editText4.requestFocus();
            return false;
        }
        if (!checkPassword(password)) {
            Toast.makeText(this, "密码格式不正确（8~16位，可包含字母数字和特殊字符）", Toast.LENGTH_SHORT).show();
            editText4.requestFocus();
            return false;
        }

        if (passwordCer.equals("")) {
            Toast.makeText(this, "请确认您的密码", Toast.LENGTH_SHORT).show();
            editText5.requestFocus();
            return false;
        }
        if (!password.equals(passwordCer)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            editText5.requestFocus();
            return false;
        }
        if (register(newname, password)) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        }
        else Toast.makeText(this, "意外错误", Toast.LENGTH_SHORT).show();

        return true;
    }

    //向数据库插入数据
    public boolean register(String username, String password) {
        try {
            Log.e("register", "begin!");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if (!exits("user"))
                db.execSQL("create table user(id integer primary key ,"
                        + "name varchar(255),"
                        + "password varchar(255))");

            Log.e("register", "success!");
            ContentValues values = new ContentValues();
            values.put("name", username);
            values.put("password", password);
            db.insert("user", null, values);
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return true;
    }

    //检验用户名是否已存在
    public boolean CheckIsDataAlreadyInDBorNot(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            String Query = "Select * from user where name =?";
            Cursor cursor = db.rawQuery(Query, new String[]{value});

            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();

        } catch (SQLiteException e) {
            e.printStackTrace();

        }
        return false;
    }

    public boolean exits(String table) {
        boolean exits = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from sqlite_master where name=" + "'" + table + "'";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() != 0) {
            exits = true;
        }
        cursor.close();
        return exits;
    }

    public boolean checkName(String str) {

        // 编译正则表达式
        Pattern pattern = Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_]{1,15}");
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    public boolean checkPassword(String str) {

        // 编译正则表达式
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_!@#$%^&*/()-+.]{8,16}");
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

}