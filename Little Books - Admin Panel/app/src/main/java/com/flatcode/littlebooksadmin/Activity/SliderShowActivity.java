package com.flatcode.littlebooksadmin.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.THEME;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ActivitySliderShowBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.MessageFormat;
import java.util.HashMap;

public class SliderShowActivity extends AppCompatActivity {

    private ActivitySliderShowBinding binding;
    private final Activity activity;
    private final Context context = activity = SliderShowActivity.this;

    private Uri imageUri = null;
    private ProgressDialog dialog;

    private int IMAGE_NUMBER = 0;
    private int item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivitySliderShowBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.slider_show);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.addOne.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 1;
        });
        binding.addTwo.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 2;
        });
        binding.addThree.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 3;
        });
        binding.addFour.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 4;
        });
        binding.addFive.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 5;
        });
        binding.addSix.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 6;
        });
        binding.addSeven.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 7;
        });
        binding.addEight.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 8;
        });
        binding.addNine.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 9;
        });
        binding.addTeen.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 10;
        });
        binding.addEleven.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 11;
        });
        binding.addTwelfth.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 12;
        });
        binding.addThirteen.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 13;
        });
        binding.addFourteenth.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 14;
        });
        binding.addFifteenth.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 15;
        });
        binding.addSixteen.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 16;
        });
        binding.addSeventeen.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 17;
        });
        binding.addEighteen.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 18;
        });
        binding.addNineteen.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 19;
        });
        binding.addTwenty.setOnClickListener(v -> {
            VOID.CropImageSlider(activity);
            IMAGE_NUMBER = 20;
        });

        getNrSliderShow();
        SliderShow();
    }

    private void getNrSliderShow() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SLIDER_SHOW);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                item = (int) dataSnapshot.getChildrenCount();
                binding.toolbar.nameSpace.setText(MessageFormat.format("Slider Show ( {0} )", item));

                if (item >= 0) {
                    binding.linearOne.setVisibility(View.VISIBLE);
                } else {
                    binding.linearOne.setVisibility(View.GONE);
                }

                if (item >= 1) {
                    binding.linearTwo.setVisibility(View.VISIBLE);
                } else {
                    binding.linearTwo.setVisibility(View.GONE);
                }

                if (item >= 2) {
                    binding.linearThree.setVisibility(View.VISIBLE);
                } else {
                    binding.linearThree.setVisibility(View.GONE);
                }

                if (item >= 3) {
                    binding.linearFour.setVisibility(View.VISIBLE);
                } else {
                    binding.linearFour.setVisibility(View.GONE);
                }

                if (item >= 4) {
                    binding.linearFive.setVisibility(View.VISIBLE);
                } else {
                    binding.linearFive.setVisibility(View.GONE);
                }

                if (item >= 5) {
                    binding.linearSix.setVisibility(View.VISIBLE);
                } else {
                    binding.linearSix.setVisibility(View.GONE);
                }

                if (item >= 6) {
                    binding.linearSeven.setVisibility(View.VISIBLE);
                } else {
                    binding.linearSeven.setVisibility(View.GONE);
                }

                if (item >= 7) {
                    binding.linearEight.setVisibility(View.VISIBLE);
                } else {
                    binding.linearEight.setVisibility(View.GONE);
                }

                if (item >= 8) {
                    binding.linearNine.setVisibility(View.VISIBLE);
                } else {
                    binding.linearNine.setVisibility(View.GONE);
                }

                if (item >= 9) {
                    binding.linearTeen.setVisibility(View.VISIBLE);
                } else {
                    binding.linearTeen.setVisibility(View.GONE);
                }

                if (item >= 10) {
                    binding.linearEleven.setVisibility(View.VISIBLE);
                } else {
                    binding.linearEleven.setVisibility(View.GONE);
                }

                if (item >= 11) {
                    binding.linearTwelfth.setVisibility(View.VISIBLE);
                } else {
                    binding.linearTwelfth.setVisibility(View.GONE);
                }

                if (item >= 12) {
                    binding.linearThirteen.setVisibility(View.VISIBLE);
                } else {
                    binding.linearThirteen.setVisibility(View.GONE);
                }

                if (item >= 13) {
                    binding.linearFourteenth.setVisibility(View.VISIBLE);
                } else {
                    binding.linearFourteenth.setVisibility(View.GONE);
                }

                if (item >= 14) {
                    binding.linearFifteenth.setVisibility(View.VISIBLE);
                } else {
                    binding.linearFifteenth.setVisibility(View.GONE);
                }

                if (item >= 15) {
                    binding.linearSixteen.setVisibility(View.VISIBLE);
                } else {
                    binding.linearSixteen.setVisibility(View.GONE);
                }

                if (item >= 16) {
                    binding.linearEighteen.setVisibility(View.VISIBLE);
                } else {
                    binding.linearEighteen.setVisibility(View.GONE);
                }

                if (item >= 17) {
                    binding.linearEighteen.setVisibility(View.VISIBLE);
                } else {
                    binding.linearEighteen.setVisibility(View.GONE);
                }

                if (item >= 18) {
                    binding.linearNineteen.setVisibility(View.VISIBLE);
                } else {
                    binding.linearNineteen.setVisibility(View.GONE);
                }

                if (item >= 19) {
                    binding.linearTwenty.setVisibility(View.VISIBLE);
                } else {
                    binding.linearTwenty.setVisibility(View.GONE);
                }
                binding.bar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SliderShow() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SLIDER_SHOW);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String one = DATA.EMPTY + dataSnapshot.child("1").getValue();
                String two = DATA.EMPTY + dataSnapshot.child("2").getValue();
                String three = DATA.EMPTY + dataSnapshot.child("3").getValue();
                String four = DATA.EMPTY + dataSnapshot.child("4").getValue();
                String five = DATA.EMPTY + dataSnapshot.child("5").getValue();
                String six = DATA.EMPTY + dataSnapshot.child("6").getValue();
                String seven = DATA.EMPTY + dataSnapshot.child("7").getValue();
                String eight = DATA.EMPTY + dataSnapshot.child("8").getValue();
                String nine = DATA.EMPTY + dataSnapshot.child("9").getValue();
                String teen = DATA.EMPTY + dataSnapshot.child("10").getValue();
                String eleven = DATA.EMPTY + dataSnapshot.child("11").getValue();
                String twelfth = DATA.EMPTY + dataSnapshot.child("12").getValue();
                String thirteen = DATA.EMPTY + dataSnapshot.child("13").getValue();
                String fourteenth = DATA.EMPTY + dataSnapshot.child("14").getValue();
                String fifteenth = DATA.EMPTY + dataSnapshot.child("15").getValue();
                String sixteen = DATA.EMPTY + dataSnapshot.child("16").getValue();
                String seventeen = DATA.EMPTY + dataSnapshot.child("17").getValue();
                String eighteen = DATA.EMPTY + dataSnapshot.child("18").getValue();
                String nineteen = DATA.EMPTY + dataSnapshot.child("19").getValue();
                String twenty = DATA.EMPTY + dataSnapshot.child("20").getValue();

                VOID.Glide(false, context, one, binding.imageOne);
                VOID.Glide(false, context, two, binding.imageTwo);
                VOID.Glide(false, context, three, binding.imageThree);
                VOID.Glide(false, context, four, binding.imageFour);
                VOID.Glide(false, context, five, binding.imageFive);
                VOID.Glide(false, context, six, binding.imageSix);
                VOID.Glide(false, context, seven, binding.imageSeven);
                VOID.Glide(false, context, eight, binding.imageEight);
                VOID.Glide(false, context, nine, binding.imageNine);
                VOID.Glide(false, context, teen, binding.imageTeen);
                VOID.Glide(false, context, eleven, binding.imageEleven);
                VOID.Glide(false, context, twelfth, binding.imageTwelfth);
                VOID.Glide(false, context, thirteen, binding.imageThirteen);
                VOID.Glide(false, context, fourteenth, binding.imageFourteenth);
                VOID.Glide(false, context, fifteenth, binding.imageFifteenth);
                VOID.Glide(false, context, sixteen, binding.imageSixteen);
                VOID.Glide(false, context, seventeen, binding.imageSeventeen);
                VOID.Glide(false, context, eighteen, binding.imageEighteen);
                VOID.Glide(false, context, nineteen, binding.imageNineteen);
                VOID.Glide(false, context, twenty, binding.imageTwenty);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadImage(String name) {
        dialog.setMessage("Posting photo...");
        dialog.show();

        String filePathAndName = "Images/SliderShow/" + (DATA.EMPTY + name);

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedImageUrl = DATA.EMPTY + uriTask.getResult();

            updateImage(uploadedImageUrl, (DATA.EMPTY + name));
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateImage(String imageUrl, String name) {
        dialog.setMessage("Posting photo...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        if (imageUri != null) {
            hashMap.put((DATA.EMPTY + name), DATA.EMPTY + imageUrl);
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SLIDER_SHOW);
        reference.updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "The photo has been posted", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = CropImage.getPickImageResultUri(context, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(context, uri)) {
                imageUri = uri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                VOID.CropImageSlider(activity);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                uploadImage((DATA.EMPTY + IMAGE_NUMBER));
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error! " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}