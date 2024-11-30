package com.ruufilms.bot;

import com.ruufilms.Beans.Group;
import com.ruufilms.Beans.Stickers;
import com.ruufilms.Models.GroupModel;
import com.ruufilms.config.WordArtConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class SecurityBot extends TelegramLongPollingBot {
    String botToken;
    HashMap<String,com.ruufilms.Beans.User> user;
    Logger logger = LogManager.getLogger(SecurityBot.class);
    HashMap<String, Group> group;
    boolean getInfo = false;
    String groupId = null;
    String groupLink = null;
    String groupName = null;
    Stickers stickers;
    public SecurityBot(String botToken, HashMap<String,com.ruufilms.Beans.User> user, HashMap<String, Group> group, Stickers stickers){
        super(botToken);
        this.botToken = botToken;
        this.user = user;
        this.group = group;
        this.stickers = stickers;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getSticker());
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(update.getMessage().getChatId());
        sendSticker.setSticker(new InputFile(stickers.getSeasonSticker(5)));
        try{
            execute(sendSticker);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
        // get group id  = System.out.println(update.getMessage().getChat().getId());
        //System.out.println(update.getMessage().getChat().getId());
        if(update.hasMessage() && update.getMessage().getNewChatMembers() != null){
            List<User> newMembers = update.getMessage().getNewChatMembers();
            for (User newMember : newMembers) {
                handleNewMember(newMember, update.getMessage().getChatId());
                if(this.user.containsKey(update.getMessage().getChatId().toString())){
                    updateRegisteredUser(newMember, update.getMessage().getChatId());
                }else{
                    registerNewUser(newMember, update.getMessage().getChatId());
                }
            }
        }else if(update.getMessage().getLeftChatMember() != null){
            System.out.println("A Member Left From Group");
        }
        String message = update.getMessage().getText();
        if(message.equals("/getinfo")){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId());
            String newMessage = "use:  /setInfo \nID- your_group_id\nLink- your_group_link\n Name- your_group_name \n";
            sendMessage.setText(newMessage);
            try {
                execute(sendMessage);
                getInfo =true;
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
        if (getInfo && message.startsWith("/setInfo")) {
            logger.info("Processing /setInfo command...");
            String[] lines = message.split("\n");
            logger.info("Received lines: " + Arrays.toString(lines));

            if (lines.length > 3) { // Adjusted length check
                String groupIdLine = lines[1].trim();
                String groupLinkLine = lines[2].trim();
                String groupNameLine = lines[3].trim();

                logger.info("Group ID line: " + groupIdLine);
                logger.info("Group Link line: " + groupLinkLine);
                logger.info("Group Name line: " + groupNameLine);

                if (groupIdLine.startsWith("ID-")) {
                    groupId = groupIdLine.substring(3).trim();
                    logger.info("Extracted Group ID: " + groupId);
                }

                if (groupLinkLine.startsWith("Link-")) {
                    groupLink = groupLinkLine.substring(5).trim();
                    logger.info("Extracted Group Link: " + groupLink);
                }

                if (groupNameLine.startsWith("Name-")) {
                    groupName = groupNameLine.substring(5).trim();
                    logger.info("Extracted Group Name: " + groupName);
                }

                if (groupId != null && groupLink != null && groupName != null) {
                    SendMessage response = new SendMessage();
                    response.setChatId(update.getMessage().getChatId().toString());
                    response.setText("Group Info Received:\n- ID: " + groupId + "\n- Link: " + groupLink + "\n- Name: " + groupName + WordArtConfig.getWordArt());
                    try {
                        GroupModel groupModel = new GroupModel();
                        groupModel.createGroup(groupId,groupName,groupLink);
                        Group groupBean = new Group();
                        groupBean.setGroupLink(groupLink);
                        groupBean.setName(groupName);
                        groupBean.setGroupId(groupId);
                        group.put(groupId,groupBean);
                        execute(response );
                        getInfo = false;
                    } catch (TelegramApiException e) {
                        logger.error("Throw A TelegramApiException", e);
                    }
                }else{
                    logger.error("Invalid Data Error");
                }
            }
        }

    }

    public void deleteJoinLeaveMessages(Long chatId, int messageID){
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageID);
        try{
            execute(deleteMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return "Ruu-SecurityBot";
    }

    private void registerNewUser(User member, Long chatId){

    }

    private void updateRegisteredUser(User member, Long chatId){

    }

    private void handleNewMember(User newMember, Long chatId){
        String welcomeMessage = "Welcome, "+ newMember.getFirstName() + "!";
        if(newMember.getLastName() != null){
            welcomeMessage += " "+ newMember.getLastName();
        }
        welcomeMessage += " 🎉";

        SendMessage message = new SendMessage();
        message.setText(welcomeMessage);
        message.setChatId(chatId);
        try {
            Message sentMessage = execute(message); // Sends the message
            scheduledMessageDeletion(chatId, sentMessage.getMessageId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void scheduledMessageDeletion(Long chatId, Integer messageId){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try{
                    DeleteMessage deleteMessage = new DeleteMessage();
                    deleteMessage.setChatId(chatId.toString());
                    deleteMessage.setMessageId(messageId);
                    execute(deleteMessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }
            }
        }, 30000);
    }
}
