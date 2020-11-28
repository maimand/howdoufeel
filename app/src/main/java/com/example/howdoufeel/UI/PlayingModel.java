package com.example.howdoufeel.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayingModel extends ViewModel {
    // Create a LiveData with a String
    private MutableLiveData<Boolean> isPlaying;

    public MutableLiveData<Boolean> getCurrentName() {
        if (isPlaying == null) {
            isPlaying = new MutableLiveData<Boolean>();
        }
        return isPlaying;
    }
}
