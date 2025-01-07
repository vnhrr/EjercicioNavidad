package com.example.diseointerfaces

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ImprimirActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imprimir) // Cambia esto a tu XML de la actividad

        // Recuperar los datos enviados desde MainActivity
        val productos = intent.getStringArrayListExtra("productos") ?: arrayListOf()
        val precios = intent.getDoubleArrayExtra("precios")?.toList() ?: listOf()
        val cantidades = intent.getIntegerArrayListExtra("cantidades")?.toList() ?: listOf()

        // Obtener las vistas donde se van a mostrar los datos
        val textViewDatos = findViewById<TextView>(R.id.textViewDatos)
        val textViewTotal = findViewById<TextView>(R.id.textViewTotal)
        val buttonCerrar = findViewById<Button>(R.id.buttonCerrar)

        // Mostrar los datos de los productos en "Datos compra"
        var datosCompra = StringBuilder()
        var totalCompra = 0.0

        for (i in productos.indices) {
            val nombreProducto = productos[i]
            val precio = precios.getOrNull(i) ?: 0.0
            val cantidad = cantidades.getOrNull(i) ?: 0
            val totalProducto = precio * cantidad
            totalCompra += totalProducto

            // Formatear los datos del producto
            datosCompra.append("$nombreProducto - Cantidad: $cantidad - Total: €%.2f\n".format(totalProducto))
        }

        // Mostrar los datos de compra
        textViewDatos.text = datosCompra.toString()

        // Mostrar el coste total
        textViewTotal.text = "Coste total: €%.2f".format(totalCompra)

        // Acción del botón "Cerrar"
        buttonCerrar.setOnClickListener {
            finishAffinity()
        }
    }
}