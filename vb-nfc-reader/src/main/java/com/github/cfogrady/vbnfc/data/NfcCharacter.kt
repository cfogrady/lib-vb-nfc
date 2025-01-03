package com.github.cfogrady.vbnfc.data

open class NfcCharacter(
    val dimId: UShort,
    var charIndex: UShort,
    var phase: Byte,
    var attribute: Attribute,
    var ageInDays: Byte,
    var nextAdventureMissionStage: Byte, // next adventure mission stage on the character's dim
    var mood: Byte,
    var vitalPoints: UShort,
    var transformationCountdown: UShort,
    var injuryStatus: InjuryStatus,
    var trophies: UShort,
    var currentPhaseBattlesWon: UShort,
    var currentPhaseBattlesLost: UShort,
    var totalBattlesWon: UShort,
    var totalBattlesLost: UShort,
    var activityLevel: Byte,
    var heartRateCurrent: UByte,
    var transformationHistory: Array<Transformation>
) {

    data class Transformation(
        val toCharIndex: Byte,
        val yearsSince1988: Byte,
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
    enum class InjuryStatus {
        None,
        Injury,
        InjuryHealed,
        InjuryTwo,
        InjuryTwoHealed,
        InjuryThree,
        InjuryThreeHealed,
        InjuryFour,
    }

    fun getTransformationHistoryString(separator: String = System.lineSeparator()): String {
        val builder = StringBuilder()
        for(i in transformationHistory.indices) {
            builder.append(transformationHistory[i])
            if(i != transformationHistory.size-1) {
                builder.append(separator)
            }
        }
        return builder.toString()
    }
}
