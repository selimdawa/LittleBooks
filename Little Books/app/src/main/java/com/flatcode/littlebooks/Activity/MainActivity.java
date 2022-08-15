package com.flatcode.littlebooks.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.flatcode.littlebooks.Fragment.CategoriesFragment;
import com.flatcode.littlebooks.Fragment.FollowersFragment;
import com.flatcode.littlebooks.Fragment.HomeFragment;
import com.flatcode.littlebooks.Fragment.SettingsFragment;
import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityMainBinding;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityMainBinding binding;
    Activity activity;
    Context context = activity = MainActivity.this;

    MeowBottomNavigation meowBottomNavigation;

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
                .replace(R.id.settings, new MainActivity.SettingFragment())
                .commit();
        // Color Mode -------------------------------- End

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        if (sharedPreferences.getString(DATA.COLOR_OPTION, "ONE").equals("ONE")) {
            binding.toolbar.mode.setBackgroundResource(R.drawable.sun);
        } else if (sharedPreferences.getString(DATA.COLOR_OPTION, "NIGHT_ONE").equals("NIGHT_ONE")) {
            binding.toolbar.mode.setBackgroundResource(R.drawable.moon);
        }

        meowBottomNavigation = binding.bottomNavigation;

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_settings));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_books));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_group));
        meowBottomNavigation.setOnShowListener(item -> {
            Fragment fragment = null;
            switch (item.getId()) {
                case 1:
                    binding.toolbar.card.setVisibility(View.GONE);
                    fragment = new SettingsFragment();
                    break;
                case 2:
                    binding.toolbar.card.setVisibility(View.VISIBLE);
                    fragment = new HomeFragment();
                    break;
                case 3:
                    binding.toolbar.card.setVisibility(View.GONE);
                    fragment = new FollowersFragment();
                    break;
                case 4:
                    binding.toolbar.card.setVisibility(View.GONE);
                    fragment = new CategoriesFragment();
                    break;
            }
            loadFragment(fragment);
        });

        //meowBottomNavigation.setCount(3, numberBooks);

        meowBottomNavigation.show(2, true);

        meowBottomNavigation.setOnClickMenuListener(item -> {
            switch (item.getId()) {
                case 1:
                    Toast.makeText(getApplicationContext(), R.string.settings, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), R.string.home, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), R.string.followers_books, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), R.string.categories, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        meowBottomNavigation.setOnReselectListener(item -> {
            switch (item.getId()) {
                case 1:
                    Toast.makeText(getApplicationContext(), R.string.settings, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), R.string.home, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), R.string.followers_books, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), R.string.categories, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        MobileAds.initialize(getApplicationContext(), initializationStatus -> {
        });

        binding.toolbar.image.setOnClickListener(v ->
                VOID.IntentExtra(context, CLASS.PROFILE, DATA.PROFILE_ID, DATA.FirebaseUserUid));

        loadUserInfo();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                fragment).commit();
    }

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(Objects.requireNonNull(DATA.FirebaseUserUid)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileImage = DATA.EMPTY + snapshot.child(DATA.PROFILE_IMAGE).getValue();
                VOID.Glide(true, context, profileImage, binding.toolbar.image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        VOID.closeApp(context, activity);
    }

    // Color Mode ----------------------------- Start
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(DATA.COLOR_OPTION)) {
            this.recreate();
        }
    }

    public static class SettingFragment extends PreferenceFragmentCompat {
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
}