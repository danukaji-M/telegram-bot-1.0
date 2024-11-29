package com.ruufilms.bot;

import com.ruufilms.config.AppConfig;
import com.ruufilms.services.FileHandle;
import com.ruufilms.services.TorrentService;
import org.slf4j.Logger;
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
import java.util.concurrent.TimeUnit;

public class UploadBot extends TelegramLongPollingBot {

    private Logger logger;
    private static UploadBot instance;

    static AppConfig.Config config;
    String botToken;

    private UploadBot(DefaultBotOptions option) {
        super(option);
    }

    // Static method for getting the single instance
    public static UploadBot getInstance(DefaultBotOptions options) {
        if (instance == null) {
            synchronized (UploadBot.class) {
                if (instance == null) {
                    instance = new UploadBot(options);
                }
            }
        }
        return instance;
    }

    // Initialization method
    public void initialize(String botToken, AppConfig.Config config, Logger logger) {
        this.botToken = botToken;
        this.config = config;
        this.logger = logger;
        assert config.isDebugEnabled() : "Bot successfully initialized";
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
            FileHandle fileHandle = new FileHandle();
            fileHandle.setUser_id(update.getMessage().getChatId().toString());
            fileHandle.createDownloadFolder();
            try {
                Document doc = update.getMessage().getDocument();
                GetFile getFile = new GetFile();
                getFile.setFileId(doc.getFileId());

                File file = execute(getFile);
                String filePath = file.getFilePath();
                String torFilePath = config.getTelegramLocalServerInstallationPath() + config.getUniversalPath() + this.botToken + "/" + filePath;

                logger.info("Document file path: {}", torFilePath);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                FileHandle finalFileHandle = fileHandle;
                final Object lock = new Object();
                executor.submit(() -> {
                    synchronized (lock) {
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
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.warn("Executor did not terminate in the expected time. Forcing shutdown...");
                    executor.shutdownNow();
                }

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
