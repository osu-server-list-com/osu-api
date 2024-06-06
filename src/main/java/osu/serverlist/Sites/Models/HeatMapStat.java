package osu.serverlist.Sites.Models;

import java.util.concurrent.ConcurrentHashMap;

public class HeatMapStat {
    public ConcurrentHashMap<String, HeatMapStats> dateItems = new ConcurrentHashMap<>();
}
