package com.github.cfogrady.vbnfc.vb

import kotlin.random.Random

data class SpecialMission(val type: Type, val id: UShort, val goal: UShort, val timeLimitInMinutes: UShort = 60u, val progress: UShort = 0u, val timeElapsedInMinutes: UShort = 0u, val status: Status = Status.AVAILABLE) {

    enum class Type {
        NONE,
        STEPS,
        VITALS,
        BATTLES,
        WINS
    }

    enum class Status {
        UNAVAILABLE,
        IN_PROGRESS,
        FAILED,
        COMPLETED,
        AVAILABLE,
    }

    companion object {
        fun standardMiles() : SpecialMission {
            return SpecialMission(
                type = Type.STEPS,
                id = Random.nextInt().toUShort(),
                goal = 6000u)
        }

        fun standardVitals(): SpecialMission {
            return SpecialMission(
                type = Type.VITALS,
                id = Random.nextInt().toUShort(),
                goal = 360u)
        }

        fun standardBattles(): SpecialMission {
            return SpecialMission(
                type = Type.BATTLES,
                id = Random.nextInt().toUShort(),
                goal = 10u)
        }

        fun standardWins(): SpecialMission {
            return SpecialMission(
                type = Type.WINS,
                id = Random.nextInt().toUShort(),
                goal = 6u)
        }
    }
}