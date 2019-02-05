package com.rewieer.brostagram.view.ui;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.rewieer.brostagram.R;

public class BroInput extends AppCompatEditText {
    public BroInput(Context context) {
        super(context);
        setup();
    }

    public BroInput(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setup();
    }

    void setup() {
        setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bro_input_borderless, null));
    }
}
