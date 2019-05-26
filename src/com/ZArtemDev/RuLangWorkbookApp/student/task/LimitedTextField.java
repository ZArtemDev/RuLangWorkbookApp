package com.ZArtemDev.RuLangWorkbookApp.student.task;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class LimitedTextField extends TextField {
    LimitedTextField(){
        super();
        addTextLimiter(this, 1);
        this.setPrefColumnCount(1);
    }

    LimitedTextField(int value){
        super();
        addTextLimiter(this, value);
        this.setPrefColumnCount(value);
    }

    public static void addTextLimiter(final TextField tf, final int maxLength){
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(tf.getText().length() > maxLength){
                    String s =tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
}
