package com.example.conectarinterneto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.conectarinterneto.R.id.editText;


public class MainActivity extends AppCompatActivity {
    private String contenido = "";
    private TextView text;
    private String urrl="https://freetexthost.net/ZLoyKJR";
    private static final String DEBUG_TAG = "HttpExample";
    private TextView textView;
    private TextView textDebug;
    private View view;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.texto);
        edit = (EditText) findViewById(editText);
        text.setMovementMethod(new ScrollingMovementMethod());
    }

    public void descargar(View view){
        urrl= String.valueOf(edit.getText());
        if(urrl.equals("https://freetexthost.net/ZLoyKJR")){
            if (isConnected()){
                try {
                    new DownloadWebpageTask().execute(urrl);
                } catch (Exception e) {
                    String contenido="error al conectar";
                    text.setText(contenido);
                }
            } else {
                String contenido="error al conectar";
                text.setText(contenido);
            }
        }else{
            String contenido="url incorrecta\nprueba con https://freetexthost.net/ZLoyKJR";
            text.setText(contenido);
        }
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        String resultado="";
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "No se puede recuperar la página web. URL puede no ser válida.";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                String[] parts = result.split("starto");
                String retocar = parts[1];
                String[] parts2 = retocar.split("</p><p>");
                for (String part : parts2) {
                    resultado += part;
                }
                String[] parts3 = resultado.split("span");
                String resultado2 = parts3[0] + "" + parts3[2];
                String[] parts4 = resultado2.split("<>");
                String resultado3 = parts4[0] + "\n" + parts4[1];
                text.setText(resultado3);
            } catch (Exception e) {
                String contenido="error al cargar";
                text.setText(contenido);
            }
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String webPage = "";
            String data = "";
            while ((data = reader.readLine()) != null) {
                webPage += data + "\n";
            }
            return webPage;
        }catch(Exception e){
            String contenido="error al descargar";
            text.setText(contenido);
            return "error";
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}

