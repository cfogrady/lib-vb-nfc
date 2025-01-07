package com.github.cfogrady.vbnfc.be

import com.github.cfogrady.vbnfc.data.NfcCharacter
import java.util.Objects

class BENfcCharacter(
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
    trainingPp: UShort = 0u,
    currentPhaseBattlesWon: UShort = 0u,
    currentPhaseBattlesLost: UShort = 0u,
    totalBattlesWon: UShort = 0u,
    totalBattlesLost: UShort = 0u,
    activityLevel: Byte = 0,
    heartRateCurrent: UByte = 0u,
    transformationHistory: Array<Transformation> = Array(8) {Transformation(UByte.MAX_VALUE, UShort.MAX_VALUE, UByte.MAX_VALUE, UByte.MAX_VALUE)},
    appReserved1: ByteArray = ByteArray(12), // this is a 12 byte array reserved for new app features, a custom app should be able to safely use this for custom features
    appReserved2: Array<UShort> = Array(3) { 0u},
    var trainingHp: UShort = 0u,
    var trainingAp: UShort = 0u,
    var trainingBp: UShort = 0u,
    var remainingTrainingTimeInMinutes: UShort = 0u,
    var abilityRarity: AbilityRarity = AbilityRarity.None,
    var abilityType: UShort = 0u,
    var abilityBranch: UShort = 0u,
    var abilityReset: Byte = 0,
    var rank: Byte = 0,
    var itemType: Byte = 0,
    var itemMultiplier: Byte = 0,
    var itemRemainingTime: Byte = 0,
    internal val otp0: ByteArray, // OTP matches the character to the dim
    internal val otp1: ByteArray, // OTP matches the character to the dim
    var characterCreationFirmwareVersion: FirmwareVersion = FirmwareVersion(1, 0),
) :
    NfcCharacter(
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
        trophies = trainingPp,
        currentPhaseBattlesWon = currentPhaseBattlesWon,
        currentPhaseBattlesLost = currentPhaseBattlesLost,
        totalBattlesWon = totalBattlesWon,
        totalBattlesLost = totalBattlesLost,
        activityLevel = activityLevel,
        heartRateCurrent = heartRateCurrent,
        transformationHistory = transformationHistory,
        appReserved1 = appReserved1,
        appReserved2 = appReserved2,
    )
{
    fun getTrainingPp(): UShort {
        return trophies
    }

    fun setTrainingPp(trainingPp: UShort) {
        trophies = trainingPp
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BENfcCharacter
        if(!super.equals(other)) return false

        if (trainingHp != other.trainingHp) return false
        if (trainingAp != other.trainingAp) return false
        if (trainingBp != other.trainingBp) return false
        if (remainingTrainingTimeInMinutes != other.remainingTrainingTimeInMinutes) return false
        if (abilityRarity != other.abilityRarity) return false
        if (abilityType != other.abilityType) return false
        if (abilityBranch != other.abilityBranch) return false
        if (abilityReset != other.abilityReset) return false
        if (rank != other.rank) return false
        if (itemType != other.itemType) return false
        if (itemMultiplier != other.itemMultiplier) return false
        if (itemRemainingTime != other.itemRemainingTime) return false
        if (!otp0.contentEquals(other.otp0)) return false
        if (!otp1.contentEquals(other.otp1)) return false
        if (characterCreationFirmwareVersion != other.characterCreationFirmwareVersion) return false
        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(
            super.hashCode(),
            trainingHp,
            trainingAp,
            trainingBp,
            remainingTrainingTimeInMinutes,
            abilityRarity,
            abilityType,
            abilityBranch,
            abilityReset,
            rank,
            itemType,
            itemMultiplier,
            itemRemainingTime,
            otp0.contentHashCode(),
            otp1.contentHashCode(),
            characterCreationFirmwareVersion)
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun toString(): String {
        return """
${super.toString()}
BENfcCharacter(
    trainingHp=$trainingHp,
    trainingAp=$trainingAp,
    trainingBp=$trainingBp,
    remainingTrainingTimeInMinutes=$remainingTrainingTimeInMinutes,
    abilityRarity=$abilityRarity,
    abilityType=$abilityType,
    abilityBranch=$abilityBranch,
    abilityReset=$abilityReset,
    rank=$rank,
    itemType=$itemType,
    itemMultiplier=$itemMultiplier,
    itemRemainingTime=$itemRemainingTime,
    otp0=${otp0.toHexString()},
    otp1=${otp1.toHexString()},
    characterCreationFirmwareVersion=$characterCreationFirmwareVersion
)"""
    }


}
