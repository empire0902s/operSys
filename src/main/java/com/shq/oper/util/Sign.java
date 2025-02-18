package com.shq.oper.util;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: ltg
 * @date: Created in 11:50 2018/7/2
 */
public class Sign {
    public static String[] a = new String[]{"timestamp", "username", "sysCode"};

    public Sign() {
    }

    public static <T> String sign(Map<String, T> var0, Object... var1) {
        var0 = Collections.sort(var0);
        StringBuilder var2 = new StringBuilder();
        Iterator var3 = var0.entrySet().iterator();

        while(true) {
            while(true) {
                Object var5;
                do {
                    if (!var3.hasNext()) {
                        return Md5.encrypt(new Object[]{var2.append(JsonUtils.toJson(var1)).toString()});
                    }

                    Map.Entry var4 = (Map.Entry)var3.next();
                    var5 = var4.getValue();
                } while(null == var5);

                if (var5.getClass().isArray()) {
                    Object[] var10 = (Object[])((Object[])var5);
                    int var11 = var10.length;

                    for(int var8 = 0; var8 < var11; ++var8) {
                        Object var9 = var10[var8];
                        var2.append(String.valueOf(var9));
                    }
                } else if (var5 instanceof Collection) {
                    Iterator var6 = ((Collection)var5).iterator();

                    while(var6.hasNext()) {
                        Object var7 = var6.next();
                        var2.append(String.valueOf(var7));
                    }
                } else {
                    var2.append(var5);
                }
            }
        }
    }



}
