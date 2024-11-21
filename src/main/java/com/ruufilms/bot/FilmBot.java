package com.ruufilms.bot;

import com.ruufilms.config.AppConfig;
import com.ruufilms.services.FileHandle;
import com.ruufilms.services.TorrentService;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FilmBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(FilmBot.class);
    static AppConfig.Config config = new AppConfig.Config(AppConfig.INSTANCE.properties);
    Dotenv dotenv = Dotenv.load();
    String botToken;
    public FilmBot(DefaultBotOptions option,String botToken){
        super(option, botToken);
        this.botToken =botToken;

        assert config.isDebugEnabled() : "Start Bot Success Fully";
    }
    @Override
    public String getBotUsername() {
        return "Ruu-Films";
    }

    @Override
    public void onUpdateReceived(Update update) {
        assert config.isDebugEnabled() : "Update Message Incoming";
        logger.info("Received an update : {}",update);
        System.out.println(update);
        if(update.getMessage().hasDocument()) {
            logger.info("Document received in update.");
            FileHandle fileHandle;
            try {
                Document doc = update.getMessage().getDocument();
                GetFile getFile = new GetFile();
                getFile.setFileId(doc.getFileId());

                File file = execute(getFile);
                String filePath = file.getFilePath();
                String torFilePath = dotenv.get("TELEGRAM_LOCAL_SERVER_INSTALLATION_PATH") + dotenv.get("UNIVERSAL_PATH") + this.botToken + "/" + filePath;

                logger.info("Document file path: {}", torFilePath);

                fileHandle = new FileHandle();
                ExecutorService executor = Executors.newSingleThreadExecutor();
                FileHandle finalFileHandle = fileHandle;

                executor.submit(() -> {
                    synchronized (this){
                        try {
                            logger.info("Starting TorrentService with file: {}", torFilePath);
                            new TorrentService(torFilePath, String.valueOf(finalFileHandle.getFilmDownloadFolder()));
                            logger.info("End Torrent Download.");
                        } catch (Throwable e) {
                            logger.error("Error during torrent service execution", e);
                        }
                    }
                });

                executor.shutdown();

                FileHandle newUpFiles = new FileHandle();
                newUpFiles.DeleteFiles(new java.io.File(torFilePath));
                logger.info("Number of files to upload: {}", newUpFiles.getUploadFilePath().size());

                for (String upFile : newUpFiles.getUploadFilePath()) {
                    logger.info("Uploading file: {}", upFile);

                    SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(update.getMessage().getChatId());
                    sendDocument.setDocument(new InputFile(new java.io.File(upFile)));
                    try {
                        execute(sendDocument);
                        logger.info("File uploaded successfully: {}", upFile);
                    } catch (TelegramApiException e) {
                        logger.error("Failed to upload file: {}", upFile, e);
                    }
                }
                fileHandle.deleteFolder();
            } catch (Throwable e) {
                logger.error("Error while handling document", e);
            }
        }
    }
}
