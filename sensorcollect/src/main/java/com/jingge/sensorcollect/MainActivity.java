package com.jingge.sensorcollect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.jingge.sensorcollect.pojo.AccData;
import com.jingge.sensorcollect.pojo.GravityData;
import com.jingge.sensorcollect.pojo.GyroData;
import com.jingge.sensorcollect.pojo.HumidData;
import com.jingge.sensorcollect.pojo.LightData;
import com.jingge.sensorcollect.pojo.LocationData;
import com.jingge.sensorcollect.pojo.MagneData;
import com.jingge.sensorcollect.pojo.OrientData;
import com.jingge.sensorcollect.pojo.PressData;
import com.jingge.sensorcollect.pojo.ProxData;
import com.jingge.sensorcollect.pojo.RotationData;
import com.jingge.sensorcollect.pojo.TempData;
import com.jingge.sensorcollect.util.FileUtil;
import com.jingge.sensorcollect.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ning";
    private static int SENSOR_DELAY = SensorManager.SENSOR_DELAY_NORMAL;
    /*private final float[] LocationData = new float[4];
    private final float[] TempData = new float[2];
    private final float[] GravityData = new float[4];
    private final float[] GyroData = new float[4];
    private final float[] LightData = new float[2];
    private final float[] MagneData = new float[4];
    private final float[] OrientData = new float[4];
    private final float[] PressData = new float[4];
    private final float[] ProxData = new float[2];
    private final float[] HumidData = new float[2];
    private final float[] RotationData = new float[5];*/
    List<LocationData> locationDataList = FileUtil.newArrayList();
    List<AccData> accDataList = FileUtil.newArrayList();
    List<TempData> tempDataList = FileUtil.newArrayList();
    List<GravityData> gravityDataList = FileUtil.newArrayList();
    List<GyroData> gyroDataList = FileUtil.newArrayList();
    List<LightData> lightDataList = FileUtil.newArrayList();
    List<MagneData> magneDataList = FileUtil.newArrayList();
    List<OrientData> orientDataList = FileUtil.newArrayList();
    List<PressData> pressDataList = FileUtil.newArrayList();
    List<ProxData> proxDataList = FileUtil.newArrayList();
    List<HumidData> humidDataList = FileUtil.newArrayList();
    List<RotationData> rotationDataList = FileUtil.newArrayList();
    //????????????????????????????????????intent
    Intent foregroundService;
    //?????????????????????
    private String path;
    //????????????????????????????????????????????????????????????????????????
//    private final ActivityResultLauncher<String[]> locationPermissionRequest =
//            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
//                        Boolean fineLocationGranted = result.getOrDefault(
//                                Manifest.permission.ACCESS_FINE_LOCATION, false);
//                        Boolean coarseLocationGranted = result.getOrDefault(
//                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
//                        Boolean backgroundLocationGranted = result.getOrDefault(
//                                Manifest.permission.ACCESS_BACKGROUND_LOCATION, false);
//                        if (fineLocationGranted != null && fineLocationGranted) {
//                            // Precise location access granted.
//                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
//                            // Only approximate location access granted.
//                        } else {
//                            // No location access granted.
//                        }
//                    }
//            );
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private Sensor mTemperature;
    private Sensor mGravity;
    private Sensor mGyroscope;
    private Sensor mLight;
    private Sensor mMagneticField;
    private Sensor mOrientation;
    private Sensor mPressure;
    private Sensor mProximity;
    private Sensor mRelativeHumidity;
    private Sensor mRotationVector;
    private EditText et_filename;
    private TextView tv_Accelerometer;
    private TextView tv_Location;
    //???????????????????????????
    private final LocationListener mLocationListener = new LocationListener() {

        // Provider??????????????????????????????????????????????????????????????????????????????????????????
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged");
        }

        // Provider???enable???????????????????????????GPS?????????
        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");

        }

        // Provider???disable???????????????????????????GPS?????????
        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");

        }

        //??????????????????????????????????????????Provider?????????????????????????????????????????????
        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String provider = location.getProvider();
            double altitude = location.getAltitude();
            float accuracy = location.getAccuracy();

            long timestamp = location.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            String time = sdf.format(timestamp);

            String locations = String.format("????????????:\n??????: %f??? ??????: %f??? ?????????%f???????????????????????????%f", longitude,
                    latitude, altitude, accuracy) + "\n?????????" + getAddress(latitude, longitude) + "\n???????????????" + provider + "\n???????????????" + time;
            Log.d(TAG, locations);
