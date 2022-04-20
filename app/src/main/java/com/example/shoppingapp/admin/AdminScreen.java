package com.example.shoppingapp.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shoppingapp.R;
import com.example.shoppingapp.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdminScreen extends AppCompatActivity {
    private int selectedValuePosition;
    private String selectedValue;
    private String[] values = new String[]{"S","M","L","XL","XXL","XXXL"};
    private ArrayAdapter<String> arrayAdapter;
    private Spinner spinner;
    private Uri imageUri;
    EditText mcreateitemname, mproductDescription, mproductPrice, mproductQuantity, mshippingCost;
    CircleImageView mproductImage;
    FloatingActionButton msaveProduct;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        mcreateitemname = findViewById(R.id.createitemname);
        mproductDescription = findViewById(R.id.productDescription);
        mproductPrice = findViewById(R.id.productPrice);
        mproductQuantity = findViewById(R.id.productQuantity);
        mshippingCost = findViewById(R.id.shippingCost);
        mproductImage = findViewById(R.id.productImage);
        msaveProduct = findViewById(R.id.saveProduct);
        spinner = findViewById(R.id.spinner);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Product Info ..");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, values);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedValue = values[i];
                selectedValuePosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mproductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewImage();
            }
        });
        msaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProducts();
            }
        });
    }

    private void saveProducts() {
        String productName = mcreateitemname.getText().toString();
        String productDescription  = mproductDescription.getText().toString();
        String productPrice = mproductPrice.getText().toString();
        String productQuantity = mproductQuantity.getText().toString();
        String productShippingCost = mshippingCost.getText().toString();

        if(TextUtils.isEmpty(productName)){
            Toast.makeText(this,"Product Name Field cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(productDescription)){
            Toast.makeText(this,"Product Description Field cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(productPrice)){
            Toast.makeText(this,"Product Price Field cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(productQuantity)){
            Toast.makeText(this,"Product Name Field cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(productShippingCost)){
            Toast.makeText(this,"Product Name Field cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(imageUri == null){
            Toast.makeText(this,"Please a product Image",Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedValue == null){
            Toast.makeText(this,"Please select a size",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        storageReference
                .child("Images")
                .child(String.valueOf(System.currentTimeMillis()))
                .putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            task.getResult().getStorage().getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String productID = String.valueOf(Calendar.getInstance().getTimeInMillis());
                                            DocumentReference documentReference = firebaseFirestore
                                                    .collection("Products")
                                                    .document(firebaseAuth.getCurrentUser().getUid())
                                                    .collection("allProducts")
                                                    .document(productID);
                                            Map<String , Object> productMap = new HashMap<>();
                                            productMap.put("productImage", task.getResult().toString());
                                            productMap.put("productName", productName);
                                            productMap.put("productDescription", productDescription);
                                            productMap.put("productPrice", productPrice);
                                            productMap.put("productQuantity", productQuantity);
                                            productMap.put("productShippingCost", productShippingCost);
                                            productMap.put("size",selectedValue);
                                            productMap.put("productID",productID);
                                            productMap.put("selectedPosition",selectedValuePosition);

                                            documentReference.set(productMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    mcreateitemname.getText().clear();
                                                    mproductDescription.getText().clear();
                                                    mproductPrice.getText().clear();
                                                    mproductQuantity.getText().clear();
                                                    mshippingCost.getText().clear();
                                                    imageUri = null;
                                                    mproductImage.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Product Successfully Added",Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();

                                                        }
                                                    });
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });

    }

    private void addNewImage() {
        CropImage
                .activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                mproductImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}