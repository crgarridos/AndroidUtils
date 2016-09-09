package com.cgarrido.android.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.io.File;

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

    public static void openWithGDocs(Activity activity, Uri uri) {
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(uri, "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

//        Intent intent = Intent.createChooser(target, "Open Pdf");
        try {
            activity.startActivity(target);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
//            https://play.google.com/store/apps/details?id=com.google.android.apps.pdfviewer
            Intent goToMarket = new Intent(Intent.ACTION_VIEW)
//                    .setData(Uri.parse("market://search?q=pdf&c=apps"));
                    .setDataAndType(Uri.parse("http://docs.google.com/viewer?url=" + uri.toString()), "text/html");
//            market://search?q=<seach_query>&c=apps
            activity.startActivity(goToMarket);
//            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=id=com.google.android.apps.pdfviewer"));

        }
    }


    public static boolean openWithPDFReader(Activity activity, File file) {


        Uri path = Uri.fromFile(file);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(path, "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            activity.startActivity(target);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "No Application Available to View PDF",
                    Toast.LENGTH_LONG).show();
            // Instruct the user to install a PDF reader here, or something
//            https://play.google.com/store/apps/details?id=com.google.android.apps.pdfviewer
            Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("market://search?q=pdf&c=apps"));
            try {
                activity.startActivity(goToMarket);
            } catch (ActivityNotFoundException ignored) {
                return false;
            }
        }
        return true;

    }

    public static Intent openGPSSettings() {
        return new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }
}
