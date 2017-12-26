package com.teamerp.ipechackathon.ipechackathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class qr extends AppCompatActivity {

    ImageView qr_img;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Qr Code");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        setSupportActionBar(toolbar);

        final SharedPreferences prefs = getSharedPreferences("DB_NAME",MODE_PRIVATE);
        String toll_id = prefs.getString("toll_id", "");
        qr_img=(ImageView)findViewById(R.id.qr_img);

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Log.e("type123", DbHandler.getString(qr.this,"type",""));
        OrderRequest orderRequest=ServiceGenerator.createService(OrderRequest.class);
        Call<OrderPOJO> call=orderRequest.call(DbHandler.getString(qr.this,"tid",""),DbHandler.getString(qr.this,"amount",""),"60",DbHandler.getString(qr.this,"route",""));
        call.enqueue(new Callback<OrderPOJO>() {
            @Override
            public void onResponse(Call<OrderPOJO> call, Response<OrderPOJO> response) {
                if(response.code()==200){
                    Picasso
                            .with(qr.this)
                            .load(response.body().getUrl())
                            .into(qr_img);
//                    try {
//                        bitmap = TextToImageEncode(response.body().getOrder_id());
//
//                        qr_img.setImageBitmap(bitmap);
//
//                    } catch (WriterException e) {
//                        e.printStackTrace();
//                    }
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(qr.this,"Error connecting to server",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderPOJO> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(qr.this,"Error connecting to server",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    500, 500, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
