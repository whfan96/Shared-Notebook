package com.example.lungteng.finalapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    Intent intent = new Intent();
    private FirebaseAuth mAuth;
    private EditText accountEdit, passwordEdit;
    private TextInputLayout accoutLayout, passwordLayout;
    private Button buttonLogin, buttonSignup, ForgotPassword;

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        accountEdit = (EditText) findViewById(R.id.account_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        accoutLayout = (TextInputLayout) findViewById(R.id.account_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        passwordLayout.setErrorEnabled(true);
        accoutLayout.setErrorEnabled(true);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        ForgotPassword = (Button) findViewById(R.id.ForgotPassword);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        //自動登入
//        try{
//            if(!mAuth.toString().isEmpty()){
//                signInto(mAuth.getCurrentUser().getEmail().toString());
//            }
//        } catch (Exception ex){
//            ex.getMessage();
//        }

        //按下忘記密碼按鈕後
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Login.this)
                        .setTitle("是否確定重設密碼?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final View item = LayoutInflater.from(Login.this).inflate(R.layout.edit_email, null);
                                new AlertDialog.Builder(Login.this)
                                        .setTitle("請輸入帳號(常用信箱)")
                                        .setView(item)
                                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                EditText editEmail = (EditText) item.findViewById(R.id.editEmail);
                                                if (editEmail.getText().toString().isEmpty() || editEmail.getText().toString().trim().equals(null)) {
                                                    Toast.makeText(getApplicationContext(), "輸入錯誤，請輸入正確的Email ", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                                    auth.sendPasswordResetEmail(editEmail.getText().toString())
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(Login.this, "Email 驗證已寄出", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        Toast.makeText(Login.this, "Email 驗證" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                            }
                        }).show();
            }

        });

        //按下登入按鈕後
        buttonLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    accoutLayout.setError("請輸入帳號");
                } else {
                    accoutLayout.setError(null);
            }
                if (TextUtils.isEmpty(password)) {
                    passwordLayout.setError("請輸入密碼");
                } else {
                    passwordLayout.setError(null);
                }
                if (!accountEdit.getText().toString().equals("") && !passwordEdit.getText().toString().equals("")) {
                    signIn(account, password);
                }
            }
        } );

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.setClass(Login.this , Signup.class);
                startActivity(intent);
            }
        });
    }
    public void signIn(final String account, String password){
        //使用者登入
        mAuth.signInWithEmailAndPassword(account, password).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    intent.setClass(Login.this, DataPersonalNotes.class);
                    startActivity(intent);
                    Login.this.finish();
                    Toast.makeText(Login.this,"登入成功", Toast.LENGTH_SHORT).show();
                }
                else if (!task.isSuccessful()){
                    Toast.makeText(Login.this, "登入失敗，請檢查帳號或密碼", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void signInto(String account){
//        //使用者自動登入
//        intent.setClass(Login.this, PersonalNotes.class);
//        startActivity(intent);
//        Login.this.finish();
//    }

}
