package com.ruufilms.accountaccessing;

import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.telegram.telegrambots.meta.generics.UpdatesHandler;

public class TelegramClient {
    public static void main(String[] args) {
        Client client = Client.create(new UpdatesHandler(),
                null,
                null);

        TdApi.SetTdlibParameters parameters = new TdApi.SetTdlibParameters();
        parameters.databaseDirectory = "./tdlib";
        parameters.useMessageDatabase = true;
        parameters.useFileDatabase =true;
        parameters.useChatInfoDatabase = true;
        parameters.apiId = 24150008;
        parameters.apiHash = "b63bf4acbec8984467850b3474d218ef";
        parameters.systemLanguageCode = "en";
        parameters.deviceModel = "Desktop";
        parameters.applicationVersion = "1.0";
        parameters.enableStorageOptimizer = true;

        TdApi.SendMessage sendMessage = new TdApi.SendMessage();
        sendMessage.chatId = 6335286775L; // ID of the chat
        sendMessage.inputMessageContent = new TdApi.InputMessageText(
                new TdApi.FormattedText("Hello, World!", null), false, true);

        client.send(sendMessage, result -> {
            if (result instanceof TdApi.Message) {
                System.out.println("Message sent successfully!");
            }
        }, null);

    }
    static class UpdatesHandler implements Client.ResultHandler{

        @Override
        public void onResult(TdApi.Object object) {
            if(object instanceof TdApi.UpdateNewMessage){
                TdApi.UpdateNewMessage newMessage = (TdApi.UpdateNewMessage) object;
                System.out.println("New Message" + newMessage.message.content.toString());
            }
        }
    }
    static class AuthorizationHandler implements Client.ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            if (object instanceof TdApi.AuthorizationStateWaitPhoneNumber) {
                System.out.println("Please enter your phone number.");
                // Use client.send() to provide the phone number
            } else if (object instanceof TdApi.AuthorizationStateWaitCode) {
                System.out.println("Enter the authentication code sent to your Telegram.");
                // Use client.send() to provide the code
            } else if (object instanceof TdApi.AuthorizationStateReady) {
                System.out.println("Authorization successful!");
            } else if (object instanceof TdApi.AuthorizationStateClosed) {
                System.out.println("Client is closed.");
            }
        }
    }
}
