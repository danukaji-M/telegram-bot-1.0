/**
 * @author Danukaji
 */
package com.ruufilms;

import com.ruufilms.bot.FilmBot;
import com.ruufilms.migration.Migration;
import com.ruufilms.services.FileHandle;
import io.github.cdimascio.dotenv.Dotenv;
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

            //FilmBotThread
            Runnable fBotThread = ()->{
                DefaultBotOptions option = new DefaultBotOptions();
                option.setBaseUrl(dotenv.get("TELEGRAM_LOCAL_SERVER_HOST"));
                LongPollingBot bot = new FilmBot(option ,dotenv.get("FILM_BOT_API_KEY"));
                try {
                    botsApi.registerBot(bot);
                    logger.info("Bot started successfully.");
                } catch (TelegramApiException e) {
                    logger.error("Error occurred during execution.", e);
                }
            };

            Thread fthread = new Thread(fBotThread);

            //Thread Start Area
            //thread.start();
            fthread.start();
        }catch (TelegramApiException telegramApiException){
            System.out.println("Telegram Api Exception was detected ...");
        }
    }
}