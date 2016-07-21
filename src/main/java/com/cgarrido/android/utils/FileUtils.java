package com.ylly.android.utils;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.ylly.android.utils.YllyUtils;

import java.io.File;

/**
 * Created by cristian on 01/09/2015.
 */
public abstract class FileUtils extends de.greenrobot.common.io.FileUtils{
    public static File getPathFromUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Get the cursor
        Cursor cursor = YllyUtils.getCtx().getContentResolver().query(uri,
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

    public static String getMimeType(String fileUrl) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUrl);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static String getExtension(File file) {
        int lastDotIndex = file.getName().lastIndexOf(".");
        return lastDotIndex > 0 ? file.getName().substring(lastDotIndex + 1).toLowerCase() : "";
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
