package com.flatcode.littlebooksadmin.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.flatcode.littlebooksadmin.Adapter.MainAdapter;
import com.flatcode.littlebooksadmin.Model.Book;
import com.flatcode.littlebooksadmin.Model.Main;
import com.flatcode.littlebooksadmin.Model.User;
import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.CLASS;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.THEME;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityMainBinding binding;

    List<Main> list;
    MainAdapter adapter;

    Context context = MainActivity.this;

    private static final int SETTINGS_CODE = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .registerOnSharedPreferenceChangeListener(this);
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Color Mode ----------------------------- Start
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new MainActivity.SettingsFragment())
                .commit();
        // Color Mode -------------------------------- End

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        if (sharedPreferences.getString(DATA.COLOR_OPTION, "ONE").equals("ONE")) {
            binding.toolbar.mode.setBackgroundResource(R.drawable.sun);
        } else if (sharedPreferences.getString(DATA.COLOR_OPTION, "NIGHT_ONE").equals("NIGHT_ONE")) {
            binding.toolbar.mode.setBackgroundResource(R.drawable.moon);
        }

        binding.toolbar.image.setOnClickListener(v ->
                VOID.IntentExtra(context, CLASS.PROFILE, DATA.PROFILE_ID, DATA.FirebaseUserUid));

        userInfo();

        binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new MainAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);

        nrItems();
    }

    int U = 0, P = 0, M = 0, B = 0, S = 0, FS = 0, FN = 0, FA = 0, AD = 0, E = 0, CA = 0;

    private void nrItems() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                U = 0;
                P = 0;
                AD = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User item = data.getValue(User.class);
                    assert item != null;
                    if (item.getId() != null) {
                        U++;
                        if (item.getBooksCount() >= 1)
                            P++;
                        if (!(item.getAdLoad() == 0 && item.getAdClick() == 0))
                            AD++;
                    }
                }
                nrBooks();
            }

            private void nrBooks() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        B = 0;
                        M = 0;
                        E = 0;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Book item = data.getValue(Book.class);
                            assert item != null;
                            if (item.getId() != null)
                                B++;
                            if (item.getPublisher().equals(DATA.FirebaseUserUid))
                                M++;
                            if (item.getEditorsChoice() != 0)
                                E++;
                        }
                        nrCategories();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            private void nrCategories() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        CA = 0;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Book item = data.getValue(Book.class);
                            assert item != null;
                            if (item.getId() != null)
                                if (item.getPublisher().equals(DATA.FirebaseUserUid))
                                    CA++;
                        }
                        nrSliderShow();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void nrSliderShow() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SLIDER_SHOW);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        S = 0;
                        S = (int) dataSnapshot.getChildrenCount();
                        nrFollowers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void nrFollowers() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.FOLLOW)
                        .child(DATA.FirebaseUserUid).child(DATA.FOLLOWERS);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {FS = 0;
                        FS = (int) dataSnapshot.getChildrenCount();
                        nrFollowing();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void nrFollowing() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.FOLLOW)
                        .child(DATA.FirebaseUserUid).child(DATA.FOLLOWING);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FN = 0;
                        FN = (int) dataSnapshot.getChildrenCount();
                        nrFavorites();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void nrFavorites() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.FAVORITES).child(DATA.FirebaseUserUid);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FA = 0;
                        FA = (int) dataSnapshot.getChildrenCount();
                        IdeaPosts(U, P, M, B, S, FS, FN, FA, AD, E, CA);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(DATA.FirebaseUserUid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                VOID.Glide(true, context, user.getProfileImage(), binding.toolbar.image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void IdeaPosts(int users, int publishers, int myBooks, int allBooks, int sliderShow,
                           int followers, int following, int favorites, int ads, int editorsChoice, int categories) {
        list.clear();
        Main item1 = new Main(R.drawable.ic_person, "Users", users, CLASS.USERS);
        Main item2 = new Main(R.drawable.ic_add, "Add Book", 0, CLASS.BOOK_ADD);
        Main item3 = new Main(R.drawable.ic_book_white, "My Books", myBooks, CLASS.MY_BOOKS);
        Main item4 = new Main(R.drawable.ic_books, "All Books", allBooks, CLASS.ALL_BOOKS);
        Main item5 = new Main(R.drawable.ic_rank, "Top Publisher", publishers, CLASS.TOP_PUBLISHERS);
        Main item6 = new Main(R.drawable.ic_users, "Editors Choice", editorsChoice, CLASS.EDITORS_CHOICE);
        Main item7 = new Main(R.drawable.ic_add_category, "Add Category", 0, CLASS.CATEGORY_ADD);
        Main item8 = new Main(R.drawable.ic_category_gray, "Categories", categories, CLASS.CATEGORIES);
        Main item9 = new Main(R.drawable.ic_slider, "Slider Show", sliderShow, CLASS.SLIDER_SHOW);
        Main item10 = new Main(R.drawable.ic_followers, "Followers", followers, CLASS.FOLLOWERS);
        Main item11 = new Main(R.drawable.ic_following, "Following", following, CLASS.FOLLOWING);
        Main item12 = new Main(R.drawable.ic_star_selected, "Favorites", favorites, CLASS.FAVORITES);
        Main item13 = new Main(R.drawable.ic_ads, "AD's", ads, CLASS.ADS);
        Main item14 = new Main(R.drawable.ic_privacy_policy, "Privacy Policy", 0, CLASS.PRIVACY_POLICY);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);
        list.add(item7);
        list.add(item8);
        list.add(item9);
        list.add(item10);
        list.add(item11);
        list.add(item12);
        list.add(item13);
        list.add(item14);
        adapter.notifyDataSetChanged();
        binding.bar.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    // Color Mode ----------------------------- Start
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("color_option")) {
            this.recreate();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_CODE) {
            this.recreate();
        }
    }
    // Color Mode -------------------------------- End

    @Override
    protected void onResume() {
        userInfo();
        nrItems();
        super.onResume();
    }
}