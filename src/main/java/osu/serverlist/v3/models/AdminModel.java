package osu.serverlist.v3.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminModel {
    private List<String> last_voters = new ArrayList<>();
    private HashMap<String, Integer> stats_today = new HashMap<>();
    private HashMap<String, Integer> stats_last14days = new HashMap<>();
    private HashMap<String, Integer> stats_total = new HashMap<>();


    public List<String> getLast_voters() {
        return this.last_voters;
    }

    public void setLast_voters(List<String> last_voters) {
        this.last_voters = last_voters;
    }

    public HashMap<String,Integer> getStats_today() {
        return this.stats_today;
    }

    public void setStats_today(HashMap<String,Integer> stats_today) {
        this.stats_today = stats_today;
    }

    public HashMap<String,Integer> getStats_last14days() {
        return this.stats_last14days;
    }

    public void setStats_last14days(HashMap<String,Integer> stats_last14days) {
        this.stats_last14days = stats_last14days;
    }

    public HashMap<String,Integer> getStats_total() {
        return this.stats_total;
    }

    public void setStats_total(HashMap<String,Integer> stats_total) {
        this.stats_total = stats_total;
    }

    
}
