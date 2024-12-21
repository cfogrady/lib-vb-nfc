package com.github.cfogrady.vbnfc.data

open class NfcCharacter(
    var dimId: UShort,
    var charIndex: UShort,
    var phase: Byte,
    var attribute: Attribute,
    var mood: Byte,
    var vitalPoints: UShort,
    var transformationCountdown: UShort,
    var injuryStatus: UShort,
    var trophies: UShort,
    var currentPhaseBattlesWon: UShort,
    var currentPhaseBattlesLost: UShort,
    var totalBattlesWon: UShort,
    var totalBattlesLost: UShort,
    var activityLevel: Byte,
    var heartRateCurrent: Byte,
    var transformationHistory: Array<Transformation>
) {

    data class Transformation(
        val toCharIndex: Byte,
        val year: Byte,
        val month: Byte,
        val day: Byte)

    enum class AbilityRarity {
        None,
        Common,
        Rare,
        SuperRare,
        SuperSuperRare,
        UltraRare,
    }

    enum class Attribute {
        None,
        Virus,
        Data,
        Vaccine,
        Free
    }
}
