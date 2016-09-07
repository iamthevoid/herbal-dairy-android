package com.iam.herbaldairy.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor {

    public static TextWatcher integerTextWatcher(final EditText editText, final int howMany) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    final String str = editable.toString();
                    Pattern pattern = Pattern.compile("\\D");
                    final String s = editable.toString();
                    String last = String.valueOf(s.charAt(editable.length() - 1));
                    Matcher matcher = pattern.matcher(last);
                    if (matcher.find()) {
                        editText.setText(s.replaceAll("\\D", ""));
                    }
                    if (editable.length() > howMany) {
                        editText.setText(str.substring(0, str.length()-1));
                        editText.setSelection(howMany);
                    }
                }
            }
        };
    }
}
