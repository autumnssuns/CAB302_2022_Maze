package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PromptTextArea extends JTextArea implements FocusListener {
    private String promptText;
    private boolean active;
    private Color actualColor;
    private Font actualFont;

    public PromptTextArea(String prompt) {
        super(prompt);
        this.promptText = prompt;
        this.active = true;
        this.actualFont = this.getFont();
        this.actualColor = this.getForeground();
        super.addFocusListener(this);
        changeColor();
    }

    public void writeText(String text){
        if (text.isEmpty()) return;
        super.setText(text);
        active = false;
        changeColor();
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().isEmpty()){
            super.setText("");
            active = false;
            changeColor();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()){
            super.setText(promptText);
            active = true;
            changeColor();
        }
    }

    @Override
    public String getText(){
        return active ? "" : super.getText();
    }

    private void changeColor(){
        if (active){
            this.setForeground(Color.GRAY);
            this.setFont(actualFont.deriveFont(Font.ITALIC));
        } else {
            this.setForeground(actualColor);
            this.setFont(actualFont);
        }
    }
}
