package com.example.data.model

enum class ServiceCategory(val displayName: String, val hindiName: String, val iconName: String) {
    ALL("All Services", "Sabhi Kaam", "category"),
    MISTRI("Mason / Mistri", "मिस्त्री / राजमिस्त्री", "handyman"),
    ELECTRICIAN("Electrician", "इलेक्ट्रीशियन", "bolt"),
    PLUMBER("Plumber", "प्लंबर", "water_drop"),
    CARPENTER("Carpenter", "बढ़ई / कारपेंटर", "carpenter"),
    PAINTER("Painter", "पेंटर / रंगाई", "format_paint"),
    TILE_WORKER("Tile Worker", "टाइल कारीगर", "grid_view"),
    MECHANIC("Mechanic", "मैकेनिक", "build"),
    CLEANER("Deep Cleaning", "सफाई / डीप क्लीनर", "cleaning_services"),
    APPLIANCE_REPAIR("Appliance Repair", "उपकरण मरम्मत", "tv")
}

enum class VerificationLevel(val title: String, val badgeColorHex: Long, val stepNumber: Int) {
    BASIC("Basic Profile", 0xFF9E9E9E, 1),
    PHONE_VERIFIED("Phone Verified", 0xFF2196F3, 2),
    IDENTITY_VERIFIED("Identity Verified", 0xFF3F51B5, 3),
    SKILL_VERIFIED("Skill / Work Verified", 0xFF009688, 4),
    TRUSTED_WORKER("Trusted Worker ★", 0xFFFF9800, 5)
}

enum class RequestStatus {
    REQUESTED,
    ACCEPTED,
    IN_PROGRESS,
    COMPLETED,
    PAID_RELEASED,
    CANCELLED
}

enum class EscrowStatus {
    PENDING_DEPOSIT,
    SECURED_IN_ESCROW,
    RELEASED_TO_WORKER,
    REFUNDED
}
