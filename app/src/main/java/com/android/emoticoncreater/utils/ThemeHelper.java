

package com.android.emoticoncreater.utils;

import android.support.annotation.StyleRes;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.app.MDBaseActivity;
import com.android.emoticoncreater.config.Constants;

/**
 * Created on 2017/10/30 13:26:25
 * Copied from Copyright (C) 2017 Kosh.
 * Modified by Copyright (C) 2017 ThirtyDegreesRay.
 */

public class ThemeHelper {

    public static void apply(MDBaseActivity activity) {
        String theme = PrefUtils.getTheme();
        activity.setTheme(getTheme(theme));
    }

    @StyleRes
    public static int getTheme(String theme) {
        switch (theme) {
            case Constants.LIGHT_TEAL:
                return R.style.ThemeLightTeal_Teal;
            case Constants.DARK:
                return R.style.ThemeDark_Teal;
        }
        return R.style.ThemeLightTeal_Teal;
    }
}
