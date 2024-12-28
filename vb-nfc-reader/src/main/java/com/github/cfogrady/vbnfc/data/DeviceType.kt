package com.github.cfogrady.vbnfc.data

enum class DeviceType(val typeId: UShort) {
    VitalSeries(2u),
    VitalCharacters(3u),
    VitalBraceletBE(4u);

    companion object {
        @JvmStatic
        fun fromTypeId(typeId: UShort): DeviceType {
            return DeviceType.entries[typeId.toInt()-2]
        }
    }
}