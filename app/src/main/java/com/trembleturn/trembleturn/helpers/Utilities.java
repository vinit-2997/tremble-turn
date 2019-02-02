package com.trembleturn.trembleturn.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utilities {

    private static Utilities utils;
    Context activity;

    private Utilities() {
    }

    public static Utilities getInstance() {
        if (utils == null)
            utils = new Utilities();
        return utils;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void toggleEditText(boolean state, EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setFocusable(state);
            editTexts[i].setEnabled(state);
            editTexts[i].setFocusableInTouchMode(state);
        }
    }

    public static void toggleEditText(boolean state, AppCompatEditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setFocusable(state);
            editTexts[i].setEnabled(state);
            editTexts[i].setFocusableInTouchMode(state);
        }
    }

    /**
     * Method use to hide keyboard.
     *
     * @param ctx context of current activity.
     */
    public void hideKeyboard(Activity ctx) {
        try {
            if (ctx.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method use to show keyboard on current screen.
     *
     * @param ctx context of current activity.
     */
    public final void showKeyboard(Activity ctx) {
        try {
            if (ctx.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method use to check whether user is online or not.
     *
     * @param context context of current activity.
     * @return true if user is online else returns false.
     */
    public final boolean isOnline(Context context) {
        if (context != null) {
            try {
                ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Method use to check number is numeric or not
     *
     * @param number String number.
     * @return true if number is number else false.
     */
    public boolean isNumeric(String number) {
        String numExp = "^[-+]?[0-9]*\\.?[0-9]+$";
        Pattern pattern = Pattern.compile(numExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }


    /**
     * Method use to get IP address of user device.
     *
     * @return
     */
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        @SuppressWarnings("deprecation")
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("", "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "";
    }

}
