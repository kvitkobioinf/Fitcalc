package pl.fitcalc.fitcalc;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class Utils {
    public static int dpsToPixels(int dps, Context mContext) {
        Resources r = mContext.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
    }
}
