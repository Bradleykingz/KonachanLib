import entities.Wallpaper;
import service.Konachan;

/**
 * Created by Mxrck on 22/11/2015.
 */
public class Webservice {

    public static void main(String[] args) {
        Konachan konachan = new Konachan(true);
        Wallpaper[] wallpapers = konachan.posts();
        for (Wallpaper wallpaper: wallpapers) {
            System.out.println(wallpaper.getFile_url());
        }
    }

}
