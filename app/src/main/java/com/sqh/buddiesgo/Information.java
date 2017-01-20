package com.sqh.buddiesgo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Information extends AppCompatActivity {
    private Bundle b;
    private String memail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        b = getIntent().getExtras();
        memail = b.getString("buddyemail");
        String username = b.getString("buddyname");
        TextView name = (TextView)findViewById(R.id.username);
        name.setText(username);
        TextView email = (TextView)findViewById(R.id.email);
        email.setText(memail);
    }

    public void backToUser(View view){
        Intent intent = new Intent(Information.this, BuddiesActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
    public void clipBoard(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("email",memail);
        clipboard.setPrimaryClip(clip);
    }
}
