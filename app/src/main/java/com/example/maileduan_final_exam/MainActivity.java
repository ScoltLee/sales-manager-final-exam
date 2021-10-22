package com.example.maileduan_final_exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnInsert, btnDelete, btnUpdate, btnQuery;
    EditText edtMa, edtTen, edtSoLuong, edtGia;
    SQLiteDatabase database = null;
    ListView lv;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnInsert = findViewById(R.id.btnInsert);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnQuery = findViewById(R.id.btnQuery);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtMa = findViewById(R.id.edtMa);
        edtGia = findViewById(R.id.edtGia);
        edtTen = findViewById(R.id.edtTen);
        lv = findViewById(R.id.lv);

        //	Tạo Database
        database = openOrCreateDatabase("dbbanhang.sqlite", MODE_PRIVATE, null);
        //	Tạo Table nếu không tồn tại
        try {
            docreattable(); //Gọi hàm Tạo Table
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Error", "Table is exists");
        }

        //	Insert Record to tablehang
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                doinsertrecordtohang(); // Gọi hàm chèn thêm hàng mới
            }
        });

        //Hiển thị data lên listview ngay khi vào ứng dụng
        displayData();
        //	Delete Record from tablehang
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                deleterowLop(); // Gọi hàm xóa hàng
            }
        });

        //	Update Record to tablehang
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                doupdaterowhang(); //Gọi hàm update hàng
            }
        });

        //	Query data from tablehang
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                querytablehang(); // Gọi hàm truy vấn hàng
            }
        });

    }

    private void deleterowLop() {
        // TODO Auto-generated method stub
        String ma = edtMa.getText().toString();
        int d = database.delete("tablehang", "mahang=?", new String[]{ma});
        String msg = "";
        if (d == 0) {
            msg = "Delete Row " + ma + " Fail";
        } else {
            msg = "Delete Row " + ma + " Sucessful";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        //Hiển thị luôn dữ liệu lên list view sau khi xoá
        displayData();
    }

    private void doupdaterowhang() {
        // TODO Auto-generated method stub
        String ma = edtMa.getText().toString();
        ContentValues values = new ContentValues();
        values.put("tenhang", edtTen.getText().toString());
        values.put("soluong", Integer.parseInt(edtSoLuong.getText().toString()));
        values.put("dongia", Integer.parseInt(edtGia.getText().toString()));
        String msg = "";
        int ret = database.update("tablehang", values,
                "mahang=?", new String[]{ma});
        if (ret == 0) {
            msg = "Failed to update";
        } else {
            msg = "updating is successful";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        //Hiển thị luôn dữ liệu lên list view sau khi cập nhật
        displayData();
    }

    private void doinsertrecordtohang() {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        values.put("mahang", edtMa.getText().toString());
        values.put("tenhang", edtTen.getText().toString());
        values.put("soluong", Integer.parseInt(edtSoLuong.getText().toString()));
        values.put("dongia", Integer.parseInt(edtGia.getText().toString()));
        String msg = "";
        if (database.insert("tablehang", null, values) == -1) {
            msg = "Failed to insert record";
        } else {
            msg = "insert record is successful";
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        //Hiển thị luôn dữ liệu lên list view sau khi chèn
        displayData();
    }

    public void querytablehang() {
        displayData();
        Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
    }

    private void docreattable() {
        // TODO Auto-generated method stub
        String sql1 = "CREATE TABLE tablehang (mahang TEXT primary key,tenhang TEXT, soluong INTERGER,dongia INTEGER)";
        database.execSQL(sql1);
    }

    private void displayData() {
        ArrayList<String> myList = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myList);
        lv.setAdapter(adapter);
        Cursor c = database.rawQuery("SELECT *FROM tablehang  ", null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            myList.add(c.getString(0) + " - " + c.getString(1) + " - " + c.getString(2) + " - " +
                    c.getString(3));
            c.moveToNext();
        }
        c.close();
        adapter.notifyDataSetChanged();
    }
}
