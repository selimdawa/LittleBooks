package com.flatcode.littlebooksadmin.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooksadmin.Adapter.EditorsChoiceAdapter;
import com.flatcode.littlebooksadmin.Model.EditorsChoice;
import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.THEME;
import com.flatcode.littlebooksadmin.databinding.ActivityEditorsChoiceBinding;

import java.util.ArrayList;

public class EditorsChoiceActivity extends AppCompatActivity {

    private ActivityEditorsChoiceBinding binding;
    Context context = EditorsChoiceActivity.this;

    ArrayList<EditorsChoice> list;
    EditorsChoiceAdapter adapter;
    EditorsChoice editorsChoice = new EditorsChoice();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityEditorsChoiceBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.editors_choice);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new EditorsChoiceAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);

        IdeaPosts();
    }

    public void IdeaPosts() {
        list.clear();
        for (int i = 0; i < 50; i++) {
            list.add(editorsChoice);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}