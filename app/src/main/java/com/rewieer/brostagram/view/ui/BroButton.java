package com.rewieer.brostagram.view.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.rewieer.brostagram.R;

public class BroButton extends AppCompatTextView {
    public BroButton(Context context) {
        super(context);
        setup();
    }

    public BroButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setup();
    }

    void setup() {
        setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bro_button_1, null));
        setTextColor(Color.WHITE);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);

        if (!isInEditMode()) {
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.latobold);
            setTypeface(typeface);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() != enabled) {
            if (!enabled) {
                setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bro_button_1_disabled, null));
            } else {
                setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bro_button_1, null));
            }
        }
        super.setEnabled(enabled);
    }
}
