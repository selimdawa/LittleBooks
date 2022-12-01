package com.flatcode.littlebooksadmin.Unit;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.flatcode.littlebooksadmin.Model.Book;
import com.flatcode.littlebooksadmin.Model.Category;
import com.flatcode.littlebooksadmin.R;
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
                dialogDelete.dismiss();
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            dialogDelete.dismiss();
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public static void deleteCategory(Dialog dialogDelete, Context context, String id, String name) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("Deleting " + name + " ...");
        dialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
        reference.child(id).removeValue().addOnSuccessListener(unused1 -> {
            dialog.dismiss();
            Toast.makeText(context, "category Deleted Successfully...", Toast.LENGTH_SHORT).show();
            dialogDelete.dismiss();
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

    public static void isFavorite(final ImageView add, final String Id, final String UserId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.FAVORITES).child(UserId);
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
        if (image.getTag().equals("add")) {
            FirebaseDatabase.getInstance().getReference().child(DATA.FAVORITES).child(DATA.FirebaseUserUid)
                    .child(bookId).setValue(true);
        } else {
            FirebaseDatabase.getInstance().getReference().child(DATA.FAVORITES).child(DATA.FirebaseUserUid)
                    .child(bookId).removeValue();
        }
    }

    public static void checkLove(ImageView image, String bookId) {
        if (image.getTag().equals("love")) {
            FirebaseDatabase.getInstance().getReference().child(DATA.LOVES).child(bookId)
                    .child(DATA.FirebaseUserUid).setValue(true);
            incrementItemCount(DATA.BOOKS, bookId, DATA.LOVES_COUNT);
        } else {
            FirebaseDatabase.getInstance().getReference().child(DATA.LOVES).child(bookId)
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
                        DATA.EMPTY + bookUrl, DATA.EMPTY + bookTitle, false, false, null, null);
            }
        }).show();
    }

    public static void moreCategories(Context context, Category item) {
        String id = item.getId();
        String name = item.getCategory();
        String publisher = item.getPublisher();

        //options to show in dialog
        String[] options = {"Edit", "Delete"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options").setItems(options, (dialog, which) -> {
            //handle dialog option click
            if (which == 0) {
                //Edit clicked ,Open new activity to edit the book info
                VOID.IntentExtra(context, CLASS.CATEGORY_EDIT, DATA.CATEGORY_ID, id);
            } else if (which == 1) {
                //Delete Clicked
                dialogOptionDelete(context, DATA.EMPTY + publisher, null,
                        null, null, true, false, DATA.EMPTY + id, DATA.EMPTY + name);
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

    public static void dialogOptionDelete(Context context, String publisher, String bookId, String bookUrl, String bookTitle, boolean isCategory, boolean isEditorsChoice, String categoryId, String categoryName) {
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
        if (isCategory) {
            title.setText(R.string.do_you_want_to_delete_the_category);
        } else {
            title.setText(R.string.do_you_want_to_delete_the_book);
        }

        dialog.findViewById(R.id.yes).setOnClickListener(view -> {
            if (isCategory) {
                deleteCategory(dialog, context, categoryId, categoryName);
            } else if (isEditorsChoice) {
                dialogUpdateEditorsChoice(dialog, context, bookId);
            } else {
                deleteBook(dialog, context, publisher, bookId, bookUrl, bookTitle);
            }
        });

        dialog.findViewById(R.id.no).setOnClickListener(view2 -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public static void CropImageSquare(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIX_SQUARE, DATA.MIX_SQUARE)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static void CropImageSlider(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIX_SLIDER_X, DATA.MIX_SLIDER_Y)
                .setAspectRatio(16, 9)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static void dialogUpdateEditorsChoice(Dialog dialogDelete, Context context, String bookId) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Updating Editors Choice...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.EDITORS_CHOICE, 0);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        reference.child(bookId).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Editors Choice updated...", Toast.LENGTH_SHORT).show();
            dialogDelete.dismiss();
            //finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to update db duo to " + e.getMessage(), Toast.LENGTH_SHORT).show();
            dialogDelete.dismiss();
        });
    }

    public static void addToEditorsChoice(Context context, Activity activity, String bookId, int number) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Updating Editors Choice...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.EDITORS_CHOICE, number);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        reference.child(bookId).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Editors Choice updated...", Toast.LENGTH_SHORT).show();
            activity.finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to update db duo to " + e.getMessage(), Toast.LENGTH_SHORT).show();
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