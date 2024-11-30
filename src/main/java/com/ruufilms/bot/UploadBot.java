package com.ruufilms.bot;

import com.ruufilms.Beans.Stickers;
import com.ruufilms.config.AppConfig;
import com.ruufilms.Beans.User;
import com.ruufilms.services.FileHandle;
import com.ruufilms.services.TorrentService;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;


public class UploadBot extends TelegramLongPollingBot {
    private boolean film = false;
    private boolean tvSeries = false;
    private boolean bseason = false;
    private boolean newSeries = false;
    private boolean oldSeries = false;

    private String season;
    private Logger logger;
    static AppConfig.Config config = new AppConfig.Config(AppConfig.INSTANCE.properties);
    String botToken;
    private User user;
    Stickers stickers;

    public UploadBot(DefaultBotOptions option, String botToken, Logger logger, Stickers stickers) {
        super(option,botToken);
        this.logger = logger;
        this.stickers = stickers;
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
        if(update.getMessage().getText().equals("/setFilm")){
            film = true;
            tvSeries = false;
        }else if(update.getMessage().getText().equals("/setSeries")){
            film = false;
            tvSeries = true;
        }
        if(update.getMessage().hasDocument() && film) {
            FileHandle fileHandle = new FileHandle(update.getMessage().getChatId().toString());
            logger.info("Document received in update.");
            fileHandle.createDownloadFolder();
            try {
                Document doc = update.getMessage().getDocument();
                GetFile getFile = new GetFile();
                getFile.setFileId(doc.getFileId());

                File file = execute(getFile);
                String filePath = file.getFilePath();
                String torFilePath = config.getTelegramLocalServerInstallationPath() + config.getUniversalPath() + config.getFilmBotApiKey() + "/" + filePath;

                logger.info("Document file path: {}", torFilePath);
                try {
                    logger.info("Starting TorrentService with file: {}", torFilePath);
                    // Initialize and execute the TorrentService
                    TorrentService torrentService = new TorrentService(torFilePath, String.valueOf(fileHandle.getFilmDownloadFolder()));
                    logger.info("Torrent download started successfully.");
                    // You can handle any post-download logic here
                    logger.info("End Torrent Download.");
                } catch (Throwable e) {
                    logger.error("Error during torrent service execution", e);
                }
                FileHandle newUpFiles = new FileHandle(update.getMessage().getChatId().toString());
                newUpFiles.DeleteFiles(new java.io.File(torFilePath));
                logger.info("Number of files to upload: {}", newUpFiles.getUploadFilePath().size());

                for (String upFile : newUpFiles.getUploadFilePath()) {
                    logger.info("Uploading file: {}", upFile);

                    SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(6335286775L);
                    sendDocument.setDocument(new InputFile(new java.io.File(upFile)));
                    try {
                        execute(sendDocument);
                        logger.info("File uploaded successfully: {}", upFile);
                    } catch (TelegramApiException e) {
                        logger.error("Failed to upload file: {}", upFile, e);
                    }
                }
//                fileHandle.deleteFolder();
            } catch (Throwable e) {
                logger.error("Error while handling document", e);
            }
        }
        if(tvSeries){
            if(update.getMessage().getText().startsWith("/season")){
                season = update.getMessage().getText().substring(7).trim();
                bseason = true;
            }
        }
        if(tvSeries && bseason){
            if(update.getMessage().getText().equals("/old")){
                oldSeries = true;
                newSeries = false;
            }else if (update.getMessage().getText().equals("/new")){
                oldSeries = false;
                newSeries = true;
            }
        }
        //
        if(tvSeries && update.getMessage().hasDocument() && (Integer.valueOf(season) > 0)){
            FileHandle fileHandle = new FileHandle(update.getMessage().getChatId().toString());
        }

        if(update.getMessage().getText().equals("/reset")){
            film = false;
            tvSeries = false;
            bseason = false;
            newSeries = false;
            oldSeries = false;
        }
    }
}
