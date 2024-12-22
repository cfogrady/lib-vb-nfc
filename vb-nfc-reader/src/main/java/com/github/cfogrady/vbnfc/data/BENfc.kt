package com.github.cfogrady.vbnfc.data

class BENfc(nfcCharacter: BENfcCharacter, nfcDevice: BENfcDevice):
    NfcData<BENfcCharacter, BENfcDevice>(nfcCharacter, nfcDevice)
{
    lateinit var appReserved1: ByteArray
    var gender: Byte = 0
    lateinit var registeredDims: ByteArray // Will do this later in a fancier way as a method
    var injuryStatus: UShort = 0u
    lateinit var appReserved2: Array<UShort>
    var charIndex: UShort = 0u
    var dimNumber: UShort = 0u
    var phase: Byte = 0
    var attribute: Byte = 0
    var unusedCurrDays: Byte = 0
    var pp: UShort = 0u
    var battlesWon: UShort = 0u
    var battlesLost: UShort = 0u
    var totalBattlesWon: UShort = 0u
    var totalBattlesLost: UShort = 0u
    var unusedWinPctg: Byte = 0
    var reserved1: Byte = 0
    var saveFirmwareVersion: UShort = 0u
    var advMissionStage: Byte = 0
    var mentalPercentage: Byte = 0
    var activityLevel: Byte = 0
    var heartRateCurrent: Byte = 0
    var vitalPoints: UShort = 0u
    var itemEffectMentalStateValue: Byte = 0
    var itemEffectMentalStateMinutesRemaining: Byte = 0
    var itemEffectActivityLevelValue: Byte = 0
    var itemEffectActivityLevelMinutesRemaining: Byte = 0
    var itemEffectVitalPointsChangeValue: Byte = 0
    var itemEffectVitalPointsChangeMinutesRemaining: Byte = 0
    var reserved2: Byte = 0
    var digivolutionCountdown: UShort = 0u
    var vitalPointsCurr: UShort = 0u
    var reserved3: UShort? = 0u
    var year: Byte = 0 // set to 0
    var month: Byte = 0 // set to 0
    var day: Byte = 0
    var vitalPointsHistory0: UShort = 0u
    var vitalPointsHistory2: UShort = 0u
    var vitalPointsHistory3: UShort = 0u
    var vitalPointsHistory4: UShort = 0u
    var vitalPointsHistory5: UShort = 0u
    var vitalPointsHistory1: UShort = 0u
    var year0PreviousVitalPoints: Byte = 0
    var month0PreviousVitalPoints: Byte = 0
    var day0PreviousVitalPoints: Byte = 0
    var year1PreviousVitalPoints: Byte = 0
    var month1PreviousVitalPoints: Byte = 0
    var day1PreviousVitalPoints: Byte = 0
    var year2PreviousVitalPoints: Byte = 0
    var month2PreviousVitalPoints: Byte = 0
    var day2PreviousVitalPoints: Byte = 0
    var year3PreviousVitalPoints: Byte = 0
    var month3PreviousVitalPoints: Byte = 0
    var day3PreviousVitalPoints: Byte = 0
    var year4PreviousVitalPoints: Byte = 0
    var month4PreviousVitalPoints: Byte = 0
    var day4PreviousVitalPoints: Byte = 0
    var year5PreviousVitalPoints: Byte = 0
    var month5PreviousVitalPoints: Byte = 0
    var day5PreviousVitalPoints: Byte = 0
    var charNum0: Byte = 0
    var year0EvoHistory: Byte = 0
    var month0EvoHistory: Byte = 0
    var day0EvoHistory: Byte = 0
    var charNum1: Byte = 0
    var year1EvoHistory: Byte = 0
    var month1EvoHistory: Byte = 0
    var day1EvoHistory: Byte = 0
    var charNum2: Byte = 0
    var year2EvoHistory: Byte = 0
    var month2EvoHistory: Byte = 0
    var day2EvoHistory: Byte = 0
    var charNum3: Byte = 0
    var year3EvoHistory: Byte = 0
    var month3EvoHistory: Byte = 0
    var day3EvoHistory: Byte = 0
    var charNum4: Byte = 0
    var year4EvoHistory: Byte = 0
    var month4EvoHistory: Byte = 0
    var day4EvoHistory: Byte = 0
    var charNum5: Byte = 0
    var year5EvoHistory: Byte = 0
    var month5EvoHistory: Byte = 0
    var day5EvoHistory: Byte = 0
    var charNum6: Byte = 0
    var year6EvoHistory: Byte = 0
    var month6EvoHistory: Byte = 0
    var day6EvoHistory: Byte = 0
    var charNum7: Byte = 0
    var year7EvoHistory: Byte = 0
    var month7EvoHistory: Byte = 0
    var day7EvoHistory: Byte = 0
    var trainingHp: UShort = 0u
    var trainingAp: UShort = 0u
    var trainingBp: UShort = 0u
    var reserved4: UShort = 0u
    var reserved5: Byte = 0
    var questionMark: Byte = 0
    var remainingTrainingTime: UShort = 0u
    var rank: Byte = 0
    var reserved6: Byte = 0
    var abilityRarity: Byte = 0
    var reserved7: Byte = 0
    var abilityType: UShort = 0u
    var abilityBranch: UShort = 0u
    var abilityReset: Byte = 0
    var reserved8: UShort = 0u
    var itemType: Byte = 0
    var itemMultiplier: Byte = 0
    var itemRemainingTime: Byte = 0
    lateinit var otp0: ByteArray
    lateinit var otp1: ByteArray
    lateinit var reserved9: Array<UShort>
    var firmwareVersion: UShort = 0u
}