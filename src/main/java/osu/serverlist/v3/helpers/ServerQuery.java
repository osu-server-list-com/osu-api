package osu.serverlist.v3.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import commons.marcandreher.Commons.Flogger;
import commons.marcandreher.Commons.MySQL;
import commons.marcandreher.Commons.Flogger.Prefix;
import commons.marcandreher.Utils.TimestampConverter;
import osu.serverlist.Cache.CategoriesCh;
import osu.serverlist.Cache.RefreshHeatmap;
import osu.serverlist.Sites.Models.ServerHelper;
import osu.serverlist.Sites.Models.Stats;
import osu.serverlist.v3.models.CategorieModel;
import osu.serverlist.v3.models.ServerModel;

public class ServerQuery {

    private final String SQL_QUERY = "SELECT `id`,`name`,`players`,`votes`,`expired`,`url`,`logo_loc`,`created`,`online`,`categories`,`banner_url`,`discord_url` FROM un_servers WHERE `id` = ?";

    public ServerModel queryServer(int id, MySQL mysql) throws SQLException {
        ResultSet serverResultSet = mysql.Query(SQL_QUERY, String.valueOf(id));
        if (serverResultSet.next()) {
            return buildServerModel(serverResultSet);
        }
        return null;
    }

    public class ServerLite {
        public String name;
        public int id;
    }

    public ServerLite[] queryServerLite(MySQL mysql, ResultSet serverResultSet) throws SQLException {
        List<ServerLite> servers = new ArrayList<>();
        while (serverResultSet.next()) {
            ServerLite server = new ServerLite();
            server.id = serverResultSet.getInt("id");
            server.name = serverResultSet.getString("name");
            servers.add(server);
        }
        return servers.toArray(ServerLite[]::new);
    }

    public ServerModel[] queryServer(MySQL mysql, ResultSet serverResultSet) throws SQLException {
        List<ServerModel> servers = new ArrayList<>();
        while (serverResultSet.next()) {
            servers.add(buildServerModel(serverResultSet));
        }
        return servers.toArray(ServerModel[]::new);
    }

    private ServerModel buildServerModel(ResultSet serverResultSet) throws SQLException {
        ServerModel server = new ServerModel();
        int id = serverResultSet.getInt("id");
        server.setName(serverResultSet.getString("name"));
        server.setId(id);
        server.setUrl(serverResultSet.getString("url"));
        server.setSafe_name(server.getName().toLowerCase().replaceAll(" ", ""));
        server.setSafe_categories(serverResultSet.getString("categories"));
        server.setOnline(serverResultSet.getBoolean("online"));

        HashMap<String, String> createdMap = new HashMap<>();
        createdMap.put("converted", TimestampConverter.getDiffString(serverResultSet.getString("created")));
        createdMap.put("raw", serverResultSet.getString("created"));
        server.setCreated(createdMap);

        HashMap<String, Integer> statMap = new HashMap<>();
        statMap.put("expired", serverResultSet.getInt("expired"));
        statMap.put("votes", serverResultSet.getInt("votes"));
        statMap.put("players", serverResultSet.getInt("players"));

        try {
            Stats moreStats = RefreshHeatmap.cacheItems.get(id).dateItems
                    .get(ServerHelper.getDate()).stats;

            if (moreStats != null) {
                statMap.put("score", moreStats.getScore());
                statMap.put("maps", moreStats.getmaps());
                statMap.put("plays", moreStats.getplays());
                statMap.put("clans", moreStats.getCLANS());
                statMap.put("registered", moreStats.getregistered());
            }
        } catch (Exception e) {
            Flogger.instance.log(Prefix.WARNING, "Date not found for Server [" + id + "] Date [" + ServerHelper.getDate() + "]", id);
        }

        server.setStats(statMap);

        HashMap<String, String> customizationMap = new HashMap<>();
        customizationMap.put("logo", ServerHelper.repairLogo(serverResultSet.getString("logo_loc")));

        if (serverResultSet.getString("banner_url") != null)
            customizationMap.put("banner", ServerHelper.repairBanner(serverResultSet.getString("banner_url")));

        if (serverResultSet.getString("discord_url") != null)
            customizationMap.put("discord", serverResultSet.getString("discord_url"));

        server.setCustomizations(customizationMap);


        List<CategorieModel> categories = new ArrayList<>();

        
        for (String categorieStr : server.getSafe_categories().split(",")) {
            CategorieModel model = new CategorieModel();

            int catId = Integer.parseInt(categorieStr);

            if (CategoriesCh.catMap.get(catId) == null)
                continue;

            model.setId(catId);
            model.setName(CategoriesCh.catMap.get(catId).getName());
            categories.add(model);
           
        }
        server.setCategories(categories);

        return server;
    }
}
