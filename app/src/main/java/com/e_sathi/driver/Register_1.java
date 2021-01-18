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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class Register_1 extends AppCompatActivity {

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT_1 = 200;
    private final static int IMAGE_RESULT_2= 201;
    private final static int IMAGE_RESULT_3= 202;
    private ByteArrayOutputStream baos;
    private byte[] data1;
    private byte[] data2;
    private byte[] data3;
    private FirebaseUser user;
    private ImageView Iv1;
    private ImageView Iv2;
    private ImageView Iv3;
    private Bitmap bt;
    private String top="2";
    private Uri picUri;
    private String user_id;
    private String username;
    private DatabaseReference dbref;
    private UploadTask uploadTask;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_1);
        Intent intent = getIntent();
        top= intent.getStringExtra("top");
        Iv1=(ImageView)findViewById(R.id.iv1);
        Iv2=(ImageView)findViewById(R.id.iv2);
        Iv3=(ImageView)findViewById(R.id.iv3);
        user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = user.getUid();
        progress = new ProgressDialog(this);
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
        if(data1==null){
            Toast.makeText(this,"Please Upload Driving License",Toast.LENGTH_LONG).show();
        }
        else if(data2==null){
            Toast.makeText(this,"Please Upload Vehcile Image",Toast.LENGTH_LONG).show();
        }
        else if(data3==null){
            Toast.makeText(this,"Please Upload RC Image",Toast.LENGTH_LONG).show();
        }
        else{
            upload();
        }
    }

    private void upload() {
        progress.setMessage("Registering...");
        progress.show();
        progress.setCancelable(false);
        if (user != null) {
                username=user.getDisplayName();
                StorageReference stref = FirebaseStorage.getInstance().getReference().child("E-Sathi_Driver").child(user_id).child(username);
                uploadTask= (UploadTask) stref.child("License").putBytes(data1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadTask = (UploadTask) stref.child("Vehcile_Image").putBytes(data2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploadTask = (UploadTask) stref.child("RC").putBytes(data3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        dbref = FirebaseDatabase.getInstance().getReference().child("E-Sathi_Driver").child(user_id).child(username).child("Information");
                                        dbref.child("All_okay").setValue("True").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progress.dismiss();
                                                showCustomDialog_tick();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Register_1.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                                                progress.dismiss();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Register_1.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                                        progress.dismiss();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register_1.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register_1.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
            }
        }


    public void Prev(View view) {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

            if (requestCode == IMAGE_RESULT_1) {

                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    try {
                        bt = BitmapFactory.decodeFile(filePath);
                        Iv1.setImageBitmap(bt);
                        bt.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        data1 = baos.toByteArray();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (requestCode == IMAGE_RESULT_2) {

                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    try {
                        bt = BitmapFactory.decodeFile(filePath);
                        Iv2.setImageBitmap(bt);
                        bt.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        data2 = baos.toByteArray();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (requestCode == IMAGE_RESULT_3) {

                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    try {
                        bt = BitmapFactory.decodeFile(filePath);
                        Iv3.setImageBitmap(bt);
                        bt.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        data3 = baos.toByteArray();
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

    public void up1(View view) {
        startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT_1);
    }

    public void up2(View view) {
        startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT_2);
    }

    public void up3(View view) {
        startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT_3);
    }
    private void showCustomDialog_tick() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.tick_custom, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        TextView tv=(TextView)dialogView.findViewById(R.id.customtv);
        tv.setText(getResources().getString(R.string.Registered));
        TextView tv2=(TextView)dialogView.findViewById(R.id.customtv1);
        tv2.setText(getResources().getString(R.string.Next_okay));
        Button bt = (Button) dialogView.findViewById(R.id.buttonOk);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(Register_1.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (top.equals("1")) {
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