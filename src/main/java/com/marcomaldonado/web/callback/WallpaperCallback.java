package com.marcomaldonado.web.callback;

import entities.Tag;
import entities.Wallpaper;

/**
 * Created by Mxrck on 05/12/15.
 */
public interface WallpaperCallback extends Callback {

    void onSuccess(Wallpaper[] walppaers, Tag[] tags);

}
