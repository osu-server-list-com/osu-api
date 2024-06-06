package osu.serverlist.Cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import commons.marcandreher.Cache.Action.DatabaseAction;
import commons.marcandreher.Commons.Flogger;
import osu.serverlist.Sites.Models.HeatMapStat;
import osu.serverlist.Sites.Models.HeatMapStats;
import osu.serverlist.Sites.Models.Stats;

public class RefreshHeatmap extends DatabaseAction {
    private static final String SERVER_SQL = "SELECT * FROM `un_servers` WHERE `visible` = 1";
    private static final String HEATMAP_STAT_SQL = "SELECT `date`, `type`, `value` FROM `un_crawler` WHERE `srv_id` = ?";
    private static final String HEATMAP_STAT_VOTE_SQL = "SELECT COUNT(`id`) AS `votes` FROM `un_votes` WHERE votetime = ? AND `srv_id` = ?";

    public static ConcurrentHashMap<Integer, HeatMapStat> cacheItems = new ConcurrentHashMap<>();

    @Override
    public void executeAction(Flogger logger) {
        super.executeAction(logger);
        try {
            ResultSet serverResultSet = mysql.Query(SERVER_SQL);

            while (serverResultSet.next()) {
                HeatMapStat stat = new HeatMapStat();
                int serverId = serverResultSet.getInt("id");

                ConcurrentHashMap<String, HeatMapStats> dateItems = new ConcurrentHashMap<>();

                ResultSet heatmapResultSet = mysql.Query(HEATMAP_STAT_SQL, String.valueOf(serverId));
                while (heatmapResultSet.next()) {
                    String date = heatmapResultSet.getString("date");
                    HeatMapStats heatMapStats = dateItems.get(date);
                    if (heatMapStats == null) {
                        heatMapStats = new HeatMapStats();
                        dateItems.put(date, heatMapStats);
                    }
                    String type = heatmapResultSet.getString("type");
                    int value = heatmapResultSet.getInt("value");

                    Stats stats = heatMapStats.stats;
                    if (stats == null) {
                        stats = new Stats();
                        heatMapStats.stats = stats;
                    }

                    switch (type) {
                        case "PLAYERCHECK":
                            stats.setplayers(value);
                            break;
                        case "PLAYS":
                            stats.setplays(value);
                            break;
                        case "MAPS":
                            stats.setmaps(value);
                            break;
                        case "CLANS":
                            stats.setCLANS(value);
                            break;
                        case "REGISTERED_PLAYERS":
                            stats.setregistered(value);
                            break;
                        case "BANNED_PLAYERS":
                            stats.setbanned(value);
                            break;
                    }
                }

                for (Map.Entry<String, HeatMapStats> entry : dateItems.entrySet()) {
                    String date = entry.getKey();
                    HeatMapStats heatMapStats = entry.getValue();
                    Stats stats = heatMapStats.stats;

                    if (stats == null) {
                        stats = new Stats();
                        heatMapStats.stats = stats;
                    }

                    ResultSet voteResultSet = mysql.Query(HEATMAP_STAT_VOTE_SQL, date, String.valueOf(serverId));
                    if (voteResultSet.next()) {
                        stats.votes = voteResultSet.getInt("votes");
                    }

                    int score = (stats.votes * 2) + stats.getplayers();
                    stats.setScore(score);
                }

                stat.dateItems = dateItems;
                cacheItems.put(serverId, stat);
            }

        } catch (SQLException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
