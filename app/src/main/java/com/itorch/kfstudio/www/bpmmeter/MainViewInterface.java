package com.itorch.kfstudio.www.bpmmeter;

import java.util.List;

public interface MainViewInterface {

    void onNotesLoaded(List<Note> notes);

    void onNoteDeleted();

    void onDataNotAvailable();

    int OnAvgCount(Float f);

    int OnMinCount(Integer integer);

    int OnMaxCount(Integer integer);
}