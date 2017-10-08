package com.example.xinyichen.reflect;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;


public class Record extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();

                try
                {
                    // NOTE: You must use the same region in your REST call as you used to obtain your subscription keys.
                    //   For example, if you obtained your subscription keys from westcentralus, replace "westus" in the
                    //   URL below with "westcentralus".
                    URIBuilder uriBuilder = new URIBuilder("https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize");
                    URI uri = uriBuilder.build();
                    HttpPost request = new HttpPost(uri);
                    // Request headers. Replace the example key below with your valid subscription key.
                    request.setHeader("Content-Type", "application/json");
                    request.setHeader("Ocp-Apim-Subscription-Key", "cb9deca8171d4ce9b993515f8df5cc29");

                    // Request body. Replace the example URL below with the URL of the image you want to analyze.
                    StringEntity reqEntity = new StringEntity("{ \"url\": \"http://xinyichen.me/assets/img/dog.jpg\" }");
                    request.setEntity(reqEntity);

                    HttpResponse response = httpClient.execute(request);
                    HttpEntity entity = response.getEntity();

                    if (entity != null)
                    {
                        System.out.println(EntityUtils.toString(entity));
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        };

        new Thread(r).start();

    }
}
