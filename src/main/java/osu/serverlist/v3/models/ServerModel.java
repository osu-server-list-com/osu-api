package osu.serverlist.v3.models;

import java.util.HashMap;

public class ServerModel {

    private int id;
    private String name;
    private String url;

    private String safe_name;

    private String safe_categories;

    private boolean online;

    private HashMap<Integer, String> categories;
    private HashMap<String, Integer> stats;
    private HashMap<String, String> created;
    private HashMap<String, String> customizations = new HashMap<>();


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSafe_name() {
        return this.safe_name;
    }

    public void setSafe_name(String safe_name) {
        this.safe_name = safe_name;
    }

    public String getSafe_categories() {
        return this.safe_categories;
    }

    public void setSafe_categories(String safe_categories) {
        this.safe_categories = safe_categories;
    }

    public boolean isOnline() {
        return this.online;
    }

    public boolean getOnline() {
        return this.online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public HashMap<Integer,String> getCategories() {
        return this.categories;
    }

    public void setCategories(HashMap<Integer,String> categories) {
        this.categories = categories;
    }

    public HashMap<String,Integer> getStats() {
        return this.stats;
    }

    public void setStats(HashMap<String,Integer> stats) {
        this.stats = stats;
    }

    public HashMap<String,String> getCreated() {
        return this.created;
    }

    public void setCreated(HashMap<String,String> created) {
        this.created = created;
    }

    public HashMap<String,String> getCustomizations() {
        return this.customizations;
    }

    public void setCustomizations(HashMap<String,String> customizations) {
        this.customizations = customizations;
    }


}