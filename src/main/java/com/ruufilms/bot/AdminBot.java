package com.ruufilms.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminBot extends TelegramLongPollingBot {
    public AdminBot(String botToken){
        super(botToken);
    }
    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "Admin Bot";
    }
}
