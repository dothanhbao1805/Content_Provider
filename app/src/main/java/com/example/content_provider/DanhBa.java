package com.example.content_provider;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.content_provider.Model.Contact;

import java.util.ArrayList;

public class DanhBa extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1001;
    ListView lvDanhBa;
    ArrayList<Contact> dsDanhBa;
    ArrayAdapter<Contact> adapterDanhBa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danh_ba);

        // Yêu cầu quyền nếu chưa được cấp
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            addControls();
            showAllControlFromDevice();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Gọi phương thức của lớp cha

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addControls();
                showAllControlFromDevice();
            } else {
                // Quyền bị từ chối, thông báo cho người dùng hoặc xử lý lỗi
                Toast.makeText(this, "Bạn cần cấp quyền để truy cập danh bạ!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showAllControlFromDevice() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            dsDanhBa.clear();
            while (cursor.moveToNext()) {
                String tenCotName = ContactsContract.Contacts.DISPLAY_NAME;
                String tenCotPhone = ContactsContract.CommonDataKinds.Phone.NUMBER;
                int viTriCotName = cursor.getColumnIndex(tenCotName);
                int viTriCotPhone = cursor.getColumnIndex(tenCotPhone);
                String name = cursor.getString(viTriCotName);
                String phone = cursor.getString(viTriCotPhone);
                Contact contact = new Contact(name, phone);
                dsDanhBa.add(contact);
            }
            cursor.close();
            adapterDanhBa.notifyDataSetChanged();
        }
    }

    private void addControls() {
        lvDanhBa = findViewById(R.id.lvDanhBa);
        dsDanhBa = new ArrayList<>();
        adapterDanhBa = new ArrayAdapter<>(
                DanhBa.this, android.R.layout.simple_list_item_1, dsDanhBa
        );
        lvDanhBa.setAdapter(adapterDanhBa);
    }
}
