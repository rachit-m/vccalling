package com.labs.poziom.whereabouts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class FontManager extends TextView {


    public FontManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontManager(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
        setTypeface(tf);
    }

}