package com.dlvn.mcustomerportal.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import com.dlvn.mcustomerportal.BuildConfig;
import com.google.common.primitives.Ints;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    public static byte[] convertFileToByteArray(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public static File convertByteToFile(byte[] bytes, File file) throws IOException {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);

        } catch (FileNotFoundException e) {
            myLog.printTrace(e);
        } catch (IOException ioe) {
            myLog.printTrace(ioe);
        } finally {
            // close the streams using close method
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ioe) {
                myLog.printTrace(ioe);
            }
        }
        return file;
    }

    public static String convertFileToEncodeStringBase64(File file) throws IOException {
        byte[] bytes = convertFileToByteArray(file);
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encodedString;
    }

    public static File convertStringToDecodeByteBase64(String str, File file) throws IOException {
        byte[] decodeString = Base64.decode(str, Base64.DEFAULT);
        return convertByteToFile(decodeString, file);
    }

    public static File convertStringToDecodeByteBase64(List<Integer> lst, File file) throws IOException {

        byte[] array = new byte[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            array[i] = lst.get(i).byteValue();
        }
        return convertByteToFile(array, file);
    }

    public static File convertStringToDecodeByteNormal(String str, File file) throws IOException {
        byte[] decodeString = str.getBytes("UTF-8");
        return convertByteToFile(decodeString, file);
    }

    public static void openFile(Context context, File file) {

        String fName = file.getAbsolutePath();
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        Intent target = new Intent(Intent.ACTION_VIEW);

        String mime = "*/*";
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        if (mimeTypeMap.hasExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString())))
            mime = mimeTypeMap
                    .getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()));

        if (fName.endsWith(".pdf")) {
            target.setDataAndType(uri, "application/pdf");
        } else if (fName.endsWith(".docx")) {
            target.setDataAndType(uri, "application/msword");
        } else if (fName.endsWith(".xls")) {
            target.setDataAndType(uri, "application/vnd.ms-excel");
        } else
            target.setDataAndType(uri, mime);

//        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Intent intent = Intent.createChooser(target, "Open File");
        try {
            context.startActivity(target);
        } catch (ActivityNotFoundException e) {
            myLog.printTrace(e);
        }
    }

    public static void writeStringToFile(String data, File file) {
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            myLog.printTrace(e);
        }
    }

    /**
     * Method support get real path from media store follow by API Code
     */

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
