package adab.esy.es.laughwithus;

/**
 * Created by Alia on 6/30/2015.
 */
public class Video {
    public String name;
    public String link;
    public static final String TAG_NAME="name";
    public static final String TAG_LINK = "link";

    public Video(String name,String link){
        this.name = name;
        this.link = link;
    }

    @Override
    public String toString() {
        return name;
    }
}
