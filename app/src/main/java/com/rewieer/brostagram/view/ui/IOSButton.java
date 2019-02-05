package com.rewieer.brostagram.view.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.rewieer.brostagram.R;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

public class IOSButton extends AppCompatTextView {
    public IOSButton(Context context) {
        super(context);
        setup();
    }

    public IOSButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setup();
    }

    void setup() {
        setTextColor(getResources().getColor(R.color.colorIOSBlue, null));
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        if (!isInEditMode()) {
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.latobold);
            setTypeface(typeface);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() != enabled) {
            if (!enabled) {
                setTextColor(getResources().getColor(R.color.colorGrayVeryDark, null));
            } else {
                setTextColor(getResources().getColor(R.color.colorIOSBlue, null));
            }
        }
        super.setEnabled(enabled);
    }
}
