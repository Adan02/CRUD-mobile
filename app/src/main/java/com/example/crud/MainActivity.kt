package com.example.crud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var edtNama : EditText
    private lateinit var edtNIM : EditText
    private lateinit var btnSub : Button
    private lateinit var save : DatabaseReference
    private lateinit var mhsList:MutableList<Mahasiswa>
    private lateinit var listmhs:ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        save = FirebaseDatabase.getInstance().getReference("Mahasiswa")
        edtNama = findViewById(R.id.edit_nama)
        edtNIM = findViewById(R.id.edit_nim)
        btnSub = findViewById(R.id.btnsub)
        listmhs=findViewById(R.id.listmhs)
        btnSub.setOnClickListener(this)
        mhsList= mutableListOf()
        save.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    mhsList.clear()
                    for (h in snapshot.children){
                        val mahasiswa=h.getValue(Mahasiswa::class.java)
                        if (mahasiswa != null) {
                            mhsList.add(mahasiswa)
                        }
                    }
                    val adapter = MahasiswaAdapter(this@MainActivity,R.layout.item,mhsList)
                    listmhs.adapter=adapter
                }
            }

        })
    }

    override fun onClick(p0: View?) {
        nyimpan()
    }

    private fun nyimpan(){
        val nama = edtNama.text.toString().trim()
        val nim = edtNIM.text.toString().trim()

        if (nama.isEmpty()){
            edtNama.error = "Isi Nama"
            return
        }
        if (nim.isEmpty()){
            edtNIM.error = "Isi NIM"
            return
        }


        val Id = save.push().key
        val mhs = Mahasiswa(Id!!,nama,nim)
        if (Id != null) {
            save.child(Id).setValue(mhs).addOnCompleteListener{
                Toast.makeText(applicationContext,"Berhasil menambahkan data", Toast.LENGTH_SHORT).show()
            }
        }

    }
}