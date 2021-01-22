package com.change.demox.views.webview.webcache.component.loader;


import com.change.demox.views.webview.webcache.component.offline.CacheRequest;

import java.util.Map;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public class SourceRequest {

    private String url;
    private boolean cacheAble;
    private Map<String, String> headers;
    private String userAgent;
    private int webViewCache;

    public SourceRequest(CacheRequest request, boolean cacheAble) {
        this.cacheAble = cacheAble;
        this.url = request.getUrl();
        this.headers = request.getHeaders();
        this.userAgent = request.getUserAgent();
        this.webViewCache = request.getWebViewCacheMode();
    }

    public String getUrl() {
        return url;
    }

    public boolean isCacheable() {
        return cacheAble;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getWebViewCache() {
        return webViewCache;
    }
}
