// Hands-on 1: Inline Function & Reified Generics
//
// Konteks: JVM menerapkan TYPE ERASURE pada generics — informasi tipe generik
// (mis. List<String> vs List<Int>) DIHAPUS saat dikompilasi menjadi bytecode
// .class, sehingga pada saat runtime JVM hanya melihat "List" biasa. Karena
// itu, fungsi generik biasa TIDAK BISA melakukan pengecekan `value is T`.
//
// Kotlin punya solusi: kombinasi `inline` + `reified`. Fungsi inline "disalin"
// (di-inline) langsung ke tempat pemanggilannya oleh compiler backend Kotlin
// SEBELUM menjadi bytecode, sehingga tipe T yang konkret (mis. String) ikut
// tersalin dan tidak pernah "dihapus" oleh JVM.
//
// Tugas: Lengkapi fungsi isType() di bawah agar bisa mengecek tipe value pada
// runtime.

// TODO 1: Tambahkan modifier `inline` pada fungsi ini
// TODO 2: Ubah <T> menjadi <reified T> agar T bisa dipakai di `is` check
inline fun <reified T> isType(value: Any): Boolean {
    // TODO 3: Ganti baris di bawah dengan: return value is T
    return value is T
}

fun main() {
    println(isType<String>("Hello"))   // harus mencetak: true
    println(isType<Int>("Hello"))      // harus mencetak: false
    println(isType<String>(123))       // harus mencetak: false

    // Pertanyaan diskusi (jawab di komentar):
    // Kenapa `fun <T> isType(value: Any): Boolean = value is T` TIDAK BISA
    // dikompilasi tanpa `inline` + `reified`? Jelaskan hubungannya dengan
    // type erasure pada JVM bytecode.
    //
    // Jawaban:
    // Karena type erasure, saat dikompilasi menjadi bytecode informasi tipe T
    // dihapus, sehingga pada runtime JVM tidak tahu T itu tipe apa (String, Int,
    // dll). Akibatnya pengecekan `value is T` tidak mungkin dilakukan dan
    // compiler menolaknya ("Cannot check for instance of erased type: T").
    // Dengan `inline`, isi fungsi disalin ke setiap tempat pemanggilannya, dan
    // dengan `reified`, T di dalamnya diganti dengan tipe konkret dari pemanggil
    // (mis. isType<String> menjadi `value is String`). Jadi tipe T tidak pernah
    // terhapus dan pengecekan `is` bisa dilakukan saat runtime.
}
