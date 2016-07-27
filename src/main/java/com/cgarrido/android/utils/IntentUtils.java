package com.cgarrido.android.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;

/**
 * Created by cristian on 01/09/2015.
 */
public abstract class IntentUtils {
    public static void pickImageFromGallery(Fragment ctx, int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        galleryIntent.setType("image/*");
        ctx.startActivityForResult(galleryIntent, requestCode);
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//        ctx.startActivityForResult(Intent.createChooser(intent,
//                "Complete action using"), requestCode);
    }

    public static Intent openBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        return i;
    }

    public static Intent openGPSSettings() {
        return new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }
}
