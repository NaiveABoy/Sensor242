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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jingge.sensorcollect.util.CompressUtil;
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
    //定义用于保活的前台服务的intent
    Intent foregroundService;
    //文件的存储地址
    private String path;
    //声明权限请求处理逻辑（已过时，使用框架申请权限）
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
    //定义位置服务监听器
    private final LocationListener mLocationListener = new LocationListener() {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged");
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
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

            String locations = String.format("位置信息:\n经度: %f， 纬度: %f， 海拔：%f，定位精度（米）：%f", longitude,
                    latitude, altitude, accuracy) + "\n地址：" + getAddress(latitude, longitude) + "\n定位来源：" + provider + "\n定位时间：" + time;
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

            //更新位置信息
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
    //定义传感器事件监听器
    private final SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            StringBuffer sb;
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    sb = new StringBuffer();
                    sb.append("X轴上的加速度（m/s2）:");
                    sb.append(event.values[0]);
                    sb.append("\nY轴上的加速度（m/s2）:");
                    sb.append(event.values[1]);
                    sb.append("\nZ轴上的加速度（m/s2）:");
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
                    sb.append("环境温度（℃）:");
                    sb.append(event.values[0]);
                    tv_Temperature.setText(sb.toString());
//                    TempData[0] = event.values[0];
//                    saveData(TempData, event.timestamp, event.values[0]);
                    TempData tempData = new TempData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]));
                    tempDataList.add(tempData);
                    break;
                case Sensor.TYPE_GRAVITY:
                    sb = new StringBuffer();
                    sb.append("X轴上的重力（m/s2）:");
                    sb.append(event.values[0]);
                    sb.append("\nY轴上的重力（m/s2）:");
                    sb.append(event.values[1]);
                    sb.append("\nZ轴上的重力（m/s2）:");
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
                    sb.append("X轴上的旋转速率（rad/s）:");
                    sb.append(event.values[0]);
                    sb.append("\nY轴上的旋转速率（rad/s）:");
                    sb.append(event.values[1]);
                    sb.append("\nZ轴上的旋转速率（rad/s）:");
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
                    sb.append("光照强度（lx）:");
                    sb.append(event.values[0]);
                    tv_Light.setText(sb.toString());
//                    LightData[0] = event.values[0];
//                    saveData(LightData, event.timestamp, event.values[0]);
                    LightData lightData = new LightData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]));
                    lightDataList.add(lightData);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sb = new StringBuffer();
                    sb.append("X轴上的环境地磁场（μT）:");
                    sb.append(event.values[0]);
                    sb.append("\nY轴上的环境地磁场（μT）:");
                    sb.append(event.values[1]);
                    sb.append("\nZ轴上的环境地磁场（μT）:");
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
                    sb.append("设备绕X轴的旋转度数（°）：");
                    sb.append(event.values[0]);
                    sb.append("\n设备绕Y轴的旋转度数（°）：");
                    sb.append(event.values[1]);
                    sb.append("\n设备绕Z轴的旋转度数（°）：");
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
                    sb.append("环境气压（hPa）:");
                    sb.append(event.values[0]);
                    tv_Pressure.setText(sb.toString());
//                    PressData[0] = event.values[0];
//                    saveData(PressData, event.timestamp, event.values[0]);
                    PressData pressData = new PressData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]));
                    pressDataList.add(pressData);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    sb = new StringBuffer();
                    sb.append("接近屏幕距离（cm）：");
                    sb.append(event.values[0]);
                    tv_Proximity.setText(sb.toString());
//                    ProxData[0] = event.values[0];
//                    saveData(ProxData, event.timestamp, event.values[0]);
                    ProxData proxData = new ProxData(String.valueOf(stampConvert(event.timestamp)), String.valueOf(event.values[0]));
                    proxDataList.add(proxData);
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    sb = new StringBuffer();
                    sb.append("环境相对湿度:");
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
                    sb.append("旋转矢量x*sin(θ/2)：");
                    sb.append(event.values[0]);
                    sb.append("\n旋转矢量y*sin(θ/2)：");
                    sb.append(event.values[1]);
                    sb.append("\n旋转矢量z*sin(θ/2)：");
                    sb.append(event.values[2]);
                    sb.append("\n旋转矢量cos(θ/2)");
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
    //下拉选择动作类别的控件
    private Spinner spinner_action;
    //记录动作类别的参数
    private int action_no;
    private CheckBox check_Action;

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
        spinner_action = findViewById(R.id.spinner_action);
        check_Action = findViewById(R.id.check_action);

        Button btn_start = findViewById(R.id.btn_start);
        Button btn_stop = findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

        //获取动作下拉框选择的动作类型
        spinner_action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] actions = getResources().getStringArray(R.array.actions);
                Toast.makeText(MainActivity.this, "你选择的动作类型是:" + actions[pos], Toast.LENGTH_SHORT).show();
                if (check_Action.isChecked()) {
                    action_no = pos + 1;
                } else {
                    action_no = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
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
                //检查是否已关闭电池优化
                ignoreBatteryOptimization(this);

                //给文件名设置默认值
                Date date = new Date();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String time = "sensorlog_" + sdf.format(date);
//        String time = String.valueOf(System.currentTimeMillis());
                et_filename.setText(time);

                //获取存储文件名
                filename = et_filename.getText().toString();

                //获取主存储目录，若不存在则创建此目录
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "242Collection";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
//                path = path + File.separatorChar + filename + ".xlsx";
                tv_Path.setText("存储路径：" + path);
                et_filename.setEnabled(false);
                startSensorListening();

                //获取WIFI连接信息
                tv_Wifi.setText("设备周边WIFI接入点信息：" + getWifiInfo());

                //启动前台服务以保活
                foregroundService = new Intent(this, SensorService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0后才支持
                    startForegroundService(foregroundService);
                } else {
                    startService(foregroundService);
                }

                break;
            case R.id.btn_stop:
                stopSensorListening();
                locationManager.removeUpdates(mLocationListener);
//                String[] title = new String[]{"timestamp", "1", "2", "3"};
                try {
                    export();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                et_filename.setEnabled(true);

                //每次stop后，清除暂存区的数据
                clearData();

                //关闭用于前台保活的service
                stopService(foregroundService);

                break;
        }
    }

    public void startSensorListening() {
        super.onResume();

//       请求精确定位权限（已过时，使用框架获取）
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

        //启动位置服务监听
        initLocationListener();
    }

    public void stopSensorListening() {
        sensorManager.unregisterListener(listener);
        //停止位置服务监听
        locationManager.removeUpdates(mLocationListener);
    }

    //启动位置服务监听的函数
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


    //通过经纬度获取地址
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

    //将数据存入数组的方法（已弃用）
