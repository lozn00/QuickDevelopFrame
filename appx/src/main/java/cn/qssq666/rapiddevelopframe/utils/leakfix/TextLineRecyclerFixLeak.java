package cn.qssq666.rapiddevelopframe.utils.leakfix;

import android.os.Build;
import android.text.style.CharacterStyle;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class TextLineRecyclerFixLeak {
    private static Field sCached;
    private static Field sCharacterStyleSpanSet;
    private static Field sMetricAffectingSpanSpanSet;
    private static Field sReplacementSpanSpanSet;
    private static Field sSpans;
    private static Field sSpanned;
    private static Field sText;

    static {
        try {
            Class clazz = Class.forName("android.text.TextLine");
            sCached = clazz.getDeclaredField("sCached");
            sSpanned = clazz.getDeclaredField("mSpanned");
            sText = clazz.getDeclaredField("mText");
            sCached.setAccessible(true);
            sSpanned.setAccessible(true);
            sText.setAccessible(true);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                sCharacterStyleSpanSet = clazz.getDeclaredField("mCharacterStyleSpanSet");
                sMetricAffectingSpanSpanSet = clazz.getDeclaredField("mMetricAffectingSpanSpanSet");
                sReplacementSpanSpanSet = clazz.getDeclaredField("mReplacementSpanSpanSet");
                Class spanSetClass = Class.forName("android.text.SpanSet");
                sSpans = spanSetClass.getDeclaredField("spans");
                sCharacterStyleSpanSet.setAccessible(true);
                sMetricAffectingSpanSpanSet.setAccessible(true);
                sReplacementSpanSpanSet.setAccessible(true);
                sSpans.setAccessible(true);
            }
        } catch (Exception e) {
            Log.e("TextLineRecycler", "", e);
        }
    }

    /**
     * recycle system TextLine
     */
    public static void recycle() {
        try {
            Object cached = sCached.get(null);
            int length = Array.getLength(cached);
            if (cached != null) {
                synchronized (cached) {
                    for (int i = 0; i < length; i++) {
                        Object textLine = Array.get(cached, i);
                        if (textLine != null) {
                            Object text = sText.get(textLine);
                            // mText has been recycled, recycle mSpanned and other SpanSet here  
                            if (null == text && null != sSpanned.get(textLine)) {
                                sSpanned.set(textLine, null);
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                    recycleSpan(sCharacterStyleSpanSet, textLine);
                                    recycleSpan(sMetricAffectingSpanSpanSet, textLine);
                                    recycleSpan(sReplacementSpanSpanSet, textLine);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TextLineRecycler", "", e);
        }
    }

    private static <T extends CharacterStyle> void recycleSpan(Field field, Object textLine)
            throws IllegalArgumentException, IllegalAccessException {
        Object spanSet = field.get(textLine);
        T[] spans = (T[]) TextLineRecyclerFixLeak.sSpans.get(spanSet);
        if (spans != null) {
            for (int i = 0; i < spans.length; i++) {
                spans[i] = null;
            }
        }
    }
}  