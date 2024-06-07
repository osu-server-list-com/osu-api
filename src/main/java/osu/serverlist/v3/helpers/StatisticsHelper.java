package osu.serverlist.v3.helpers;

import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;
import commons.marcandreher.Commons.Flogger;
import commons.marcandreher.Commons.MySQL;
import osu.serverlist.v3.models.ServerStat;

public class StatisticsHelper {

    private final MySQL mysql;
    private final Flogger logger;

    public StatisticsHelper(MySQL mysql, Flogger logger) {
        this.mysql = mysql;
        this.logger = logger;
    }

    private static final String TOTAL_STAT_SQL = "SELECT SUM(CASE WHEN `location` = 'SERVER_SITE_CLICKS' THEN `clicks` ELSE 0 END) AS `server_site_clicks_total`, SUM(CASE WHEN `location` = 'VOTE_SITE_CLICKS' THEN `clicks` ELSE 0 END) AS `vote_site_clicks_total`, SUM(CASE WHEN `location` = 'PLAY_CLICKS' THEN `clicks` ELSE 0 END) AS `play_clicks_total`, SUM(CASE WHEN `location` = 'DISCORD_JOINS' THEN `clicks` ELSE 0 END) AS `discord_joins_total` FROM `un_analytics` WHERE `srv_id` = ?";
    private static final String TOTAL_STAT_DATE_SQL = TOTAL_STAT_SQL + " AND `date` = CURDATE();";
    private static final String TOTAL_STAT_YESTER_DATE_SQL = TOTAL_STAT_SQL + " AND DATE(`date`) = CURDATE() - INTERVAL 1 DAY";
    private static final String TOTAL_STAT_DATE_LAST_14_DAYS_SQL = TOTAL_STAT_SQL + " AND `date` >= DATE_SUB(CURDATE(), INTERVAL 14 DAY);";

    public ConcurrentHashMap<Integer, ServerStat> getAllStatistics() {
        ConcurrentHashMap<Integer, ServerStat> hashMap = new ConcurrentHashMap<>();
        try {
            ResultSet serverResultSet = mysql.Query("SELECT * FROM `un_servers`");
            while (serverResultSet.next()) {
                int serverId = serverResultSet.getInt("id");
                ServerStat stat = new ServerStat();

                populateStatistics(stat, TOTAL_STAT_SQL, serverId, this::setTotalStatistics);
                populateStatistics(stat, TOTAL_STAT_DATE_SQL, serverId, this::setTodayStatistics);
                populateStatistics(stat, TOTAL_STAT_YESTER_DATE_SQL, serverId, this::setYesterdayStatistics);
                populateStatistics(stat, TOTAL_STAT_DATE_LAST_14_DAYS_SQL, serverId, this::setLast14DaysStatistics);

                hashMap.put(serverId, stat);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return hashMap;
    }

    public ServerStat getStatisticsForServer(int serverId) {
        ServerStat stat = new ServerStat();
        try {
            populateStatistics(stat, TOTAL_STAT_SQL, serverId, this::setTotalStatistics);
            populateStatistics(stat, TOTAL_STAT_DATE_SQL, serverId, this::setTodayStatistics);
            populateStatistics(stat, TOTAL_STAT_YESTER_DATE_SQL, serverId, this::setYesterdayStatistics);
            populateStatistics(stat, TOTAL_STAT_DATE_LAST_14_DAYS_SQL, serverId, this::setLast14DaysStatistics);
        } catch (Exception e) {
            logger.error(e);
        }
        return stat;
    }

    private void populateStatistics(ServerStat stat, String query, int serverId, StatisticSetter setter) throws Exception {
        ResultSet resultSet = mysql.Query(query, String.valueOf(serverId));
        while (resultSet.next()) {
            setter.setStatistics(stat, resultSet);
        }
    }

    private void setTotalStatistics(ServerStat stat, ResultSet resultSet) throws Exception {
        stat.setTotalServerSiteClicks(resultSet.getInt("server_site_clicks_total"));
        stat.setTotalServerVoteSiteClicks(resultSet.getInt("vote_site_clicks_total"));
        stat.setTotaldiscordServerLinkClicks(resultSet.getInt("discord_joins_total"));
        stat.setTotalOsuServerLinkClicks(resultSet.getInt("play_clicks_total"));
    }

    private void setTodayStatistics(ServerStat stat, ResultSet resultSet) throws Exception {
        stat.setTodayServerSiteClicks(resultSet.getInt("server_site_clicks_total"));
        stat.setTodayServerVoteSiteClicks(resultSet.getInt("vote_site_clicks_total"));
        stat.setTodaydiscordServerLinkClicks(resultSet.getInt("discord_joins_total"));
        stat.setTodayOsuServerLinkClicks(resultSet.getInt("play_clicks_total"));
    }

    private void setYesterdayStatistics(ServerStat stat, ResultSet resultSet) throws Exception {
        stat.setYesterdayServerSiteClicks(resultSet.getInt("server_site_clicks_total"));
        stat.setYesterdayServerVoteSiteClicks(resultSet.getInt("vote_site_clicks_total"));
        stat.setYesterdaydiscordServerLinkClicks(resultSet.getInt("discord_joins_total"));
        stat.setYesterdayOsuServerLinkClicks(resultSet.getInt("play_clicks_total"));
    }

    private void setLast14DaysStatistics(ServerStat stat, ResultSet resultSet) throws Exception {
        stat.setLast14thDaysServerSiteClicks(resultSet.getInt("server_site_clicks_total"));
        stat.setLast14thDaysServerVoteSiteClicks(resultSet.getInt("vote_site_clicks_total"));
        stat.setLast14thDaysdiscordServerLinkClicks(resultSet.getInt("discord_joins_total"));
        stat.setLast14thDaysOsuServerLinkClicks(resultSet.getInt("play_clicks_total"));
    }

    @FunctionalInterface
    private interface StatisticSetter {
        void setStatistics(ServerStat stat, ResultSet resultSet) throws Exception;
    }
}
