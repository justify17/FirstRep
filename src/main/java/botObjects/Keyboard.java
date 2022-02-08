package botObjects;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с клавиатурой
 */
public class Keyboard {
    private ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
    private SendMessage message;

    public Keyboard(SendMessage message) {
        this.message = message;
    }

    public void setButtonGo() {
        message.setReplyMarkup(rkm);
        rkm.setSelective(true);
        rkm.setResizeKeyboard(true);
        rkm.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("/go");
        keyboard.add(keyboardFirstRow);
        rkm.setKeyboard(keyboard);
    }
}