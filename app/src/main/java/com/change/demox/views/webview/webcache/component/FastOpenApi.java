package com.change.demox.views.webview.webcache.component;


import com.change.demox.views.webview.webcache.component.config.CacheConfig;
import com.change.demox.views.webview.webcache.component.config.FastCacheMode;
import com.change.demox.views.webview.webcache.component.offline.ResourceInterceptor;

/**
 * Created by Ryan
 * at 2019/11/1
 */
public interface FastOpenApi {

    void setCacheMode(FastCacheMode mode, CacheConfig cacheConfig);

    void addResourceInterceptor(ResourceInterceptor interceptor);
}
