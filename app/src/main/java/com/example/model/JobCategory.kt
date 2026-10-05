package com.example.model

enum class JobCategory(val displayName: String, val iconName: String) {
    MANUFACTURING("නිෂ්පාදන", "precision_manufacturing"),
    FACTORY("කර්මාන්තශාලා", "factory"),
    OFFICE("කාර්යාල", "business_center"),
    IT("තොරතුරු තාක්ෂණ", "computer"),
    DATA_ENTRY("දත්ත ඇතුළත් කිරීම", "keyboard"),
    DRIVER("රියදුරු", "directions_car"),
    DELIVERY("බෙදාහැරීම්", "local_shipping"),
    SALES("විකුණුම්", "point_of_sale"),
    HOTEL("හෝටල්", "hotel"),
    SECURITY("ආරක්ෂක", "shield"),
    CONSTRUCTION("ඉදිකිරීම්", "construction"),
    OTHER("වෙනත්", "category");

    companion object {
        fun fromDisplayName(name: String): JobCategory {
            return entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) } ?: OTHER
        }
    }
}
