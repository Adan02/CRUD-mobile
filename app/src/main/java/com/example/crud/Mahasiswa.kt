package com.example.crud

data class Mahasiswa(
    val id : String,
    val nama :String,
    val nim :String
){
    constructor():this("","",""){}
}