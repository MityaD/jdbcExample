public enum Config {

    HOST ("localhost"),
    PORT ("5432"),
    DBNAME ("postgres"),
    DB_USER ("postgres"),
    DB_PASS ("1989g13a");

    private String configDB;

    Config(String configDB) {
        this.configDB = configDB;
    }

    public String getConfigDB() {
        return configDB;
    }

    @Override
    public String toString() {
        return "Config{" +
                "configDB='" + configDB + '\'' +
                '}';

    }
}
