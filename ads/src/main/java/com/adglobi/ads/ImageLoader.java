package com.adglobi.ads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader{
    MemoryCache memoryCache=new MemoryCache();
    ExecutorService executorService;
    GetImageResult imageLoadingListener;
    //create pool of 5 threads
    public ImageLoader(GetImageResult imageLoadingListener){
        executorService= Executors.newFixedThreadPool(1);
        this.imageLoadingListener = imageLoadingListener;
    }

    //get bitmap either from cache or from web
    public void getBitmap(String url) {
        Bitmap b = memoryCache.get(url);
        if(b!=null) {
            imageLoadingListener.onImageLoaded(b);
            return;
        }
        //from web
        try {
            executorService.submit(new PhotosLoader(url));
        } catch (Throwable ex){
            ex.printStackTrace();
            if(ex instanceof OutOfMemoryError)
                memoryCache.clear();
        }
    }
    //download image from url
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    public void DisplayImage(String url, ImageView imageView) {
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else {
            executorService.submit(new PhotosLoader(url));
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(url));
    }
    //Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }
    class PhotosLoader implements Runnable {
        String url;
        PhotosLoader(String url){
            this.url=url;
        }

        @Override
        public void run() {
            try{
                Bitmap bmp=getBitmapFromURL(url);
                memoryCache.put(url, bmp);
                imageLoadingListener.onImageLoaded(bmp);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
    }
}
