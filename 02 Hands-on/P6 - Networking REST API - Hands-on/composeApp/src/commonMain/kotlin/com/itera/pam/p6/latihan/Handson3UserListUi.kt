package com.itera.pam.p6.latihan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Hands-on 3: Tampilkan Data di Compose dengan Loading/Error UI
// Tugas: pakai UserListStateHolder (dari Handson2Repository.kt) untuk
// menampilkan:
// - CircularProgressIndicator() selagi UiState.Loading
// - LazyColumn berisi nama & email user selagi UiState.Success
// - Pesan error + tombol "Coba Lagi" (yang memanggil ulang load()) selagi
//   UiState.Error
//
// CATATAN: fungsi ini sengaja belum menampilkan apa-apa selain placeholder —
// lengkapi semua TODO di bawah supaya UI-nya berfungsi.

@Composable
fun Handson3Screen() {
    // TODO 1: buat state holder: val holder = remember { UserListStateHolder() }
    val holder = remember { UserListStateHolder() }

    // TODO 2: collect state-nya: val state by holder.state.collectAsState()
    val state by holder.state.collectAsState()

    // TODO 3: panggil holder.load() sekali saat pertama kali screen ini muncul,
    //         pakai: LaunchedEffect(Unit) { holder.load() }
    LaunchedEffect(Unit) { holder.load() }

    // TODO 4: buat scope coroutine untuk tombol retry:
    //         val scope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()

    // TODO 5: buat `when (state)` yang menangani 3 kasus:
    //         - UiState.Loading -> CircularProgressIndicator()
    //         - UiState.Success -> LazyColumn { items(state.users) { user -> ... } }
    //         - UiState.Error   -> Text(state.message) + Button("Coba Lagi") yang
    //                              memanggil scope.launch { holder.load() }
    Column(modifier = Modifier.padding(16.dp)) {
        when (val currentState = state) {
            is UiState.Loading -> CircularProgressIndicator()

            is UiState.Success -> {
                // heightIn: LazyColumn butuh batas tinggi karena App.kt membungkus
                // layar ini dengan Column yang bisa di-scroll (verticalScroll).
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(currentState.users) { user ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(user.name)
                            Text(user.email)
                        }
                    }
                }
            }

            is UiState.Error -> {
                Text(currentState.message)
                Button(onClick = { scope.launch { holder.load() } }) {
                    Text("Coba Lagi")
                }
            }
        }
    }
}
