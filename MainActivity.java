package com.example.a21173004_2_6;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    myDBHelper myDBHelper;
    EditText edtName;
    EditText edtPhoneNumber;
    EditText edtEmailAddress;
    EditText edtAddress;
    EditText edtSecure;
    EditText edtNameResult, edtPhoneNumberResult, edtEmailAddressResult, edtSecureResult, edtAddressResult;
    Button btnInit, btnInsert, btnSelect;
    SQLiteDatabase sqlDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtEmailAddress = findViewById(R.id.edtEmailAddress);
        edtAddress = findViewById(R.id.edtAddress);
        edtSecure = findViewById(R.id.edtSecure);

        btnInit = (Button) findViewById(R.id.btnInit);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnSelect = (Button) findViewById(R.id.btnSelect);

        edtNameResult = findViewById(R.id.edtNameResult);
        edtPhoneNumberResult = findViewById(R.id.edtPhoneNumberResult);
        edtEmailAddressResult = findViewById(R.id.edtEmailAddressResult);
        edtAddressResult = findViewById(R.id.edtAddressResult);
        edtSecureResult = findViewById(R.id.edtSecureResult);


        myDBHelper = new myDBHelper(this);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myDBHelper.getWritableDatabase();
                myDBHelper.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() { //조회
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                sqlDB = myDBHelper.getWritableDatabase();

                sqlDB.execSQL("INSERT INTO USER1 VALUES ('" + edtName.getText().toString() + "' ,' " + edtPhoneNumber.getText().toString() + "' , '" + edtEmailAddress.getText().toString() + "', " + edtSecure.getText().toString() + ",'" + edtAddress.getText().toString() + "');");
                sqlDB.close();
                Toast.makeText(getApplicationContext(), "입력됨", 0).show();

            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myDBHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM user1;", null);

                String strNames = "이름" + "\r\n" + "\r\n";
                String strPhoneNumbers = "핸드폰번호" + "\r\n" + "\r\n";
                String strEmailAddress = "이메일주소" + "\r\n" + "\r\n";
                String strSecure = "비밀번호" + "\r\n" + "\r\n";
                String strAddress = "주소" + "\r\n" + "\r\n";


                while (cursor.moveToNext()) {
                    strNames += cursor.getString(0) + "\r\n";
                    strPhoneNumbers += cursor.getString(1) + "\r\n";
                    strEmailAddress += cursor.getString(2) + "\r\n";
                    strSecure += cursor.getString(3) + "\r\n";
                    strAddress += cursor.getString(4) + "\r\n";


                }
                edtNameResult.setText(strNames);
                edtPhoneNumberResult.setText(strPhoneNumbers);
                edtEmailAddressResult.setText(strEmailAddress);
                edtSecureResult.setText(strSecure);
                edtAddressResult.setText(strAddress);


                cursor.close();
                sqlDB.close();

            }
        });
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE user1 ( gName CHAR(20), gPhoneNumber CHAR(20), gEmailAddress CHAR(40) PRIMARY KEY,gSecure INTEGER, gAddress CHAR(100));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS user1");
            onCreate(db);

        }
    }
}