package chip8;

/** I do this too, keep all extensions in a single file.  It would probably
 * be nice to have a tool that alphabetizes by receiver name
 * in Java I usually use Ctrl-N (find class) for most navigation, but there is no class here
 * so I end up using Ctrl-Shift-N (find file) more in Kotlin
 */

/** my favorite extension in Kotlin!
 * when using this, you can hit Ctrl-B (go to declaration) on the '+'
 * and it will come to this method if it's working properly
 */
fun StringBuilder.plus(item: Any?): StringBuilder {
    this append item
    return this
}

/** In Kotlin I like to put simple methods on one line, and often skip brackets completely
 * using '=' declaration will return the last expression and infer the return type of the method from it */

/** can't use '=' declaration here, since the return value would change to StringBuilder
 * as each call to plus() returns builder for chaining.
 * simplest way is just write it with brackets, but you can also explicity make
 * Unit.VALUE the last expression:
 * fun StringBuilder.line(line: String) = { this + line + "\n"; Unit.VALUE }
 * but then you need brackets anyways, so there's little point
 */
fun StringBuilder.line(line: String) {
    this + line + "\n"
}

fun Int.toHex() = Integer.toHexString(this)

fun Byte.toHex() = Integer.toHexString(this.toInt())

fun Byte.high() = (this.toInt() and 0xf0) shr 4

fun Byte.low() = this.toInt() and 0xf

/** for cases like this I like to explicity state the return type,
 *  since it's not obvious from the code at a quick glance */
fun address(msb: Byte, lsb: Byte): Int = ((msb.toInt() and 0xf) shl 8) or (lsb.toInt().and(0xff))