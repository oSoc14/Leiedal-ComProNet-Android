package osoc.leiedal.android.aandacht.View.model.apiAccess;

/**
 * Access the backend API
 */
public interface iAPIAccess {

    /**
     * Checks if the user login is correct
     * @param user username
     * @param pass password
     * @return true if correct, false if not correct
     */
    public boolean login(String user,String pass);

}
