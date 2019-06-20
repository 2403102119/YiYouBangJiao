package com.tangchaoke.yiyoubangjiao.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by Administrator on 2015/12/19.
 */
public class BitmapUtils {

    /**
     * 图片缩放
     * wf.wh必须不能是int
     *
     * @param source
     * @param wf
     * @param hf
     * @return
     */
    public static Bitmap zoom(Bitmap source, float wf, float hf) {
        Matrix matrix = new Matrix();
        float sx = wf / source.getWidth();
        float sy = hf / source.getHeight();
        matrix.postScale(sx, sy);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    /**
     * 头像圆形裁剪
     *
     * @param source
     * @return
     */
    public static Bitmap circleBitMap(Bitmap source) {
        final Paint paint = new Paint();
        //抗锯齿效果
        int width = source.getWidth();
        paint.setAntiAlias(true);
        //指定大小bitmap
        Bitmap target = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //根据target生成一个画布
        Canvas canvas = new Canvas(target);
        //在画布上画了一个圆
        //参数CX,cy-->确定绘制圆的圆心点
        //半径参数
        //画笔
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        //这句话是关键:
        //分析：我们以一张图片作为画布，在上面画了一个圆-->画图展示-->"这时候,绘制的圆和图片本身就出现了一个圆形的交集图案"
        //setXfermode：设置当绘制的图像出现相交情况时候的处理方式的,它包含的常用模式有哪几种
        //PorterDuff.Mode.SRC_IN 取两层图像交集部门,只显示上层图像,注意这里是指取相交叉的部分,然后显示上层图像
        //PorterDuff.Mode.DST_IN 取两层图像交集部门,只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //使用设置了setXfermode方案的paint绘制图像
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    public static Bitmap createImageThumbnail(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception e) {
        }
        return bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
