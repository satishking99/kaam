package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.EscrowStatus
import com.example.data.model.RequestStatus
import com.example.data.model.ServiceCategory
import com.example.data.model.VerificationLevel

class Converters {
    @TypeConverter
    fun fromCategory(value: ServiceCategory): String = value.name

    @TypeConverter
    fun toCategory(value: String): ServiceCategory = try {
        ServiceCategory.valueOf(value)
    } catch (e: Exception) {
        ServiceCategory.ALL
    }

    @TypeConverter
    fun fromVerificationLevel(value: VerificationLevel): String = value.name

    @TypeConverter
    fun toVerificationLevel(value: String): VerificationLevel = try {
        VerificationLevel.valueOf(value)
    } catch (e: Exception) {
        VerificationLevel.BASIC
    }

    @TypeConverter
    fun fromRequestStatus(value: RequestStatus): String = value.name

    @TypeConverter
    fun toRequestStatus(value: String): RequestStatus = try {
        RequestStatus.valueOf(value)
    } catch (e: Exception) {
        RequestStatus.REQUESTED
    }

    @TypeConverter
    fun fromEscrowStatus(value: EscrowStatus): String = value.name

    @TypeConverter
    fun toEscrowStatus(value: String): EscrowStatus = try {
        EscrowStatus.valueOf(value)
    } catch (e: Exception) {
        EscrowStatus.SECURED_IN_ESCROW
    }
}
