package com.example.aion.data.repository

import com.example.aion.data.dao.UserDao
import com.example.aion.data.entities.UserPreferenceEntity
import com.example.aion.data.entities.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    fun getUserProfile(): Flow<UserProfileEntity?>
    suspend fun saveUserProfile(profile: UserProfileEntity)
    fun getAllPreferences(): Flow<List<UserPreferenceEntity>>
    fun getPreference(key: String): Flow<UserPreferenceEntity?>
    suspend fun savePreference(preference: UserPreferenceEntity)
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override fun getUserProfile() = userDao.getUserProfile()
    
    override suspend fun saveUserProfile(profile: UserProfileEntity) {
        userDao.insertOrUpdateProfile(profile)
    }

    override fun getAllPreferences() = userDao.getAllPreferences()

    override fun getPreference(key: String) = userDao.getPreference(key)

    override suspend fun savePreference(preference: UserPreferenceEntity) {
        userDao.insertOrUpdatePreference(preference)
    }
}
