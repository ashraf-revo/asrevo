package org.revo.tube.Util;

import com.comcast.viper.hlsparserj.tags.UnparsedTag;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Created by ashraf on 16/04/17.
 */
public class Util {

    public static String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String TOString(List<UnparsedTag> tags, Function<String, String> function) {
        StringBuilder builder = new StringBuilder();
        for (Iterator i$ = tags.iterator(); i$.hasNext(); builder.append("\n")) {
            UnparsedTag tag = (UnparsedTag) i$.next();
            builder.append(tag.getRawTag());
            if (tag.getURI() != null && tag.getTagName().equals("EXTINF"))
                builder.append("\n").append(function.apply(tag.getURI()));
        }
        return builder.toString();
    }

    public static String convertStringToHex(String str) {
        StringBuilder hex = new StringBuilder("0x");
        for (char c : str.toCharArray()) {
            hex.append(Integer.toHexString((int) c));
        }
        return hex.toString();
    }

    public static int comp(UnparsedTag s1, UnparsedTag s2) {
        String[] s1_ = s1.getURI().replace(".ts", "").split("_");
        String[] s2_ = s2.getURI().replace(".ts", "").split("_");
        if (s1_[0].compareTo(s2_[0]) != 0) return s1_[0].compareTo(s2_[0]);
        for (int i = 1; i < s1_.length; i++) {
            if (Integer.valueOf(s1_[i]).compareTo(Integer.valueOf(s2_[i])) != 0)
                return Integer.valueOf(s1_[i]).compareTo(Integer.valueOf(s2_[i]));
        }
        return 0;
    }
}
