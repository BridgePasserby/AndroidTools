package gcy.androidtools.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Hashtable;

/**
 * Copyright (c) 2018, 北京视达科科技有限责任公司 All rights reserved.
 * author：chongyang.gao
 * date：2018/8/16
 * description：一个二维码的生成工具
 */

public final class QrUtils {

    private QrUtils(){

    }

    /**
     * 根据内容，生成二维码图片
     * @param content 二维码的内容
     * @param QR_WIDTH  图片的宽度
     * @param QR_HEIGHT 图片的高度
     * @return
     */
    //对输入的内容生成二维码
    public static Bitmap generateBitmap(String content, int QR_WIDTH, int QR_HEIGHT) {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix;
        try {
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        if ((x > 30 && y > 30) && (x < QR_WIDTH - 30 && y < QR_HEIGHT - 30)) {
                            pixels[y * QR_WIDTH + x] = 0xffffffff;
                        }
                    }

                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加二维码中心logo
     */
    public static Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        if (logoBitmap == null || qrBitmap == null) {
            return null;
        }
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }

}
