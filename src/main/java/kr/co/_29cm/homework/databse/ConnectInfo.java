package kr.co._29cm.homework.databse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectInfo {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
