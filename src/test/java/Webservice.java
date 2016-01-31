import com.marcomaldonado.web.callback.WallpaperCallback;
import com.marcomaldonado.konachan.entities.Tag;
import com.marcomaldonado.konachan.entities.Wallpaper;
import com.marcomaldonado.konachan.service.Konachan;

import java.io.IOException;

/**
 * Created by Mxrck on 22/11/2015.
 */
public class Webservice {

    public static void main(String[] args) throws Exception {
        final Konachan konachan = new Konachan(false);
        konachan.posts(1, 50 ,new WallpaperCallback() {
            public void onSuccess(Wallpaper[] wallpapers, Tag[] tags) {
                System.out.println("Wallpaper urls");
                for (Wallpaper wallpaper : wallpapers) {
                    System.out.println(wallpaper.getJpeg_url());
                    konachan.saveWallpaper(wallpaper.getId()+".jpg", "D:\\wallpapers\\", wallpaper.getFile_url());
                    if (tags != null) {
                        System.out.println();
                        System.out.println("Related Tags");
                        for (Tag tag :
                                tags) {
                            System.out.println(tag.getName());
                        }
                    }
                }
            }

            public void onStart() {
                System.out.println("Starting");
            }

            public void onFailure(int error, String message) {
                System.out.println(message);
            }
        }).join();
    }

}
