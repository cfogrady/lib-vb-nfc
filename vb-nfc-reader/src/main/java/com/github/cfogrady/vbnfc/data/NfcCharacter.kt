package com.github.cfogrady.vbnfc.data

import java.util.Objects

open class NfcCharacter(
    val dimId: UShort,
    var charIndex: UShort = 0u,
    var stage: Byte = 0,
    var attribute: Attribute = Attribute.None,
    var ageInDays: Byte = 0,
    var nextAdventureMissionStage: Byte = 1, // next adventure mission stage on the character's dim
    var mood: Byte = 50,
    var vitalPoints: UShort = 0u,
    var transformationCountdown: UShort = 0u,
    var injuryStatus: InjuryStatus = InjuryStatus.None,
    var trophies: UShort = 0u,
    var currentPhaseBattlesWon: UShort = 0u,
    var currentPhaseBattlesLost: UShort = 0u,
    var totalBattlesWon: UShort = 0u,
    var totalBattlesLost: UShort = 0u,
    var activityLevel: Byte = 0,
    var heartRateCurrent: UByte = 0u,
    var transformationHistory: Array<Transformation> = Array(8) {Transformation(-1, -1, -1, -1)}
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



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NfcCharacter

        if (dimId != other.dimId) return false
        if (charIndex != other.charIndex) return false
        if (stage != other.stage) return false
        if (attribute != other.attribute) return false
        if (ageInDays != other.ageInDays) return false
        if (nextAdventureMissionStage != other.nextAdventureMissionStage) return false
        if (mood != other.mood) return false
        if (vitalPoints != other.vitalPoints) return false
        if (transformationCountdown != other.transformationCountdown) return false
        if (injuryStatus != other.injuryStatus) return false
        if (trophies != other.trophies) return false
        if (currentPhaseBattlesWon != other.currentPhaseBattlesWon) return false
        if (currentPhaseBattlesLost != other.currentPhaseBattlesLost) return false
        if (totalBattlesWon != other.totalBattlesWon) return false
        if (totalBattlesLost != other.totalBattlesLost) return false
        if (activityLevel != other.activityLevel) return false
        if (heartRateCurrent != other.heartRateCurrent) return false
        if (!transformationHistory.contentEquals(other.transformationHistory)) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(
            dimId,
            charIndex,
            stage,
            attribute,
            ageInDays,
            nextAdventureMissionStage,
            mood,
            vitalPoints,
            transformationCountdown,
            injuryStatus,
            trophies,
            currentPhaseBattlesWon,
            currentPhaseBattlesLost,
            totalBattlesWon,
            totalBattlesLost,
            activityLevel,
            heartRateCurrent,
            transformationHistory.contentHashCode()
        )
    }

    override fun toString(): String {
        return """NfcCharacter(
    dimId=$dimId,
    charIndex=$charIndex,
    stage=$stage,
    attribute=$attribute,
    ageInDays=$ageInDays,
    nextAdventureMissionStage=$nextAdventureMissionStage,
    mood=$mood,
    vitalPoints=$vitalPoints,
    transformationCountdown=$transformationCountdown,
    injuryStatus=$injuryStatus,
    trophies=$trophies,
    currentPhaseBattlesWon=$currentPhaseBattlesWon,
    currentPhaseBattlesLost=$currentPhaseBattlesLost,
    totalBattlesWon=$totalBattlesWon,
    totalBattlesLost=$totalBattlesLost,
    activityLevel=$activityLevel,
    heartRateCurrent=$heartRateCurrent,
    transformationHistory=${transformationHistory.contentToString()}
)"""
    }


}
