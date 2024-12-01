package com.ruufilms.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruufilms.Beans.ChannelData;
import com.ruufilms.Beans.Stickers;
import com.ruufilms.Models.TvSeriesModel;
import com.ruufilms.accountaccessing.spring.FlaskController;
import com.ruufilms.config.AppConfig;
import com.ruufilms.Beans.User;
import com.ruufilms.services.FileHandle;
import com.ruufilms.services.FilmDetailsSearch;
import com.ruufilms.services.TorrentService;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UploadBot extends TelegramLongPollingBot {
    private boolean film = false;
    private boolean tvSeries = false;
    private boolean bseason = false;
    private boolean newSeries = false;
    private boolean oldSeries = false;
    private boolean newSeriesGroupCreate = false;
    private boolean start = false;
    private boolean setGroup = false;
    private boolean dataRecieve = false;
    private String season;
    private Logger logger;
    static AppConfig.Config config = new AppConfig.Config(AppConfig.INSTANCE.properties);
    String botToken;
    HashMap<String, User> user;
    Stickers stickers;
    String channelId;
    String channelName;
    String channelLink;
    String title;
    String year;
    String poster;
    String imdbRatings;
    String director;
    String country;
    String genres;
    String plot;
    Map<String, Object> details;
    public UploadBot(DefaultBotOptions option, String botToken, Logger logger, Stickers stickers, HashMap<String , User> user) {
        super(option,botToken);
        this.logger = logger;
        this.stickers = stickers;
        this.user = user;
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
        if(update.getMessage().getText().equals("/start")){
            start = true;
        }
        if(start){
            //set film for the upload a film
            if(update.getMessage().getText().equals("/setfilm")){
                film = true;
                tvSeries = false;
                //set Series for the upload TvSeries
            }else if(update.getMessage().getText().equals("/setseries")){
                film = false;
                tvSeries = true;
                sendMessage(update.getMessage().getChatId().toString(),"Send Your Tv Series Season : /season number");
            }

            //set film for the upload film
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

            //set tv series season
            if(tvSeries){
                if(update.getMessage().getText().startsWith("/season")){
                    season = update.getMessage().getText().substring(7).trim();
                    bseason = true;
                    sendMessage(update.getMessage().getChatId().toString(),"Send Your Tv Series Upload for a New or Old tv Series New : /new ,Old : /old");
                }
            }
            if(tvSeries && bseason){
                if(update.getMessage().getText().equals("/old")){
                    oldSeries = true;
                    newSeries = false;
                    sendMessage(update.getMessage().getChatId().toString(),"All Tv Series: /oldall \n" +
                            "Search Tv Series : /search series name");
                }else if (update.getMessage().getText().equals("/new")){
                    oldSeries = false;
                    newSeries = true;
                    sendMessage(update.getMessage().getChatId().toString(), "Send Your Tv Series Name with /setname");
                }
            }

            if(oldSeries){
                if(update.getMessage().getText().equals("/oldall")){

                } else if (update.getMessage().getText().startsWith("/search")) {
                    String tvSeriesName = update.getMessage().getText().substring(7).trim();
                    sendMessage(update.getMessage().getChatId().toString(), "Finding Your Group");
                }
            }else if(newSeries){
                if(update.getMessage().getText().startsWith("/setname")){
                    String name = update.getMessage().getText().substring(9).trim();

                    try {
                        FilmDetailsSearch fds = new FilmDetailsSearch();
                        dataRecieve = true;
                        details = fds.FilmData(name);


                        imdbRatings = details.get("imdbRating").toString();
                        title = details.get("Title").toString();
                        director = details.get("Director").toString();
                        poster = details.get("Poster").toString();
                        genres = details.get("Genre").toString();
                        year = details.get("Year").toString();
                        country = details.get("Country").toString();
                        plot = details.get("Plot").toString();
                        System.out.println("Success");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if(dataRecieve && update.getMessage().getText().equals("/create")){
                System.out.println("Start");
                System.out.println(details);
                FlaskController flaskController = new FlaskController();
                ObjectMapper objectMapper = new ObjectMapper();

                ChannelData channelData = new ChannelData();
                channelData.setChannel_name(title);
                channelData.setAbout(plot);
                channelData.setPhone_number("+94761964531");
                channelData.setUsers(getAdminUsers(user));
                channelData.setPhoto(poster);

                System.out.println(channelData);

                String data = flaskController.createChannel(channelData);
                JsonNode jsonNode = null;
                try {
                    jsonNode = objectMapper.readTree(data);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                String status = jsonNode.get("status").asText();
                JsonNode channelDataNode = jsonNode.get("channel_data");

                channelName = channelDataNode.get("channel_name").asText();
                channelId = channelDataNode.get("channel_id").asText();
                channelLink = channelDataNode.get("channel_link").asText();
                if(status.equals("success")){
                    TvSeriesModel tvSeriesModel = new TvSeriesModel();
                    tvSeriesModel.createTvSeries(channelId,title, channelLink,year); //here have database updates
                    ArrayList <String> genreArrayList = new ArrayList<>();
                    String [] genreArray = genres.split(", ");
                    for(String genre: genreArray){
                        genreArrayList.add(genre);
                    }
                    tvSeriesModel.createTvHasGenres(channelId,genreArrayList); //their have may database updates
                    sendMessage(update.getMessage().getChatId().toString(),"Database Created Now Upload Your Torrent Files One By One.");
                    newSeriesGroupCreate = true;
                    setGroup = true;
                }

            }
            //
            if(tvSeries && update.getMessage().hasDocument() && (Integer.valueOf(season) > 0 && setGroup)){
                FileHandle fileHandle = new FileHandle(update.getMessage().getChatId().toString());
            }

            if(update.getMessage().getText().equals("/reset")){
                film = false;
                tvSeries = false;
                bseason = false;
                newSeries = false;
                oldSeries = false;
                start = false;
                newSeriesGroupCreate = false;
                setGroup = false;
            }
        }
    }

    private void sendMessage(String chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            logger.error("Send Message Error", e);
        }
    }

    private void sendDocument(String chatId, String document){
        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(new InputFile(document));
        sendDocument.setThumbnail(new InputFile("thumbnail.webp"));
        try{
            execute(sendDocument);
        }catch (TelegramApiException e){
            logger.error("Error Throw While Sending Document");
        }
    }

    private void sendOldTvSeriesInlineKeyBoard(String chatId){

    }

    private void tvSeriesOnCallbackQueryRecieved(CallbackQuery callbackQuery){

    }

    private @NotNull List<String> getAdminUsers(@NotNull HashMap <String, User> userCache){
        List<String> adminUserIds = new ArrayList<>();
        for (Map.Entry<String, User> entry : userCache.entrySet()) {
            User user = entry.getValue();
            if (user.getUserType() == 1) {
                adminUserIds.add(entry.getKey());  // Add user ID (key) to the list
            }
        }
        return adminUserIds;
    }
}
