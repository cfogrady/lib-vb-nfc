package com.github.cfogrady.vbnfc

import android.util.Log
import java.nio.ByteOrder

class NfcCharacterFactory {
    fun buildNfcCharacterFromBytes(rawCharacter: ByteArray, productId: UShort): NfcCharacter {
        val pages = ConvertToPages(rawCharacter)
        val abilityRarity = pages[80][2].toInt()
        Log.i("NfcCharacterFactory", "Ability Rarity: $abilityRarity")
        return NfcCharacter(
            dimId = pages[26].getUInt16(2, ByteOrder.BIG_ENDIAN),
            slotId = pages[26].getUInt16(0, ByteOrder.BIG_ENDIAN),
            abilityRarity = NfcCharacter.AbilityRarity.entries[abilityRarity],
            abilityId = pages[81].getUInt16(2, ByteOrder.BIG_ENDIAN),
            mood = pages[40][1].toUByte(),
            vitals = pages[41].getUInt16(byteOrder = ByteOrder.BIG_ENDIAN),
            transformationTimer = pages[43].getUInt16(1, ByteOrder.BIG_ENDIAN),
            hpTraining = pages[72].getUInt16(byteOrder = ByteOrder.BIG_ENDIAN),
            apTraining = pages[72].getUInt16(2, ByteOrder.BIG_ENDIAN),
            bpTraining = pages[73].getUInt16(byteOrder = ByteOrder.BIG_ENDIAN),
            trainingLimit = pages[74].getUInt16(2, ByteOrder.BIG_ENDIAN)
        )
    }
}