//            LocationData[0] = timestamp;
//            LocationData[1] = (float) longitude;
//            LocationData[2] = (float) latitude;
//            LocationData[3] = (float) altitude;

//            saveData(LocationData, (float) timestamp, (float) longitude, (float) latitude, (float) altitude);
            LocationData locationData = new LocationData(String.valueOf(timestamp), String.valueOf(longitude),
                    String.valueOf(latitude), String.valueOf(altitude),
                    String.valueOf(accuracy), provider);
            locationDataList.add(locationData);

            //??????????????????
            tv_Location.setText(locations);
        }
    };
    private TextView tv_Gravity;
    private TextView tv_Temperature;
    private TextView tv_Gyro;
    private TextView tv_Light;
    private TextView tv_MagneticField;
    private TextView tv_Orientation;
    private TextView tv_Pressure;
    private TextView tv_Proximity;
    private TextView tv_RelativeHumidity;
    private TextView tv_RotationVector;
    //??????????????????????????????
    private final SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            StringBuffer sb = new StringBuffer();
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    sb = new StringBuffer();
                    sb.append("X?????????????????????m/s2???:");
                    sb.append(event.values[0]);
                    sb.append("\nY?????????????????????m/s2???:");
                    sb.append(event.values[1]);
                    sb.append("\nZ?????????????????????m/s2???:");
                    sb.append(event.values[2]);
                    tv_Accelerometer.setText(sb.toString());
//                    AccData[0] = event.values[0];
//                    AccData[1] = event.values[1];
//                    AccData[2] = event.values[2];
                    AccData accData = new AccData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]),
                            String.valueOf(event.values[1]), String.valueOf(event.values[2]));
                    accDataList.add(accData);
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    sb = new StringBuffer();
                    sb.append("?????????????????????:");
                    sb.append(event.values[0]);
                    tv_Temperature.setText(sb.toString());
//                    TempData[0] = event.values[0];
//                    saveData(TempData, event.timestamp, event.values[0]);
                    TempData tempData = new TempData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]));
                    tempDataList.add(tempData);
                    break;
                case Sensor.TYPE_GRAVITY:
                    sb = new StringBuffer();
                    sb.append("X??????????????????m/s2???:");
                    sb.append(event.values[0]);
                    sb.append("\nY??????????????????m/s2???:");
                    sb.append(event.values[1]);
                    sb.append("\nZ??????????????????m/s2???:");
                    sb.append(event.values[2]);
                    tv_Gravity.setText(sb.toString());
//                    GravityData[0] = event.values[0];
//                    GravityData[1] = event.values[1];
//                    GravityData[2] = event.values[2];
//                    saveData(GravityData, event.timestamp, event.values[0], event.values[1], event.values[2]);
                    GravityData gravityData = new GravityData(String.valueOf(stampConvert(event.timestamp)),
                            String.valueOf(event.values[0]), String.valueOf(event.values[1]), String.valueOf(event.values[2]));
                    gravityDataList.add(gravityData);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sb = new StringBuffer();
                    sb.append("X????????????????????????rad/s???:");
                    sb.append(event.values[0]);
                    sb.append("\nY????????????????????????rad/s???:");
                    sb.append(event.values[1]);
                    sb.append("\nZ????????????????????????rad/s???:");
                    sb.append(event.values[2]);
                    tv_Gyro.setText(sb.toString());
//                    GyroData[0] = event.values[0];
//                    GyroData[1] = event.values[1];
//                    GyroData[2] = event.values[2];
//                    saveData(GyroData, event.timestamp, event.values[0], event.values[1], event.values[2]);
                    GyroData gyroData = new GyroData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]),
                            String.valueOf(event.values[1]), String.valueOf(event.values[2]));
                    gyroDataList.add(gyroData);
                    break;
                case Sensor.TYPE_LIGHT:
                    sb = new StringBuffer();
                    sb.append("???????????????lx???:");
                    sb.append(event.values[0]);
                    tv_Light.setText(sb.toString());
//                    LightData[0] = event.values[0];
//                    saveData(LightData, event.timestamp, event.values[0]);
                    LightData lightData = new LightData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]));
                    lightDataList.add(lightData);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sb = new StringBuffer();
                    sb.append("X?????????????????????????????T???:");
                    sb.append(event.values[0]);
                    sb.append("\nY?????????????????????????????T???:");
                    sb.append(event.values[1]);
                    sb.append("\nZ?????????????????????????????T???:");
                    sb.append(event.values[2]);
                    tv_MagneticField.setText(sb.toString());
