package adab.esy.es.laughwithus;

/**
 * Created by Alia on 6/30/2015.
 */
public class Type {

    public int id;
    public String name;
    public static final String TAG_ID = "type_id";
    public static final String TAG_NAME = "name";

    public Type(int id,String name){

        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
