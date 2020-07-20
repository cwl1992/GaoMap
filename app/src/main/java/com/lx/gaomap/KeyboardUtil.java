package com.lx.gaomap;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {

    public static void hideKeyboard(final Activity act) {
        if (null == act) {
            return;
        }
        if (null == act.getCurrentFocus()) {
            return;
        }
        ((InputMethodManager) act
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(
                        act.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive(v)) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }
}