//                    MagneData[0] = event.values[0];
//                    MagneData[1] = event.values[1];
//                    MagneData[2] = event.values[2];
//                    saveData(MagneData, event.timestamp, event.values[0], event.values[1], event.values[2]);
                    MagneData magneData = new MagneData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]),
                            String.valueOf(event.values[1]), String.valueOf(event.values[2]));
                    magneDataList.add(magneData);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    sb = new StringBuffer();
                    sb.append("?????????X?????????????????????????????");
                    sb.append(event.values[0]);
                    sb.append("\n?????????Y?????????????????????????????");
                    sb.append(event.values[1]);
                    sb.append("\n?????????Z?????????????????????????????");
                    sb.append(event.values[2]);
                    tv_Orientation.setText(sb);
//                    OrientData[0] = event.values[0];
//                    OrientData[1] = event.values[1];
//                    OrientData[2] = event.values[2];
//                    saveData(OrientData, event.timestamp, event.values[0], event.values[1], event.values[2]);
                    OrientData orientData = new OrientData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]),
                            String.valueOf(event.values[1]), String.valueOf(event.values[2]));
                    orientDataList.add(orientData);
                    break;
                case Sensor.TYPE_PRESSURE:
                    sb = new StringBuffer();
                    sb.append("???????????????hPa???:");
                    sb.append(event.values[0]);
                    tv_Pressure.setText(sb.toString());
//                    PressData[0] = event.values[0];
//                    saveData(PressData, event.timestamp, event.values[0]);
                    PressData pressData = new PressData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]));
                    pressDataList.add(pressData);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    sb = new StringBuffer();
                    sb.append("?????????????????????cm??????");
                    sb.append(event.values[0]);
                    tv_Proximity.setText(sb.toString());
//                    ProxData[0] = event.values[0];
//                    saveData(ProxData, event.timestamp, event.values[0]);
                    ProxData proxData = new ProxData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]));
                    proxDataList.add(proxData);
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    sb = new StringBuffer();
                    sb.append("??????????????????:");
                    sb.append(event.values[0]);
                    sb.append("%");
                    tv_RelativeHumidity.setText(sb.toString());
//                    HumidData[0] = event.values[0];
//                    saveData(HumidData, event.timestamp, event.values[0]);
                    HumidData humidData = new HumidData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]));
                    humidDataList.add(humidData);
                    break;
                case Sensor.TYPE_ROTATION_VECTOR:
                    sb = new StringBuffer();
                    sb.append("????????????x*sin(??/2)???");
                    sb.append(event.values[0]);
                    sb.append("\n????????????y*sin(??/2)???");
                    sb.append(event.values[1]);
                    sb.append("\n????????????z*sin(??/2)???");
                    sb.append(event.values[2]);
                    sb.append("\n????????????cos(??/2)");
                    sb.append(event.values[3]);
                    tv_RotationVector.setText(sb);
//                    RotationData[0] = event.values[0];
//                    RotationData[1] = event.values[1];
//                    RotationData[2] = event.values[2];
//                    saveData(RotationData, event.timestamp, event.values[0], event.values[1], event.values[2]);
                    RotationData rotationData = new RotationData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]),
                            String.valueOf(event.values[1]), String.valueOf(event.values[2]), String.valueOf(event.values[3]));
                    rotationDataList.add(rotationData);
                    break;

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private TextView tv_Path;
    private LocationManager locationManager;
    private String filename = "";
    private EditText et_delay;
    private TextView tv_Wifi;

    //    private List<Sensor> tempSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mRelativeHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        mRotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

//        tempSensors = sensorManager.getSensorList(Sensor.TYPE_MOTION_DETECT);

        /*sensors = new StringBuffer();
        for (Sensor sensor : deviceSensors) {
            sensors.append(sensor.toString()).append("\n");
        }*/

        et_delay = findViewById(R.id.et_delay);
        et_filename = findViewById(R.id.et_filename);
        tv_Path = findViewById(R.id.tv_Path);
        tv_Wifi = findViewById(R.id.tv_Wifi);
        tv_Location = findViewById(R.id.tv_Location);
        tv_Accelerometer = findViewById(R.id.tv_Accelerometer);
        tv_Temperature = findViewById(R.id.tv_Temperature);
        tv_Gravity = findViewById(R.id.tv_Gravity);
        tv_Gyro = findViewById(R.id.tv_Gyro);
        tv_Light = findViewById(R.id.tv_Light);
        tv_MagneticField = findViewById(R.id.tv_MagneticField);
        tv_Orientation = findViewById(R.id.tv_Orientation);
        tv_Pressure = findViewById(R.id.tv_Pressure);
        tv_Proximity = findViewById(R.id.tv_Proximity);
        tv_RelativeHumidity = findViewById(R.id.tv_RelativeHumidity);
        tv_RotationVector = findViewById(R.id.tv_RotationVector);

        Button btn_start = findViewById(R.id.btn_start);
        Button btn_stop = findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
