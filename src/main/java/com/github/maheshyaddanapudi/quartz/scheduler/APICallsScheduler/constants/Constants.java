package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.constants;

public class Constants {

    // General Constants
    public static final String NONE = "NONE";
    public static final String FALSE = "false";
    public static final String TRUE= "true";
    public static final String TEST = "test";
    public static final String STRING_INITIALIZR = "";
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String DEFAULT = "default";
    public static final String ZERO = "0";

    public static final String ROLES = "roles";
    public static final String RESOURCE_ACCESS = "resource_access";

    public static final String GENERIC_ROOT_URL = "/**";
    public static final String GENERIC_OAUTH_URL = "/oauth/**";
    public static final String GENERIC_API_URL = "/api/**";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String PATCH = "PATCH";
    public static final String DELETE = "DELETE";

    // Embedded MariaDB Constants
    public static final String MARIADB_ARGS_MAX_CONNECTIONS = "--max-connections=";
    public static final String MARIADB_ARGS_WAIT_TIMEOUT = "--wait-timeout=";
    public static final String MARIADB_ARGS_CONNECT_TIMEOUT = "--connect-timeout=";
    public static final String MARIADB4J_SPRING_SERVICE = "mariaDB4jSpringService";

    public static final String MARIADB_URL_EXTN_AUTO_RECONNECT = "autoReconnect=true";
    public static final String MARIADB_URL_EXTN_USE_MYSQL_METADATA = "useMysqlMetadata=true";

    public static final String DB = "db";
    public static final String MIGRATE = "migrate";
    public static final String MYSQL = "external-mysql";
    public static final String MARIADB4J = "embedded-mariadb4j";
    public static final String JDBC_URL = "jdbcUrl";
    public static final String JDBC_USERNAME = "username";
    public static final String JDBC_PASSWORD = "password";

    // Embedded OAuth2 Constants
    public static final String EMBEDDED_OAUTH2 = "embedded-oauth2";
    public static final String SECURITY = "security";

    public static final String resourceIds = "resourceIds";
    public static final String USER_ADMIN_RESOURCE = "USER_ADMIN_RESOURCE";
    public static final String userDetailsService = "userDetailsService";
    public static final String BAD_CREDENTIALS = "Bad Credentials";
    public static final String USER_NOT_FOUND = "User Not Found";
    public static final String authorizedGrantTypes = "authorization_code,password,refresh_token,implicit";
    public static final String OAUTH_SECUIRTY = "oauth.security";
    public static final String GRANT_TYPE = "grant_type";
    public static final String REFRESH_TOKEN = "refresh_token";

    // External OAUTH2 Constants
    public static final String EXTERNAL_OAUTH2 = "external-oauth2";

    // External ADFS Constants
    public static final String EXTERNAL_ADFS = "external-adfs";

    // PCF Constants

    public static final String CLOUD = "cloud";
    public static final String CF_MYSQL = "cf-mysql";


    public static final String OPTIONS = "OPTIONS";

    // Database Common Constants
    public static final String dataSource = "dataSource";
    public static final String DATASOURCE = "datasource";
    public static final String CACHE_PREP_STMTS = "cachePrepStmts";
    public static final String PREP_STMT_CACHE_SIZE = "prepStmtCacheSize";
    public static final String _256 = "256";
    public static final String PREP_STMT_CACHE_SQL_LIMIT = "prepStmtCacheSqlLimit";
    public static final String _2048 = "2048";
    public static final String USER_SERVER_PREP_STMTS = "useServerPrepStmts";
    public static final String UTC = "UTC";
    public static final String _100 = "100";
    public static final String _2 = "2";
    public static final String CONNECTION_INIT_SQL = "connectionInitSql";
    public static final String CONNECTION_INIT_SQL_VALUE = "set character_set_client = utf8mb4;";
    public static final String QUARTZ_HK_DS = "Quartz Hikari DataSource";
    public static final String utf8mb4_unicode_ci = "utf8mb4_unicode_ci";
    public static final String USE_LEGACY_DATETIME_CODE = "useLegacyDatetimeCode";
    public static final String SERVER_TIMEZONE = "serverTimezone";
    public static final String CONNECTION_COLLATION = "connectionCollation";
    public static final String USE_SSL = " useSSL";
    public static final String AUTO_RECONNECT = "autoReconnect";
    public static final String POOL_NAME = "poolName";
    public static final String MAX_POOL_SIZE = "maximumPoolSize";
    public static final String MIN_IDLE = "minimumIdle";
    public static final String MAX_LIFETIME = "maxLifetime";
    public static final String IDLE_TIMEOUT = "idleTimeout";
    public static final String DATASOURCE_PROPERTIES = "dataSourceProperties";
    public static final String DRIVER_CLASS_NAME = "driverClassName";

    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

    public static String JSON = "json";

    public static String SCHEDULE_MAPPING_ID = "SCHEDULE_MAPPING_ID";

    public static String SUCCESS = "SUCCESS";
    public static String FAILED = "FAILED";

    public static String SCHEDULE_API_EXECUTION_JOB = "Schedule API Execution Job";
    public static String API = "API";
    public static String API_TRIGGERS = "api-triggers";
    public static String SEND_API_TRIGGER = "Send API Trigger";
}
