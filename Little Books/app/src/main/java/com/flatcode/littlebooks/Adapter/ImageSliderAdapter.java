package com.flatcode.littlebooks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.DATA;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.Objects;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderViewHolder> {

    Context context;
    int setTotalCount;
    String ImageLink;

    public ImageSliderAdapter(Context context, int setTotalCount) {
        this.setTotalCount = setTotalCount;
        this.context = context;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SliderViewHolder viewHolder, final int position) {
        FirebaseDatabase.getInstance().getReference(DATA.SLIDER_SHOW).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch (position) {
                    case 0:
                        ImageLink = Objects.requireNonNull(snapshot.child("1").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 1:
                        ImageLink = Objects.requireNonNull(snapshot.child("2").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 2:
                        ImageLink = Objects.requireNonNull(snapshot.child("3").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 3:
                        ImageLink = Objects.requireNonNull(snapshot.child("4").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 4:
                        ImageLink = Objects.requireNonNull(snapshot.child("5").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 5:
                        ImageLink = Objects.requireNonNull(snapshot.child("6").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 6:
                        ImageLink = Objects.requireNonNull(snapshot.child("7").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 7:
                        ImageLink = Objects.requireNonNull(snapshot.child("8").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 8:
                        ImageLink = Objects.requireNonNull(snapshot.child("9").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 9:
                        ImageLink = Objects.requireNonNull(snapshot.child("10").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 10:
                        ImageLink = Objects.requireNonNull(snapshot.child("11").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 11:
                        ImageLink = Objects.requireNonNull(snapshot.child("12").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 12:
                        ImageLink = Objects.requireNonNull(snapshot.child("13").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 13:
                        ImageLink = Objects.requireNonNull(snapshot.child("14").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 14:
                        ImageLink = Objects.requireNonNull(snapshot.child("15").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 15:
                        ImageLink = Objects.requireNonNull(snapshot.child("16").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 16:
                        ImageLink = Objects.requireNonNull(snapshot.child("17").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 17:
                        ImageLink = Objects.requireNonNull(snapshot.child("18").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 18:
                        ImageLink = Objects.requireNonNull(snapshot.child("19").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                    case 19:
                        ImageLink = Objects.requireNonNull(snapshot.child("20").getValue()).toString();
                        Glide.with(viewHolder.itemView).load(ImageLink).into(viewHolder.ImageSlider);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getCount() {
        return setTotalCount;
    }

    static class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView ImageSlider;
        View itemView;

        public SliderViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ImageSlider = itemView.findViewById(R.id.imageView);
        }
    }
}