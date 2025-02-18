package com.shq.oper.util;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author: ltg
 * @date: Created in 13:36 2018/7/2
 */
public class Collections {
    public Collections() {
    }

    public static <K, V> Map<K, V> sort(Map<K, V> var0) {
        TreeMap var1 = new TreeMap(Collator.getInstance(Locale.CHINA));
        var1.putAll(var0);
        return var1;
    }

    public static <K, V> Map<K, V> sortV(final Map<K, V> var0) {
        TreeMap var1 = new TreeMap(new Comparator<K>() {
            public int compare(K var1, K var2) {
                Object var3 = var0.get(var1);
                Object var4 = var0.get(var2);
                Collator var5 = Collator.getInstance(Locale.CHINA);
                int var6 = var5.compare(var3, var4);
                return var6 != 0 ? var6 : 1;
            }
        });
        var1.putAll(var0);
        return var1;
    }

}
