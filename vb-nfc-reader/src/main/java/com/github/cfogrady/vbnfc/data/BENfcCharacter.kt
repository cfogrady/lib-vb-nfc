package com.github.cfogrady.vbnfc.data

class BENfcCharacter(
    dimId: UShort,
    charIndex: UShort,
    phase: Byte,
    attribute: Attribute,
    mood: Byte,
    vitalPoints: UShort,
    transformationCountdown: UShort,
    injuryStatus: UShort,
    trainingPp: UShort,
    currentPhaseBattlesWon: UShort,
    currentPhaseBattlesLost: UShort,
    totalBattlesWon: UShort,
    totalBattlesLost: UShort,
    activityLevel: Byte,
    heartRateCurrent: Byte,
    transformationHistory: Array<Transformation>,
    var trainingHp: UShort,
    var trainingAp: UShort,
    var trainingBp: UShort,
    var remainingTrainingTime: UShort,
    var itemEffectMentalStateValue: Byte,
    var itemEffectMentalStateMinutesRemaining: Byte,
    var itemEffectActivityLevelValue: Byte,
    var itemEffectActivityLevelMinutesRemaining: Byte,
    var itemEffectVitalPointsChangeValue: Byte,
    var itemEffectVitalPointsChangeMinutesRemaining: Byte,
    var abilityRarity: AbilityRarity,
    var abilityType: UShort,
    var abilityBranch: UShort,
    var abilityReset: Byte,
    var rank: Byte,
    var itemType: Byte,
    var itemMultiplier: Byte,
    var itemRemainingTime: Byte,
) :
    NfcCharacter(
        dimId,
        charIndex,
        phase,
        attribute,
        mood,
        vitalPoints,
        transformationCountdown,
        injuryStatus,
        trainingPp,
        currentPhaseBattlesWon,
        currentPhaseBattlesLost,
        totalBattlesWon,
        totalBattlesLost,
        activityLevel,
        heartRateCurrent,
        transformationHistory
    )
{
    fun getTrainingPp(): UShort {
        return trophies
    }

    fun setTrainingPp(trainingPp: UShort) {
        trophies = trainingPp
    }
}