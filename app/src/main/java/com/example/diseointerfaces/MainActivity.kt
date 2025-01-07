package com.example.diseointerfaces

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Recuperar datos del Intent
        val productos = intent.getStringArrayListExtra("productos")?.toMutableList() ?: mutableListOf()
        val precios = intent.getDoubleArrayExtra("precios")?.toMutableList() ?: mutableListOf()
        val cantidades = intent.getIntegerArrayListExtra("cantidades")?.toMutableList() ?: mutableListOf()
        val imagenes = intent.getIntegerArrayListExtra("imagenes")?.toMutableList() ?: mutableListOf()

        // Configurar ListView
        val listView = findViewById<ListView>(R.id.ListViewProductosSeleccionados)
        val adapter = SeleccionadosAdapter(this, productos, precios, cantidades, imagenes)
        listView.adapter = adapter

        // Botones
        val incluir = findViewById<Button>(R.id.buttonAñadir)
        val imprimir = findViewById<Button>(R.id.buttonImprimir)
        val borrarSel = findViewById<Button>(R.id.buttonBorrarSelec)
        val borrar = findViewById<Button>(R.id.buttonBorrarTodo)

        incluir.setOnClickListener {
            val intent = Intent(this, IncluirActivity::class.java)
            startActivity(intent)
        }

        imprimir.setOnClickListener {
            val intent = Intent(this, ImprimirActivity::class.java)
            intent.putStringArrayListExtra("productos", ArrayList(productos))
            intent.putExtra("precios", precios.toDoubleArray())
            intent.putIntegerArrayListExtra("cantidades", ArrayList(cantidades))
            startActivity(intent)
        }

        borrarSel.setOnClickListener {
            val indicesSeleccionados = adapter.getSelectedItems()

            if (indicesSeleccionados.isNotEmpty()) {
                adapter.removeItems(indicesSeleccionados)
                Toast.makeText(this, "Elementos borrados correctamente.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay elementos seleccionados para borrar.", Toast.LENGTH_SHORT).show()
            }
        }

        borrar.setOnClickListener {
            productos.clear()
            precios.clear()
            cantidades.clear()
            imagenes.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Todos los elementos fueron borrados.", Toast.LENGTH_SHORT).show()
        }
    }
}

class SeleccionadosAdapter(
    private val context: Context,
    private val productos: MutableList<String>,
    private val precios: MutableList<Double>,
    private val cantidades: MutableList<Int>,
    private val imagenes: MutableList<Int> // Lista de imágenes
) : ArrayAdapter<String>(context, R.layout.fila_lista_principal, productos) {

    // Lista para rastrear elementos seleccionados
    private val seleccionados = MutableList(productos.size) { false }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val rowView = convertView ?: inflater.inflate(R.layout.fila_lista_principal, parent, false)

        // Configurar los elementos de la fila
        val nombreProducto = rowView.findViewById<TextView>(R.id.textViewNombreProducto)
        val precioUnitario = rowView.findViewById<TextView>(R.id.textViewPrecioUniProducto)
        val cantidad = rowView.findViewById<TextView>(R.id.textViewCantidadTotal)
        val totalProducto = rowView.findViewById<TextView>(R.id.textViewTotalProducto)
        val imagenProducto = rowView.findViewById<ImageView>(R.id.imageViewProducto) // Imagen del producto
        val checkBox = rowView.findViewById<CheckBox>(R.id.checkBoxBorrar)

        // Asignar los valores
        nombreProducto.text = productos[position]
        precioUnitario.text = "€ ${precios[position]}"
        cantidad.text = cantidades[position].toString()

        // Calcular el total del producto
        val total = precios[position] * cantidades[position]
        totalProducto.text = "€ %.2f".format(total)

        // Asignar la imagen
        imagenProducto.setImageResource(imagenes[position])

        // Configurar el estado del CheckBox
        checkBox.isChecked = seleccionados[position]
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            seleccionados[position] = isChecked
        }

        return rowView
    }

    // Método para obtener los elementos seleccionados
    fun getSelectedItems(): List<Int> {
        return seleccionados.mapIndexedNotNull { index, isSelected -> if (isSelected) index else null }
    }

    // Método para eliminar elementos seleccionados
    fun removeItems(indices: List<Int>) {
        indices.sortedDescending().forEach { index ->
            productos.removeAt(index)
            precios.removeAt(index)
            cantidades.removeAt(index)
            imagenes.removeAt(index) // Eliminar la imagen correspondiente
            seleccionados.removeAt(index)
        }
        notifyDataSetChanged()
    }
}