//        sensorManager.unregisterListener(listener);
//        locationManager.removeUpdates(mLocationListener);
        super.onStop();
    }

    @Override
    protected void onPause() {
//        sensorManager.unregisterListener(listener);
//        locationManager.removeUpdates(mLocationListener);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                //?????????????????????????????????
                ignoreBatteryOptimization(this);

                //???????????????????????????
                Date date = new Date();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String time = "sensorlog_" + sdf.format(date);
//        String time = String.valueOf(System.currentTimeMillis());
                et_filename.setText(time);

                //?????????????????????
                filename = et_filename.getText().toString();

                //??????????????????????????????????????????????????????
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "242Collection";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
//                path = path + File.separatorChar + filename + ".xlsx";
                tv_Path.setText("???????????????" + path);
                et_filename.setEnabled(false);
                startSensorListening();

                //??????WIFI????????????
                tv_Wifi.setText("????????????WIFI??????????????????"+ getWifiInfo());

                //???????????????????????????
                foregroundService = new Intent(this, SensorService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0????????????
                    startForegroundService(foregroundService);
                } else {
                    startService(foregroundService);
                }

                break;
            case R.id.btn_stop:
                stopSensorListening();
                locationManager.removeUpdates(mLocationListener);
//                String[] title = new String[]{"timestamp", "1", "2", "3"};
                export();
                et_filename.setEnabled(true);

                //??????stop??????????????????????????????
                clearData();

                //???????????????????????????service
                stopService(foregroundService);

                break;
        }
    }

    public void startSensorListening() {
        super.onResume();

//       ????????????????????????????????????????????????????????????
//        locationPermissionRequest.launch(new String[]{
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//                Manifest.permission.MANAGE_EXTERNAL_STORAGE
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//        });
        requestPermissions();

        if (!et_delay.getText().toString().isEmpty()) {
            SENSOR_DELAY = 1000000 / (Integer.parseInt(et_delay.getText().toString()));
        }

        if (mAccelerometer != null) {
            sensorManager.registerListener(listener, mAccelerometer, SENSOR_DELAY);
        }
        if (mTemperature != null) {
            sensorManager.registerListener(listener, mTemperature, SENSOR_DELAY);
        }
        if (mGravity != null) {
            sensorManager.registerListener(listener, mGravity, SENSOR_DELAY);
        }
        if (mGyroscope != null) {
            sensorManager.registerListener(listener, mGyroscope, SENSOR_DELAY);
        }
        if (mLight != null) {
            sensorManager.registerListener(listener, mLight, SENSOR_DELAY);
        }
        if (mMagneticField != null) {
            sensorManager.registerListener(listener, mMagneticField, SENSOR_DELAY);
        }
        if (mOrientation != null) {
            sensorManager.registerListener(listener, mOrientation, SENSOR_DELAY);
        }
        if (mPressure != null) {
            sensorManager.registerListener(listener, mPressure, SENSOR_DELAY);
        }
        if (mProximity != null) {
            sensorManager.registerListener(listener, mProximity, SENSOR_DELAY);
        }
        if (mRelativeHumidity != null) {
            sensorManager.registerListener(listener, mRelativeHumidity, SENSOR_DELAY);
        }
        if (mAccelerometer != null) {
            sensorManager.registerListener(listener, mAccelerometer, SENSOR_DELAY);
        }
        if (mRotationVector != null) {
            sensorManager.registerListener(listener, mRotationVector, SENSOR_DELAY);
        }

        //????????????????????????
        initLocationListener();
    }

    public void stopSensorListening() {
        sensorManager.unregisterListener(listener);
        //????????????????????????
        locationManager.removeUpdates(mLocationListener);
    }

    //?????????????????????????????????
    private void initLocationListener() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager
                .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, mLocationListener);
        locationManager
                .requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, mLocationListener);
    }


    //???????????????????????????
    public String getAddress(double latitude, double longitude) {
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null) {
            for (Address address : addressList) {
                return address.getAddressLine(0);
            }
        }
        return null;
    }

    //?????????????????????????????????????????????
