package com.marcomaldonado.web.callback;

import com.marcomaldonado.konachan.entities.Tag;
import com.marcomaldonado.konachan.entities.Wallpaper;

/**
 * Created by Mxrck on 05/12/15.
 */
public interface DownloadCallback extends Callback {

    void onSuccess(String route);

}
