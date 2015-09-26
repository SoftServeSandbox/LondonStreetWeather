package my.app.administrator.ysn_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import my.app.administrator.ysn_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String LondonWeatherUrl = "http://api.openweathermap.org/data/2.5/weather?q=London&units=metric";
    private TextView mTempTextView;
    private TextView mHumidityTextView;
    private TextView mPressureTextView;
    private TextView mWindSpeedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTempTextView = (TextView) findViewById(R.id.textViewTemp);
        mHumidityTextView = (TextView) findViewById(R.id.textViewHum);
        mPressureTextView = (TextView) findViewById(R.id.textViewPressure);
        mWindSpeedTextView = (TextView) findViewById(R.id.textViewWindSpeed);

        WeatherTask weatherProvider = new WeatherTask();
        weatherProvider.execute(LondonWeatherUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class WeatherTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                JSONObject jsonWeather = result;
                Log.v("weatherProvider", jsonWeather.toString());

                int temp = jsonWeather.getJSONObject("main").getInt("temp");
                Log.v("weatherProvider", Integer.toString(temp));

                int pressure = jsonWeather.getJSONObject("main").getInt("pressure");
                Log.v("weatherProvider", Integer.toString(pressure));

                int humidity = jsonWeather.getJSONObject("main").getInt("humidity");
                Log.v("weatherProvider", Integer.toString(humidity));

                double windSpeed = jsonWeather.getJSONObject("wind").getDouble("speed");
                Log.v("weatherProvider", Double.toString(windSpeed));

                mTempTextView.setText(String.format("Temperature: %s \u00b0C", Integer.toString(temp)));
                mHumidityTextView.setText( String.format("Pressure: %s hpa", Integer.toString(pressure)));
                mPressureTextView.setText( String.format("Humidity: %s %%", Integer.toString(humidity)));
                mWindSpeedTextView.setText( String.format("Wind Speed: %s m/s", Double.toString(windSpeed)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                InputStream weatherStream = new URL(params[0]).openStream();

                BufferedInputStream bis = new BufferedInputStream(weatherStream);
                byte[] buffer = new byte[1024];
                StringBuilder sb = new StringBuilder();
                int bytesRead = 0;
                while ((bytesRead = bis.read(buffer)) > 0) {
                    String text = new String(buffer, 0, bytesRead);
                    sb.append(text);
                }
                bis.close();

                return new JSONObject(sb.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

            }

            return null;
        }
    }
}
