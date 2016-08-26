package com.cgarrido.android.utils.image;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cgarrido.android.utils.MetricsUtils;
import com.cgarrido.android.utils.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cristian on 15/09/2015.
 *
 */
public abstract class ImageUtils {
    public static final String FILE_PREFIX = "file://";
    public static final String ASSETS_PREFIX = "assets://";
    private static ImageLoader mImageLoader;


    public static ImageLoader getImageLoader() {
        if (mImageLoader == null)
            mImageLoader = ImageLoader.getInstance();
        return mImageLoader;
    }

    public static BitmapFactory.Options getBitmapOptions(String fullpath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fullpath, options);
        return options;
    }


    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        int maxW = bmp1.getWidth() > bmp2.getWidth() ? bmp1.getWidth() : bmp2.getWidth();
        int maxH = bmp1.getHeight() > bmp2.getHeight() ? bmp1.getHeight() : bmp2.getHeight();
        Bitmap bmOverlay = Bitmap.createBitmap(maxW, maxH, bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    public static Bitmap scaledOverlay(Bitmap below, Bitmap above){
        int maxW = below.getWidth() > above.getWidth() ? below.getWidth() : above.getWidth();
        int maxH = below.getHeight() > above.getHeight() ? below.getHeight() : above.getHeight();

        Bitmap bmOverlay = Bitmap.createBitmap(maxW, maxH, below.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        Bitmap scaledBelow = Bitmap.createScaledBitmap(below, maxW, maxH, false);
        Bitmap scaledAbove = Bitmap.createScaledBitmap(above, maxW, maxH, false);
        canvas.drawBitmap(scaledBelow, new Matrix(), null);
        canvas.drawBitmap(scaledAbove, 0, 0, null);
        return bmOverlay;
    }


    public  static Bitmap getBitmapFromAsset(Context ctx, String strName) throws IOException {
        AssetManager assetManager = ctx.getAssets();
        InputStream istr = assetManager.open(strName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        istr.close();
        return bitmap;
    }


    public static Bitmap getBitmapFromPath(String fullPath) throws IOException
    {
        if (!fullPath.contains("://"))
            throw new IOException("The file path should contain the uri prefix");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = getImageLoader().loadImageSync(fullPath);
//        Bitmap bitmap = BitmapFactory.decodeFile(fullPath, options);
        return bitmap;
    }

    public static Bitmap getBitmapFromPathNoCache(String fullPath) throws IOException
    {
        if (fullPath.contains("://"))
            throw new IOException("The file path should NOT contain the uri prefix");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = AppManager.getImageLoader().loadImageSync(fullPath);
        Bitmap bitmap = BitmapFactory.decodeFile(fullPath, options);
        return bitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
        return resizedBitmap;
    }

    public static Bitmap flipX(Bitmap bitmap) {
        Bitmap mutableBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());//bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Matrix flipHorizontalMatrix = new Matrix();
        flipHorizontalMatrix.setScale(-1,1);
        flipHorizontalMatrix.postTranslate(bitmap.getWidth(), 0);

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(bitmap, flipHorizontalMatrix, null);
        return mutableBitmap;
    }


    public static String encodeBase64(Bitmap image, Bitmap.CompressFormat format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String s = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        Log.d("encodeBase64",s);
        return s;
    }
}
