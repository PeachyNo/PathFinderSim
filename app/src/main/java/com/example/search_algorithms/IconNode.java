package com.example.search_algorithms;

import android.widget.ImageButton;

public class IconNode {
    public int icon_type;
    private ImageButton button;


    IconNode(ImageButton b){
        icon_type = Values.ICON_TYPE_CLEAR;
        button = b;
    }

    public ImageButton getButton() {
        return button;
    }
}
