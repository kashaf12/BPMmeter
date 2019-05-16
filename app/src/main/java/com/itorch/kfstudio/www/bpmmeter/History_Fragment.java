package com.itorch.kfstudio.www.bpmmeter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class History_Fragment extends Fragment implements MainViewInterface,NotesAdapter.OnNoteItemClick,AddNoteViewInterface{
    RecyclerView rvNotes;
    RecyclerView.Adapter adapter;
    List<Note> notesList;
    Note note;
    TextView no_history;
    AutoCompleteTextView content;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_history,container,false);
        rvNotes = view.findViewById(R.id.mainListView);
        rvNotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        no_history=view.findViewById(R.id.no_history);
        return view;
        
    }
    @Override

    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        loadNotes();
    }
    private void loadNotes(){

        LocalCacheManager.getInstance(getActivity()).getNotes(this);


    }

    @Override
    public void onDataUpdates() {

    }

    @Override
    public void onNotesLoaded(List<Note> notes) {
        notesList = notes;

        if(notesList.size() == 0){

            no_history.setVisibility(View.VISIBLE);
            onDataNotAvailable();
        }else {

            no_history.setVisibility(View.GONE);
            adapter = new NotesAdapter(this, notes);
            rvNotes.setAdapter(adapter);

        }

    }

    @Override
    public void onNoteDeleted() {

    }

    @Override
    public void onDataNotAvailable() {

    }


    @Override
    public int OnAvgCount(Float f) {
     return 0;
    }

    @Override
    public int OnMinCount(Integer integer) {
        return 0;
    }

    @Override
    public int OnMaxCount(Integer integer) {
        return 0;
    }


    @Override
    public void onNoteClick(final int pos) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View alertLayout = inflater.inflate(R.layout.alertdialog, null);
        final AlertDialog alertDialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert).create();
        alertDialog.setView(alertLayout);
        alertDialog.show();
        content = alertDialog.findViewById(R.id.autoCompleteUpdate);
        Button update =alertDialog.findViewById(R.id.update);
        Button cancel =alertDialog.findViewById(R.id.cancel);
        Button delete = alertDialog.findViewById(R.id.delete);
        note = (Note)notesList.get(pos);
        content.setText(note.getLabel());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalCacheManager.getInstance(getActivity()).updateNote(History_Fragment.this,content.getText().toString(),note);
                notesList.set(pos,note);
                listVisibility();
                alertDialog.dismiss();
                }});
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }});
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalCacheManager.getInstance(getActivity()).deleteNote(History_Fragment.this,notesList.get(pos));
                notesList.remove(pos);
                listVisibility();
                alertDialog.dismiss();
            }});

    }
    private void listVisibility(){

        adapter.notifyDataSetChanged();
    }
    @Override
    public void onResume(){
        super.onResume();
        loadNotes();
    }
}
