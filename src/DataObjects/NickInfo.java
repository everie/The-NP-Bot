package DataObjects;

/**
 * Created by Hans on 26-07-2016.
 */
public class NickInfo {
    private String ircNick;
    private String lfmNick;
    private String authKey;
    private boolean isReg;

    public String getIrcNick() {
        return ircNick;
    }

    public void setIrcNick(String ircNick) {
        this.ircNick = ircNick;
    }

    public String getLfmNick() {
        return lfmNick;
    }

    public void setLfmNick(String lfmNick) {
        this.lfmNick = lfmNick;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public boolean getIsReg() {
        return isReg;
    }

    public void setisReg(boolean isReg) {
        this.isReg = isReg;
    }
}
