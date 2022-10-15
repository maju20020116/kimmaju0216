package com.example.a21173004_2_7;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    myDBHelper myDBHelper;
    SQLiteDatabase sqlDB;
    TextView jinputCount;
    EditText jinputMessage;
    Button asendBtn,acloseBtn;
    Button btnInit,btnInsert,btnSelect;
    EditText smsResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jinputCount = findViewById(R.id.ainputCount);
        jinputMessage = findViewById(R.id.ainputMessage);

        btnInit = findViewById(R.id.btnInit);
        btnInsert= findViewById(R.id.btnInsert);
        btnSelect = findViewById(R.id.btnSelect);
        smsResult =findViewById(R.id.smsResult);
        asendBtn = findViewById(R.id.asendBtn);


        myDBHelper= new  myDBHelper(this);
        btnInit.setOnClickListener(new View.OnClickListener() { //초기화
            @Override
            public void onClick(View view) {
                sqlDB = myDBHelper.getWritableDatabase();
                myDBHelper.onUpgrade(sqlDB,1,2);
                sqlDB.close();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                sqlDB =myDBHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO sms VALUES ('"+jinputMessage.getText().toString()+"');");
                sqlDB.close();
                Toast.makeText(getApplicationContext(),"입력됨",0).show();

            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myDBHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM sms;",null);

                String strsmsResult = "메세지"+"\r\n"+"\r\n";



                while (cursor.moveToNext()){
                    strsmsResult+= cursor.getString(0)+"\r\n";

                }

                smsResult.setText(strsmsResult);
                cursor.close();
                sqlDB.close();
            }
        });



        asendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button jcloseButton = findViewById(R.id.acloseBtn);
        jcloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextWatcher watcher =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence str, int i, int i1, int i2) {
                byte[] bytes = null;
                try {
                    bytes = str.toString().getBytes("KSC5601");
                    int strCount = bytes.length;
                    jinputCount.setText(strCount+"/80바이트");

                } catch (UnsupportedEncodingException ex){
                    ex.printStackTrace();
                }
            }



            @Override
            public void afterTextChanged(Editable strEditable) {
                String str = strEditable.toString();
                try{
                    byte[] strBytes = str.getBytes("KSC5601");
                    if(strBytes.length > 80){
                        strEditable.delete(strEditable.length()-2,strEditable.length()-1);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        };
        jinputMessage.addTextChangedListener(watcher);

    }

    public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context) {
        super(context, "groupDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sms ( gmessage CHAR(80));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sms");
        onCreate(db);

    }
        }
    }

