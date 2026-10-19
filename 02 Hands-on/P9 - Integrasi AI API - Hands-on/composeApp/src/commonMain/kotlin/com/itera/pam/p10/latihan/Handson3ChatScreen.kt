package com.itera.pam.p10.latihan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itera.pam.p10.MockAiRepository
import kotlinx.coroutines.launch

// Hands-on 3: AI-Powered Chat UI
// Tugas: buat UI chat sederhana: TextField input, tombol "Kirim", daftar
// bubble pesan (isUser: Boolean, text: String) yang bisa di-scroll, dan
// indikator loading saat menunggu jawaban dari AiRepository.ask()
// (pakai MockAiRepository() secara default).
//
// CATATAN: layar ini sengaja hanya menampilkan placeholder di bawah sampai
// semua TODO dilengkapi — itu bagian dari latihan.

data class ChatMessage(val isUser: Boolean, val text: String)

@Composable
fun Handson3Screen() {
    // TODO 1: Buat state `messages`: remember { mutableStateOf(listOf<ChatMessage>()) }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }

    // TODO 2: Buat state `input` (String, awalnya "") dan `isLoading` (Boolean, awalnya false)
    var input by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val repository = remember { MockAiRepository() }

    // TODO 3: Buat Column berisi:
    //   - LazyColumn yang menampilkan semua `messages` (beda alignment untuk isUser=true/false)
    //   - CircularProgressIndicator jika isLoading == true
    //   - Row berisi OutlinedTextField(input) + Button("Kirim")
    Column(modifier = Modifier.padding(16.dp)) {
        // heightIn: LazyColumn butuh batas tinggi karena App.kt membungkus
        // layar ini dengan Column yang bisa di-scroll (verticalScroll).
        LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
            items(messages) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Text(message.text, modifier = Modifier.padding(8.dp))
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        Row {
            OutlinedTextField(value = input, onValueChange = { input = it })
            Button(onClick = {
                // TODO 4: Saat "Kirim" diklik:
                //   - tambahkan ChatMessage(isUser = true, text = input) ke `messages`
                //   - kosongkan `input`, set isLoading = true
                //   - di dalam rememberCoroutineScope().launch { ... }, panggil
                //     MockAiRepository().ask(pertanyaan), lalu tambahkan hasilnya sebagai
                //     ChatMessage(isUser = false, text = hasil) ke `messages`
                //   - set isLoading = false setelah selesai
                val pertanyaan = input
                messages = messages + ChatMessage(isUser = true, text = pertanyaan)
                input = ""
                isLoading = true
                scope.launch {
                    val hasil = repository.ask(pertanyaan).getOrElse { "Error: ${it.message}" }
                    messages = messages + ChatMessage(isUser = false, text = hasil)
                    isLoading = false
                }
            }) {
                Text("Kirim")
            }
        }
    }
}
