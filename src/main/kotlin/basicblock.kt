package chip8

data class BasicBlock(address: Int, instructions: ByteArray)

fun gatherBasicBlocks(rom: ByteArray): List<BasicBlock>? {
    return null;
}