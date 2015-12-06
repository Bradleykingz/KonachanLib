package com.marcomaldonado.konachan.entities;

import java.io.Serializable;

/**
 * Created by Mxrck on 19/11/2015.
 */
public class Wallpaper implements Serializable {

    private Integer id;
    private String tags;
    private Integer created_at;
    private Integer creator_id;
    private String author;
    private Integer change;
    private String source;
    private Integer score;
    private String md5;
    private long file_size;
    private String file_url;
    private boolean is_shown_in_index;
    private String preview_url;
    private Integer preview_width;
    private Integer preview_height;
    private Integer actual_preview_width;
    private Integer actual_preview_height;
    private String sample_url;
    private Integer sample_width;
    private Integer sample_height;
    private long sample_file_size;
    private String jpeg_url;
    private Integer jpeg_width;
    private Integer jpeg_height;
    private long jpeg_file_size;
    private String rating;
    private boolean has_children;
    private Integer parent_id;
    private String status;
    private Integer width;
    private Integer height;
    private boolean is_held;
    private String frames_pending_string;
    private String frames_string;

    public Integer getId() {
        return id;
    }

    public String[] getTags() {
        String [] tagsArray = new String[0];
        if (tags != null) {
            tagsArray = tags.split(" ");
        }
        return tagsArray;

    }

    public Integer getCreated_at() {
        return created_at;
    }

    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    public Integer getScore() {
        return score;
    }

    public long getFile_size() {
        return file_size;
    }

    public String getFile_url() {
        return file_url;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public Integer getPreview_width() {
        return preview_width;
    }

    public Integer getPreview_height() {
        return preview_height;
    }

    public String getSample_url() {
        return sample_url;
    }

    public Integer getSample_width() {
        return sample_width;
    }

    public Integer getSample_height() {
        return sample_height;
    }

    public long getSample_file_size() {
        return sample_file_size;
    }

    public String getJpeg_url() {
        return jpeg_url;
    }

    public Integer getJpeg_width() {
        return jpeg_width;
    }

    public Integer getJpeg_height() {
        return jpeg_height;
    }

    public long getJpeg_file_size() {
        return jpeg_file_size;
    }

    public String getRating() {
        return rating;
    }

    public String getStatus() {
        return status;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
