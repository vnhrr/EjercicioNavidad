package com.example.diseointerfaces

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
import androidx.appcompat.app.AppCompatActivity

class IncluirActivity : AppCompatActivity(){

    private val productos = arrayOf(
        "Pan",
        "Galletas",
        "Carne",
        "Dorada",
        "Mango"
    )

    private val precioUni = arrayOf(
        0.70,
        1.34,
        4.67,
        5.80,
        0.80
    )

    private val imagenProductos = arrayOf(
        R.mipmap.pan,
        R.mipmap.galletas,
        R.mipmap.carne,
        R.mipmap.dorada,
        R.mipmap.mango
    )

    private val secciones = arrayOf(
        "Panaderia",
        "Dulces",
        "Carniceria",
        "Pescaderia",
        "Fruteria"
    )

    private val isCheckedList = MutableList(productos.size) { false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incluir)

        val listaProductos = findViewById<ListView>(R.id.ListViewProductos)
        val adaptadorPersonalizado = AdaptadorPersonalizado(this, R.layout.fila_lista_incluir, productos)
        listaProductos.adapter = adaptadorPersonalizado

        val incluir = findViewById<Button>(R.id.buttonAñadir)

        incluir.setOnClickListener {
            val productosSeleccionados = mutableListOf<String>()
            val preciosSeleccionados = mutableListOf<Double>()
            val cantidadesSeleccionadas = mutableListOf<Int>()
            val imagenesSeleccionadas = mutableListOf<Int>()

            for (i in isCheckedList.indices) {
                if (isCheckedList[i]) { // Si está seleccionado
                    productosSeleccionados.add(productos[i])
                    preciosSeleccionados.add(precioUni[i])
                    imagenesSeleccionadas.add(imagenProductos[i]) // Añadir la imagen correspondiente

                    // Recuperar la cantidad del TextView en la fila personalizada
                    val listaProductos = findViewById<ListView>(R.id.ListViewProductos)
                    val rowView = listaProductos.getChildAt(i)
                    val cantidadText = rowView?.findViewById<TextView>(R.id.textViewUnidades)?.text.toString()
                    cantidadesSeleccionadas.add(cantidadText.toIntOrNull() ?: 0)
                }
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putStringArrayListExtra("productos", ArrayList(productosSeleccionados))
            intent.putExtra("precios", preciosSeleccionados.toDoubleArray()) // Pasar como DoubleArray
            intent.putIntegerArrayListExtra("cantidades", ArrayList(cantidadesSeleccionadas)) // Pasar como IntegerArrayList
            intent.putIntegerArrayListExtra("imagenes", ArrayList(imagenesSeleccionadas)) // Pasar imágenes como IntegerArrayList
            startActivity(intent)
        }
    }

    private inner class AdaptadorPersonalizado(
        context: Context,
        resource: Int,
        objects: Array<String>
    ) : ArrayAdapter<String>(context, resource, objects) {
        override fun getDropDownView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            return crearFilaPersonalizada(position, convertView, parent)
        }

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            return crearFilaPersonalizada(position, convertView, parent)
        }

        private fun crearFilaPersonalizada(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            val layoutInflater = LayoutInflater.from(context)
            val rowView = convertView ?: layoutInflater.inflate(R.layout.fila_lista_incluir, parent, false)

            rowView.findViewById<TextView>(R.id.textViewNombreProducto).text = productos[position]
            rowView.findViewById<TextView>(R.id.textViewPrecioUni).text = precioUni[position].toString()
            rowView.findViewById<ImageView>(R.id.imageViewProducto).setImageResource(imagenProductos[position])
            rowView.findViewById<TextView>(R.id.textViewSeccion).text = secciones[position]
            val seleccionado = rowView.findViewById<CheckBox>(R.id.checkBoxSeleccionado)

            val cant = rowView.findViewById<TextView>(R.id.textViewUnidades)
            cant.text = "0"

            val mas = rowView.findViewById<Button>(R.id.buttonMas)
            val menos = rowView.findViewById<Button>(R.id.buttonMenos)

            mas.setOnClickListener {
                val cantidad = cant.text.toString().toInt()
                cant.text = (cantidad + 1).toString()
            }

            menos.setOnClickListener {
                val cantidad = cant.text.toString().toInt()
                if (cantidad > 0) {
                    cant.text = (cantidad - 1).toString()
                }
            }

            seleccionado.setOnCheckedChangeListener { _, isChecked ->
                isCheckedList[position] = isChecked
            }

            return rowView
        }
    }
}
