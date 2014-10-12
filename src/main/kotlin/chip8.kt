package chip8;

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.DataInputStream

trait Decoder {
    fun before(opCode: Int, address: Int)
    fun unknown(opCode: Int)
    fun clear()
    fun ret()
    fun callRca(address: Int)
    fun jmp (address: Int)
    fun call(address: Int)
    fun jeq (reg: Int, value: Int)
    fun jneq (reg: Int, value: Int)
    fun jeqr (reg1: Int, reg2: Int)
    fun set (reg: Int, value: Int)
    fun add (reg: Int, value: Int)
    fun setr (reg1: Int, reg2: Int)
    fun or (reg1: Int, reg2: Int)
    fun and (reg1: Int, reg2: Int)
    fun xor (reg1: Int, reg2: Int)
    fun addr (reg1: Int, reg2: Int)
    fun sub (reg1: Int, reg2: Int)
    fun shr (reg1: Int)
    fun subb (reg1: Int, reg2: Int)
    fun shl (reg1: Int)
    fun jneqr (reg1: Int, reg2: Int)
    fun seti (value: Int)
    fun jmpv0 (address: Int)
    fun rand (reg: Int, value: Int)
    fun draw (reg1: Int, reg2: Int, value: Int)
    fun jkey (reg: Int)
    fun jnkey (reg: Int)
    fun getdelay (reg: Int)
    fun waitkey (reg: Int)
    fun setdelay (reg: Int)
    fun setsound (reg: Int)
    fun addi (reg: Int)
    fun spritei (reg: Int)
    fun bcd (reg: Int)
    fun push (reg: Int)
    fun pop (reg: Int)
}

fun decode(decoder: Decoder, address:Int, msb: Byte, lsb: Byte) {
    val opCode = (msb.toInt() shl 8 or lsb.toInt().and(0xff)).and(0xffff)
    decoder.before(opCode, address)
    when (msb.high()) {
        0x0 -> {
            when (msb.toInt() shl 8 or lsb.toInt()) {
                0x00e0 -> decoder.clear()
                0x00ee -> decoder.ret()
                else -> decoder.callRca(address(msb, lsb))
            }
        }
        0x1 -> decoder.jmp(address(msb, lsb))
        0x2 -> decoder.call(address(msb, lsb))
        0x3 -> decoder.jeq(msb.low(), lsb.toInt())
        0x4 -> decoder.jneq(msb.low(), lsb.toInt())
        0x5 -> decoder.jeqr(msb.low(), lsb.high())
        0x6 -> decoder.set(msb.low(), lsb.toInt())
        0x7 -> decoder.add(msb.low(), lsb.toInt())
        0x8 -> {
            val reg1 = msb.low()
            val reg2 = lsb.high()
            when(lsb.low()) {
                0x0 -> decoder.setr(reg1, reg2)
                0x1 -> decoder.or(reg1, reg2)
                0x2 -> decoder.and(reg1, reg2)
                0x3 -> decoder.xor(reg1, reg2)
                0x4 -> decoder.addr(reg1, reg2)
                0x5 -> decoder.sub(reg1, reg2)
                0x6 -> decoder.shr(reg1)
                0x7 -> decoder.subb(reg1, reg2)
                0xe -> decoder.shl(reg1)
                else -> decoder.unknown(opCode)
            }
        }
        0x9 -> {
            val reg1 = msb.low()
            val reg2 = lsb.high()
            decoder.jneqr(reg1, reg2)
        }
        0xa -> decoder.seti(address(msb, lsb))
        0xb -> decoder.jmpv0(address(msb, lsb))
        0xc -> decoder.rand(msb.low(), lsb.toInt())
        0xd -> decoder.draw(msb.low(), lsb.high(), lsb.low())
        0xe -> {
            when(lsb.toInt() or 0xff) {
                0x9e -> decoder.jkey(msb.low())
                0xa1 -> decoder.jnkey(msb.low())
                else -> decoder.unknown(opCode)
            }
        }
        0xf -> {
            val reg = msb.low()
            when(lsb.toInt() or 0xff) {
                0x07 -> decoder.getdelay(reg)
                0x0a -> decoder.waitkey(reg)
                0x15 -> decoder.setdelay(reg)
                0x18 -> decoder.setsound(reg)
                0x1e -> decoder.addi(reg)
                0x29 -> decoder.spritei(reg)
                0x33 -> decoder.bcd(reg)
                0x55 -> decoder.push(reg)
                0x65 -> decoder.pop(reg)
                else -> decoder.unknown(opCode)
            }
        }
        else -> decoder.unknown(opCode)
    }
}

fun readRom(fileName: String): ByteArray = DataInputStream(BufferedInputStream(FileInputStream(fileName))).readBytes()

fun main(args: Array<String>) {
    val rom = readRom("roms/maze.rom")
    println(disassemble(rom))
}
