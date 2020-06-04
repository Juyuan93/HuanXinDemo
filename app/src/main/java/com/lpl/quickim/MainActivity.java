package com.lpl.quickim;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lpl.quickim.im.ChatActivity;
import com.lpl.quickim.runtimepermissions.PermissionsManager;
import com.lpl.quickim.runtimepermissions.PermissionsResultAction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText contactidEditText;
    private Button imBtn;
    private Button sendImBtn;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        initView();
    }

    private void initView() {
        loginBtn=findViewById(R.id.login);
        registerBtn=findViewById(R.id.register);
        imBtn = findViewById(R.id.im_button);
        sendImBtn = findViewById(R.id.send_im_button);

        usernameEditText=findViewById(R.id.username);
        passwordEditText=findViewById(R.id.password);
        contactidEditText=findViewById(R.id.contact_id);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        imBtn.setOnClickListener(this);
        sendImBtn.setOnClickListener(this);
    }


    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance()
                .requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(String permission) {

                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.login://登录
                Login();
                break;

            case R.id.register://注册
                Toast.makeText(MainActivity.this,"未开放",Toast.LENGTH_SHORT).show();
                break;

            case R.id.im_button://聊天
                ChatCIm();

                break;

            case R.id.send_im_button://自定义消息
                ustomMCessage();
                break;


        }

    }

    private void Login() {

        String currentUsername = usernameEditText.getText().toString().trim();
        String currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }

    private void ustomMCessage() {

        String   contactid=  contactidEditText.getText().toString().trim();
        if (TextUtils.isEmpty(contactid)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        // it's single chat
        //联系人的id
        intent.putExtra(Constant.EXTRA_USER_ID, contactid);
        intent.putExtra(Constant.I_IM, 1);
        startActivity(intent);
    }

    private void ChatCIm() {
        String   contactid=  contactidEditText.getText().toString().trim();
        if (TextUtils.isEmpty(contactid)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        // it's single chat
        //联系人的id
        intent.putExtra(Constant.EXTRA_USER_ID, contactid);
        startActivity(intent);

    }
}
