
package com.example.moavara.Best;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.moavara.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class ViewMarker extends MarkerView {

    private final TextView tvContent;
    private final CardView cview;

    public ViewMarker(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
        cview = findViewById(R.id.cview);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius((int) (100f * Resources.getSystem().getDisplayMetrics().density));
        gradientDrawable.setColor(Color.parseColor("#0D0E10"));
        gradientDrawable.setStroke((int) (2f * Resources.getSystem().getDisplayMetrics().density), Color.parseColor("#621CEF"));

        cview.setBackground(gradientDrawable);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            tvContent.setText(Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
