package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.model.Ambiente;
import com.example.myapplication.view.PlanoView;
import com.example.myapplication.model.viewmodel.PlanoViewModel;
import java.util.List;

public class PlanoFragment extends Fragment {

    private PlanoViewModel viewModel;
    private PlanoView planoView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PlanoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plano, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        planoView = view.findViewById(R.id.plano_view);

        // Observar cambios en la lista de ambientes
        viewModel.getAmbientes().observe(getViewLifecycleOwner(), this::actualizarPlano);

        // Observar selección de ambiente
        viewModel.getAmbienteSeleccionado().observe(getViewLifecycleOwner(),
                this::mostrarDetalleAmbiente);

        // Configurar listener de clicks
        planoView.setOnAmbienteClickListener(ambiente -> {
            viewModel.seleccionarAmbiente(ambiente);
        });
    }

    private void actualizarPlano(List<Ambiente> ambientes) {
        if (ambientes != null && !ambientes.isEmpty()) {
            planoView.setAmbientes(ambientes);
        }
    }

    private void mostrarDetalleAmbiente(Ambiente ambiente) {
        if (ambiente != null) {
            planoView.setAmbienteResaltado(ambiente);

            AmbienteDetalleDialog dialog = AmbienteDetalleDialog.newInstance(ambiente);
            dialog.setOnDismissListener(() -> {
                viewModel.limpiarSeleccion();
                planoView.setAmbienteResaltado(null);
            });
            dialog.show(getParentFragmentManager(), "detalle_ambiente");
        }
    }
}