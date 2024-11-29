/**
 * @author Danukaji
 */
package com.ruufilms;

import com.ruufilms.accountaccessing.AccountHandlingBot;
import com.ruufilms.bot.AdminBot;
import com.ruufilms.bot.UploadBot;
import com.ruufilms.bot.TvSeriesBot;
import com.ruufilms.config.AppConfig;
import com.ruufilms.migration.Migration;
import com.ruufilms.Beans.User;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    static AppConfig.Config config = new AppConfig.Config(AppConfig.INSTANCE.properties);
    User user = new User();
    public static void main(String[] args) {
        final Logger logger = LogManager.getLogger(UploadBot.class);
        try{
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            //initialized env reader
            Dotenv dotenv = Dotenv.load();

            //Made Thread for Database migration
            Runnable migration = Migration::new;

            Thread thread = new Thread(migration);
            Thread thread1 = getThread1(config, botsApi, logger);
            Thread thread2 = getThread2(config, botsApi, logger);
            Thread thread3 = getThread3(config, botsApi,logger);
            Thread thread4 = getThread4(config, botsApi,logger);

            //Thread Start Area
//            thread.start();
            thread1.start();
            thread2.start();
            thread3.start();
            thread4.start();
        }catch (TelegramApiException telegramApiException){
            System.out.println("Telegram Api Exception was detected ...");
        }
    }


    @Contract("_, _, _ -> new")
    private static Thread getThread1(AppConfig.Config config, TelegramBotsApi botsApi, Logger logger) {
        Runnable fBotThread = ()->{
            DefaultBotOptions option = new DefaultBotOptions();
            option.setBaseUrl(config.getTelegramLocalServerHost());
            UploadBot bot = new UploadBot(option,config.getFilmBotApiKey(),logger);
            try {
                botsApi.registerBot(bot);
                logger.info("Film Bot started successfully.");
            } catch (TelegramApiException e) {
                logger.error("Film Bot Error Occurred during Registering bot", e);
            }
        };
        return new Thread(fBotThread);
    }
    @Contract("_, _, _ -> new")
    private static Thread getThread2(AppConfig.Config config, TelegramBotsApi botsApi, Logger logger) {
        Runnable tBotThread = ()->{
            DefaultBotOptions options = new DefaultBotOptions();
            options.setBaseUrl(config.getTelegramLocalServerHost());
            LongPollingBot bot = new TvSeriesBot(options, config.getTvSeriesBotApiKey());
            try{
                botsApi.registerBot(bot);
                logger.info("Tv Series Bot started successfully.");
            }catch (TelegramApiException e){
                logger.error("Tv Series Bot Error Occurred during Registering bot",e);
            }
        };
        return new Thread(tBotThread);
    }
    @Contract("_, _, _ -> new")
    private static Thread getThread3(AppConfig.Config config, TelegramBotsApi botsApi, Logger logger){
        Runnable thread = ()->{
            LongPollingBot bot = new AdminBot(config.getAdminBotApiKey());
            try{
                botsApi.registerBot(bot);
                logger.info("Admin Bot started successfully.");
            }catch (TelegramApiException e){
                logger.error("Admin Bot Error Occurred during Registering bot", e);
            }
        };
        return new Thread(thread);
    }
    private static Thread getThread4(AppConfig.Config config, TelegramBotsApi botsApi, Logger logger){
        Runnable thread = ()->{
            LongPollingBot bot = new AccountHandlingBot(config.getAccountAdminBotApiKey());
            try{
                botsApi.registerBot(bot);
                logger.info("Account andling bot successfully started");
            }catch(TelegramApiException e){
                logger.error("Account Handling bot error occured during registration", e);
            }

        };
        return new Thread(thread);
    }
}