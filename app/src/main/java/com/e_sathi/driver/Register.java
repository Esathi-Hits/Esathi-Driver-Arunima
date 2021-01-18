
package com.e_sathi.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class Register extends AppCompatActivity {

    private Bitmap bt;
    private FirebaseUser user;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    private ByteArrayOutputStream baos;
    private byte[] data1;
    private ImageView iv;
    private Uri picUri;
    private String user_id;
    private DatabaseReference dbref;
    private ProgressDialog progress;
    private TextInputLayout e1;
    private TextInputEditText e11;
    private TextInputLayout e2;
    private TextInputEditText e21;
    private TextInputLayout e3;
    private TextInputLayout e4;
    private TextInputEditText e41;
    private TextInputLayout e5;
    private TextInputEditText e51;
    private TextInputLayout e6;
    private TextInputEditText e71;
    private TextInputLayout e7;
    private TextInputLayout e8;
    private TextInputLayout e9;
    private TextInputEditText e91;
    private String username;
    private String age;
    private String per_address;
    private String city;
    private String pincode;
    private String email;
    private String License_no;
    private String model;
    private String vehcile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = user.getUid();
        e1=(TextInputLayout) findViewById(R.id.name);
        e11=(TextInputEditText) findViewById(R.id.name_ed);
        e11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > e1.getCounterMaxLength())
                    e1.setError("Max character length is " + e1.getCounterMaxLength());
                else
                    e1.setError(null);
            }
        });
        e2=(TextInputLayout) findViewById(R.id.age);
        e21=(TextInputEditText) findViewById(R.id.age_ed);
        e21.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != e2.getCounterMaxLength())
                    e2.setError("Character length Must be " + e2.getCounterMaxLength());
                else
                    e2.setError(null);
            }
        });
        e3=(TextInputLayout) findViewById(R.id.Permanent_Address);
        e4=(TextInputLayout) findViewById(R.id.City);
        e41=(TextInputEditText) findViewById(R.id.city_ed);
        e41.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > e4.getCounterMaxLength())
                    e4.setError("Max character length is " + e4.getCounterMaxLength());
                else
                    e4.setError(null);
            }
        });
        e5=(TextInputLayout) findViewById(R.id.Pincode);
        e51=(TextInputEditText) findViewById(R.id.Pincode_ed);
        e51.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != e5.getCounterMaxLength())
                    e5.setError("Character length Must be " + e5.getCounterMaxLength());
                else
                    e5.setError(null);
            }
        });
        e6=(TextInputLayout) findViewById(R.id.email);
        e7=(TextInputLayout) findViewById(R.id.License);
        e71=(TextInputEditText) findViewById(R.id.License_ed);
        e71.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != e7.getCounterMaxLength())
                    e7.setError("Character length Must be " + e7.getCounterMaxLength());
                else
                    e7.setError(null);
            }
        });
        e8=(TextInputLayout) findViewById(R.id.model);
        e9=(TextInputLayout) findViewById(R.id.number);
        e91=(TextInputEditText) findViewById(R.id.number_ed);
        e91.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != e9.getCounterMaxLength())
                    e9.setError("Character length Must be " + e9.getCounterMaxLength());
                else
                    e9.setError(null);
            }
        });
        progress = new ProgressDialog(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        iv=(ImageView)findViewById(R.id.profile_image);
        baos = new ByteArrayOutputStream();
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }
    public void Next(View view) {
        username = e1.getEditText().getText().toString();
        age = e2.getEditText().getText().toString();
        per_address = e3.getEditText().getText().toString();
        city = e4.getEditText().getText().toString();
        pincode = e5.getEditText().getText().toString();
        email = e6.getEditText().getText().toString();
        License_no = e7.getEditText().getText().toString();
        model = e8.getEditText().getText().toString();
        vehcile_no = e9.getEditText().getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(age) || TextUtils.isEmpty(per_address) || TextUtils.isEmpty(city) || TextUtils.isEmpty(pincode) || TextUtils.isEmpty(License_no) || TextUtils.isEmpty(model) || TextUtils.isEmpty(vehcile_no)) {
            Toast.makeText(this, "Oh no Some feilds are empty fill and try again", Toast.LENGTH_LONG).show();
        }
        else if(data1==null){
            Toast.makeText(this,"Please Select Profile Image",Toast.LENGTH_LONG).show();
        }
        else{
            add();
        }

    }
    public void Select(View view) {
        startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
    }
    public void add(){
        progress.setMessage("Saving Information...");
        progress.show();
        progress.setCancelable(false);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Username", username);
        map.put("Age", age );
        map.put("Permanent_Address", per_address);
        map.put("City", city);
        map.put("Pincode", pincode);
        map.put("Email", email);
        map.put("License_No", License_no);
        map.put("Model", model);
        map.put("Vehcile_No", vehcile_no);
        map.put("All_okay","False");
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username).build();
            user.updateProfile(profileUpdates);
            StorageReference stref = FirebaseStorage.getInstance().getReference().child("E-Sathi_Driver").child(user_id).child(username).child("Profile Image");
            UploadTask uploadTask = (UploadTask) stref.putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dbref = FirebaseDatabase.getInstance().getReference().child("E-Sathi_Driver").child(user_id).child(username).child("Information");
                    dbref.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Register.this, "Step 1 Done Successfully...", Toast.LENGTH_LONG).show();
                            progress.dismiss();
                            Intent intent=new Intent(Register.this,Register_1.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Step 1 Failed", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this, "Step 1 Failed", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            });
        }
    }
    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_RESULT) {

                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    try {
                        bt = BitmapFactory.decodeFile(filePath);
                        iv.setImageBitmap(bt);
                        bt.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                        data1 = baos.toByteArray();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;
        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());
    }
    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }
    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("pic_uri", picUri);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        picUri = savedInstanceState.getParcelable("pic_uri");
    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (user!=null) {
            user_exit();
        }
    }
    public void user_exit(){
        Intent intent6 = new Intent(Intent.ACTION_MAIN);
        intent6.addCategory(Intent.CATEGORY_HOME);
        intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent6);
        finish();
    }
}