package com.marcomaldonado.konachan.service;

import com.google.gson.Gson;
import com.marcomaldonado.web.callback.DownloadCallback;
import com.marcomaldonado.web.callback.WallpaperCallback;
import com.marcomaldonado.konachan.entities.Wallpaper;
import com.marcomaldonado.konachan.entities.Tag;
import com.marcomaldonado.web.tools.helpers.HTMLHelper;
import us.monoid.web.BinaryResource;
import us.monoid.web.Resty;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mxrck on 22/11/2015.
 */
public class Konachan {

    private Resty resty;
    private boolean safeForWork = false;
    private final String postsUrl = "http://konachan.com/post.json";
    private final String tagsUrl = "http://konachan.com/tag.json";
    private int limitRelatedTags = 5;
    private HashMap<String, Object> queryParams;
    private HTMLHelper htmlHelper = HTMLHelper.getInstance();

    public Konachan(boolean safeForWork) {
        queryParams = new HashMap<String, Object>();
        resty = new Resty();
        resty.identifyAsMozilla();
        this.safeForWork = safeForWork;
    }

    public Wallpaper[] search(String search)
    {
        return this.posts(1, 25, search);
    }

    public Wallpaper[] search(int page, int limit, String search)
    {
        return this.posts(page, limit, search);
    }

    public Wallpaper[] posts()
    {
        return this.posts(1, 25);
    }

    public Wallpaper[] posts(int limit) {
        return this.posts(1, limit);
    }


    public Wallpaper[] posts(int page, int limit)
    {
        return this.posts(page, limit, (String) null);
    }

    public Thread search(String search, WallpaperCallback callback)
    {
        return this.posts(1, 25, search, callback);
    }

    public Thread search(int page, int limit, String search, WallpaperCallback callback)
    {
        return this.posts(page, limit, search, callback);
    }

    public Thread posts(WallpaperCallback callback)
    {
        return this.posts(1, 25, null, callback);
    }

    public Thread posts(int limit, WallpaperCallback callback) {
        return this.posts(1, limit, null, callback);
    }


    public Thread posts(int page, int limit, WallpaperCallback callback)
    {
        return this.posts(page, limit, null, callback);
    }

    private Thread posts(final int page, final int limit, final String search, final WallpaperCallback callback)
    {
        final Konachan self = this;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    if (callback != null) {
                        callback.onStart();
                    }
                    Wallpaper[] wallpapers = self.posts(page, limit, search);
                    Tag[] tags = null;
                    if (search != null) {
                        tags = self.tags(search, 1, self.getLimitRelatedTags());
                    }
                    if (callback != null) {
                        callback.onSuccess(wallpapers, tags);
                    }
                }
                catch (Exception ex) {
                    if (callback != null) callback.onFailure(
                            KonachanErrors.GENERIC_ERROR,
                            KonachanErrors.message(KonachanErrors.GENERIC_ERROR)
                    );
                }
            }
        });
        thread.start();
        return thread;
    }

    private Wallpaper[] posts(int page, int limit, String search)
    {
        this.queryParams.put("limit", limit);
        this.queryParams.put("page", page);
        if (search != null) {
            this.queryParams.put("tags", this.cleanTag(search));
        }
        String response = "[]";
        try {
            response = this.resty.text(
                this.postsUrl+"?"+htmlHelper.urlEncodeUTF8(this.queryParams)
            ).toString();
        }
        catch (Exception ex) {}
        finally {
            queryParams.clear();
        }
        Gson gson = new Gson();
        Wallpaper[] wallpapers = gson.fromJson(response, Wallpaper[].class);

        if (this.safeForWork) {
            ArrayList<Wallpaper> wallpapersSafe = new ArrayList<Wallpaper>();
            for(Wallpaper wallpaper: wallpapers) {
                if (wallpaper.getRating().equalsIgnoreCase("s")) {
                    wallpapersSafe.add(wallpaper);
                }
            }
            wallpapers = wallpapersSafe.toArray(new Wallpaper[0]);
        }

        return wallpapers;
    }

    public Tag[] tags(String tagname, int page, int limit)
    {
        this.queryParams.put("order", "count");
        this.queryParams.put("limit", limit);
        this.queryParams.put("page", page);
        this.queryParams.put("name", this.cleanTag(tagname));
        String response = "[]";
        System.out.println(this.tagsUrl+"?"+htmlHelper.urlEncodeUTF8(this.queryParams));
        try {
            response = this.resty.text(
                    this.tagsUrl+"?"+htmlHelper.urlEncodeUTF8(this.queryParams)
            ).toString();
        }
        catch (Exception ex) {}
        finally {
            queryParams.clear();
        }
        Gson gson = new Gson();
        Tag[] tags = gson.fromJson(response, Tag[].class);
        return tags;
    }

    private String cleanTag(String tagname)
    {
        return tagname.toLowerCase().trim().replace(' ', '_');
    }

    public int getLimitRelatedTags()
    {
        return limitRelatedTags;
    }

    public void setLimitRelatedTags(int limitRelatedTags)
    {
        this.limitRelatedTags = limitRelatedTags;
    }

    public boolean isSafeForWork() {
        return safeForWork;
    }

    public void setSafeForWork(boolean safeForWork) {
        this.safeForWork = safeForWork;
    }

    public String saveWallpaper(String filename, String folderPath, String imageURL) {
        if (filename == null) {
            filename = imageURL.substring( imageURL.lastIndexOf('/')+1, imageURL.length() );
            String filenameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));
        }
        Resty downloader = new Resty();
        downloader.identifyAsMozilla();
        File imageFile = new File(folderPath+File.separator+filename);
        if (imageFile.exists()) return imageFile.getPath();
        BinaryResource binaryResource = null;
        try {
            binaryResource = downloader.bytes(imageURL);
            binaryResource.save(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return folderPath + File.separator + filename;
    }

    public Thread saveWallpaper(final String filename, final String folderPath, final String imageURL, final DownloadCallback callback)
    {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    if (callback != null) {
                        callback.onStart();
                    }
                    String save = saveWallpaper(filename, folderPath, imageURL);
                    if (save != null) {
                        if (callback != null) {
                            callback.onSuccess(save);
                        }
                    }
                    else {
                        if (callback != null) {
                            callback.onFailure(
                                    KonachanErrors.UNKNOW_ERROR,
                                    KonachanErrors.message(KonachanErrors.UNKNOW_ERROR
                                    )
                            );
                        }
                    }
                }
                catch (Exception ex) {
                    if (callback != null) {
                        callback.onFailure(
                                KonachanErrors.GENERIC_ERROR,
                                KonachanErrors.message(KonachanErrors.GENERIC_ERROR
                                )
                        );
                    }
                }
            }
        });
        thread.start();
        return thread;
    }

}