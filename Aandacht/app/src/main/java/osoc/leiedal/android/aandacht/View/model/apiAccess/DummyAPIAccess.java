package osoc.leiedal.android.aandacht.View.model.apiAccess;

/**
 * Dummy API access for development purposes
 */
public class DummyAPIAccess implements iAPIAccess {

    /* ============================================================================================
        STATIC MEMBERS
    ============================================================================================ */

    //singleton
    private static iAPIAccess api = null;

    /* ============================================================================================
        STATIC METHODS
    ============================================================================================ */

    @Override
    public boolean login(String user, String pass) {
        return user.equals("user") && pass.equals("pass");
    }


    public static iAPIAccess getInstance(){
        if (api == null){
            api = new DummyAPIAccess();
        }
        return api;
    }
}
