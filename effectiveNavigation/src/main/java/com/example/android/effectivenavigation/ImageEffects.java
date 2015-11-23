package com.example.android.effectivenavigation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by raHuL on 9/12/2015.
 */
public class ImageEffects
{
    private static  Bitmap originalimage;
    private static String effect_name;
    private static Bitmap frame;

    public static Bitmap getFrame() {
        return frame;
    }

    public static void setFrame(Bitmap frame) {
        ImageEffects.frame = frame;
    }
    public static Bitmap getOriginalimage() {
        return originalimage;
    }

    public static void setOriginalimage(Bitmap originalimage) {
        ImageEffects.originalimage = originalimage;
    }

    public static String getEffect_name() {
        return effect_name;
    }

    public static void setEffect_name(String effect_name) {
        ImageEffects.effect_name = effect_name;
    }



    public static Bitmap createSepiaToningEffect(int depth, double red, double green, double blue) {
        // image size
        int width = getOriginalimage().getWidth();
        int height = getOriginalimage().getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, getOriginalimage().getConfig());
        // constant grayscale
        final double GS_RED = 0.3;
        final double GS_GREEN = 0.59;
        final double GS_BLUE = 0.11;
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = getOriginalimage().getPixel(x, y);
                // get color on each channel
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // apply grayscale sample
                B = G = R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                // apply intensity level for sepid-toning on each channel
                R += (depth * red);
                if(R > 255) { R = 255; }

                G += (depth * green);
                if(G > 255) { G = 255; }

                B += (depth * blue);
                if(B > 255) { B = 255; }

                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    public static Bitmap CreateGreyscale() {

        Bitmap src =getOriginalimage();
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = src.getWidth();
        int height = src.getHeight();

        // scan through every single pixel
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }


        public static Bitmap Contrast(double value)
        {
            Bitmap src = getOriginalimage();
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.red(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.red(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }


    public static Bitmap smooth() {
        Bitmap src = getOriginalimage();
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.setAll(1);
        convMatrix.Matrix[1][1] = 5;
        convMatrix.Factor =14; //(8 + 5)
        convMatrix.Offset = 0;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }


    public static Bitmap CreateBlur() // not working......
    {
        Bitmap src = getOriginalimage();
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.setAll(1);
        convMatrix.Factor=9;
        convMatrix.Offset =0;
        return ConvolutionMatrix.computeConvolution3x3(src,convMatrix);

    }
    public static Bitmap CreateSharpen()
    {
        Bitmap src = getOriginalimage();
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.setAll(0);
        convMatrix.Matrix[0][1] = -3;
        convMatrix.Matrix[1][0] = -3;
        convMatrix.Matrix[1][1] = 21;
        convMatrix.Matrix[1][0] = -3;
        convMatrix.Matrix[2][1] = -3;
        convMatrix.Factor=8;
        convMatrix.Offset =1;
        return ConvolutionMatrix.computeConvolution3x3(src,convMatrix);

    }

    public static Bitmap Emboss()
    {
        Bitmap src = getOriginalimage();
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.setAll(0);
        convMatrix.Matrix[0][0] = -18;
        convMatrix.Matrix[0][1] = -9;
        convMatrix.Matrix[1][0] = -9;
        convMatrix.Matrix[1][1] = 9;
        convMatrix.Matrix[1][2] = 9;
        convMatrix.Matrix[2][1] = 9;
        convMatrix.Matrix[2][2] = 18;

        convMatrix.Factor=8;
        convMatrix.Offset =1;
        return ConvolutionMatrix.computeConvolution3x3(src,convMatrix);

    }

    public static Bitmap Brightness(int x) //
    {
        Bitmap src = getOriginalimage();
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.setAll(1);
        convMatrix.Matrix[1][1] = 9+x;
        convMatrix.Factor=8;
        convMatrix.Offset =1;
        return ConvolutionMatrix.computeConvolution3x3(src,convMatrix);
    }


    public static Bitmap Overlay(Bitmap bmp1, Bitmap bmp2 , Context ctx)
    {

        //int y = bmp2.getHeight();
        // int x = bmp1.getHeight();
        //  int v =0;


       /*
        Bitmap overlay = Bitmap.createBitmap(bmp1.getWidth(),bmp1.getHeight(),bmp1.getConfig());
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bmp1, new Matrix(),null);
        canvas.drawBitmap(bmp2, new Matrix(),null);
        return overlay;
         */
        Resources r = ctx.getResources();
        Drawable[] layers = new Drawable[2];

        layers[0] = new BitmapDrawable(r,bmp1);
        layers[1] = new BitmapDrawable(r,bmp2);
        LayerDrawable layer = new LayerDrawable(layers);
        Bitmap b = Bitmap.createBitmap(bmp2.getWidth(),bmp2.getHeight(),bmp2.getConfig());
        layer.setBounds(0,0,bmp2.getWidth(),bmp2.getHeight());
        layer.draw(new Canvas(b));
        return b;
    }


}
