package com.github.cfogrady.vbnfc.data

class BENfc (
    private val nfcData: ByteArray
){
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
    init {
        appReserved1 = readByteArray(0, 12)
        gender = readByte(12)
        registeredDims = readByteArray(32, 15)
        injuryStatus = readUShort(64)
        appReserved2 = readUShortArray(66, 3)
        charIndex = readUShort(72)
        dimNumber = readUShort(74)
        phase = readByte(76)
        attribute = readByte(77)
        unusedCurrDays = readByte(78)
        pp = readUShort(96)
        battlesWon = readUShort(98)
        battlesLost = readUShort(100)
        totalBattlesWon = readUShort(102)
        totalBattlesLost = readUShort(104)
        unusedWinPctg = readByte(106)
        reserved1 = readByte(107)
        saveFirmwareVersion = readUShort(108)
        advMissionStage = readByte(128)
        mentalPercentage = readByte(129)
        activityLevel = readByte(130)
        heartRateCurrent = readByte(131)
        vitalPoints = readUShort(132)
        itemEffectMentalStateValue = readByte(134)
        itemEffectMentalStateMinutesRemaining = readByte(135)
        itemEffectActivityLevelValue = readByte(136)
        itemEffectActivityLevelMinutesRemaining = readByte(137)
        itemEffectVitalPointsChangeValue = readByte(138)
        itemEffectVitalPointsChangeMinutesRemaining = readByte(139)
        reserved2 = readByte(140)
        digivolutionCountdown = readUShort(141)
        vitalPointsCurr = readUShort(160)
        reserved3 = readUShort(162)
        year = readByte(164)
        month = readByte(165)
        day = readByte(166)
        vitalPointsHistory0 = readUShort(176)
        vitalPointsHistory1 = readUShort(178)
        vitalPointsHistory2 = readUShort(180)
        vitalPointsHistory3 = readUShort(182)
        vitalPointsHistory4 = readUShort(184)
        vitalPointsHistory5 = readUShort(186)
        year0PreviousVitalPoints = readByte(188)
        month0PreviousVitalPoints = readByte(189)
        day0PreviousVitalPoints = readByte(190)
        year1PreviousVitalPoints = readByte(192)
        month1PreviousVitalPoints = readByte(193)
        day1PreviousVitalPoints = readByte(194)
        year2PreviousVitalPoints = readByte(195)
        month2PreviousVitalPoints = readByte(196)
        day2PreviousVitalPoints = readByte(197)
        year3PreviousVitalPoints = readByte(198)
        month3PreviousVitalPoints = readByte(199)
        day3PreviousVitalPoints = readByte(200)
        year4PreviousVitalPoints = readByte(201)
        month4PreviousVitalPoints = readByte(202)
        day4PreviousVitalPoints = readByte(203)
        year5PreviousVitalPoints = readByte(204)
        month5PreviousVitalPoints = readByte(205)
        day5PreviousVitalPoints = readByte(206)
        charNum0 = readByte(208)
        year0EvoHistory = readByte(209)
        month0EvoHistory = readByte(210)
        day0EvoHistory = readByte(211)
        charNum1 = readByte(212)
        year1EvoHistory = readByte(213)
        month1EvoHistory = readByte(214)
        day1EvoHistory = readByte(215)
        charNum2 = readByte(216)
        year2EvoHistory = readByte(217)
        month2EvoHistory = readByte(218)
        day2EvoHistory = readByte(219)
        charNum3 = readByte(224)
        year3EvoHistory = readByte(225)
        month3EvoHistory = readByte(226)
        day3EvoHistory = readByte(227)
        charNum4 = readByte(228)
        year4EvoHistory = readByte(229)
        month4EvoHistory = readByte(230)
        day4EvoHistory = readByte(231)
        charNum5 = readByte(232)
        year5EvoHistory = readByte(233)
        month5EvoHistory = readByte(234)
        day5EvoHistory = readByte(235)
        charNum6 = readByte(240)
        year6EvoHistory = readByte(241)
        month6EvoHistory = readByte(242)
        day6EvoHistory = readByte(243)
        charNum7 = readByte(244)
        year7EvoHistory = readByte(245)
        month7EvoHistory = readByte(246)
        day7EvoHistory = readByte(247)
        trainingHp = readUShort(256)
        trainingAp = readUShort(258)
        trainingBp = readUShort(260)
        reserved4 = readUShort(262)
        reserved5 = readByte(264)
        questionMark = readByte(265)
        remainingTrainingTime = readUShort(266)
        rank = readByte(288)
        reserved6 = readByte(289)
        abilityRarity = readByte(290)
        reserved7 = readByte(291)
        abilityType = readUShort(292)
        abilityBranch = readUShort(294)
        abilityReset = readByte(296)
        reserved8 = readUShort(297)
        itemType = readByte(299)
        itemMultiplier = readByte(300)
        itemRemainingTime = readByte(301)
        otp0 = readByteArray(352, 8)
        otp1 = readByteArray(368, 8)
        reserved9 = readUShortArray(376, 2)
        firmwareVersion = readUShort(380)
    }

    private fun readByte(offset: Int): Byte {
        return nfcData[offset]
    }

    private fun readUShort(offset: Int): UShort {
        return (nfcData[offset].toUByte().toInt() or (nfcData[offset + 1].toUByte().toInt() shl 8)).toUShort()
    }

    private fun readByteArray(offset: Int, length: Int): ByteArray {
        return nfcData.sliceArray(offset..<offset + length)
    }

    private fun readUShortArray(offset: Int, length: Int): Array<UShort> {
        val result = Array<UShort>(length) { 0u }
        for (i in 0..<length) {
            result[i] = readUShort(offset + i * 2)
        }
        return result
    }
}