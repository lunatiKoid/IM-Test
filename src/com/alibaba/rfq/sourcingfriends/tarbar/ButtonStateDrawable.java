package com.alibaba.rfq.sourcingfriends.tarbar;

import com.alibaba.rfq.sourcingfriends.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ButtonStateDrawable extends Drawable {

    private int       label;
    private Bitmap    bitmap;
    private Shader    labelShader;
    private Shader    imageShader;
    private boolean   onState;
    public static int WIDTH;
    private Context   context;

    public ButtonStateDrawable(Context context, int imageId, int deposit, boolean onState) {
        super();
        this.context = context;

        this.bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
        this.label = deposit;
        this.onState = onState;
        if (onState) {
            // 字的颜色的渐变色的设置
            labelShader = new LinearGradient(0, 0, 0, 10, new int[] { Color.DKGRAY, Color.DKGRAY }, null,
                                             Shader.TileMode.MIRROR);
        } else {
            labelShader = new LinearGradient(0, 0, 0, 10, new int[] { Color.DKGRAY, Color.DKGRAY }, null,
                                             Shader.TileMode.MIRROR);

            this.bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        int bwidth = bitmap.getWidth();
        int bheight = bitmap.getHeight();

        int x = (WIDTH - bwidth) / 2;
        int y = 4;
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(33);
        p.setFakeBoldText(true);
        p.setTextAlign(Align.CENTER);
        p.setShader(labelShader);
        p.setAntiAlias(true);
        canvas.drawText(context.getResources().getString(label), WIDTH / 2, y + bheight + 11, p);

        if (!this.onState) {
            p.setShader(imageShader);
        } else {
            // this.bitmap =
            // BitmapFactory.decodeResource(context.getResources(), imageId);
            p.setShader(null);

            // Log.i("ButtonStateDrawable","w="+bwidth+",h="+bheight+",x="+x+",y="+y+",width="+WIDTH);

            int x_st = 15;
            int y_st = y - 1;
            int x_ed = WIDTH - 15;
            int y_ed = 32 + y + 1;

            Shader bgShader = new LinearGradient(x_st, y_st, x_ed, y_ed, new int[] {
                    context.getResources().getColor(R.color.tabbar_1), // color should be changed
                    context.getResources().getColor(R.color.tabbar_2) }, //
                                                 null, Shader.TileMode.CLAMP);
            p.setShader(bgShader);
            p.setStyle(Paint.Style.FILL);
            RectF rect = new RectF(x_st, y_st, x_ed, y_ed);
            canvas.drawRoundRect(rect, 5.0f, 5.0f, p);
        }
        canvas.drawBitmap(bitmap, x, y, p);
    }

    private String string(int label2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }
}
