package com.ruufilms.handler;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.annotation.Contract;
import org.telegram.telegrambots.meta.api.methods.send.*;
import com.ruufilms.enums.MessageInfo;
import com.ruufilms.enums.MessageType;

import javax.validation.constraints.NotNull;

/**
 * A handler class for managing and sending messages of different types.
 * This class contains multiple constructors to create a message handler object with varying parameters.
 */

public class MessageHandler {
    private MessageInfo info = MessageInfo.GENERAL;
    private MessageType type = MessageType.TEXT;
    private String message;
    private String chatId;
    private String signature;
    private String filePath;
    private String thumbnail = Dotenv.load().get("THUMBNAIL");

    /**
     * Default constructor for the MessageHandler.
     * Initializes a MessageHandler with default values.
     */
    public MessageHandler() {}

    /**
     * Constructor to initialize a MessageHandler with specific parameters.
     *
     * @param info The message information type (e.g., general, specific category).
     * @param message The message content.
     * @param chatId The ID of the chat to which the message will be sent.
     * @param type The type of the message (e.g., TEXT, PHOTO, etc.).
     * @param signature The signature to append to the message (can be null).
     * @param filePath The path to any file associated with the message (can be null).
     * @throws IllegalArgumentException if the chatId is null or empty.
     */
    public MessageHandler(MessageInfo info, String message, String chatId, MessageType type, String signature, String filePath) {
        if (chatId == null || chatId.isEmpty()) {
            throw new IllegalArgumentException("Chat ID cannot be null or empty");
        }

        this.info = info;
        this.message = message;
        this.type = type;
        this.chatId = chatId;
        this.signature = signature;
        this.filePath = filePath;
    }

    /**
     * Constructor to initialize a MessageHandler with default MessageInfo (GENERAL).
     *
     * @param message The message content.
     * @param chatId The ID of the chat to which the message will be sent.
     * @param type The type of the message (e.g., TEXT, PHOTO, etc.).
     * @param signature The signature to append to the message (can be null).
     * @param filePath The path to any file associated with the message (can be null).
     */
    public MessageHandler(String message, String chatId, MessageType type, String signature, String filePath) {
        this(MessageInfo.GENERAL, message, chatId, type, signature, filePath);
    }

    /**
     * Constructor to initialize a MessageHandler with default MessageInfo (GENERAL) and default MessageType (TEXT).
     *
     * @param message The message content.
     * @param chatId The ID of the chat to which the message will be sent.
     * @param signature The signature to append to the message (can be null).
     */
    public MessageHandler(String message, String chatId, String signature) {
        this(MessageInfo.GENERAL, message, chatId, MessageType.TEXT, signature, null);
    }

    /**
     * Constructor to initialize a MessageHandler with default MessageInfo (GENERAL) and default MessageType (TEXT).
     *
     * @param message The message content.
     * @param chatId The ID of the chat to which the message will be sent.
     */
    public MessageHandler(String message, String chatId) {
        this(MessageInfo.GENERAL, message, chatId, MessageType.TEXT, null, null);
    }


    public MessageInfo getInfo() {
        return info;
    }

    public void setInfo(MessageInfo info) {
        this.info = info;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Handles the creation and return of an appropriate message object based on the message type.
     *
     * This method checks the message type and delegates the handling of the message to the corresponding method:
     * - TEXT: Handles text messages.
     * - DOCUMENT: Handles document messages.
     * - TORRENT: Handles torrent messages.
     * - VIDEO: Handles video messages.
     * - PHOTO: Handles photo messages.
     * - COMMAND: Handles command messages.
     * - UNKNOWN: Handles unknown message types.
     *
     * @return An appropriate message object based on the message type, such as a {@link SendMessage},
     *         {@link SendDocument}, {@link SendPhoto}, or {@link SendVideo}, depending on the message type.
     */

    public Object ReplyMessage(){
        switch(getType()){
            case TEXT:
                return handleTextMessage();
            case DOCUMENT:
                return handleDocumentMessage();
            case TORRENT:
                return handleTorrentMessage();
            case VIDEO:
                return handleVideoMessage();
            case PHOTO:
                return handlePhotoMessage();
            case COMMAND:
                return handleCommandMessage();
            case UNKNOWN:
            default:
               return handleUnknownMessage();
        }
    }

    private SendMessage handleUnknownMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId());
        sendMessage.setText("Unknown message type received.");
        return sendMessage;
    }


    private SendMessage handleCommandMessage() {
        return null;
    }

    private SendPhoto handlePhotoMessage() {
        return null;
    }

    private SendVideo handleVideoMessage() {
        return null;
    }

    private SendDocument handleTorrentMessage() {
        return null;
    }


    private SendDocument handleDocumentMessage() {
        return null;
    }


    private SendMessage handleTextMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId());
        String textMessage = getMessage();
        if (getSignature() != null) {
            textMessage += "\n\n" + getSignature();
        }
        sendMessage.setText(textMessage);
        sendMessage.enableHtml(true);
        return sendMessage;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail){
        this.thumbnail = (thumbnail == null || thumbnail.isEmpty())? null: filePath;
    }
}
