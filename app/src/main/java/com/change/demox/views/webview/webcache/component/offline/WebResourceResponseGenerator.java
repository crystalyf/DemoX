package com.change.demox.views.webview.webcache.component.offline;

import android.webkit.WebResourceResponse;

import com.change.demox.views.webview.webcache.component.WebResource;


/**
 * Created by Ryan
 * at 2019/10/8
 */
public interface WebResourceResponseGenerator {

    WebResourceResponse generate(WebResource resource, String urlMime);

}
