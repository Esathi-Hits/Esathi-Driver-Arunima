package com.e_sathi.driver;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

public class Authentication extends AppCompatActivity {
    private TextInputLayout e1;
    private TextInputEditText e11;
    private TextInputLayout e2;
    private AutoCompleteTextView e21;
    private String phone;
    private String code;
    private String phone_no;

    public static final String[] countryAreaCodes = {"+93", "+355", "+213", "+376", "+244", "+672", "+54", "+374", "+297", "+61", "+43",
            "+994", "+973", "+880", "+375", "+32", "+501", "+229", "+975", "+591", "+387", "+267", "+55", "+673", "+359", "+226", "+95", "+257", "+855",
            "+237", "+55", "+673", "+359", "+226", "+95", "+257", "+855", "+237", "+1", "+238", "+236", "+235", "+56", "+86", "+61", "+61",
            "+57", "+269", "+242", "+682", "+506", "+385", "+53", "+357", "+420", "+45", "+253", "+670", "+593", "+20", "+503", "+240", "+291", "+372", "+251", "+500",
            "+298", "+679", "+358", "+33", "+689", "+241", "+220", "+995", "+49", "+233", "+350", "+30", "+299", "+502", "+224", "+245", "+592", "+509", "+504",
            "+852", "+36", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225", "+1876", "+81", "+962", "+7", "+254", "+686", "+965", "+996",
            "+856", "+371", "+961", "+266", "+231", "+218", "+423", "+370", "+352", "+853", "+389", "+261", "+265", "+60", "+960", "+223", "+356", "+692", "+222",
            "+230", "+262", "+52", "+691", "+373", "+377", "+976", "+382", "+212", "+258", "+264", "+674", "+977", "+31", "+687", "+64", "+505", "+227", "+234", "+683", "+850", "+47",
            "+968", "+92", "+680", "+507", "+675", "+595", "+51", "+63", "+870", "+48", "+351", "+974", "+40", "+7", "+250", "+590", "+685", "+378", "+239", "+966",
            "+221", "+381", "+248", "+232", "+65", "+421", "+386", "+677", "+252", "+27", "+82", "+34", "+94", "+290", "+508", "+249", "+597", "+268", "+46", "+41", "+963", "+886", "+992",
            "+255", "+66", "+228", "+690", "+676", "+216", "+90", "+993", "+688", "+971", "+256" ,"+380", "+598", "+998", "+678", "+39", "+58", "+681", "+967", "+260", "+263"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        e1=(TextInputLayout) findViewById(R.id.Phone_no);
        e11=(TextInputEditText) findViewById(R.id.Phone_no_ed);
        e11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != e1.getCounterMaxLength())
                    e1.setError("Character length Must be " + e1.getCounterMaxLength());
                else
                    e1.setError(null);
            }
        });
        e2 =(TextInputLayout)findViewById(R.id.Country_code);
        e21=(AutoCompleteTextView)findViewById(R.id.code);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.text,countryAreaCodes);
        e21.setAdapter(adapter);
    }
    public void goto_verification(View view) {
        code= e2.getEditText().getText().toString();
        phone_no= e1.getEditText().getText().toString();
        if(phone_no.length()<10){
            Toast.makeText(Authentication.this,"Invalid Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(code.length()<2){
            Toast.makeText(Authentication.this,"Invalid Country Code",Toast.LENGTH_SHORT).show();
        }
        else {
            phone = code + "" + phone_no;
            Intent intent = new Intent(Authentication.this, Verification.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
        }
    }
}