package com.iwz.WzFramwork.mod.tool.common.img.serv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Base64;

import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.mod.tool.common.img.ToolImgMain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 描述：
 * 作者：小辉
 * 时间：2019/10/09
 */
public class ToolImgServApi extends ServApi {
    private ToolImgMain mMain;

    protected ToolImgServApi(ToolImgMain main) {
        super(main);
        mMain = main;
    }

    private static ToolImgServApi mToolImgServApi;

    public static ToolImgServApi getInstance(ToolImgMain main) {
        if (mToolImgServApi == null) {
            synchronized (ToolImgServApi.class) {
                if (mToolImgServApi == null) {
                    mToolImgServApi = new ToolImgServApi(main);
                }
            }
        }
        return mToolImgServApi;
    }

    /**
     * bitmap中的透明色用白色替换（解决分享中图片四周带黑边的问题）
     *
     * @param bitmap
     * @return
     */
    public Bitmap changeColor(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] colorArray = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = getMixtureWhite(bitmap.getPixel(j, i));
                colorArray[n++] = color;
            }
        }
        return Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * 获取和白色混合颜色
     *
     * @return
     */
    private int getMixtureWhite(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(getSingleMixtureWhite(red, alpha), getSingleMixtureWhite(green, alpha), getSingleMixtureWhite(blue, alpha));
    }

    /**
     * 获取单色的混合值
     *
     * @param color
     * @param alpha
     * @return
     */
    private int getSingleMixtureWhite(int color, int alpha) {
        int newColor = color * alpha / 255 + 255 - alpha;
        return newColor > 255 ? 255 : newColor;
    }
    /**
     * 压缩图片大小，适应微信分享
     *
     * @param bitMap 需要压缩的图片
     * @return 压缩后的图片
     */
    public Bitmap compressBitmap(Bitmap bitMap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置此参数是仅仅读取图片的宽高到options中，不会将整张图片读到内存中，防止oom
        options.inJustDecodeBounds = true;
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        bitMap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
        return bitMap;
    }

    /**
     * 将bitmap转为ByteArray
     *
     * @param bmp         bitmap数据
     * @param needRecycle bitmap是否需要回收
     * @return
     */
    public byte[] bimapToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle) {
                bmp.recycle();
            }
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
//                ExceptionProxy.catchException(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

    /**
     * 将bitmap转为ByteArray
     *
     * @param bmp         bitmap数据
     * @param needRecycle bitmap是否需要回收
     * @return
     */
    public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
//            ExceptionProxy.catchException(e);
        }
        return result;
    }

    /**
     * bitmap转base64
     *
     * @param bitmap bitmap对象
     * @return
     */
    public String bitmapToBase64(Bitmap bitmap) {
        String result = "";
        ByteArrayOutputStream bos = null;
        try {
            if (null != bitmap) {
                bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将bitmap放入字节数组流中
                bos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
                bos.close();
                byte[] bitmapByte = bos.toByteArray();
                result = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * bitmap转base64
     *
     * @param base64String base64加密后的字符串
     * @return
     */
    public Bitmap base64ToBitmap(String base64String) {
        Bitmap bitmap = null;
        try {
            byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception ex) {
//            ExceptionProxy.catchException(ex);
        }
        return bitmap;
    }

    public static String imgFileToBase64(String path) {
        String result = "";
        File file = new File(path);
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            result = Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
