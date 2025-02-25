package com.vsb.ala.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vsb.ala.R;
import com.vsb.ala.model.object.Office;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {

private FirebaseUser office;
private DatabaseReference reference;
private String officeID;
private CardView crd_new_product, crd_product, crd_order,crd_report,  crd_info, crd_log_out;
private ProgressBar progress_bar;

    TextView txt_name;
    String name, address, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        txt_name = findViewById(R.id.txt_name);
        crd_new_product = findViewById(R.id.crd_new_product);
        crd_product = findViewById(R.id.crd_products);
        crd_order = findViewById(R.id.crd_order);
        crd_report = findViewById(R.id.crd_report);
        crd_info = findViewById(R.id.crd_info);
        crd_log_out = findViewById(R.id.crd_log_out);
        progress_bar = findViewById(R.id.progress_bar);

        office = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Office");
        officeID = office.getUid();


        progress_bar.setVisibility(View.VISIBLE);

        reference.child(officeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Office officeProfile = snapshot.getValue(Office.class);

                if (officeProfile != null){
                     name = officeProfile.name;
                     address = officeProfile.address;
                     email = officeProfile.email;

                    txt_name.setText(name);
                progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        crd_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NewProductActivity.class));
            }
        });

        crd_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, WarehouseActivity.class));
            }
        });

        crd_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, OrderActivity.class));
            }
        });

        crd_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ReportActivity.class));
            }
        });

        crd_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("address", address);
                startActivity(intent);
            }
        });

        crd_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                Toast.makeText(MenuActivity.this, "Log out successful!", Toast.LENGTH_LONG).show();
            }
        });


    }
}