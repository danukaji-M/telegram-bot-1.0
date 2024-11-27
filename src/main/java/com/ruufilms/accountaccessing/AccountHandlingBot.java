package com.ruufilms.accountaccessing;

import com.ruufilms.bot.FilmBot;
import com.ruufilms.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AccountHandlingBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(FilmBot.class);
    static AppConfig.Config config = new AppConfig.Config(AppConfig.INSTANCE.properties);
    boolean Stat = false;
    boolean password = false;
    String otp;
    String pwd;
    boolean verify = false;
    public AccountHandlingBot(String token){
        super(token);
    }
    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        if(message.equals("/start")){
            Stat = true;

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Send Your Otp Key");
            sendMessage.setChatId(update.getMessage().getChatId());
            try{
                execute(sendMessage);
            }catch (TelegramApiException e){
                logger.error("Error message execution", e);
            }
        }else if(Stat){
            Stat = false;
            password = true;
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Enter Your Otp Key");
            sendMessage.setChatId(update.getMessage().getChatId());
            try{
                execute(sendMessage);
            }catch (TelegramApiException e){
                logger.error("Error message execution", e);
            }
        }else if(password){
            password = false;
            verify = true;
            otp = update.getMessage().getText();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Enter Your Password");
            sendMessage.setChatId(update.getMessage().getChatId());
            try{
                execute(sendMessage);
            }catch (TelegramApiException e){
                logger.error("Error message execution", e);
            }
        }else if(verify){
            pwd = update.getMessage().getText();
            verify = false;
        }
    }

    @Override
    public String getBotUsername() {
        return "Account Administration Bot";
    }
}
