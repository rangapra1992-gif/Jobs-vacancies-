package com.example.model

enum class JobType(val displayName: String) {
    FULL_TIME("පූර්ණ කාලීන"),
    PART_TIME("අර්ධ කාලීන"),
    CONTRACT("කොන්ත්රාත්"),
    INTERNSHIP("පුහුණුලාභී");

    companion object {
        fun fromDisplayName(name: String): JobType {
            return entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) } ?: FULL_TIME
        }
    }
}
