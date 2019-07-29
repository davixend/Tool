package timetap.com.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import java.io.File;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class abzarhaye proje
 */
public class Utils extends Activity {

    /**
     * close keyboard
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * open keyboard
     *
     * @param activity
     * @param view
     */
    public static void showKeyboard(Activity activity, View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * address ye video (path) ro migire va yek bitmap az an barmigardune
     *
     * @param path address video
     * @return Bitmap
     */
    public static Bitmap getImageFromVideo(String path) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Video.Thumbnails.MICRO_KIND);
        return thumb;
    }

    /**
     * yek Uri migirad va path an ra barmigardanad
     *
     * @param contentURI Uri
     * @param activity
     * @return String Path
     */
    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    /**
     * yek Url File migirad va noe file ra barmigardanad
     *
     * @param url
     * @return String
     */
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * yek Url File migirad va check mikonad ke aya vojod darad ya na
     *
     * @param uri
     * @return Boolean
     */
    public static boolean isExistsFile(String uri) {
        File file = new File(URI.create(uri).getPath());
        return file.exists();
    }

    /**
     * yek float az noe dp migirad va tabdil b px mikonad
     *
     * @param context
     * @param dpValue
     * @return int
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * yek float az noe sp migirad va tabdil b px mikonad
     *
     * @param context
     * @param spValue
     * @return int
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getWidthDesplay() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getHeightDesplay() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static double getRandomNumber() {
        double x = Math.random();
        return x;
    }

    /**
     * yek timestamp migirad va un ra tabdil b time shamsi mikone
     *
     * @param milliSeconds
     * @return
     */
    public static Date convertTimeMillisToDate(Long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.getTime();
    }

    public static Long getTimeCurrent() {
        return System.currentTimeMillis();
    }

    /**
     * check Validation mobile number
     *
     * @param tel String number mobile
     * @return Boolean
     */
    public static boolean isValidateMobile(String tel) {
        boolean isValid = false;
        String expression = "(\\+98|0)?9\\d{9}";
        CharSequence inputStr = tel;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * check Validation Email
     *
     * @param email String
     * @return Boolean
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static void shareText(Activity activity, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, "اشتراک گزاری"));
    }


    /**
     * sorate internet ra barmigardanad
     *
     * @param activity
     * @return int
     */
    private static int getNetSpeed(Activity activity) {
        int netSpeed = 0;
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);

        boolean isMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if (isMobile) {
            TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
            int networkType = telephonyManager.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    netSpeed = 0;
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    netSpeed = 0;
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    netSpeed = 0;
                    break;
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    netSpeed = 0;
                    break;
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    netSpeed = 0;
                    break;

                case TelephonyManager.NETWORK_TYPE_UMTS:
                    netSpeed = 1;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    netSpeed = 1;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    netSpeed = 1;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    netSpeed = 1;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    netSpeed = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    netSpeed = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    netSpeed = 2;
                    break;
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    netSpeed = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    netSpeed = 5;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    netSpeed = 10;
                    break;

                default:
                    netSpeed = 4;
                    break;
            }
        } else if (isWifi) {
            WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(activity.getApplicationContext().WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                int linkSpeed = wifiInfo.getLinkSpeed(); //measured using WifiInfo.LINK_SPEED_UNITS
                netSpeed = linkSpeed / 10;
            }
        }
        return netSpeed;
    }

    /**
     * ye String migirad va tabdil be md5 mikonad
     *
     * @param s String
     * @return String
     */
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * version android ra barmigardanad
     *
     * @return int
     */
    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * yek time az noe milisec migirad va tabdil be time ghabele moshahede mikonad
     *
     * @param time int
     * @return String
     */
    public static String getTimeAsIntMill(int time) {
        int min = 0;
        int sec = 0;
        if (time >= 1000) {
            if ((time / 1000) >= 60) {
                sec = ((time % 60000) / 1000);
                min = time / 60000;
            } else {
                sec = time / 1000;
            }
        }
        return "0" + min + ":" + ((sec >= 10) ? sec : "0" + sec);
    }


    public static String getTimeFromInt(int time) {
        if (time < 60) {
            return "00:" + (((time >= 10) ? time : "0" + time));
        } else {
            int min = time / 60;
            int sec = time % 60;
            return ((min >= 10) ? min : "0" + min) + ":" + ((sec >= 10) ? sec : "0" + sec);
        }

    }

    /**
     * yek String adad migire va tabdil be adade farsi mikonad
     *
     * @param str
     * @return String
     */
    public static String convertNumberToPersion(String str) {
        str = str.replaceAll("0", "٠");
        str = str.replaceAll("1", "١");
        str = str.replaceAll("2", "٢");
        str = str.replaceAll("3", "٣");
        str = str.replaceAll("4", "٤");
        str = str.replaceAll("5", "٥");
        str = str.replaceAll("6", "٦");
        str = str.replaceAll("7", "٧");
        str = str.replaceAll("8", "٨");
        str = str.replaceAll("9", "٩");
        return str;
    }

    /**
     * yek String adad migire va tabdile be adade english mikone
     *
     * @param digit
     * @return String
     */
    public static String toEnglishDigits(String digit) {
        digit = digit.replaceAll("۰", "0");
        digit = digit.replaceAll("۱", "1");
        digit = digit.replaceAll("۲", "2");
        digit = digit.replaceAll("۳", "3");
        digit = digit.replaceAll("۴", "4");
        digit = digit.replaceAll("۵", "5");
        digit = digit.replaceAll("۶", "6");
        digit = digit.replaceAll("۷", "7");
        digit = digit.replaceAll("۸", "8");
        digit = digit.replaceAll("۹", "9");

        return digit;
    }

    /**
     * ye String migirad va check mikonad k aya adad hastesh ya na
     *
     * @param s String
     * @return Boolean
     */
    public static boolean isNumber(String s) {
        boolean isValidInteger = false;
        try {
            Integer.parseInt(s);
            isValidInteger = true;
        } catch (NumberFormatException ex) {
        }
        return isValidInteger;
    }

    /**
     * clear notification
     *
     * @param context
     * @param notificationId
     */
    public static void clearNotification(Context context, int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }


    /**
     * yek edittext migire va zamani k dakhele edittext matn type beshe direction ra avaz mikonad
     *
     * @param editText
     */
    public static void setDirection(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.length() > 0) {
                    editText.setGravity(Gravity.LEFT);
                } else {
                    editText.setGravity(Gravity.RIGHT);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * baraye faal kardn va gheirefaal kardn wakeLock
     *
     * @param activity
     * @param flag
     */
    public static void setWakeLock(Activity activity, boolean flag) {
        if (activity.getWindow() != null)
            if (flag) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
    }

    public static String[] countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines;
    }

    /**
     * hazf kardan code haye javascript dar yek text
     *
     * @param html
     * @return
     */
    public static String stripHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

    public static long getLogTimer(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

}