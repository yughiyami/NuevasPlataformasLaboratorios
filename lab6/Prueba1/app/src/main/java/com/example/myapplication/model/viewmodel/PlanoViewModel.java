package com.example.myapplication.model.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.PointF;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.model.Ambiente;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PlanoViewModel extends AndroidViewModel {

    private MutableLiveData<List<Ambiente>> ambientesLiveData;
    private MutableLiveData<Ambiente> ambienteSeleccionadoLiveData;

    public PlanoViewModel(Application application) {
        super(application);
        ambientesLiveData = new MutableLiveData<>();
        ambienteSeleccionadoLiveData = new MutableLiveData<>();
        cargarAmbientes(application.getApplicationContext());
    }

    public LiveData<List<Ambiente>> getAmbientes() {
        return ambientesLiveData;
    }

    public LiveData<Ambiente> getAmbienteSeleccionado() {
        return ambienteSeleccionadoLiveData;
    }

    public void seleccionarAmbiente(Ambiente ambiente) {
        ambienteSeleccionadoLiveData.setValue(ambiente);
    }

    public void limpiarSeleccion() {
        ambienteSeleccionadoLiveData.setValue(null);
    }

    private void cargarAmbientes(Context context) {
        try {
            InputStream is = context.getAssets().open("ambientes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ambientesArray = jsonObject.getJSONArray("ambientes");

            List<Ambiente> ambientes = new ArrayList<>();

            for (int i = 0; i < ambientesArray.length(); i++) {
                JSONObject ambienteJson = ambientesArray.getJSONObject(i);

                String id = ambienteJson.getString("id");
                String nombre = ambienteJson.getString("nombre");
                String tipo = ambienteJson.getString("tipo");
                String descripcion = ambienteJson.getString("descripcion");
                float area = (float) ambienteJson.getDouble("area");
                boolean esCircular = ambienteJson.optBoolean("esCircular", false);

                Ambiente ambiente;

                if (esCircular) {
                    JSONObject centroJson = ambienteJson.getJSONObject("centro");
                    float centroX = (float) centroJson.getDouble("x");
                    float centroY = (float) centroJson.getDouble("y");
                    float radio = (float) ambienteJson.getDouble("radio");

                    ambiente = new Ambiente(id, nombre, tipo, descripcion, area,
                            new PointF(centroX, centroY), radio);
                } else {
                    JSONArray verticesArray = ambienteJson.getJSONArray("vertices");
                    List<PointF> vertices = new ArrayList<>();

                    for (int j = 0; j < verticesArray.length(); j++) {
                        JSONObject vertexJson = verticesArray.getJSONObject(j);
                        float x = (float) vertexJson.getDouble("x");
                        float y = (float) vertexJson.getDouble("y");
                        vertices.add(new PointF(x, y));
                    }

                    ambiente = new Ambiente(id, nombre, tipo, descripcion, area, vertices);
                }

                ambientes.add(ambiente);
            }

            ambientesLiveData.setValue(ambientes);

        } catch (Exception e) {
            e.printStackTrace();
            ambientesLiveData.setValue(new ArrayList<>());
        }
    }

    public Ambiente buscarAmbientePorCoordenadas(float x, float y) {
        List<Ambiente> ambientes = ambientesLiveData.getValue();
        if (ambientes != null) {
            for (Ambiente ambiente : ambientes) {
                if (ambiente.contienePunto(x, y)) {
                    return ambiente;
                }
            }
        }
        return null;
    }
}