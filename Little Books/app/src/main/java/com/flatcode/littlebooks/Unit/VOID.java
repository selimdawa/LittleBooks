package com.flatcode.littlebooks.Unit;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.flatcode.littlebooks.BuildConfig;
import com.flatcode.littlebooks.Model.ADs;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class VOID {

    public static void IntentClear(Context context, Class c) {
        Intent intent = new Intent(context, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void Intent(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void IntentExtra(Context context, Class c, String key, String value) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    public static void IntentExtra2(Context context, Class c, String key, String value, String key2, String value2) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        context.startActivity(intent);
    }

    public static void IntentExtra3(Context context, Class c, String key, String value,
                                    String key2, String value2, String key3, String value3) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        intent.putExtra(key3, value3);
        context.startActivity(intent);
    }

    public static void deleteBook(Dialog dialogDelete, Context context, String publisher, String bookId, String bookUrl, String bookTitle) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("Deleting " + bookTitle + " ...");
        dialog.show();

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        storageReference.delete().addOnSuccessListener(unused -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
            reference.child(bookId).removeValue().addOnSuccessListener(unused1 -> {
                dialog.dismiss();
                Toast.makeText(context, "Books Deleted Successfully...", Toast.LENGTH_SHORT).show();
                dialogDelete.dismiss();
                incrementItemRemoveCount(DATA.USERS, publisher, DATA.BOOKS_COUNT);
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public static void loadPdfInfo(String pdfUrl, TextView size) {
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getMetadata().addOnSuccessListener(storageMetadata -> {
            //get size in bytes
            double bytes = storageMetadata.getSizeBytes();
            //convert bytes to KB, MB
            double kb = bytes / 1024;
            double mb = kb / 1024;

            if (mb > 1)
                size.setText(String.format("%.2f", mb) + " MB");
            else if (kb > 1)
                size.setText(String.format("%.2f", mb) + " MB");
            else
                size.setText(String.format("%.2f", bytes) + " bytes");
        });
    }

    public static void loadCategory(String categoryId, TextView category) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
        ref.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String Category = DATA.EMPTY + snapshot.child(DATA.CATEGORY).getValue();
                category.setText(Category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void incrementItemCount(String database, String id, String childDB) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(database);
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get views count
                String itemsCount = DATA.EMPTY + snapshot.child(childDB).getValue();
                if (itemsCount.equals(DATA.EMPTY) || itemsCount.equals(DATA.NULL))
                    itemsCount = DATA.EMPTY + DATA.ZERO;

                int newItemsCount = Integer.parseInt(itemsCount) + 1;
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(childDB, newItemsCount);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(database);
                reference.child(id).updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void incrementItemRemoveCount(String database, String id, String childDB) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(database);
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get views count
                String lovesCount = DATA.EMPTY + snapshot.child(childDB).getValue();
                if (lovesCount.equals(DATA.EMPTY) || lovesCount.equals(DATA.NULL))
                    lovesCount = DATA.EMPTY + DATA.ZERO;

                int i = Integer.parseInt(lovesCount);
                if (i > 0) {
                    int removeLovesCount = Integer.parseInt(lovesCount) - 1;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(childDB, removeLovesCount);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(database);
                    reference.child(id).updateChildren(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void downloadBook(Context context, String bookId, String bookTitle, String bookUrl) {

        String nameWithExtension = bookTitle + ".pdf";

        //progress dialog
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Downloading " + nameWithExtension + "..."); //e-9. Downloading ABC_Book.paf
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //download from firebase storage using url
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        storageReference.getBytes(DATA.MAX_BYTES_PDF).addOnSuccessListener(bytes -> {
            saveDownloadedBook(context, progressDialog, bytes, nameWithExtension, bookId);
            incrementItemCount(DATA.BOOKS, bookId, DATA.DOWNLOADS_COUNT);
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(context, "Failed to download due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private static void saveDownloadedBook(Context context, ProgressDialog progressDialog, byte[] bytes, String nameWithExtension, String bookId) {
        try {
            File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            downloadsFolder.mkdirs();

            String FilePath = downloadsFolder.getPath() + "/" + nameWithExtension;

            FileOutputStream out = new FileOutputStream(FilePath);
            out.write(bytes);
            out.close();

            Toast.makeText(context, "Saved to Download Folder", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

            incrementItemCount(DATA.BOOKS, bookId, DATA.DOWNLOADS_COUNT);
        } catch (Exception e) {
            Toast.makeText(context, "Failed saving to Download Folder due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    public static void Glide(Boolean isUser, Context context, String Url, ImageView Image) {
        try {
            if (Url.equals(DATA.BASIC)) {
                if (isUser) {
                    Image.setImageResource(R.drawable.basic_user);
                } else {
                    Image.setImageResource(R.drawable.basic_book);
                }
            } else {
                Glide.with(context).load(Url).placeholder(R.color.image_profile).into(Image);
            }
        } catch (Exception e) {
            Image.setImageResource(R.drawable.basic_book);
        }
    }

    public static void GlideBlur(Boolean isUser, Context context, String Url, ImageView Image, int level) {
        try {
            if (Url.equals(DATA.BASIC)) {
                if (isUser) {
                    Image.setImageResource(R.drawable.basic_user);
                } else {
                    Image.setImageResource(R.drawable.basic_book);
                }
            } else {
                Glide.with(context).load(Url).placeholder(R.color.image_profile)
                        .apply(bitmapTransform(new BlurTransformation(level))).into(Image);
            }
        } catch (Exception e) {
            Image.setImageResource(R.drawable.basic_book);
        }
    }

    public static void closeApp(Context context, Activity a) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_close_app);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.yes).setOnClickListener(v -> a.finish());
        dialog.findViewById(R.id.no).setOnClickListener(v -> dialog.cancel());

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);
    }

    public static void dialogLogout(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.yes).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            IntentClear(context, CLASS.AUTH);
        });

        dialog.findViewById(R.id.no).setOnClickListener(v -> dialog.cancel());

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);
    }

    public static void shareApp(Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share app");
        shareIntent.putExtra(Intent.EXTRA_TEXT, " Download the app now from Google Play " + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        context.startActivity(Intent.createChooser(shareIntent, "Choose how to share"));
    }

    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void dialogAboutApp(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about_app);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(getWebsiteIntent());
            }

            public Intent getWebsiteIntent() {
                return new Intent(Intent.ACTION_VIEW, Uri.parse(DATA.WEB_SITE));
            }
        });

        dialog.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(getOpenFacebookIntent());
            }

            public Intent getOpenFacebookIntent() {
                try {
                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + DATA.FB_ID));
                } catch (Exception e) {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + DATA.FB_ID));
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public static void isFavorite(final ImageView add, final String Id, final String UserId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.FAVORITES).child(UserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Id).exists()) {
                    add.setImageResource(R.drawable.ic_star_selected);
                    add.setTag("added");
                } else {
                    add.setImageResource(R.drawable.ic_star_unselected);
                    add.setTag("add");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void checkFavorite(ImageView image, String bookId) {
        if (image.getTag().equals("add"))
            FirebaseDatabase.getInstance().getReference(DATA.FAVORITES).child(DATA.FirebaseUserUid)
                    .child(bookId).setValue(true);
        else
            FirebaseDatabase.getInstance().getReference(DATA.FAVORITES).child(DATA.FirebaseUserUid)
                    .child(bookId).removeValue();
    }

    public static void checkLove(ImageView image, String bookId) {
        if (image.getTag().equals("love")) {
            FirebaseDatabase.getInstance().getReference(DATA.LOVES).child(bookId)
                    .child(DATA.FirebaseUserUid).setValue(true);
            incrementItemCount(DATA.BOOKS, bookId, DATA.LOVES_COUNT);
        } else {
            FirebaseDatabase.getInstance().getReference(DATA.LOVES).child(bookId)
                    .child(DATA.FirebaseUserUid).removeValue();
            incrementItemRemoveCount(DATA.BOOKS, bookId, DATA.LOVES_COUNT);
        }
    }

    public static void moreOptionDialog(Context context, Book item) {
        String bookId = item.getId();
        String bookUrl = item.getUrl();
        String bookTitle = item.getTitle();
        String publisher = item.getPublisher();

        //options to show in dialog
        String[] options = {"Edit", "Delete"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options").setItems(options, (dialog, which) -> {
            //handle dialog option click
            if (which == 0) {
                //Edit clicked ,Open new activity to edit the book info
                IntentExtra(context, CLASS.BOOK_EDIT, DATA.BOOK_ID, bookId);
            } else if (which == 1) {
                //Delete Clicked
                dialogOptionDelete(context, DATA.EMPTY + publisher, DATA.EMPTY + bookId,
                        DATA.EMPTY + bookUrl, DATA.EMPTY + bookTitle);
            }
        }).show();
    }

    public static void isLoves(ImageView image, String bookId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.LOVES).child(bookId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(DATA.FirebaseUserUid).exists()) {
                    image.setImageResource(R.drawable.ic_heart_selected);
                    image.setTag("loved");
                } else {
                    image.setImageResource(R.drawable.ic_heart_unselected);
                    image.setTag("love");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void nrLoves(TextView number, String bookId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.LOVES).child(bookId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                number.setText(MessageFormat.format(" {0} ", dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void dialogOptionDelete(Context context, String publisher, String bookId, String bookUrl, String bookTitle) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView title = dialog.findViewById(R.id.title);
        title.setText(R.string.do_you_want_to_delete_the_book);

        dialog.findViewById(R.id.yes).setOnClickListener(view -> deleteBook(dialog, context, publisher, bookId, bookUrl, bookTitle));
        dialog.findViewById(R.id.no).setOnClickListener(view2 -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public static void CropImageSquare(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIN_SQUARE, DATA.MIN_SQUARE)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static void BannerAd(Context context, AdView adView, String bannerName) {
        MobileAds.initialize(context, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                AdUserCount(DATA.FirebaseUserUid, DATA.AD_LOAD, 1);
                AdCount(DATA.FirebaseUserUid, bannerName, DATA.ADS_LOADED_COUNT);
            }

            @Override
            public void onAdOpened() {
                AdUserCount(DATA.FirebaseUserUid, DATA.AD_CLICK, 1);
                AdCount(DATA.FirebaseUserUid, bannerName, DATA.ADS_CLICKED_COUNT);
            }
        });
    }

    public static void BannerAdTwo(Context context, AdView adView, String bannerName, AdView adView2, String bannerName2) {
        MobileAds.initialize(context, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdOpened() {
                AdUserCount(DATA.FirebaseUserUid, DATA.AD_CLICK, 1);
                AdCount(DATA.FirebaseUserUid, bannerName, DATA.ADS_CLICKED_COUNT);
            }
        });

        adView2.loadAd(adRequest);
        adView2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                AdUserCount(DATA.FirebaseUserUid, DATA.AD_LOAD, 2);
                AdCount(DATA.FirebaseUserUid, bannerName, DATA.ADS_LOADED_COUNT);
                AdCount(DATA.FirebaseUserUid, bannerName2, DATA.ADS_LOADED_COUNT);
            }

            @Override
            public void onAdOpened() {
                AdUserCount(DATA.FirebaseUserUid, DATA.AD_CLICK, 1);
                AdCount(DATA.FirebaseUserUid, bannerName2, DATA.ADS_CLICKED_COUNT);
            }
        });
    }

    public static void AdCount(String userId, String bannerName, String key) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.AD_S).child(userId);
        ref.child(bannerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get views count
                String adCount = DATA.EMPTY + snapshot.child(key).getValue();
                if (adCount.equals(DATA.EMPTY) || adCount.equals(DATA.NULL)) {
                    adCount = "0";
                }

                long newAdCount = Long.parseLong(adCount) + 1;
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(key, newAdCount);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.AD_S).child(userId);
                reference.child(bannerName).updateChildren(hashMap).addOnCompleteListener(
                        task -> AdName(DATA.FirebaseUserUid, bannerName)
                );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void AdUserCount(String userId, String key, int number) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get views count
                String adCount = DATA.EMPTY + snapshot.child(key).getValue();
                if (adCount.equals(DATA.EMPTY) || adCount.equals(DATA.NULL)) {
                    adCount = "0";
                }

                long newAdCount = Long.parseLong(adCount) + number;
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(key, newAdCount);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(userId);
                reference.updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void AdName(String userId, String bannerName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.AD_S).child(userId);
        ref.child(bannerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get views count
                ADs item = snapshot.getValue(ADs.class);
                assert item != null;
                if (item.getName() == null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(DATA.NAME, bannerName);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.AD_S).child(userId);
                    reference.child(bannerName).updateChildren(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void Intro(Context context, ImageView background, ImageView backWhite, ImageView backDark) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE")) {
            background.setImageResource(R.drawable.background_day);
            backWhite.setVisibility(View.VISIBLE);
            backDark.setVisibility(View.GONE);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE")) {
            background.setImageResource(R.drawable.background_night);
            backWhite.setVisibility(View.GONE);
            backDark.setVisibility(View.VISIBLE);
        }
    }

    public static void Logo(Context context, ImageView background) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE")) {
            background.setImageResource(R.drawable.logo);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE")) {
            background.setImageResource(R.drawable.logo_night);
        }
    }

    public static String getFileExtension(Uri uri, Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}