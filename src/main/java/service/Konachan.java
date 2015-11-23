package service;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import entities.Wallpaper;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;

/**
 * Created by Mxrck on 22/11/2015.
 */
public class Konachan {

    private Client client;
    private WebResource webResource;
    private boolean safeForWork = false;
    private final String baseUrl = "http://konachan.com/post.json";

    public Konachan(boolean safeForWork) {
        client = Client.create();
        this.safeForWork = safeForWork;
        this.webResource = client.resource(baseUrl);
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
        return this.posts(page, limit, null);
    }

    private Wallpaper[] posts(int page, int limit, String search)
    {
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("limit", limit+"");
        queryParams.add("page", page+"");
        if (search != null) {
            queryParams.add("tags", search);
        }
        String response = this.webResource.queryParams(queryParams).get(String.class);
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

    public boolean isSafeForWork() {
        return safeForWork;
    }

    public void setSafeForWork(boolean safeForWork) {
        this.safeForWork = safeForWork;
    }
}
