package com.example.lungteng.finalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Signup extends AppCompatActivity {
    Intent intent = new Intent();
    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    private EditText accountEdit, passwordEdit, nameEdit;
    private TextInputLayout accoutLayout, passwordLayout, nameLayout;
    private Button btnSignup;

    private void initView() {
        mAuth = FirebaseAuth.getInstance();

        btnSignup = (Button) findViewById(R.id.btnSignup);

        accountEdit = (EditText) findViewById(R.id.account_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        nameEdit = (EditText) findViewById(R.id.name_edit);

        accoutLayout = (TextInputLayout) findViewById(R.id.account_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        nameLayout = (TextInputLayout) findViewById(R.id.name_layout);

        passwordLayout.setErrorEnabled(true);
        accoutLayout.setErrorEnabled(true);
        nameLayout.setErrorEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        //按下註冊按鈕後
        btnSignup.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String name = nameEdit.getText().toString();

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

                if (TextUtils.isEmpty(name)) {
                    nameLayout.setError("請輸入暱稱");
                } else {
                    nameLayout.setError(null);
                }

                if (!nameEdit.getText().toString().equals("") && !accountEdit.getText().toString().equals("") && !passwordEdit.getText().toString().equals("")) {
                    createUser(account, password, name);
                }
            }
        });
    }
    public void createUser(final String account, String password, final String name) {
        //為使用者創帳號
        mAuth.createUserWithEmailAndPassword(account, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final String userUID =  mAuth.getInstance().getCurrentUser().getUid();
                    reference.child("users").child(userUID).child("email").setValue(account);
                    reference.child("users").child(userUID).child("name").setValue(name);
                    Toast.makeText(Signup.this, "註冊成功", Toast.LENGTH_SHORT).show();
                    intent.setClass(Signup.this, Login.class);
                    startActivity(intent);
                    Signup.this.finish();
                } else {
                    Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
