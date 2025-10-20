package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import  com.example.myapplication.model.Ambiente;
import java.util.ArrayList;
import java.util.List;

public class PlanoView extends View {

    private List<Ambiente> ambientes;
    private Paint paintSalon;
    private Paint paintPatio;
    private Paint paintBorde;
    private Paint paintTexto;
    private Paint paintEtiqueta;
    private OnAmbienteClickListener listener;
    private Ambiente ambienteResaltado;

    private float escala = 1.0f;
    private float offsetX = 0f;
    private float offsetY = 0f;

    public interface OnAmbienteClickListener {
        void onAmbienteClick(Ambiente ambiente);
    }

    public PlanoView(Context context) {
        super(context);
        init();
    }

    public PlanoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlanoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ambientes = new ArrayList<>();

        // Paint para salones
        paintSalon = new Paint();
        paintSalon.setColor(Color.parseColor("#E3F2FD"));
        paintSalon.setStyle(Paint.Style.FILL);
        paintSalon.setAntiAlias(true);

        // Paint para patios
        paintPatio = new Paint();
        paintPatio.setColor(Color.parseColor("#C8E6C9"));
        paintPatio.setStyle(Paint.Style.FILL);
        paintPatio.setAntiAlias(true);

        // Paint para bordes
        paintBorde = new Paint();
        paintBorde.setColor(Color.parseColor("#424242"));
        paintBorde.setStyle(Paint.Style.STROKE);
        paintBorde.setStrokeWidth(4);
        paintBorde.setAntiAlias(true);

        // Paint para texto
        paintTexto = new Paint();
        paintTexto.setColor(Color.parseColor("#212121"));
        paintTexto.setTextSize(28);
        paintTexto.setTextAlign(Paint.Align.CENTER);
        paintTexto.setAntiAlias(true);
        paintTexto.setFakeBoldText(true);

