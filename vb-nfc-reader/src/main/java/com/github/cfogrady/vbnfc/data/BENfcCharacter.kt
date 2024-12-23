package com.github.cfogrady.vbnfc.data

class BENfcCharacter(
    dimId: UShort,
    charIndex: UShort,
    phase: Byte,
    attribute: Attribute,
    mood: Byte,
    vitalPoints: UShort,
    transformationCountdown: UShort,
    injuryStatus: InjuryStatus,
    trainingPp: UShort,
    currentPhaseBattlesWon: UShort,
    currentPhaseBattlesLost: UShort,
    totalBattlesWon: UShort,
    totalBattlesLost: UShort,
    activityLevel: Byte,
    heartRateCurrent: UByte,
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
    internal val otp0: ByteArray, // OTP matches the character to the dim
    internal val otp1: ByteArray, // OTP matches the character to the dim
    var appReserved1: ByteArray, // this is a 12 byte array reserved for new app features, a custom app should be able to safely use this for custom features
    var appReserved2: Array<UShort>, // this is a 3 element array reserved for new app features, a custom app should be able to safely use this for custom features
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

    fun getWinPercentage(): Byte {
        val totalBatles = currentPhaseBattlesWon + currentPhaseBattlesLost
        if (totalBatles == 0u) {
            return 0
        }
        return ((100u * currentPhaseBattlesWon) / totalBatles).toByte()
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun toString(): String {
        return """
    dimId: $dimId,
    charIndex: $charIndex,
    phase: $phase,
    attribute: $attribute,
    mood: $mood,
    vitalPoints: $vitalPoints,
    transformationCountdown: $transformationCountdown,
    injuryStatus: $injuryStatus,
    trainingPp: $trophies,
    currentPhaseBattlesWon: $currentPhaseBattlesWon,
    currentPhaseBattlesLost: $currentPhaseBattlesLost,
    totalBattlesWon: $totalBattlesWon,
    totalBattlesLost: $totalBattlesLost,
    activityLevel: $activityLevel,
    heartRateCurrent: $heartRateCurrent,
    transformationHistory: $transformationHistory,
    trainingHp: $trainingHp,
    trainingAp: $trainingAp,
    trainingBp: $trainingBp,
    remainingTrainingTime: $remainingTrainingTime,
    itemEffectMentalStateValue: $itemEffectMentalStateValue,
    itemEffectMentalStateMinutesRemaining: $itemEffectMentalStateMinutesRemaining,
    itemEffectActivityLevelValue: $itemEffectActivityLevelValue,
    itemEffectActivityLevelMinutesRemaining: $itemEffectActivityLevelMinutesRemaining,
    itemEffectVitalPointsChangeValue: $itemEffectVitalPointsChangeValue,
    itemEffectVitalPointsChangeMinutesRemaining: $itemEffectVitalPointsChangeMinutesRemaining,
    abilityRarity: $abilityRarity,
    abilityType: $abilityType,
    abilityBranch: $abilityBranch,
    abilityReset: $abilityReset,
    rank: $rank,
    itemType: $itemType,
    itemMultiplier: $itemMultiplier,
    itemRemainingTime: $itemRemainingTime,
    otp0: ${otp0.toHexString()}
    otp1: ${otp1.toHexString()}
    appReserved1: ${appReserved1.toHexString()}
    appReserved2: $appReserved2
        """.trimIndent()
    }
}