package osu.serverlist.Cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import commons.marcandreher.Cache.Action.DatabaseAction;
import commons.marcandreher.Commons.Flogger;
import osu.serverlist.Sites.Models.HeatMapStat;
import osu.serverlist.Sites.Models.HeatMapStats;
import osu.serverlist.Sites.Models.Stats;

public class RefreshHeatmap extends DatabaseAction {
    private final String SERVER_SQL = "SELECT * FROM `un_servers`";
    private final String HEATMAP_STAT_SQL = "SELECT * FROM `un_crawler` WHERE `srv_id` = ?";
    private final String HEATMAP_STAT_GET_SQL = "SELECT * FROM `un_crawler` WHERE `srv_id` = ? AND `date` = ?";

    public static HashMap<Integer, HeatMapStat> cacheItems = new HashMap<>();

    @Override
    public void executeAction(Flogger logger) {
        super.executeAction(logger);
        try {
            ResultSet serverResultSet = mysql.Query(SERVER_SQL);

            while (serverResultSet.next()) {

                HeatMapStat stat = new HeatMapStat();
                int serverId = serverResultSet.getInt("id");

                HashMap<String, HeatMapStats> dateItems = new HashMap<>();

                ResultSet heatmapResultSet = mysql.Query(HEATMAP_STAT_SQL, String.valueOf(serverId));
                while (heatmapResultSet.next()) {
                    dateItems.put(heatmapResultSet.getString("date"), null);
                }

                for (Map.Entry<String, HeatMapStats> entry : dateItems.entrySet()) {
                    HeatMapStats heatMapStats = new HeatMapStats();
                    Stats stats = new Stats();
                    ResultSet heatmapGetResultSet = mysql.Query(HEATMAP_STAT_GET_SQL, String.valueOf(serverId),
                            entry.getKey());
                    while (heatmapGetResultSet.next()) {
                        switch (heatmapGetResultSet.getString("type")) {
                            case "PLAYERCHECK":
                                stats.setplayers(heatmapGetResultSet.getInt("value"));
                                break;

                            case "PLAYS":
                                stats.setplays(heatmapGetResultSet.getInt("value"));
                                break;

                            case "MAPS":
                                stats.setmaps(heatmapGetResultSet.getInt("value"));
                                break;

                            case "CLANS":
                                stats.setCLANS(heatmapGetResultSet.getInt("value"));
                                break;

                            case "REGISTERED_PLAYERS":
                                stats.setregistered(heatmapGetResultSet.getInt("value"));
                                break;

                            case "BANNED_PLAYERS":
                                stats.setbanned(heatmapGetResultSet.getInt("value"));
                                break;
                        }

                    }
                    heatMapStats.stats.add(stats);
                    dateItems.put(entry.getKey(), heatMapStats);
                }

                stat.dateItems = dateItems;
                cacheItems.put(serverResultSet.getInt("id"), stat);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
