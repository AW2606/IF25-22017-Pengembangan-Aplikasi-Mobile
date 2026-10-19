package com.itera.pam.p7.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itera.pam.p7.db.AppDatabase

// Hands-on 3: Offline-First — Tampilkan & Hapus Catatan
// Gunakan CatatanRepository dari Handson2Repository.kt (yang sudah kamu
// lengkapi) untuk menampilkan daftar catatan dalam LazyColumn, form untuk
// menambah catatan baru, dan tombol hapus per item.
//
// CATATAN: layar ini SENGAJA belum menampilkan apa pun (hanya placeholder
// di bawah) sampai semua TODO dilengkapi.

@Composable
fun Handson3Screen(database: AppDatabase) {
    // TODO 1: buat val repository = remember { CatatanRepository(database) }
    val repository = remember { CatatanRepository(database) }

    // TODO 2: collect repository.getAll() sebagai Compose state, misalnya:
    //         val catatanList by repository.getAll().collectAsState(initial = emptyList())
    val catatanList by repository.getAll().collectAsState(initial = emptyList())

    var judul by remember { mutableStateOf("") }
    var isi by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // TODO 4: tambahkan 2 OutlinedTextField (judul, isi) + Button("Tambah")
        //         yang memanggil repository.tambah(judul, isi) lalu mengosongkan field
        OutlinedTextField(value = judul, onValueChange = { judul = it }, label = { Text("Judul") })
        OutlinedTextField(value = isi, onValueChange = { isi = it }, label = { Text("Isi") })
        Button(onClick = {
            repository.tambah(judul, isi)
            judul = ""
            isi = ""
        }) {
            Text("Tambah")
        }

        // TODO 3: tampilkan `catatanList` dengan LazyColumn { items(catatanList) { catatan -> ... } }
        //         setiap item: Text(catatan.judul), Text(catatan.isi),
        //         Button("Hapus") { repository.hapus(catatan.id) }
        // heightIn: LazyColumn butuh batas tinggi karena App.kt membungkus
        // layar ini dengan Column yang bisa di-scroll (verticalScroll).
        LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
            items(catatanList) { catatan ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(catatan.judul)
                    Text(catatan.isi)
                    Button(onClick = { repository.hapus(catatan.id) }) {
                        Text("Hapus")
                    }
                }
            }
        }
    }

    // TODO 5: build & jalankan, tutup aplikasi, lalu buka lagi — catatan
    //         yang kamu tambahkan harus tetap ada (itulah "offline-first")
}
