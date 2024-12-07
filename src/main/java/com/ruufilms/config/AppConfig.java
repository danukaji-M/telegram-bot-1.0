package com.ruufilms.config;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public enum AppConfig {
    INSTANCE;

    private final Logger log = LoggerFactory.getLogger(AppConfig.class);
    public final Properties properties = new Properties();

    // Constructor loads properties
    AppConfig() {
        try {
            loadProperties();
        } catch (IOException e) {
            log.error("Failed to load configuration.", e);
            throw new RuntimeException("Failed to load configuration.", e);
        }
    }

    // Load properties from custom path
    private void loadProperties() throws IOException {

        Dotenv env = Dotenv.load();

        properties.put("ADMIN_BOT_API_KEY", env.get("ADMIN_BOT_API_KEY"));
        properties.put("FILM_BOT_API_KEY", env.get("FILM_BOT_API_KEY"));
        properties.put("TV_SERIES_BOT_API_KEY", env.get("TV_SERIES_BOT_API_KEY"));
        properties.put("FILM_CLIENT_BOT_API_KEY", env.get("FILM_CLIENT_BOT_API_KEY"));
        properties.put("TV_SERIES_CLIENT_API_KEY", env.get("TV_SERIES_CLIENT_API_KEY"));
        properties.put("SECURITY_BOT_API", env.get("SECURITY_BOT_API"));
        properties.put("USERS_HANDLE_BOT_API", env.get("USERS_HANDLE_BOT_API"));
        properties.put("DEBUG", env.get("DEBUG"));
        properties.put("LOG_LEVEL", env.get("LOG_LEVEL"));
        properties.put("ENABLE_ERROR_TRACKING", env.get("ENABLE_ERROR_TRACKING"));
        properties.put("ERROR_LOG_FILE", env.get("ERROR_LOG_FILE"));
        properties.put("AUTO_REPLY", env.get("AUTO_REPLY"));
        properties.put("MAX_RESPONSE_TIME", env.get("MAX_RESPONSE_TIME"));
        properties.put("ENABLE_ANTI_SPAM", env.get("ENABLE_ANTI_SPAM"));
        properties.put("MAX_CONCURRENT_REQUESTS", env.get("MAX_CONCURRENT_REQUESTS"));
        properties.put("DB_HOST", env.get("DB_HOST"));
        properties.put("DB_USER", env.get("DB_USER"));
        properties.put("DB_PASSWORD", env.get("DB_PASSWORD"));
        properties.put("REDIS_HOST", env.get("REDIS_HOST"));
        properties.put("REDIS_PORT", env.get("REDIS_PORT"));
        properties.put("REDIS_PASSWORD", env.get("REDIS_PASSWORD"));
        properties.put("TELEGRAM_LOCAL_SERVER_HOST", env.get("TELEGRAM_LOCAL_SERVER_HOST"));
        properties.put("TELEGRAM_LOCAL_SERVER_PORT", env.get("TELEGRAM_LOCAL_SERVER_PORT"));
        properties.put("PROXY_HOST", env.get("PROXY_HOST"));
        properties.put("PROXY_PORT", env.get("PROXY_PORT"));
        properties.put("PROXY_USER", env.get("PROXY_USER"));
        properties.put("PROXY_PASSWORD", env.get("PROXY_PASSWORD"));
        properties.put("ENABLE_TORRENT_FEATURE", env.get("ENABLE_TORRENT_FEATURE"));
        properties.put("ENABLE_MAGNET_FEATURE", env.get("ENABLE_MAGNET_FEATURE"));
        properties.put("ENABLE_SEARCH_FEATURE", env.get("ENABLE_SEARCH_FEATURE"));
        properties.put("ENABLE_BOOK_FEATURES", env.get("ENABLE_BOOK_FEATURES"));
        properties.put("ENABLE_WEB_API", env.get("ENABLE_WEB_API"));
        properties.put("AUTOMATIC_REACT", env.get("AUTOMATIC_REACT"));
        properties.put("AUTO_DELETE_MESSAGE", env.get("AUTO_DELETE_MESSAGE"));
        properties.put("SEARCH_ENGINE", env.get("SEARCH_ENGINE"));
        properties.put("SEARCH_RESULT_LIMIT", env.get("SEARCH_RESULT_LIMIT"));
        properties.put("SEARCH_CACHE_EXPIRY", env.get("SEARCH_CACHE_EXPIRY"));
        properties.put("ENABLE_WILDCARD_SEARCH", env.get("ENABLE_WILDCARD_SEARCH"));
        properties.put("SEARCH_LANGUAGE_PREFERENCE", env.get("SEARCH_LANGUAGE_PREFERENCE"));
        properties.put("FILM_CACHE_EXPIRY", env.get("FILM_CACHE_EXPIRY"));
        properties.put("CATEGORY_CACHE_EXPIRY", env.get("CATEGORY_CACHE_EXPIRY"));
        properties.put("CACHE_TYPE", env.get("CACHE_TYPE"));
        properties.put("MAX_SEARCH_QUERIES_PER_DAY", env.get("MAX_SEARCH_QUERIES_PER_DAY"));
        properties.put("PREMIUM_USER_REQUIRED", env.get("PREMIUM_USER_REQUIRED"));
        properties.put("ENABLE_USER_REGISTRATION", env.get("ENABLE_USER_REGISTRATION"));
        properties.put("USER_REGISTRATION_LIMIT", env.get("USER_REGISTRATION_LIMIT"));
        properties.put("ENABLE_NOTIFICATIONS", env.get("ENABLE_NOTIFICATIONS"));
        properties.put("NOTIFY_ADMIN_ON_NEW_USER", env.get("NOTIFY_ADMIN_ON_NEW_USER"));
        properties.put("NOTIFY_ADMIN_ON_ERROR", env.get("NOTIFY_ADMIN_ON_ERROR"));
        properties.put("ALERT_CHANNEL_ID", env.get("ALERT_CHANNEL_ID"));
        properties.put("MAX_FILE_SIZE_MB", env.get("MAX_FILE_SIZE_MB"));
        properties.put("ENABLE_FILE_COMPRESSION", env.get("ENABLE_FILE_COMPRESSION"));
        properties.put("ENABLE_RATING_FEATURE", env.get("ENABLE_RATING_FEATURE"));
        properties.put("ENABLE_META_FETCHING", env.get("ENABLE_META_FETCHING"));
        properties.put("ENABLE_FILM_SUGGESTION", env.get("ENABLE_FILM_SUGGESTION"));
        properties.put("ENABLE_DOWNLOAD_QUEUE", env.get("ENABLE_DOWNLOAD_QUEUE"));
        properties.put("ENABLE_TORRENT_FETCHING", env.get("ENABLE_TORRENT_FETCHING"));
        properties.put("ENABLE_DAILY_RECOMMENDATION", env.get("ENABLE_DAILY_RECOMMENDATION"));
        properties.put("MOVIE_DB_API_KEY", env.get("MOVIE_DB_API_KEY"));
        properties.put("IMDB_API_KEY", env.get("IMDB_API_KEY"));
        properties.put("OMDB_API_KEY", env.get("OMDB_API_KEY"));
        properties.put("TMDB_API_KEY", env.get("TMDB_API_KEY"));
        properties.put("TORRENT_API_KEY", env.get("TORRENT_API_KEY"));
        properties.put("GOOGLE_BACKUP_API_KEY", env.get("GOOGLE_BACKUP_API_KEY"));
        properties.put("GOOGLE_BACKUP_FOLDER_ID", env.get("GOOGLE_BACKUP_FOLDER_ID"));
        properties.put("OWNER_TELEGRAM_CONTACT_SIGNATURE", env.get("OWNER_TELEGRAM_CONTACT_SIGNATURE"));
        properties.put("FILM_DOWNLOAD_FOLDER", env.get("FILM_DOWNLOAD_FOLDER"));
        properties.put("BOOK_DOWNLOAD_FOLDER", env.get("BOOK_DOWNLOAD_FOLDER"));
        properties.put("TELEGRAM_LOCAL_SERVER_INSTALLATION_PATH", env.get("TELEGRAM_LOCAL_SERVER_INSTALLATION_PATH"));
        properties.put("UNIVERSAL_PATH", env.get("UNIVERSAL_PATH"));
        properties.put("ENABLE_ZIP_FEATURE",env.get("ENABLE_ZIP_FEATURE"));
        properties.put("ZIP_PASSWORD",env.get("ZIP_PASSWORD"));
        properties.put("ACCOUNT_ADMINISTRAION_BOT_API",env.get("ACCOUNT_ADMINISTRAION_BOT_API"));
        properties.put("ACCOUNT_AUTO_REPLY",env.get("ACCOUNT_AUTO_REPLY"));
        properties.put("AACCOUNT_CREATE_GROUP",env.get("ACCOUNT_CREATE_GROUP"));
        properties.put("ACCOUNT_CREATE_CHANNEL",env.get("ACCOUNT_CREATE_CHANNEL"));
        properties.put("ACCOUNT_CONTROL_OVER_ADMINS",env.get("ACCOUNT_CONTROL_OVER_ADMINS"));
        properties.put("ENV_EDIT_ADMIN",env.get("ENV_EDIT_ADMIN"));
        properties.put("ENV_EDIT_SUPER_ADMIN",env.get("ENV_EDIT_SUPER_ADMIN"));
        properties.put("TELEGRAM_PAYMENT",env.get("TELEGRAM_PAYMENT"));
        properties.put("CRYPTO_PAYMENT",env.get("CRYPTO_PAYMENT"));
        properties.put("ACCOUNT_NUMBER",env.get("ACCOUNT_NUMBER"));
        properties.put("TWO_STEP_PASSWORD",env.get("TWO_STEP_PASSWORD"));
        properties.put("APP_ID",env.get("APP_ID"));
        properties.put("API_HASH",env.get("API_HASH"));
        properties.put("FLASK_URL",env.get("FLASK_URL"));

        log.info("Configuration properties loaded successfully.");
    }

    public static class Config {

        final Properties properties;

        public Config(Properties properties) {
            this.properties = properties;
        }

        public String get(String key) {
            return properties.getProperty(key);
        }

        public int getInt(String key) {
            return Integer.parseInt(properties.getProperty(key));
        }

        public boolean getBoolean(String key) {
            return Boolean.parseBoolean(properties.getProperty(key));
        }

        public String getFlaskUrl(){
            return get("FLASK_URL");
        }

        public String getFilmBotApiKey() {
            return get("FILM_BOT_API_KEY");
        }

        public String getDbHost() {
            return get("DB_HOST");
        }

        public String getRedisHost() {
            return get("REDIS_HOST");
        }

        public String getAdminBotApiKey() {
            return get("ADMIN_BOT_API_KEY");
        }

        public String getTvSeriesBotApiKey() {
            return get("TV_SERIES_BOT_API_KEY");
        }

        public String getFilmClientBotApiKey() {
            return get("FILM_CLIENT_BOT_API_KEY");
        }

        public String getTvSeriesClientApiKey() {
            return get("TV_SERIES_CLIENT_API_KEY");
        }

        public String getSecurityBotApi() {
            return get("SECURITY_BOT_API");
        }

        public String getUsersHandleBotApi() {
            return get("USERS_HANDLE_BOT_API");
        }

        public boolean isDebugEnabled() {
            return getBoolean("DEBUG");
        }

        public String getLogLevel() {
            return get("LOG_LEVEL");
        }

        public boolean isErrorTrackingEnabled() {
            return getBoolean("ENABLE_ERROR_TRACKING");
        }

        public String getErrorLogFile() {
            return get("ERROR_LOG_FILE");
        }

        public boolean isAutoReplyEnabled() {
            return getBoolean("AUTO_REPLY");
        }

        public int getMaxResponseTime() {
            return getInt("MAX_RESPONSE_TIME");
        }

        public boolean isAntiSpamEnabled() {
            return getBoolean("ENABLE_ANTI_SPAM");
        }

        public int getMaxConcurrentRequests() {
            return getInt("MAX_CONCURRENT_REQUESTS");
        }

        public String getDbUser() {
            return get("DB_USER");
        }

        public String getDbPassword() {
            return get("DB_PASSWORD");
        }

        public String getRedisPort() {
            return get("REDIS_PORT");
        }

        public String getRedisPassword() {
            return get("REDIS_PASSWORD");
        }

        public String getTelegramLocalServerHost() {
            return get("TELEGRAM_LOCAL_SERVER_HOST");
        }

        public String getTelegramLocalServerPort() {
            return get("TELEGRAM_LOCAL_SERVER_PORT");
        }

        public String getProxyHost() {
            return get("PROXY_HOST");
        }

        public String getProxyPort() {
            return get("PROXY_PORT");
        }

        public String getProxyUser() {
            return get("PROXY_USER");
        }

        public String getProxyPassword() {
            return get("PROXY_PASSWORD");
        }

        public boolean isTorrentFeatureEnabled() {
            return getBoolean("ENABLE_TORRENT_FEATURE");
        }

        public boolean isZipFeatureEnabled(){
            return getBoolean("ENABLE_ZIP_FEATURE");
        }

        public String getZipPassword(){
            return get("ZIP_PASSWORD");
        }

        public boolean isMagnerFeatureEnabled() {
            return getBoolean("ENABLE_MAGNET_FEATURE");
        }

        public boolean isSearchFeatureEnabled() {
            return getBoolean("ENABLE_SEARCH_FEATURE");
        }

        public boolean isBookFeatureEnabled() {
            return getBoolean("ENABLE_BOOK_FEATURES");
        }

        public boolean isWebApiFeatureEnabled() {
            return getBoolean("ENABLE_WEB_API");
        }

        public boolean isAutomaticReactEnabled() {
            return getBoolean("AUTOMATIC_REACT");
        }

        public boolean isAutoDeleteMessageEnabled() {
            return getBoolean("AUTO_DELETE_MESSAGE");
        }

        public String getSearchEngine() {
            return get("SEARCH_ENGINE");
        }

        public int getSearchResultLimit() {
            return getInt("SEARCH_RESULT_LIMIT");
        }

        public int getSearchCacheExpiry() {
            return getInt("SEARCH_CACHE_EXPIRY");
        }

        public boolean isWildcardSearchEnabled() {
            return getBoolean("ENABLE_WILDCARD_SEARCH");
        }

        public String getSearchLanguagePreference() {
            return get("SEARCH_LANGUAGE_PREFERENCE");
        }

        public int getFilmCacheExpiry() {
            return getInt("FILM_CACHE_EXPIRY");
        }

        public int getCategoryCacheExpiry() {
            return getInt("CATEGORY_CACHE_EXPIRY");
        }

        public String getCacheType() {
            return get("CACHE_TYPE");
        }

        public int getMaxSearchQueriesPerDay() {
            return getInt("MAX_SEARCH_QUERIES_PER_DAY");
        }

        public boolean isPremiumUserRequired() {
            return getBoolean("PREMIUM_USER_REQUIRED");
        }

        public boolean isUserRegistrationEnabled() {
            return getBoolean("ENABLE_USER_REGISTRATION");
        }

        public int getUserRegistrationLimit() {
            return getInt("USER_REGISTRATION_LIMIT");
        }

        public boolean isNotificationsEnabled() {
            return getBoolean("ENABLE_NOTIFICATIONS");
        }

        public boolean isNotifyAdminOnNewUser() {
            return getBoolean("NOTIFY_ADMIN_ON_NEW_USER");
        }

        public boolean isNotifyAdminOnError() {
            return getBoolean("NOTIFY_ADMIN_ON_ERROR");
        }

        public String getAlertChannelId() {
            return get("ALERT_CHANNEL_ID");
        }

        public int getMaxFileSizeMb() {
            return getInt("MAX_FILE_SIZE_MB");
        }

        public boolean isFileCompressionEnabled() {
            return getBoolean("ENABLE_FILE_COMPRESSION");
        }

        public boolean isRatingFeatureEnabled() {
            return getBoolean("ENABLE_RATING_FEATURE");
        }

        public boolean isMetaFetchingEnabled() {
            return getBoolean("ENABLE_META_FETCHING");
        }

        public boolean isFilmSuggestionEnabled() {
            return getBoolean("ENABLE_FILM_SUGGESTION");
        }

        public boolean isDownloadQueueEnabled() {
            return getBoolean("ENABLE_DOWNLOAD_QUEUE");
        }

        public boolean isTorrentFetchingEnabled() {
            return getBoolean("ENABLE_TORRENT_FETCHING");
        }

        public boolean isDailyRecommendationEnabled() {
            return getBoolean("ENABLE_DAILY_RECOMMENDATION");
        }

        public String getMovieDbApiKey() {
            return get("MOVIE_DB_API_KEY");
        }

        public String getImdbApiKey() {
            return get("IMDB_API_KEY");
        }

        public String getOmdbApiKey() {
            return get("OMDB_API_KEY");
        }

        public String getTmdbApiKey() {
            return get("TMDB_API_KEY");
        }

        public String getTorrentApiKey() {
            return get("TORRENT_API_KEY");
        }

        public String getGoogleBackupApiKey() {
            return get("GOOGLE_BACKUP_API_KEY");
        }

        public String getGoogleBackupFolderId() {
            return get("GOOGLE_BACKUP_FOLDER_ID");
        }

        public String getOwnerTelegramContactSignature() {
            return get("OWNER_TELEGRAM_CONTACT_SIGNATURE");
        }

        public String getFilmDownloadFolder() {
            return get("FILM_DOWNLOAD_FOLDER");
        }

        public String getBookDownloadFolder() {
            return get("BOOK_DOWNLOAD_FOLDER");
        }

        public String getTelegramLocalServerInstallationPath() { return get("TELEGRAM_LOCAL_SERVER_INSTALLATION_PATH"); }

        public String getUniversalPath() {
            return get("UNIVERSAL_PATH");
        }

        public String getAccountAdminBotApiKey() {
            return get("ACCOUNT_ADMINISTRAION_BOT_API");
        }

        public boolean isAutoReplyEnabledAccount() {
            return getBoolean("ACCOUNT_AUTO_REPLY");
        }

        public boolean isCreateGroupEnabled() {
            return getBoolean("ACCOUNT_CREATE_GROUP");
        }

        public boolean isCreateChannelEnabled() {
            return getBoolean("ACCOUNT_CREATE_CHANNEL");
        }

        public boolean isControlOverAdminsEnabled() {
            return getBoolean("ACCOUNT_CONTROL_OVER_ADMINS");
        }

        public boolean isEnvEditAdminEnabled() {
            return getBoolean("ENV_EDIT_ADMIN");
        }

        public boolean isEnvEditSuperAdminEnabled() {
            return getBoolean("ENV_EDIT_SUPER_ADMIN");
        }

        public boolean isTelegramPaymentEnabled() {
            return getBoolean("TELEGRAM_PAYMENT");
        }

        public boolean isCryptoPaymentEnabled() {
            return getBoolean("CRYPTO_PAYMENT");
        }

        public String getAccountNumber() {
            return get("ACCOUNT_NUMBER");
        }

        public String getTwoStepPassword() {
            return get("TWO_STEP_PASSWORD");
        }

        public String getAppId() {
            return get("APP_ID");
        }

        public String getApiHash() {
            return get("API_HASH");
        }
    }
}
