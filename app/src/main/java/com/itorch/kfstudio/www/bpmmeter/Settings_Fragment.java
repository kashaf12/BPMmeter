package com.itorch.kfstudio.www.bpmmeter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Settings_Fragment extends Fragment {
    View view;
    EditText name1,yob,weight1;
    String x;
    SharedPreferences pref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_settings,container,false);
        name1=view.findViewById(R.id.edittext_name);
        yob=view.findViewById(R.id.edittext_yob);
        weight1=view.findViewById(R.id.edittext_weight);
        pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        String n= pref.getString("name", null); // getting String
        String y=pref.getString("yob", null); // getting String
        String w=pref.getString("weight", null); // getting String
        if(n!=null){
            name1.setText(n);
        }
        if(y!=null){
            yob.setText(y);
        }
        if(w!=null){
            weight1.setText(w);
        }
        name1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    x=name1.getText().toString();
                    x=x.trim();
                    if(!x.isEmpty()){
                        pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("name", name1.getText().toString());
                        editor.apply();
                    }
                }
            }
        });
        yob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    x=yob.getText().toString();
                    x=x.trim();
                    if(!x.isEmpty()){
                        pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("yob", yob.getText().toString());
                        editor.apply();
                    }
                }
            }
        });
        weight1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    x=weight1.getText().toString();
                    x=x.trim();
                    if(!x.isEmpty()){
                        pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("weight", weight1.getText().toString());
                        editor.apply();
                    }
                }
            }
        });

        // commit changes


        return view;
    }
}
