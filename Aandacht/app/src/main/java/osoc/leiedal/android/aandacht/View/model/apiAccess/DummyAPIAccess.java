package osoc.leiedal.android.aandacht.View.model.apiAccess;

/**
 * Dummy API access for development purposes
 */
public class DummyAPIAccess implements iAPIAccess {

    //singleton
    private static iAPIAccess api = null;


    @Override
    public boolean login(String user, String pass) {
        return "hoi".equals(user) && "hoi".equals(pass);
    }


    public static iAPIAccess getInstance(){
        if (api == null){
            api = new DummyAPIAccess();
        }
        return api;
    }
}
