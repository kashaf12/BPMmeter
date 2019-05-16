package com.itorch.kfstudio.www.bpmmeter;

import android.content.Context;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LocalCacheManager {
    private Context context;
    private static LocalCacheManager _instance;
    private AppDatabase db;


    public static LocalCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new LocalCacheManager(context);
        }
        return _instance;
    }

    public LocalCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public void getNotes(final MainViewInterface mainViewInterface) {
        db.noteDao().getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> notes) throws Exception {
                        mainViewInterface.onNotesLoaded(notes);
                    }
                });
    }

    public void getAVG(final MainViewInterface mainViewInterface){
        db.noteDao().avgUsers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Float>() {
                    @Override
                    public void accept(Float aFloat) throws Exception {
                        mainViewInterface.OnAvgCount(aFloat);
                    }
                });
    }

    public void getMin(final MainViewInterface mainViewInterface) {
        db.noteDao().minUsers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mainViewInterface.OnMinCount(integer);
                    }
                });
    }

    public void getMax(final MainViewInterface mainViewInterface) {
        db.noteDao().maxUsers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mainViewInterface.OnMaxCount(integer);
                    }
                });
    }
    public void deleteNote(final MainViewInterface mainViewInterface, final Note note) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.noteDao().delete(note);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                mainViewInterface.onNoteDeleted();
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }
    public void addNotes(final AddNoteViewInterface addNoteViewInterface, final String bpm, final String label , final String timeanddate) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Note note = new Note(bpm,label,timeanddate);
                db.noteDao().insertAll(note);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
               }

            @Override
            public void onError(Throwable e) {
                addNoteViewInterface.onDataNotAvailable();
            }
        });
    }
    public void updateNote(final AddNoteViewInterface addNoteViewInterface, final String label, final Note note) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                note.setLabel(label);
                db.noteDao().update(note);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                addNoteViewInterface.onDataUpdates();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }


}