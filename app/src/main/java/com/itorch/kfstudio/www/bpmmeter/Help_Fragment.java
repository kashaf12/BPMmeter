package com.itorch.kfstudio.www.bpmmeter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class Help_Fragment extends Fragment {


    AutoCompleteTextView autoCompleteTextView;
    Button branch,rollno;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view;
       view = inflater.inflate(R.layout.fragment_help,container,false);
       TextView kashaf = view.findViewById(R.id.kashaf);

        kashaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View alertLayout = inflater.inflate(R.layout.frame, null);
                final AlertDialog alertDialog=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Dialog_Alert).create();
                alertDialog.setView(alertLayout);
                alertDialog.show();
                autoCompleteTextView = alertLayout.findViewById(R.id.autoCompleteTextView);
                branch=alertLayout.findViewById(R.id.btn_ignore);
                rollno=alertLayout.findViewById(R.id.btn_save);
                autoCompleteTextView.setText("Kashaf Ahmed");
                branch.setText("C.S.E");
                rollno.setText("16227");
                branch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                rollno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        return view;
        
    }
}
