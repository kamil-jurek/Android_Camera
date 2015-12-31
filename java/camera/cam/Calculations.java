package camera.cam;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class Calculations {
    public static String calculateSize(BasePointer base, Pointer p, String unit) {
        String calculatedSize = " ";

        double C = base.getBaseSize();

        double bas = Math.sqrt(Math.pow(base.getPoint2X() - base.getPoint1X(), 2) +
                Math.pow(base.getPoint2Y() - base.getPoint1Y(), 2));

        double obj = Math.sqrt(Math.pow(p.getPoint2X() - p.getPoint1X(), 2) + Math.pow(p.getPoint2Y() - p.getPoint1Y(), 2));

        double x = (C * obj) / bas;

        switch (unit) {
            case "cm" : calculatedSize = String.format("%1$,.2f", x) + " cm";

                break;
            case "mm" : calculatedSize = String.format("%1$,.2f", x*10) + " mm";
                break;

            case "inch" : calculatedSize = String.format("%1$,.2f", x*0.3937) + "inch";
                break;

            case "feet" : calculatedSize = String.format("%1$,.2f", x*0.0328) + " feet";
                break;
        }

        return calculatedSize;

    }

    public static Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    public static boolean saveBaseNameArray(String[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("bases", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public static boolean saveBaseValueArray(float[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("bases", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putFloat(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public static String[] loadBaseNameArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("bases", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    public static float[] loadBaseValueArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("bases", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        float array[] = new float[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getFloat(arrayName + "_" + i, 0);
        return array;
    }

    public static void deleteSharedPreferences(String sharedPreferencesName, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(sharedPreferencesName, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
