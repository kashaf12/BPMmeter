package com.itorch.kfstudio.www.bpmmeter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    History_Fragment context;
    List<Note> noteList;
    private OnNoteItemClick onNoteItemClick;

    public NotesAdapter(History_Fragment context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
        this.onNoteItemClick = (OnNoteItemClick) context;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getActivity()).inflate(R.layout.history_list_item,parent,false);
        return new NotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.bpm.setText(noteList.get(position).getBpm());
        holder.label.setText(noteList.get(position).getLabel());
        holder.timeanddate.setText(noteList.get(position).getTimeanddate());
        }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView bpm,label,timeanddate;
        public NotesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            bpm=itemView.findViewById(R.id.ex_reps);
            label=itemView.findViewById(R.id.ex_name);
            timeanddate=itemView.findViewById(R.id.timedate);


        }

        @Override
        public void onClick(View v) {
            onNoteItemClick.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteItemClick{
        void onNoteClick(int pos);
    }
}
