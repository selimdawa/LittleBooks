package com.flatcode.littlebooks.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.flatcode.littlebooks.Adapter.SettingAdapter;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.Model.Setting;
import com.flatcode.littlebooks.Model.User;
import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.FragmentSettingsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private ArrayList<Setting> list;
    private SettingAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(LayoutInflater.from(getContext()), container, false);

        binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new SettingAdapter(getContext(), list);
        binding.recyclerView.setAdapter(adapter);

        binding.toolbar.item.setOnClickListener(v ->
                VOID.IntentExtra(getContext(), CLASS.PROFILE, DATA.PROFILE_ID, DATA.FirebaseUserUid));

        return binding.getRoot();
    }

    int E = 0, M = 0, FS = 0, FN = 0, FA = 0;

    private void nrItems() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                E = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User item = snapshot.getValue(User.class);
                    assert item != null;
                    if (!item.getId().equals(DATA.FirebaseUserUid))
                        if (item.getBooksCount() >= 1)
                            E++;
                }
                nrBooks();
            }

            private void nrBooks() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        M = 0;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Book item = data.getValue(Book.class);
                            assert item != null;
                            if (item.getPublisher().equals(DATA.FirebaseUserUid))
                                M++;
                        }
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
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FS = 0;
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
                        loadSettings(E, M, FS, FN, FA);
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

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(Objects.requireNonNull(DATA.FirebaseUserUid)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User item = snapshot.getValue(User.class);
                assert item != null;
                String ProfileImage = item.getProfileImage();
                String Username = item.getUsername();
                String Contact = item.getEmail();
                VOID.Glide(true, getContext(), ProfileImage, binding.toolbar.imageProfile);
                binding.toolbar.username.setText(Username);
                binding.toolbar.email.setText(Contact);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadSettings(int explorePublishers, int myBooks, int followers, int following, int favorites) {
        list.clear();
        Setting item = new Setting("1", "Edit Profile", R.drawable.ic_edit_white, 0, CLASS.PROFILE_EDIT);
        Setting item2 = new Setting("2", "Explore Publishers", R.drawable.ic_search_person, explorePublishers, CLASS.EXPLORE_PUBLISHERS);
        Setting item3 = new Setting("3", "Followers", R.drawable.ic_followers, followers, CLASS.FOLLOWERS);
        Setting item4 = new Setting("4", "Following", R.drawable.ic_following, following, CLASS.FOLLOWING);
        Setting item5 = new Setting("5", "My books", R.drawable.ic_books, myBooks, CLASS.MY_BOOKS);
        Setting item6 = new Setting("6", "Add book", R.drawable.ic_book_white, 0, CLASS.BOOK_ADD);
        Setting item7 = new Setting("7", "Favorites", R.drawable.ic_star_selected, favorites, CLASS.FAVORITES);
        Setting item8 = new Setting("8", "About App", R.drawable.ic_info, 0, null);
        Setting item9 = new Setting("9", "Logout", R.drawable.ic_logout_white, 0, null);
        Setting item10 = new Setting("10", "Share App", R.drawable.ic_share, 0, null);
        Setting item11 = new Setting("11", "Rate APP", R.drawable.ic_heart_selected, 0, null);
        Setting item12 = new Setting("12", "Privacy Policy", R.drawable.ic_privacy_policy, 0, CLASS.PRIVACY_POLICY);
        list.add(item);
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        loadUserInfo();
        nrItems();
        super.onResume();
    }
}