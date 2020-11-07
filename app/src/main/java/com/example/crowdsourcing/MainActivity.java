package com.example.crowdsourcing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Start foreground service that will schedule the various Jobs
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateDatabase();

        // check if user already accept permission or not
        if (checkPermissions()) {
            startService();
        }
    }

    // start Start service
    private void startService() {
        Intent serviceIntent = new Intent(getApplicationContext(), ForegroundService.class);
        ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
    }

    // check require permission for application for scan wifi
    private boolean checkPermissions() {
        boolean permissionGrant = false;
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        } else {
            permissionGrant = true;
        }
        return permissionGrant;

    }

    public void CreateDatabase() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db").build();
        Toast.makeText(this, "Database is Created",
                Toast.LENGTH_LONG).show();
    }

    // handle if user have accept permission request or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            return;
        }

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(requestCode == PERMISSION_REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            startService();
        }
    }
}