package service;

import com.google.gson.Gson;
import com.marcomaldonado.web.callback.WallpaperCallback;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import entities.Wallpaper;
import entities.Tag;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;

/**
 * Created by Mxrck on 22/11/2015.
 */
public class Konachan {

    private Client client;
    private WebResource postsResource;
    private WebResource tagsResource;
    private boolean safeForWork = false;
    private final String postsUrl = "http://konachan.com/post.json";
    private final String tagsUrl = "http://konachan.com/tag.json";
    private int limitRelatedTags = 5;

    public Konachan(boolean safeForWork) {
        client = Client.create();
        this.safeForWork = safeForWork;
        this.postsResource = client.resource(postsUrl);
        this.tagsResource = client.resource(tagsUrl);
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
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("limit", limit+"");
        queryParams.add("page", page+"");
        if (search != null) {
            queryParams.add("tags", this.cleanTag(search));
        }
        String response = this.postsResource.queryParams(queryParams).get(String.class);
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
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("order", "count");
        queryParams.add("page", page+"");
        queryParams.add("limit", limit+"");
        queryParams.add("name", this.cleanTag(tagname));
        String response = this.tagsResource.queryParams(queryParams).get(String.class);
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
}