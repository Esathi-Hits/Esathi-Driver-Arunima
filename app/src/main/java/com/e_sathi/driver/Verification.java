package com.e_sathi.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Verification extends AppCompatActivity {
    private String code,mob;
    private OtpEditText txtPinEntry;
    private FirebaseUser user;
    String user_id;
    String username;
    private FirebaseAuth auth;
    private ProgressDialog progress, progress1;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken token11;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Intent intent = getIntent();
        mob= intent.getStringExtra("phone");
        progress = new ProgressDialog(this);
        progress.setMessage("Initializing...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress1 = new ProgressDialog(this);
        progress1.setMessage("Verifying...");
        progress1.setCanceledOnTouchOutside(false);
        progress1.setCancelable(false);
        txtPinEntry = (OtpEditText) findViewById(R.id.txt_pin_entry);
        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code=s.toString();
            }
        });
        auth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                progress.dismiss();
                if (code != null) {
                    txtPinEntry.setText(code);
                    verifyVerificationCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progress.dismiss();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(Verification.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(Verification.this, "You Have Reached Maximum No. Of Times" + "\nTry After Some Time", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progress.dismiss();
                Toast.makeText(Verification.this,"OTP Sent Sucessfully", Toast.LENGTH_SHORT).show();
                mVerificationId = s;
                token11 = forceResendingToken;
            }
        };
        startPhoneNumberVerification(mob);
    }
    private void startPhoneNumberVerification(String phone)
    {
        progress.show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        progress.show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void verifyVerificationCode(String code) {
        progress1.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(Verification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user=FirebaseAuth.getInstance().getCurrentUser();
                            user_id=user.getUid();
                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("E-Sathi_Driver").child(user_id);
                            dbref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        user=FirebaseAuth.getInstance().getCurrentUser();
                                        username=user.getDisplayName();
                                        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("E-Sathi_Driver").child(user_id).child(username);
                                        dbref.orderByChild("All_okay").equalTo("True").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                                if(dataSnapshot1.exists()){
                                                    Intent intent1 = new Intent(Verification.this, HomeActivity.class);
                                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    progress1.dismiss();
                                                    startActivity(intent1);
                                                    finish();
                                                }
                                                else{
                                                    Intent intent1 = new Intent(Verification.this, Register_1.class);
                                                    Toast.makeText(Verification.this, "Please upload some important document..", Toast.LENGTH_SHORT).show();
                                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent1.putExtra("top", "1");
                                                    progress1.dismiss();
                                                    startActivity(intent1);
                                                    finish();

                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }else {
                                        Intent intent = new Intent(Verification.this, Register.class);
                                        Toast.makeText(Verification.this, "Please add some important personal info..", Toast.LENGTH_SHORT).show();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        progress1.dismiss();
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    progress1.dismiss();
                                }
                            });
                        } else {
                            progress1.dismiss();
                            String message = "Somthing is wrong, we will fix it soon...";
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    public void goto_authentication(View view) {
        Intent intent = new Intent(this,Authentication.class);
        startActivity(intent);
        finish();
    }

    public void Resend_otp(View view) {
        resendVerificationCode(mob,token11);
    }

    public void Verify(View view) {
        verifyVerificationCode(code);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Authentication.class);
        startActivity(intent);
        finish();
    }
}
