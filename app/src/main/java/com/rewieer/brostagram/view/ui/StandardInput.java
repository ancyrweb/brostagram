package com.rewieer.brostagram.view.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.rewieer.brostagram.R;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

public class StandardInput extends AppCompatEditText {
    public StandardInput(Context context) {
        super(context);
        setup();
    }

    public StandardInput(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setup();
    }

    void setup() {
        setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.input_borderless, null));
    }
}
