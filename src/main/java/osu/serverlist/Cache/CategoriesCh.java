package osu.serverlist.Cache;

import java.sql.ResultSet;
import java.util.HashMap;

import commons.marcandreher.Cache.Action.DatabaseAction;
import commons.marcandreher.Commons.Flogger;
import osu.serverlist.Sites.Models.Categorie;

public class CategoriesCh extends DatabaseAction {

    public static HashMap<Integer, Categorie> catMap = new HashMap<>(); 

    @Override
    public void executeAction(Flogger logger) {
        super.executeAction(logger);
        
        try {
            ResultSet catResult = mysql.Query("SELECT * FROM `un_categories`");
            while (catResult.next()) {
                Categorie cat = new Categorie();
                cat.setColor(catResult.getString("color"));
                cat.setId(catResult.getInt("id"));
                cat.setName(catResult.getString("name"));
                CategoriesCh.catMap.put(catResult.getInt("id"), cat);
                
            }
       
        } catch (Exception e) {
            logger.error(e);
        }
    }
    
}