//    private void saveData(float[] dataArray, float... data) {
//        for (int i = 0; i < data.length; i++) {
//            dataArray[i] = data[i];
//        }
//    }

    //使用框架申请权限
    private void requestPermissions() {

        //若已获取权限，则不重复申请
        if (XXPermissions.isGranted(this, Permission.ACCESS_BACKGROUND_LOCATION, Permission.ACCESS_COARSE_LOCATION,
                Permission.ACCESS_FINE_LOCATION, Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)) {
            return;
        }
        //申请存储权限
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE).request(new OnPermissionCallback() {

            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if (all) {
                    ToastUtil.show(MainActivity.this, "获取存储权限成功");
                }
            }

            @Override
            public void onDenied(List<String> permissions, boolean never) {
                if (never) {
                    ToastUtil.show(MainActivity.this, "被永久拒绝授权，请手动授予存储权限");
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                } else {
                    ToastUtil.show(MainActivity.this, "获取存储权限失败");
                }
            }
        });

        //申请定位及后台运行权限
        XXPermissions.with(this)
                .permission(Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION,
                        Permission.ACCESS_BACKGROUND_LOCATION).request(new OnPermissionCallback() {

            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if (all) {
                    ToastUtil.show(MainActivity.this, "获取定位及后台运行权限成功");
                }
            }

            @Override
            public void onDenied(List<String> permissions, boolean never) {
                if (never) {
                    ToastUtil.show(MainActivity.this, "被永久拒绝授权，请手动授予定位及后台运行权限");
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                } else {
                    ToastUtil.show(MainActivity.this, "获取定位及后台运行权限失败");
                }
            }
        });
    }

    //sensorEvent返回的timestamp需要转换为Unix时间戳
    private long stampConvert(long timestamp) {
        long Unix_timestamp = (new Date()).getTime() + ((timestamp - System.nanoTime()) / 1000000L);
        return Unix_timestamp;
    }

    //时间戳转换为时间
    private String stampToTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        Date date = new Date(timestamp);
        String res = sdf.format(date);
        return res;
    }

    //数据导出为excel
    private void export() throws Exception {
        ExcelExport.exportExcel(path, filename, LocationData.class, locationDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, AccData.class, accDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, GravityData.class, gravityDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, GyroData.class, gyroDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, HumidData.class, humidDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, LightData.class, lightDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, MagneData.class, magneDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, OrientData.class, orientDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, PressData.class, pressDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, ProxData.class, proxDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, RotationData.class, rotationDataList, MainActivity.this, action_no);
        ExcelExport.exportExcel(path, filename, TempData.class, tempDataList, MainActivity.this, action_no);

        //存储需要压缩的文件的路径
        String filesToCompress = filePath(LocationData.class) + filePath(AccData.class) +
                filePath(GravityData.class) + filePath(GyroData.class) + filePath(HumidData.class) +
                filePath(LightData.class) + filePath(MagneData.class) + filePath(OrientData.class) +
                filePath(PressData.class) + filePath(ProxData.class) + filePath(RotationData.class) +
                filePath(TempData.class);
        //将压缩文件打包
        CompressUtil.zip(filesToCompress, path + File.separatorChar + filename + ".zip", null);
    }

    //定义一个方便函数以传回压缩需要文件路径
    private String filePath(Class className) {
        return path + File.separatorChar + filename + File.separatorChar + className.getSimpleName() + ".xlsx" + "|";
    }

    //清除列表中暂存的数据
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
     * 忽略电池优化
     */
    public void ignoreBatteryOptimization(Activity activity) {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean hasIgnored = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if (!hasIgnored) {
                try {//先调用系统显示 电池优化权限
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {//如果失败了则引导用户到电池优化界面
                    try {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        ComponentName cn = ComponentName.unflattenFromString("com.android.settings/.Settings$HighPowerApplicationsActivity");
                        intent.setComponent(cn);
                        startActivity(intent);
                    } catch (Exception ex) {//如果全部失败则说明没有电池优化功能

                    }
                }
            }
        }
    }


    /**
     * 获取当前设备WIFI环境信息
     *
     * @return 当前周围WIFI接入点信息
     */
    private String getWifiInfo() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        List<ScanResult> scanResults = wifiManager.getScanResults();
        StringBuilder sb = new StringBuilder();
        for (ScanResult scanResult : scanResults) {
            sb.append("\n设备名：" + scanResult.SSID
                    + " 信号强度：" + scanResult.level + "/n :" + wifiManager.calculateSignalLevel(scanResult.level, 4));
        }
        return sb.toString();
    }

}