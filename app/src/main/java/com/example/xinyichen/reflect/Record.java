package com.example.xinyichen.reflect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.utils.URIBuilder;
import java.net.URI;



public class Record extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        dispatchTakeVideoIntent();


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
    }

