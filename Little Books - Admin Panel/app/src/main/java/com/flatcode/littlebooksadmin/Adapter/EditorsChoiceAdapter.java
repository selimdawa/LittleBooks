package com.flatcode.littlebooksadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooksadmin.Model.Book;
import com.flatcode.littlebooksadmin.Model.EditorsChoice;
import com.flatcode.littlebooksadmin.Unit.CLASS;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ItemBookEditorsChoiceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.List;

public class EditorsChoiceAdapter extends RecyclerView.Adapter<EditorsChoiceAdapter.ViewHolder> {

    private ItemBookEditorsChoiceBinding binding;
    private final Context context;
    public List<EditorsChoice> list;

    public EditorsChoiceAdapter(Context context, List<EditorsChoice> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemBookEditorsChoiceBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int id = position + 1;
        String editorsChoiceId = DATA.EMPTY + id;

        loadBookDetails(id, editorsChoiceId, holder.title, holder.description, holder.numberViews,
                holder.numberLoves, holder.numberDownloads, holder.image, holder.remove, holder.change,
                holder.addCard, holder.detailsCard);

        holder.numberEditorsChoice.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, id));
        holder.add.setOnClickListener(v ->
                VOID.IntentExtra2(context, CLASS.EDITORS_CHOICE_ADD, DATA.EDITORS_CHOICE_ID, editorsChoiceId, DATA.OLD_BOOK_ID, null));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, add, remove, change;
        ImageButton more;
        TextView title, description, numberViews, numberLoves, numberDownloads, numberEditorsChoice;
        LinearLayout item, item2;
        CardView addCard, detailsCard;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            title = binding.title;
            more = binding.more;
            description = binding.description;
            numberViews = binding.numberViews;
            numberLoves = binding.numberLoves;
            numberDownloads = binding.numberDownloads;
            item = binding.item;
            item2 = binding.item2;
            add = binding.add;
            numberEditorsChoice = binding.numberEditorsChoice;
            addCard = binding.addCard;
            detailsCard = binding.detailsCard;
            remove = binding.remove;
            change = binding.change;
        }
    }

    private void loadBookDetails(int i, String position, TextView title, TextView description, TextView viewsCount
            , TextView lovesCount, TextView downloadsCount, ImageView image, ImageView remove, ImageView change
            , CardView addCard, CardView detailsCard) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book item = snapshot.getValue(Book.class);
                    assert item != null;
                    if (item.getEditorsChoice() == i) {
                        String id = DATA.EMPTY + item.getId();
                        loadBook(id);
                        addCard.setVisibility(View.GONE);
                        detailsCard.setVisibility(View.VISIBLE);
                        remove.setVisibility(View.VISIBLE);
                        change.setVisibility(View.VISIBLE);
                        detailsCard.setOnClickListener(v ->
                                VOID.IntentExtra(context, CLASS.BOOK_DETAIL, DATA.BOOK_ID, id));
                        remove.setOnClickListener(v -> VOID.dialogOptionDelete(context, null, id, null
                                , null, false, true, null, null));
                        change.setOnClickListener(v -> VOID.IntentExtra2(context, CLASS.EDITORS_CHOICE_ADD,
                                DATA.EDITORS_CHOICE_ID, position, DATA.OLD_BOOK_ID, id));
                    } else {
                        addCard.setVisibility(View.VISIBLE);
                        detailsCard.setVisibility(View.GONE);
                        remove.setVisibility(View.GONE);
                        change.setVisibility(View.GONE);
                    }
                }
            }

            private void loadBook(String text) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
                ref.child(text).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get data
                        Book item = dataSnapshot.getValue(Book.class);
                        assert item != null;

                        String Title = DATA.EMPTY + item.getTitle();
                        String Description = DATA.EMPTY + item.getDescription();
                        String ViewsCount = DATA.EMPTY + item.getViewsCount();
                        String LovesCount = DATA.EMPTY + item.getLovesCount();
                        String DownloadsCount = DATA.EMPTY + item.getDownloadsCount();
                        String BookImage = DATA.EMPTY + item.getImage();

                        title.setText(Title);
                        description.setText(Description);
                        viewsCount.setText(ViewsCount);
                        lovesCount.setText(LovesCount);
                        downloadsCount.setText(DownloadsCount);
                        VOID.Glide(false, context, BookImage, image);
                        addCard.setVisibility(View.GONE);
                        detailsCard.setVisibility(View.VISIBLE);
                        remove.setVisibility(View.VISIBLE);
                        change.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}