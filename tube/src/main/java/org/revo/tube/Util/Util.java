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
}