        // Paint para fondo de etiqueta
        paintEtiqueta = new Paint();
        paintEtiqueta.setColor(Color.parseColor("#FFFFFF"));
        paintEtiqueta.setStyle(Paint.Style.FILL);
        paintEtiqueta.setAntiAlias(true);
        paintEtiqueta.setAlpha(230);
    }

    public void setAmbientes(List<Ambiente> ambientes) {
        this.ambientes = ambientes;
        calcularEscalaYOffset();
        invalidate();
    }

    public void setOnAmbienteClickListener(OnAmbienteClickListener listener) {
        this.listener = listener;
    }

    public void setAmbienteResaltado(Ambiente ambiente) {
        this.ambienteResaltado = ambiente;
        invalidate();
    }

    private void calcularEscalaYOffset() {
        if (ambientes == null || ambientes.isEmpty()) {
            return;
        }

        // Calcular los límites del plano
        RectF limites = calcularLimitesPlano();

        float planoAncho = limites.width();
        float planoAlto = limites.height();

        // Obtener el tamaño disponible del canvas (con padding)
        int padding = 60;
        float canvasAncho = getWidth() - (padding * 2);
        float canvasAlto = getHeight() - (padding * 2);

        if (canvasAncho <= 0 || canvasAlto <= 0) {
            return;
        }

        // Calcular escala para ajustar el plano al canvas
        float escalaX = canvasAncho / planoAncho;
        float escalaY = canvasAlto / planoAlto;
        escala = Math.min(escalaX, escalaY) * 0.9f; // 0.9f para dejar un margen adicional

        // Calcular offset para centrar el plano
        float planoEscaladoAncho = planoAncho * escala;
        float planoEscaladoAlto = planoAlto * escala;

        offsetX = (getWidth() - planoEscaladoAncho) / 2 - (limites.left * escala);
        offsetY = (getHeight() - planoEscaladoAlto) / 2 - (limites.top * escala);
    }

    private RectF calcularLimitesPlano() {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (Ambiente ambiente : ambientes) {
            if (ambiente.isEsCircular()) {
                PointF centro = ambiente.getCentro();
                float radio = ambiente.getRadio();
                minX = Math.min(minX, centro.x - radio);
                minY = Math.min(minY, centro.y - radio);
                maxX = Math.max(maxX, centro.x + radio);
                maxY = Math.max(maxY, centro.y + radio);
            } else {
                List<PointF> vertices = ambiente.getVertices();
                for (PointF vertex : vertices) {
                    minX = Math.min(minX, vertex.x);
                    minY = Math.min(minY, vertex.y);
                    maxX = Math.max(maxX, vertex.x);
                    maxY = Math.max(maxY, vertex.y);
                }
            }
        }

        return new RectF(minX, minY, maxX, maxY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calcularEscalaYOffset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (ambientes == null || ambientes.isEmpty()) {
            return;
        }

        canvas.save();
        canvas.translate(offsetX, offsetY);
        canvas.scale(escala, escala);

        // Dibujar cada ambiente
        for (Ambiente ambiente : ambientes) {
            dibujarAmbiente(canvas, ambiente);
        }

        // Dibujar etiquetas después para que estén encima
        for (Ambiente ambiente : ambientes) {
            dibujarEtiqueta(canvas, ambiente);
        }

        canvas.restore();
    }

    private void dibujarAmbiente(Canvas canvas, Ambiente ambiente) {
        Paint paintFondo = ambiente.getTipo().equals("patio") ? paintPatio : paintSalon;

        // Si es el ambiente resaltado, oscurecer el color
        if (ambiente == ambienteResaltado) {
            Paint paintResaltado = new Paint(paintFondo);
            int color = paintFondo.getColor();
            int r = (Color.red(color) * 3) / 4;
            int g = (Color.green(color) * 3) / 4;
            int b = (Color.blue(color) * 3) / 4;
            paintResaltado.setColor(Color.rgb(r, g, b));
            paintFondo = paintResaltado;
        }

        if (ambiente.isEsCircular()) {
            // Dibujar ambiente circular
            PointF centro = ambiente.getCentro();
            float radio = ambiente.getRadio();
            canvas.drawCircle(centro.x, centro.y, radio, paintFondo);
            canvas.drawCircle(centro.x, centro.y, radio, paintBorde);
        } else {
            // Dibujar ambiente poligonal
            List<PointF> vertices = ambiente.getVertices();
            if (vertices != null && vertices.size() >= 3) {
                Path path = new Path();
                path.moveTo(vertices.get(0).x, vertices.get(0).y);

                for (int i = 1; i < vertices.size(); i++) {
                    path.lineTo(vertices.get(i).x, vertices.get(i).y);
                }
                path.close();

                canvas.drawPath(path, paintFondo);
                canvas.drawPath(path, paintBorde);
            }
        }
    }

    private void dibujarEtiqueta(Canvas canvas, Ambiente ambiente) {
        PointF centro = ambiente.getCentroAmbiente();
        String nombre = ambiente.getNombre();

        // Medir el texto para crear el fondo
        float textWidth = paintTexto.measureText(nombre);
        float padding = 16;

        // Dibujar fondo de la etiqueta
        canvas.drawRoundRect(
                centro.x - textWidth / 2 - padding,
                centro.y - 20 - padding / 2,
                centro.x + textWidth / 2 + padding,
                centro.y + 12 + padding / 2,
                8, 8,
                paintEtiqueta
        );

        // Dibujar borde de la etiqueta
        Paint paintBordeEtiqueta = new Paint(paintBorde);
        paintBordeEtiqueta.setStrokeWidth(2);
        canvas.drawRoundRect(
                centro.x - textWidth / 2 - padding,
                centro.y - 20 - padding / 2,
                centro.x + textWidth / 2 + padding,
                centro.y + 12 + padding / 2,
                8, 8,
                paintBordeEtiqueta
        );

        // Dibujar texto
        canvas.drawText(nombre, centro.x, centro.y, paintTexto);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Convertir las coordenadas del toque al sistema de coordenadas del plano
            float x = (event.getX() - offsetX) / escala;
            float y = (event.getY() - offsetY) / escala;

            // Buscar el ambiente tocado
            for (Ambiente ambiente : ambientes) {
                if (ambiente.contienePunto(x, y)) {
                    if (listener != null) {
                        listener.onAmbienteClick(ambiente);
                    }
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }
}