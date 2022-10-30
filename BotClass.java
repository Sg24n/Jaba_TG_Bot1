package com.example.Java_Tgbot30_11_2022;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotClass extends TelegramLongPollingBot {
    @Value("${telegram.token}")
    private String token = "";
    @Value("${telegram.botName}")
    private String botName = "";


    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            System.out.println(update.getMessage().getText());

            //Указание получателя
            Long sendId = selectRecipient(chatId, text);
            //Указание ответа на присланный текст
            String sAnswer = selectAnswer(chatId, text);
            //Форматирование конечного текста для отправки
            String formatedAnswer = selectFormatOut(chatId, sendId, text, sAnswer);

            try {
                sendMessage(sendId, formatedAnswer);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendMessage(Long chatId, String responseText) throws TelegramApiException {
        var responseMessage = new SendMessage(chatId.toString(), responseText);
        execute(responseMessage);
        return;
    }

    //Обработка входящего текста_____________________________________________
    private String selectAnswer(Long chatId, String text) {
        //String sendChatId = chatId.toString();

        String inText = text.toUpperCase();
        String outText = switch (inText) {
            case "СЛАВА" -> "Украјне";
            case "КЕЙС" -> "Работает";
            case "РЕП" -> "Затычка";
            // "КАЧ" -> ytDownloader();
            default -> "Кейс не прописан";
        };

        System.out.println(outText);
        return outText;
    }
    //Обработка входящего текста_____________________________________________!

    //Выбор получателя сообщения_____________________________________________
    private Long selectRecipient(Long chatId, String sText) {
        Long sendId;
        long mishbId = 556815576L;
        long krotbId = 949979414L;
        if (sText.indexOf("Мышб") == 0) {
            sendId = mishbId;
        } else if (sText.indexOf("Кротб") == 0) {
            sendId = krotbId;
        } else {
            sendId = chatId;
        }
        return sendId;
    }
    //Выбор получателя сообщения_____________________________________________!

    //Форматирование отправляемого текста____________________________________
    private String selectFormatOut(long inputId, Long sendId, String inputText, String answer) {
        String formatedAnswer;
        if (inputId == sendId) {
            formatedAnswer = "ID чата: " + inputId + "\nВходной текст: " + inputText +"\nОтвет: " + answer;
        } else {
            formatedAnswer = "Получено от " + inputId + "\nСообщение: "+inputText;
        }
        return  formatedAnswer;
    }
    //Форматирование отправляемого текста____________________________________!
}
