package de.alphahelix.alphalibary.storage.sql2;

public class SQLInformation {

    private String userName = "root", host = "localhost", databaseName = "database (mysql) or ./plugins/AlphaLibary/basicSQLite.db (sqlite)", password = "password";
    private int port = 3306;

    public SQLInformation(String userName, String host, String databaseName, String password, int port) {
        this.userName = userName;
        this.host = host;
        this.databaseName = databaseName;
        this.password = password;
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public String getHost() {
        return host;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "SQLInformation{" +
                "userName='" + userName + '\'' +
                ", host='" + host + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                '}';
    }
}
