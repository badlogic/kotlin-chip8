package chip8;

fun StringBuilder.line(line: String) {
    this.append(line); this.append("\n")
}
fun Int.toHex(): String {
    return Integer.toHexString(this)
}
fun Byte.toHex(): String {
    return Integer.toHexString(this.toInt())
}
fun Byte.high(): Int {
    return (this.toInt() and 0xf0) shr 4
}
fun Byte.low(): Int {
    return this.toInt() and 0xf
}
fun address(msb: Byte, lsb: Byte): Int {
    return ((msb.toInt() and 0xf) shl 8) or (lsb.toInt().and(0xff))
}