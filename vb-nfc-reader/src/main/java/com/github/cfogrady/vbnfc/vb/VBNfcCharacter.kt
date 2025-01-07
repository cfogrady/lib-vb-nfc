package com.github.cfogrady.vbnfc.vb

import com.github.cfogrady.vbnfc.data.NfcCharacter
import java.time.LocalDate
import java.util.Objects

class VBNfcCharacter(
    dimId: UShort,
    charIndex: UShort = 0u,
    stage: Byte = 0,
    attribute: Attribute = Attribute.None,
    ageInDays: Byte = 0,
    nextAdventureMissionStage: Byte = 1,
    mood: Byte = 50,
    vitalPoints: UShort = 0u,
    itemEffectMentalStateValue: Byte = 0,
    itemEffectMentalStateMinutesRemaining: Byte = 0,
    itemEffectActivityLevelValue: Byte = 0,
    itemEffectActivityLevelMinutesRemaining: Byte = 0,
    itemEffectVitalPointsChangeValue: Byte = 0,
    itemEffectVitalPointsChangeMinutesRemaining: Byte = 0,
    transformationCountdownInMinutes: UShort = 0u,
    injuryStatus: InjuryStatus = InjuryStatus.None,
    trophies: UShort = 0u,
    currentPhaseBattlesWon: UShort = 0u,
    currentPhaseBattlesLost: UShort = 0u,
    totalBattlesWon: UShort = 0u,
    totalBattlesLost: UShort = 0u,
    activityLevel: Byte = 0,
    heartRateCurrent: UByte = 0u,
    transformationHistory: Array<Transformation> = Array(8) {
        Transformation(
            UByte.MAX_VALUE,
            UShort.MAX_VALUE,
            UByte.MAX_VALUE,
            UByte.MAX_VALUE
        )
    },
    appReserved1: ByteArray = ByteArray(12),
    appReserved2: Array<UShort> = Array(3) { 0u},
    var generation: UShort = 0u,
    var totalTrophies: UShort = 0u,
    var vitalHistory: Array<DailyVitals> = Array(7) {
        DailyVitals(UShort.MIN_VALUE, LocalDate.MIN)
    }
) : NfcCharacter(
    dimId = dimId,
    charIndex = charIndex,
    stage = stage,
    attribute = attribute,
    ageInDays = ageInDays,
    nextAdventureMissionStage = nextAdventureMissionStage,
    mood = mood,
    vitalPoints = vitalPoints,
    itemEffectMentalStateValue = itemEffectMentalStateValue,
    itemEffectMentalStateMinutesRemaining = itemEffectMentalStateMinutesRemaining,
    itemEffectActivityLevelValue = itemEffectActivityLevelValue,
    itemEffectActivityLevelMinutesRemaining = itemEffectActivityLevelMinutesRemaining,
    itemEffectVitalPointsChangeValue = itemEffectVitalPointsChangeValue,
    itemEffectVitalPointsChangeMinutesRemaining = itemEffectVitalPointsChangeMinutesRemaining,
    transformationCountdownInMinutes = transformationCountdownInMinutes,
    injuryStatus = injuryStatus,
    trophies = trophies,
    currentPhaseBattlesWon = currentPhaseBattlesWon,
    currentPhaseBattlesLost = currentPhaseBattlesLost,
    totalBattlesWon = totalBattlesWon,
    totalBattlesLost = totalBattlesLost,
    activityLevel = activityLevel,
    heartRateCurrent = heartRateCurrent,
    transformationHistory = transformationHistory,
    appReserved1 = appReserved1,
    appReserved2 = appReserved2,
) {

    data class DailyVitals(val vitalsGained: UShort, val date: LocalDate) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as VBNfcCharacter

        return generation == other.generation &&
                totalTrophies == other.totalTrophies &&
                vitalHistory.contentEquals(other.vitalHistory)
    }

    override fun hashCode(): Int {
        return Objects.hash(
            super.hashCode(),
            generation,
            totalTrophies,
            vitalHistory.contentHashCode()
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun toString(): String {
        return """
${super.toString()}
VBNfcCharacter(
    generation=$generation,
    totalTrophies=$totalTrophies
    vitalHistory=${vitalHistory.contentToString()}
)"""
    }
}