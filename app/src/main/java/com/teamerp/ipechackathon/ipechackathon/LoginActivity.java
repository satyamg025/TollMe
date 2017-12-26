package com.teamerp.ipechackathon.ipechackathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText car_number;
    AppCompatButton submit;
    TextInputLayout til;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ServiceGenerator.getHttpClient().addInterceptor(new AddCookiesInterceptor(LoginActivity.this));
        ServiceGenerator.getHttpClient().addInterceptor(new ReceivedCookiesInterceptor(LoginActivity.this));
        ServiceGenerator.setClient();
        ServiceGenerator.setBuilder();

        car_number=(EditText)findViewById(R.id.car_number);
        submit=(AppCompatButton)findViewById(R.id.submit);
        til=(TextInputLayout)findViewById(R.id.til);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(car_number.getText().toString().equals("")){
                    til.setError("Car number required");
                }
                else{


                    progressDialog=new ProgressDialog(LoginActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Authenticating....");
                    progressDialog.show();

                    LoginRequest loginRequest=ServiceGenerator.createService(LoginRequest.class);
                    Call<LoginPOJO> call=loginRequest.call(car_number.getText().toString());
                    call.enqueue(new Callback<LoginPOJO>() {
                        @Override
                        public void onResponse(Call<LoginPOJO> call, Response<LoginPOJO> response) {
                            progressDialog.dismiss();

                            if(response.code()==200){
                                LoginPOJO data=response.body();
                                if(data.getError()){
                                    til.setError(data.getMessage());
                                }
                                else{
                                    til.setHint("Enter OTP");
                                    car_number.setText("");
                                    til.setError("");

                                    submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(car_number.getText().toString().equals("")){
                                                til.setError("OTP required");
                                            }
                                            else{

                                                progressDialog=new ProgressDialog(LoginActivity.this);
                                                progressDialog.setCancelable(false);
                                                progressDialog.setMessage("Authenticating....");
                                                progressDialog.show();

                                                OTPRequest otpRequest=ServiceGenerator.createService(OTPRequest.class);
                                                Call<OTPPOJO> call1=otpRequest.call(car_number.getText().toString());
                                                call1.enqueue(new Callback<OTPPOJO>() {
                                                    @Override
                                                    public void onResponse(Call<OTPPOJO> call, Response<OTPPOJO> response) {
                                                        progressDialog.dismiss();
                                                        if(response.code()==200) {
                                                            OTPPOJO data = response.body();
                                                            if (data.getError()) {
                                                                til.setError(data.getMessage());

                                                            }
                                                            else{
                                                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                        else{
                                                            Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_INDEFINITE)
                                                                    .show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<OTPPOJO> call, Throwable t) {
                                                        progressDialog.dismiss();
                                                        Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_INDEFINITE)
                                                                .show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }

                            }
                            else{
                                Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_INDEFINITE)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginPOJO> call, Throwable t) {
                            progressDialog.dismiss();
                            Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_INDEFINITE)
                                    .show();
                        }
                    });
                }
            }
        });
    }
}