//    private void saveData(float[] dataArray, float... data) {
//        for (int i = 0; i < data.length; i++) {
//            dataArray[i] = data[i];
//        }
//    }

    //????????????????????????
    private void requestPermissions() {

        //???????????????????????????????????????
        if (XXPermissions.isGranted(this, Permission.ACCESS_BACKGROUND_LOCATION, Permission.ACCESS_COARSE_LOCATION,
                Permission.ACCESS_FINE_LOCATION, Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)) {
            return;
        }
        //??????????????????
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE).request(new OnPermissionCallback() {

            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if (all) {
                    ToastUtil.show(MainActivity.this, "????????????????????????");
                }
            }

            @Override
            public void onDenied(List<String> permissions, boolean never) {
                if (never) {
                    ToastUtil.show(MainActivity.this, "???????????????????????????????????????????????????");
                    // ??????????????????????????????????????????????????????????????????
                    XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                } else {
                    ToastUtil.show(MainActivity.this, "????????????????????????");
                }
            }
        });

        //?????????????????????????????????
        XXPermissions.with(this)
                .permission(Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION,
                        Permission.ACCESS_BACKGROUND_LOCATION).request(new OnPermissionCallback() {

            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if (all) {
                    ToastUtil.show(MainActivity.this, "???????????????????????????????????????");
                }
            }

            @Override
            public void onDenied(List<String> permissions, boolean never) {
                if (never) {
                    ToastUtil.show(MainActivity.this, "??????????????????????????????????????????????????????????????????");
                    // ??????????????????????????????????????????????????????????????????
                    XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                } else {
                    ToastUtil.show(MainActivity.this, "???????????????????????????????????????");
                }
            }
        });
    }

    //sensorEvent?????????timestamp???????????????Unix?????????
    private long stampConvert(long timestamp) {
        long Unix_timestamp = (new Date()).getTime() + ((timestamp - System.nanoTime()) / 1000000L);
        return Unix_timestamp;
    }

    //????????????????????????
    private String stampToTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        Date date = new Date(timestamp);
        String res = sdf.format(date);
        return res;
    }

    //???????????????excel
    private void export() {
        ExcelExport.exportExcel(path, filename, LocationData.class, locationDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, AccData.class, accDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, GravityData.class, gravityDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, GyroData.class, gyroDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, HumidData.class, humidDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, LightData.class, lightDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, MagneData.class, magneDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, OrientData.class, orientDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, PressData.class, pressDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, ProxData.class, proxDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, RotationData.class, rotationDataList, MainActivity.this);
        ExcelExport.exportExcel(path, filename, TempData.class, tempDataList, MainActivity.this);
    }

    //??????????????????????????????
    private void clearData() {
        locationDataList.clear();
        accDataList.clear();
        gravityDataList.clear();
        gyroDataList.clear();
        humidDataList.clear();
        lightDataList.clear();
        magneDataList.clear();
        orientDataList.clear();
        pressDataList.clear();
        proxDataList.clear();
        rotationDataList.clear();
        tempDataList.clear();
    }


    /**
     * ??????????????????
     */
    public void ignoreBatteryOptimization(Activity activity) {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean hasIgnored = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
            //  ????????????APP??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (!hasIgnored) {
                try {//????????????????????? ??????????????????
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {//???????????????????????????????????????????????????
                    try {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        ComponentName cn = ComponentName.unflattenFromString("com.android.settings/.Settings$HighPowerApplicationsActivity");
                        intent.setComponent(cn);
                        startActivity(intent);
                    } catch (Exception ex) {//???????????????????????????????????????????????????

                    }
                }
            }
        }
    }


    /**
     * ??????????????????WIFI????????????
     *
     * @return ????????????WIFI???????????????
     */
    private String getWifiInfo() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        List<ScanResult> scanResults = wifiManager.getScanResults();
        StringBuffer sb=new StringBuffer();
        for (ScanResult scanResult : scanResults) {
            sb.append("\n????????????"+scanResult.SSID
                    +" ???????????????"+scanResult.level+"/n :"+wifiManager.calculateSignalLevel(scanResult.level,4));
        }
        return sb.toString();
    }

}