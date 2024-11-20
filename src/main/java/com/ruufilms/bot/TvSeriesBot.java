package com.ruufilms.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TvSeriesBot extends TelegramLongPollingBot {
    private String botToken;
    TvSeriesBot(DefaultBotOptions option, String botToken){
        super(option,botToken);
        this.botToken = botToken;
    }
    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "Ruu-Tv-Series Upload Bot";
    }
}
