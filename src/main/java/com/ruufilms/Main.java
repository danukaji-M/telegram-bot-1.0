/**
 * @author Danukaji
 */
package com.ruufilms;

import com.ruufilms.bot.AdminBot;
import com.ruufilms.bot.FilmBot;
import com.ruufilms.bot.TvSeriesBot;
import com.ruufilms.migration.Migration;
import com.ruufilms.services.FileHandle;
import io.github.cdimascio.dotenv.Dotenv;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(FilmBot.class);
        try{
            FileHandle file = new FileHandle();
            file.CrateDownloadFolders();

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            //initialized env reader
            Dotenv dotenv = Dotenv.load();

            //Made Thread for Database migration
            Runnable migration = Migration::new;

            Thread thread = new Thread(migration);
            Thread thread1 = getThread1(dotenv, botsApi, logger);
            Thread thread2 = getThread2(dotenv, botsApi, logger);
            Thread thread3 = getThread3(dotenv, botsApi,logger);

            //Thread Start Area
            thread.start();
            thread1.start();
            thread2.start();
            thread3.start();
        }catch (TelegramApiException telegramApiException){
            System.out.println("Telegram Api Exception was detected ...");
        }
    }


    @Contract("_, _, _ -> new")
    private static Thread getThread1(Dotenv dotenv, TelegramBotsApi botsApi, Logger logger) {
        Runnable fBotThread = ()->{
            DefaultBotOptions option = new DefaultBotOptions();
            option.setBaseUrl(dotenv.get("TELEGRAM_LOCAL_SERVER_HOST"));
            LongPollingBot bot = new FilmBot(option ,dotenv.get("FILM_BOT_API_KEY"));
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
    private static Thread getThread2(Dotenv dotenv, TelegramBotsApi botsApi, Logger logger) {
        Runnable tBotThread = ()->{
            DefaultBotOptions options = new DefaultBotOptions();
            options.setBaseUrl(dotenv.get("TELEGRAM_LOCAL_SERVER_HOST"));
            LongPollingBot bot = new TvSeriesBot(options, dotenv.get("TV_SERIES_BOT_API_KEY"));
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
    private static Thread getThread3(Dotenv dotenv, TelegramBotsApi botsApi, Logger logger){
        Runnable thread = ()->{
            LongPollingBot bot = new AdminBot(dotenv.get("ADMIN_BOT_API_KEY"));
            try{
                botsApi.registerBot(bot);
                logger.info("Admin Bot started successfully.");
            }catch (TelegramApiException e){
                logger.error("Admin Bot Error Occurred during Registering bot", e);
            }
        };
        return new Thread(thread);
    }
}