package com.example.anull.findsuppoter.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.anull.findsuppoter.R;
import com.example.anull.findsuppoter.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "";
    Button bt_signIn, bt_Register;
    Animation downtoup;
    LinearLayout ln_button;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase db;
    DatabaseReference users;

    RelativeLayout loginLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_login);

        setUpInput();

        if (firebaseAuth.getCurrentUser() != null) {

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            ln_button.setAnimation(downtoup);

            bt_Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRegisterDialog();
                }

            });

            bt_signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLoginDialog();
                }

            });
        }
    }

    private void showLoginDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Use Email To Login");


        LayoutInflater inflater = LayoutInflater.from(this);

        View signIn_layout = inflater.inflate(R.layout.layout_signin, null);

        final MaterialEditText mt_email = signIn_layout.findViewById(R.id.et_email);
        final MaterialEditText mt_pass = signIn_layout.findViewById(R.id.et_password);

        dialog.setView(signIn_layout);
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                // dissable bt signin if is processing

                if (TextUtils.isEmpty(mt_email.getText().toString())) {
                    Snackbar.make(loginLayout, "Enter Your Email Adress", Snackbar.LENGTH_SHORT)
                            .show();
                    bt_signIn.setEnabled(true);
                    return;
                }


                if (TextUtils.isEmpty(mt_pass.getText().toString())) {
                    Snackbar.make(loginLayout, "Enter Your Password", Snackbar.LENGTH_SHORT)
                            .show();
                    bt_signIn.setEnabled(true);
                    return;
                }

                if ((mt_pass.getText().toString().length() < 6)) {
                    Snackbar.make(loginLayout, "Password must be more 6 character", Snackbar.LENGTH_SHORT)
                            .show();
                    bt_signIn.setEnabled(true);
                    return;
                }

                final SpotsDialog waitingDialog = new SpotsDialog(LoginActivity.this);
                waitingDialog.show();

                // login

                firebaseAuth.signInWithEmailAndPassword(mt_email.getText().toString(),
                        mt_pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            waitingDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            finish();

                        } else {
                            waitingDialog.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            // enable bt
                            bt_signIn.setEnabled(true);
                        }
                    }
                });


            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        dialog.show();


    }

    private void showRegisterDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Use Email To Register");


        LayoutInflater inflater = LayoutInflater.from(this);

        View register_layout = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText mt_email = register_layout.findViewById(R.id.et_email);
        final MaterialEditText mt_pass = register_layout.findViewById(R.id.et_password);
        final MaterialEditText mt_name = register_layout.findViewById(R.id.et_name);
        final MaterialEditText mt_phone = register_layout.findViewById(R.id.et_phone);

        dialog.setView(register_layout);
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                if (TextUtils.isEmpty(mt_email.getText().toString())) {
                    Snackbar.make(loginLayout, "Enter Your Email Adress", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(mt_phone.getText().toString())) {
                    Snackbar.make(loginLayout, "Enter Your Phone Number", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(mt_pass.getText().toString())) {
                    Snackbar.make(loginLayout, "Enter Your Password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if ((mt_pass.getText().toString().length() < 6)) {
                    Snackbar.make(loginLayout, "Password must be more 6 character", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }


                if (TextUtils.isEmpty(mt_name.getText().toString())) {
                    Snackbar.make(loginLayout, "Enter Your Name!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(mt_email.getText().toString(),
                        mt_pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //save user to db
                            User user = new User();
                            user.setLocation("0,0");
                            user.setEmail(mt_email.getText().toString());
                            user.setPassword(mt_pass.getText().toString());
                            user.setPhone(mt_phone.getText().toString());
                            user.setName(mt_name.getText().toString());
                            user.setAvailable("0");
                            user.setRating("3.5");

                            final SpotsDialog waitingDialog = new SpotsDialog(LoginActivity.this);
                            waitingDialog.show();

                            //use key = email
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                waitingDialog.dismiss();
                                                Snackbar.make(loginLayout, "Register Success Fully"
                                                        , Snackbar.LENGTH_SHORT).show();
                                                firebaseAuth.signInWithEmailAndPassword(mt_email.getText().toString(),
                                                        mt_pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            waitingDialog.dismiss();
                                                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                            finish();

                                                        } else {
                                                            waitingDialog.dismiss();
                                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                                                    Toast.LENGTH_SHORT).show();

                                                            // enable bt
                                                            bt_signIn.setEnabled(true);
                                                        }
                                                    }
                                                });

                                            } else {
                                                waitingDialog.dismiss();
                                                Toast.makeText(LoginActivity.this, task.getException().getMessage()
                                                        , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage()
                                    , Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void setUpInput() {
        bt_Register = findViewById(R.id.bt_register);
        bt_signIn = findViewById(R.id.bt_signin);
        loginLayout = findViewById(R.id.login_layout);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downto_up);
        ln_button = findViewById(R.id.ln_button);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

    }
}
