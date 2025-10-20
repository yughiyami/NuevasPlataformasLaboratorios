package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import  com.example.myapplication.model.Ambiente;

public class AmbienteDetalleDialog extends DialogFragment {

    private static final String ARG_ID = "id";
    private static final String ARG_NOMBRE = "nombre";
    private static final String ARG_TIPO = "tipo";
    private static final String ARG_DESCRIPCION = "descripcion";
    private static final String ARG_AREA = "area";

    private OnDismissListener dismissListener;

    public interface OnDismissListener {
        void onDialogDismiss();
    }

    public static AmbienteDetalleDialog newInstance(Ambiente ambiente) {
        AmbienteDetalleDialog dialog = new AmbienteDetalleDialog();
        Bundle args = new Bundle();
        args.putString(ARG_ID, ambiente.getId());
        args.putString(ARG_NOMBRE, ambiente.getNombre());
        args.putString(ARG_TIPO, ambiente.getTipo());
        args.putString(ARG_DESCRIPCION, ambiente.getDescripcion());
        args.putFloat(ARG_AREA, ambiente.getArea());
        dialog.setArguments(args);
        return dialog;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_ambiente_detalle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvNombre = view.findViewById(R.id.tv_nombre);
        TextView tvTipo = view.findViewById(R.id.tv_tipo);
        TextView tvDescripcion = view.findViewById(R.id.tv_descripcion);
        TextView tvArea = view.findViewById(R.id.tv_area);
        Button btnCerrar = view.findViewById(R.id.btn_cerrar);

        Bundle args = getArguments();
        if (args != null) {
            String nombre = args.getString(ARG_NOMBRE, "");
            String tipo = args.getString(ARG_TIPO, "");
            String descripcion = args.getString(ARG_DESCRIPCION, "");
            float area = args.getFloat(ARG_AREA, 0);

            tvNombre.setText(nombre);
            tvTipo.setText("Tipo: " + (tipo.equals("patio") ? "Patio" : "Salón"));
            tvDescripcion.setText(descripcion);
            tvArea.setText(String.format("Área: %.2f m²", area));
        }

        btnCerrar.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Override
    public void onDismiss(@NonNull android.content.DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDialogDismiss();
        }
    }
}