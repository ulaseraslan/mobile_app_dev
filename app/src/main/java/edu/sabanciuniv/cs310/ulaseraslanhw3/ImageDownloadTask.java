package edu.sabanciuniv.cs310.ulaseraslanhw3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {



    ImageView imgView;

    public ImageDownloadTask(ImageView imageView) {
        this.imgView = imageView;

    }

    @Override
    protected Bitmap doInBackground(String... paths) {

        String path = paths[0];
        Bitmap bitmap = null;

        try {
            URL url = new URL(path);
            InputStream is = new BufferedInputStream(url.openStream());

            bitmap = BitmapFactory.decodeStream(is);
            //current.setBitmap(bitmap);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        imgView.setImageBitmap(bitmap);
    }
}
