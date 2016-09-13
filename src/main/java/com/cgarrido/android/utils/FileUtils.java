package com.cgarrido.android.utils;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by cristian on 01/09/2015.
 */
public abstract class FileUtils extends de.greenrobot.common.io.FileUtils{
    public static File getPathFromUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Get the cursor
        Cursor cursor = AndroidUtils.getCtx().getContentResolver().query(uri,
                filePathColumn, null, null, null);
        // Move to first row
        if (cursor == null)
            return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
        return new File(imgDecodableString);
    }

    public static String getMimeType(File file) {
        return getMimeType(file.getName());
    }

    public static String getMimeType(String fileName) {
        String extension = getExtension(fileName);
        if (extension == null)
            return null;
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static String getExtension(File file) {
        return getExtension(file.getName());
    }

    public static String getExtension(String fileName) {
        int extensionDelimiter = fileName.lastIndexOf(".");
        if (extensionDelimiter == -1)
            return null;
        return fileName.substring(extensionDelimiter + 1, fileName.length());
    }


    public static boolean isImageExtension(File file) {
        switch (getExtension(file)){
            case "png":
            case "jpeg":
            case "jpg":
            case "bmp":
            case "gif":
                return true;
        }
        return false;
    }
}
