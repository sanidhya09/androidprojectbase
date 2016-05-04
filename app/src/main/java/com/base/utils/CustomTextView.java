package com.base.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.base.R;

/**
 * Created by sanidhya on 4/5/16.
 */
public class CustomTextView extends TextView {
    private final static String HELVETICA_REGULAR = "regular";
    private final static String HELVETICA_MEDIUM = "medium";
    private final static String HELVETICA_BOLD = "bold";

    public CustomTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

            final int N = a.getIndexCount();
            for (int i = 0; i < N; ++i) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case R.styleable.CustomTextView_font_name:
                        String fontName = a.getString(attr);
                        Typeface face = null;
                        assert fontName != null;
                        if (fontName.equalsIgnoreCase(HELVETICA_REGULAR)) {
                            face = Typeface.create("sans-serif", Typeface.NORMAL);
                        } else if (fontName.equalsIgnoreCase(HELVETICA_BOLD))
                            face = Typeface.create("sans-serif", Typeface.BOLD);
                        else if (fontName.equalsIgnoreCase(HELVETICA_MEDIUM))
                            face = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue-Medium_old.ttf");

                        if (face != null)
                            setTypeface(face);
                        break;
                }
            }
            a.recycle();
        }
    }

}
