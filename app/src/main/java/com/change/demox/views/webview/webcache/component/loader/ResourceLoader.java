package com.change.demox.views.webview.webcache.component.loader;

import com.change.demox.views.webview.webcache.component.WebResource;

/**
 * Created by Ryan
 * 2018/2/7 下午7:53
 */
public interface ResourceLoader {

    WebResource getResource(SourceRequest request);

}



