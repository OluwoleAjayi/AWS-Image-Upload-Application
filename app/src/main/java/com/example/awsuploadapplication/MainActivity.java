package com.example.awsuploadapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.example.awsuploadapplication.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    ImageView image;
    Bitmap bmpImage;
    Button uploadBtn;
    String ACCESS_KEY = "AKIA55EQW5MXXS6DA367";
    String SECRET_KEY = "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";
    String MY_BUCKET = "user-research-and-development";
    String OBJECT_KEY = "unique_id";
    File Uploading_Image;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        image = (ImageView) mainBinding.imageView;
        bmpImage = null;
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { takePicture(image); }
        });

        uploadBtn = (Button) mainBinding.uploadImage;
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { amazonUpload();

            }
        });




    }
    final int CAMERA_INTENT = 51;

    public  void takePicture (View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);


                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA_INTENT);

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_INTENT:
                if (data != null && data.getExtras() != null) {
                    bmpImage = (Bitmap) data.getExtras().get("data");
                }
                if (bmpImage != null) {
                    image.setImageBitmap(bmpImage);
                }
                break;
        }

        Intent viewImage = new Intent(MainActivity.this, databaseActivity.class);
        startActivity(viewImage);
    }
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = file.getAbsolutePath();
        return file;
    }

    private void amazonUpload() {
        AWSCredentials credentialsProvider = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.US_EAST_2));
      //  List<Bucket> buckets = s3.listBuckets();
     //   for (Bucket bucket:buckets) {
      //      Log.e("Bucket", "Name " + bucket.getName() + " Owner"
      //      + bucket.getOwner() + " Date " + bucket.getCreationDate());
     //   }
    //    Log.e("Size ", "" + s3.listBuckets().size());
        final TransferUtility utility = new TransferUtility(s3, getApplicationContext());
        Uploading_Image = new File(Environment.getExternalStorageDirectory().getPath() );
        final TransferObserver observer = utility.upload(MY_BUCKET, OBJECT_KEY, Uploading_Image );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("onStateChanged", id + state.name());
                if (state == TransferState.COMPLETED) {
                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                }else if (state == TransferState.FAILED) {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(MainActivity.this, "Unable t upload", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();

            }
        });
    }





}