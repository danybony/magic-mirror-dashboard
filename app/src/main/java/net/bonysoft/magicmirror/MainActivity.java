package net.bonysoft.magicmirror;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.notils.caster.Views;

import net.bonysoft.magicmirror.modules.DashboardModule;
import net.bonysoft.magicmirror.modules.DashboardModuleComposite;
import net.bonysoft.magicmirror.modules.time.TimeModule;
import net.bonysoft.magicmirror.modules.weather.WeatherIconMapper;
import net.bonysoft.magicmirror.modules.weather.WeatherInfo;
import net.bonysoft.magicmirror.modules.weather.WeatherModule;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SystemUIHider systemUIHider;

    private TextView timeLabel;
    private TextView dateLabel;
    private TextView weatherTemperatureLabel;
    private TextView todayForecastLabel;
    private DashboardModule modules;
    private ImageView todayForecastIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeLabel = Views.findById(this, R.id.label_time);
        dateLabel = Views.findById(this, R.id.label_date);
        weatherTemperatureLabel = Views.findById(this, R.id.label_weather_temperature);
        todayForecastLabel = Views.findById(this, R.id.label_today_forecast);
        todayForecastIcon = Views.findById(this, R.id.weather_icon);

        systemUIHider = new SystemUIHider(findViewById(android.R.id.content));
        keepScreenOn();

        createModules();
    }

    private void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void createModules() {
        List<DashboardModule> modulesList = new ArrayList<>();
        modulesList.add(new TimeModule(timeLabel, dateLabel));
        modulesList.add(WeatherModule.newInstance(this, weatherListener));

        modules = new DashboardModuleComposite(modulesList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerTimeTickReceiver();
        updateModules();
        systemUIHider.hideSystemUi();
    }

    private void registerTimeTickReceiver() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(timeTickReceiver, intentFilter);
    }

    private void updateModules() {
        modules.update();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(timeTickReceiver);
        systemUIHider.showSystemUi();
    }

    private final BroadcastReceiver timeTickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateModules();
        }
    };

    private final WeatherModule.WeatherListener weatherListener = new WeatherModule.WeatherListener() {

        @Override
        public void onCurrentWeatherFetched(WeatherInfo currentInfo) {
            DecimalFormat temperatureFormat = getTemperatureFormat();
            String currentTemperature = temperatureFormat.format(currentInfo.getTemperature()) + "°";
            weatherTemperatureLabel.setVisibility(View.VISIBLE);
            weatherTemperatureLabel.setText(currentTemperature);
        }

        @Override
        public void onTodayForecastFetched(WeatherInfo todayInfo) {
            DecimalFormat temperatureFormat = getTemperatureFormat();
            String minTemperature = temperatureFormat.format(todayInfo.getMinTemperature());
            String maxTemperature = temperatureFormat.format(todayInfo.getMaxTemperature());
            String todayCondition = getString(R.string.weather_condition, minTemperature, maxTemperature);
            todayForecastLabel.setVisibility(View.VISIBLE);
            todayForecastLabel.setText(todayCondition);

            int weatherResource = WeatherIconMapper.getWeatherResource(todayInfo.getIconId());
            todayForecastIcon.setImageResource(weatherResource);
        }

        @Override
        public void onCurrentWeatherUnavailable() {
            weatherTemperatureLabel.setVisibility(View.GONE);
        }

        @Override
        public void onTodayForecastUnavailable() {
            todayForecastLabel.setVisibility(View.GONE);
        }
    };

    @NonNull
    private DecimalFormat getTemperatureFormat() {
        DecimalFormat temperatureFormat = new DecimalFormat("#.#");
        temperatureFormat.setRoundingMode(RoundingMode.HALF_DOWN);
        return temperatureFormat;
    }
}
