package com.mapbox.rctmgl.components.styles;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.rctmgl.utils.DownloadMapImageTask;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nickitaliano on 9/12/17.
 */

public class RCTMGLStyle {
    private ReadableMap mReactStyle;
    private MapboxMap mMap;

    public RCTMGLStyle(@NonNull ReadableMap reactStyle, @NonNull MapboxMap map) {
        mReactStyle = reactStyle;
        mMap = map;
    }

    public List<String> getAllStyleKeys() {
        if (mReactStyle == null) {
            return new ArrayList<>();
        }

        ReadableMapKeySetIterator it = mReactStyle.keySetIterator();
        List<String> keys = new ArrayList<>();

        while (it.hasNextKey()) {
            String key = it.nextKey();

            if (!key.equals("__MAPBOX_STYLESHEET__")) {
                keys.add(key);
            }
        }

        return keys;
    }

    public RCTMGLStyleValue getStyleValueForKey(String styleKey) {
        ReadableMap styleValueConfig = mReactStyle.getMap(styleKey);

        if (styleValueConfig == null) {
            // TODO: throw exeception here
            return null;
        }

        return new RCTMGLStyleValue(styleValueConfig);
    }

    public void addImage(String uriStr) {
        if (!shouldAddImage(uriStr)) {
            return;
        }
        Map.Entry[] images = new Map.Entry[]{ new AbstractMap.SimpleEntry(uriStr, uriStr) };
        DownloadMapImageTask task = new DownloadMapImageTask(mMap, null);
        task.execute(images);
    }

    private boolean shouldAddImage(String uriStr) {
        return uriStr != null && isValidURI(uriStr);
    }

    private boolean isValidURI(String str) {
        Uri uri = Uri.parse(str);
        return UriUtil.isLocalAssetUri(uri) || UriUtil.isNetworkUri(uri);
    }
}
