package com.change.demox.views.webview.webcache.component.offline;

import com.change.demox.views.webview.webcache.component.WebResource;

/**
 * Created by Ryan
 * at 2019/9/27
 */
public interface ResourceInterceptor {

    WebResource load(Chain chain);

}
