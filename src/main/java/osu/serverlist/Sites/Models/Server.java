package osu.serverlist.Sites.Models;

import java.util.ArrayList;

public class Server {

    private int id;
    private String name;
    private String url;
    private String logo_loc;
    private String created;

    private int votes;
    private int players;

    private String safe_name;

    private String safe_categories;

    private String timestamp;
    private ArrayList<String> list_categories;

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getList_categories() {
        return this.list_categories;
    }

    public void setList_categories(ArrayList<String> list_categories) {
        this.list_categories = list_categories;
    }


    public String getSafe_categories() {
        return this.safe_categories;
    }

    public void setSafe_categories(String safe_categories) {
        this.safe_categories = safe_categories;
    }

    



    public String getSafe_name() {
        return this.safe_name;
    }

    public void setSafe_name(String safe_name) {
        this.safe_name = safe_name;
    }


    public int getVotes() {
        return this.votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }



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

    public String getLogo_loc() {
        return this.logo_loc;
    }

    public void setLogo_loc(String logo_loc) {
        this.logo_loc = logo_loc;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getPlayers() {
        return this.players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

}