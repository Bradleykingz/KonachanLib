KonachanLib
====================================================================

A Simple [Konachan](http://konachan.com) API consumer for java


Usage
----------------------------

Simple usage, instance with true for wallpapers SFW or false if you don't care about that.

```java
int page = 1;
int limit = 1;

Konachan konachan = new Konachan(true); // SFW
Wallpaper[] wallpapers = konachan.posts( page, limit );
for( Wallpaper wallpaper : wallpapers ) {
  System.out.println(wallpaper.getJpeg_url());
}
```

You can also use an asynchronous call, with a callback

```java
konachan.posts(page, limit, new WallpaperCallback() {
            public void onSuccess(Wallpaper[] wallpapers, Tag[] tags) {
                // Your code when the petition success
                // For this method tags always will be null
                for( Wallpaper wallpaper : wallpapers ) {
                  System.out.println(wallpaper.getJpeg_url());
                }
            }

            public void onStart() { 
                // Before start the petition
            }

            public void onFailure(int error, String message) {
                // On some Error
            }
});
```

Search
----------------------------

Search by tags

```java
Konachan konachan = new Konachan(false);
        konachan.search(page, limit, "suzumiya haruhi", new WallpaperCallback() {
            public void onSuccess(Wallpaper[] wallpapers, Tag[] tags) {
                // Your code when the petition success
                for( Wallpaper wallpaper : wallpapers ) {
                  System.out.println(wallpaper.getJpeg_url());
                }
                // For this method tags will be related tags to the search
                // example, you search by "haruhi" a related tag will be "suzumiya_haruhi"
                for( Tag tag : tags ) {
                  System.out.println(tag.getName());
                }
            }

            public void onStart() { 
                // Before start the petition
            }

            public void onFailure(int error, String message) {
                // On some Error
            }
});
```

Getting related tags using synchronous version

```java
Konachan konachan = new Konachan(true); // SFW
Tag[] tags = konachan.tags( "suzumiya haruhi", page, limit );
for( Tag tag : tags ) {
  System.out.println(tag.getName());
}
```

Wallpaper methods
----------------------------
String getAuthor
int getCreated_at
long getFile_size
getFile_url
getHeight
getId
getJpeg_file_size
getJpeg_height
getJpeg_url
getJpeg_width
getPreview_height
getPreview_url
getPreview_width
getRating
getSample_file_size
getSample_height
getSample_url
getSample_width
getScore
getSource
getStatus
getTags
getWidth
