package com.example.xinyichen.reflect;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraActivity;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.utils.URIBuilder;

import java.io.File;
import java.net.URI;



public class Record extends HiddenCameraActivity {


    private CameraConfig mCameraConfig;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        setUpCamera();

        //Take a picture
        findViewById(R.id.recordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take picture using the camera without preview.
                takePicture();
            }
        });


        //dispatchTakeVideoIntent();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //noinspection MissingPermission
                startCamera(mCameraConfig);
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        analyzeFrame(bitmap);
    }

    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, "Cannot open camera.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, "Cannot write image captured by camera.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camra permission before initializing it.
                Toast.makeText(this, "Camera permission not available.", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //This error will never happen while hidden camera is used from activity or fragment
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(this, "Your device does not have front camera.", Toast.LENGTH_LONG).show();
                break;
        }
    }



    static final int REQUEST_VIDEO_CAPTURE = 1;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();


            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this,videoUri);

            Bitmap bitmap = retriever.getFrameAtTime(1000000);
            Bitmap bitmap2 = retriever.getFrameAtTime(2000000);





            analyzeFrame(bitmap);
            analyzeFrame(bitmap2);

        }
    }





    byte[] imageArray;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    protected void analyzeFrame(Bitmap img)
    {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageArray = stream.toByteArray();

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    HttpClient httpClient = new DefaultHttpClient();

                    try {
                        // NOTE: You must use the same region in your REST call as you used to obtain your subscription keys.
                        //   For example, if you obtained your subscription keys from westcentralus, replace "westus" in the
                        //   URL below with "westcentralus".
                        URIBuilder uriBuilder = new URIBuilder("https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize");
                        URI uri = uriBuilder.build();
                        HttpPost request = new HttpPost(uri);
                        // Request headers. Replace the example key below with your valid subscription key.
                        request.setHeader("Content-Type", "application/octet-stream");
                        request.setHeader("Ocp-Apim-Subscription-Key", "cb9deca8171d4ce9b993515f8df5cc29");

                        ByteArrayEntity be = new ByteArrayEntity(imageArray);
                        request.setEntity(be);

                        HttpResponse response = httpClient.execute(request);
                        HttpEntity entity = response.getEntity();

                        if (entity != null) {
                            System.out.println(EntityUtils.toString(entity));
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            };

            new Thread(r).start();
        }

        public void setUpCamera() {
            //Setting camera configuration
            mCameraConfig = new CameraConfig()
                    .getBuilder(this)
                    .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                    .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                    .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                    .setImageRotation(CameraRotation.ROTATION_270)
                    .build();


            //Check for the camera permission for the runtime
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                //Start camera preview
                startCamera(mCameraConfig);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
            }
        }


    }

