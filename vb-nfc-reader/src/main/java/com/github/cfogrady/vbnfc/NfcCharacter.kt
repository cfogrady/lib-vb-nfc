package com.github.cfogrady.vbnfc

data class NfcCharacter(
    val dimId: UShort,
    val slotId: UShort,
    val abilityRarity: AbilityRarity,
    val abilityId: UShort,
    val mood: UByte,
    val vitals: UShort,
    val transformationTimer: UShort,
    val hpTraining: UShort,
    val apTraining: UShort,
    val bpTraining: UShort,
    val trainingLimit: UShort
) {
    enum class AbilityRarity {
        None,
        Common,
        Rare,
        SuperRare,
        SuperSuperRare,
        UltraRare,
    }
}
