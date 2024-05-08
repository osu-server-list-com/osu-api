package osu.serverlist.Sites.Models;

public class ClientServer {

    private int id;
    private String name;
    private String safe_name;
    private String url;
    private String devserver;

    private String image;
    private int players;
    private int votes;

    private String timestamp;

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

    public String getSafe_name() {
        return this.safe_name;
    }

    public void setSafe_name(String safe_name) {
        this.safe_name = safe_name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDevserver() {
        return this.devserver;
    }

    public void setDevserver(String devserver) {
        this.devserver = devserver;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPlayers() {
        return this.players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getVotes() {
        return this.votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
}
