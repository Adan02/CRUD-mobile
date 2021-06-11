package com.example.crud

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MahasiswaAdapter(val konteks : Context, val layoutmhs:Int, val mhsList : List<Mahasiswa>):ArrayAdapter<Mahasiswa>(konteks,layoutmhs,mhsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater= LayoutInflater.from(konteks)
        val view:View=layoutInflater.inflate(layoutmhs,null)
        val Nama : TextView= view.findViewById(R.id.nama)
        val NIM : TextView=view.findViewById(R.id.nim)
        val edit : TextView=view.findViewById(R.id.edit)
        val mahasiswa=mhsList[position]
        edit.setOnClickListener{
            update(mahasiswa)
        }
        Nama.text=mahasiswa.nama
        NIM.text=mahasiswa.nim
        return view
    }
    private fun update(mahasiswa: Mahasiswa) {
        val builder=AlertDialog.Builder(konteks)
        builder.setTitle("Edit data")
        val inflater=LayoutInflater.from(konteks)
        val view =inflater.inflate(R.layout.update,null)
        val editNama = view.findViewById<EditText>(R.id.edit_nama)
        val editNIM = view.findViewById<EditText>(R.id.edit_nim)
        editNama.setText(mahasiswa.nama)
        editNIM.setText(mahasiswa.nim)

        builder.setView(view)
        builder.setPositiveButton("Update"){p0,p1 ->
            val db = FirebaseDatabase.getInstance().getReference("Mahasiswa")
            val nama = editNama.text.toString().trim()
            val NIM = editNIM.text.toString().trim()
            if (nama.isEmpty()){
                editNama.error="Isi Nama"
                editNama.requestFocus()
                return@setPositiveButton
            }
            if (NIM.isEmpty()) {
                editNIM.error = "Isi NIM"
                editNIM.requestFocus()
                return@setPositiveButton
            }
            val mahasiswa = Mahasiswa(mahasiswa.id,nama,NIM)
            db.child(mahasiswa.id!!).setValue(mahasiswa)
            Toast.makeText(konteks,"Data diupdate",Toast.LENGTH_SHORT).show()
        }
        builder.setNeutralButton("Cancel"){p0,p1->

        }
        builder.setNegativeButton("Hapus"){p0,p1->
            val db = FirebaseDatabase.getInstance().getReference("Mahasiswa").child(mahasiswa.id)
            db.removeValue()
            Toast.makeText(konteks,"Data dihapus",Toast.LENGTH_SHORT).show()
        }
        val alert =builder.create()
        alert.show()
    }
}