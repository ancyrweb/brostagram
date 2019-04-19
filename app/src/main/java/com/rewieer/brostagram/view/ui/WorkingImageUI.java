package com.rewieer.brostagram.view.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.appcompat.widget.AppCompatImageView;

public class WorkingImageUI extends AppCompatImageView {
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = Math.max(0.1f, Math.min(10f, detector.getScaleFactor()));
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);

            return true;
        }
    }

    private ScaleGestureDetector mScaleListener = new ScaleGestureDetector(getContext(), new ScaleListener());

    public WorkingImageUI(Context context) {
        super(context);
    }

    public WorkingImageUI(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        mScaleListener.onTouchEvent(event);
        return true;
    }
}
