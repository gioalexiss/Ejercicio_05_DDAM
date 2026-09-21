package com.example.eje_garg_06;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eje_garg_06.util.Basicas;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private boolean isResultCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View.OnClickListener appendClickListener = v -> {
            Button b = (Button) v;
            String currentText = display.getText().toString();
            String appendText = b.getText().toString();

            if (isResultCalculated) {
                // Si justo acabamos de dar "=" y presionan un número o punto, se reinicia la pantalla
                if (appendText.matches("[0-9().]")) {
                    display.setText(appendText);
                } else {
                    // Si presionan un operador como "+", se lo concatenamos al resultado anterior
                    display.append(appendText);
                }
                isResultCalculated = false;
            } else if (currentText.equals("0")) {
                // Si la pantalla es 0, reemplazar por el número si aplica
                if (appendText.matches("[0-9(]")) {
                    display.setText(appendText);
                } else {
                    display.append(appendText);
                }
            } else {
                // De forma estándar ir concatenando la expresión a la vista (ej. "7", luego "+", luego "7" = "7+7")
                display.append(appendText);
            }
        };

        // Todos estos botones sólo concatenan a la expresión visualmente
        int[] appendButtons = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6,
                R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_add, R.id.btn_sub, R.id.btn_mul, R.id.btn_div, R.id.btn_res,
                R.id.btn_punto, R.id.btn_abrirParentesis, R.id.btn_cerrarParentesis
        };

        for (int id : appendButtons) {
            findViewById(id).setOnClickListener(appendClickListener);
        }

        // Botón C: Limpia la pantalla
        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            display.setText("0");
            isResultCalculated = false;
        });

        // Botón Igual: Evalúa toda la expresión en pantalla
        findViewById(R.id.btn_eq).setOnClickListener(v -> {
            try {
                String expr = display.getText().toString();
                if (expr.isEmpty()) return;

                // Llama al evaluador de expresiones de la clase de utilidad
                double result = Basicas.evaluar(expr);

                // Formateo del número para evitar decimales innecesarios (ej. .0)
                if (result == (long) result) {
                    display.setText(String.format(java.util.Locale.US, "%d", (long) result));
                } else {
                    display.setText(String.format(java.util.Locale.US, "%s", result));
                }
                
                isResultCalculated = true;

            } catch (ArithmeticException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Expresión inválida", Toast.LENGTH_SHORT).show();
            }
        });
    }
}