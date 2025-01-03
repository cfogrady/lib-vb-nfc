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
    var itemEffectMentalStateValue: Byte = 0,
    var itemEffectMentalStateMinutesRemaining: Byte = 0,
    var itemEffectActivityLevelValue: Byte = 0,
    var itemEffectActivityLevelMinutesRemaining: Byte = 0,
    var itemEffectVitalPointsChangeValue: Byte = 0,
    var itemEffectVitalPointsChangeMinutesRemaining: Byte = 0,
    var transformationCountdownInMinutes: UShort = 0u,
    var injuryStatus: InjuryStatus = InjuryStatus.None,
    var trophies: UShort = 0u,
    var currentPhaseBattlesWon: UShort = 0u,
    var currentPhaseBattlesLost: UShort = 0u,
    var totalBattlesWon: UShort = 0u,
    var totalBattlesLost: UShort = 0u,
    var activityLevel: Byte = 0,
    var heartRateCurrent: UByte = 0u,
    var transformationHistory: Array<Transformation> = Array(8) {Transformation(-1, -1, -1, -1)},
    var appReserved1: ByteArray = ByteArray(12), // this is a 12 byte array reserved for new app features, a custom app should be able to safely use this for custom features
    var appReserved2: Array<UShort> = Array(3) {0u}, // this is a 3 element array reserved for new app features, a custom app should be able to safely use this for custom features
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

    fun getWinPercentage(): Byte {
        val totalBatles = currentPhaseBattlesWon + currentPhaseBattlesLost
        if (totalBatles == 0u) {
            return 0
        }
        return ((100u * currentPhaseBattlesWon) / totalBatles).toByte()
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
        if (itemEffectMentalStateValue != other.itemEffectMentalStateValue) return false
        if (itemEffectMentalStateMinutesRemaining != other.itemEffectMentalStateMinutesRemaining) return false
        if (itemEffectActivityLevelValue != other.itemEffectActivityLevelValue) return false
        if (itemEffectActivityLevelMinutesRemaining != other.itemEffectActivityLevelMinutesRemaining) return false
        if (itemEffectVitalPointsChangeValue != other.itemEffectVitalPointsChangeValue) return false
        if (itemEffectVitalPointsChangeMinutesRemaining != other.itemEffectVitalPointsChangeMinutesRemaining) return false
        if (transformationCountdownInMinutes != other.transformationCountdownInMinutes) return false
        if (injuryStatus != other.injuryStatus) return false
        if (trophies != other.trophies) return false
        if (currentPhaseBattlesWon != other.currentPhaseBattlesWon) return false
        if (currentPhaseBattlesLost != other.currentPhaseBattlesLost) return false
        if (totalBattlesWon != other.totalBattlesWon) return false
        if (totalBattlesLost != other.totalBattlesLost) return false
        if (activityLevel != other.activityLevel) return false
        if (heartRateCurrent != other.heartRateCurrent) return false
        if (!transformationHistory.contentEquals(other.transformationHistory)) return false
        if (!appReserved1.contentEquals(other.appReserved1)) return false
        if (!appReserved2.contentEquals(other.appReserved2)) return false

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
            itemEffectMentalStateValue,
            itemEffectMentalStateMinutesRemaining,
            itemEffectActivityLevelValue,
            itemEffectActivityLevelMinutesRemaining,
            itemEffectVitalPointsChangeValue,
            itemEffectVitalPointsChangeMinutesRemaining,
            transformationCountdownInMinutes,
            injuryStatus,
            trophies,
            currentPhaseBattlesWon,
            currentPhaseBattlesLost,
            totalBattlesWon,
            totalBattlesLost,
            activityLevel,
            heartRateCurrent,
            transformationHistory.contentHashCode(),
            appReserved1.contentHashCode(),
            appReserved2.contentHashCode()
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
    itemEffectMentalStateValue=$itemEffectMentalStateValue,
    itemEffectMentalStateMinutesRemaining=$itemEffectMentalStateMinutesRemaining,
    itemEffectActivityLevelValue=$itemEffectActivityLevelValue,
    itemEffectActivityLevelMinutesRemaining=$itemEffectActivityLevelMinutesRemaining,
    itemEffectVitalPointsChangeValue=$itemEffectVitalPointsChangeValue,
    itemEffectVitalPointsChangeMinutesRemaining=$itemEffectVitalPointsChangeMinutesRemaining,
    transformationCountdown=$transformationCountdownInMinutes,
    injuryStatus=$injuryStatus,
    trophies=$trophies,
    currentPhaseBattlesWon=$currentPhaseBattlesWon,
    currentPhaseBattlesLost=$currentPhaseBattlesLost,
    totalBattlesWon=$totalBattlesWon,
    totalBattlesLost=$totalBattlesLost,
    activityLevel=$activityLevel,
    heartRateCurrent=$heartRateCurrent,
    transformationHistory=${transformationHistory.contentToString()},
    appReserved1=${appReserved1.contentToString()},
    appReserved2=${appReserved2.contentToString()}
)"""
    }


}
