package edu.uci.cs297p.arfurniture.seller;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uci.cs297p.arfurniture.R;
import edu.uci.cs297p.arfurniture.item.Item;

public class SellerFragment extends Fragment implements PostItemListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller, viewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayout gridLayout = view.findViewById(R.id.seller_category_grid);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            @Item.Category final int category = i;
            cardView.setOnClickListener(clickedView -> {
                showPostItemDialog(category);
            });

        }
    }

    private void showPostItemDialog(@Item.Category int category) {
        PostItemFragment postItemFragment = new PostItemFragment(getContext(), category);
        // SETS the target fragment for use later when sending results
        postItemFragment.setTargetFragment(SellerFragment.this, 300);
        postItemFragment.show(getFragmentManager(), "fragment_edit_name");
    }

    @Override
    public void onSubmit(Bundle args) {
        Log.d("SellerFragment", "onSubmit " + args.toString());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        List<Task<Uri>> uploadTasks = new ArrayList<>();
        List<String> imageUrlList = new ArrayList<>();

        // Post item to the backend
        Map<String, Object> itemData = new HashMap<>();
        for (String key : args.keySet()) {
            // Handle scale vector
            if (PostItemFragment.SCALE_KEY.equals(key)) {
                float[] data = args.getFloatArray(key);
                if (data != null && data.length == 3) {
                    List<Double> scale = new ArrayList<>();
                    scale.add((double) data[0]);
                    scale.add((double) data[1]);
                    scale.add((double) data[2]);
                    itemData.put(key, scale);
                }
            }
            // Upload picture list
            else if (PostItemFragment.PICTURE_KEY.equals(key)) {
                List<Bitmap> pictureList = args.getParcelableArrayList(key);

                for (Bitmap bitmap : pictureList) {

                    StorageReference ref = storageRef.child(System.nanoTime() + Integer.toHexString(hashCode()) + ".jpg");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = ref.putBytes(data);
                    uploadTask.addOnFailureListener(
                            exception -> Log.d("SellerFragment", "Uploading picture failed " + bitmap.toString())
                    ).addOnSuccessListener(
                            taskSnapshot -> Log.d("SellerFragment", "Uploading picture succeed " + bitmap.toString())
                    );

                    Task<Uri> uriTask = uploadTask.continueWithTask((Task<UploadTask.TaskSnapshot> task) -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();

                    }).addOnCompleteListener((Task<Uri> task) -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            imageUrlList.add(downloadUri.toString());
                        }
                    });

                    uploadTasks.add(uriTask);

                }
                itemData.put("imageURLs", imageUrlList);
            } else {
                itemData.put(key, args.get(key));
            }
        }

        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener((task) -> {

            Toast.makeText(getContext(), "Uploaded " + imageUrlList.size() + " images", Toast.LENGTH_SHORT).show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("items")
                    .add(itemData)
                    .addOnSuccessListener((DocumentReference documentReference) ->
                            Log.d("SellerFragment", "DocumentSnapshot written with ID: " + documentReference.getId())
                    )
                    .addOnFailureListener((@NonNull Exception e) ->
                            Log.e("SellerFragment", "Error adding document", e)
                    );
        });
    }
}

// Defines the listener interface
interface PostItemListener {
    void onSubmit(Bundle args);
}
