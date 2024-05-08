package osu.serverlist.Cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import commons.marcandreher.Cache.Action.DatabaseAction;
import commons.marcandreher.Commons.Flogger;

public class ClientKeys extends DatabaseAction {

    private final String KEY_SQL = "SELECT * FROM `un_clients`";

    public static ArrayList<String> keys = new ArrayList<>();

    @Override
    public void executeAction(Flogger logger) {
        ArrayList<String> keys = new ArrayList<>();
        super.executeAction(logger);

        ResultSet keyResultSet = mysql.Query(KEY_SQL);
        try {
            while (keyResultSet.next()) {
                keys.add(keyResultSet.getString("key"));
            }

            ClientKeys.keys = keys;
        } catch (SQLException e) {
            logger.error(e);
        }
    }
    
}
