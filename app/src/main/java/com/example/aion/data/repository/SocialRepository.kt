package com.example.aion.data.repository

import com.example.aion.data.dao.SocialDao
import com.example.aion.data.entities.SquadEntity
import com.example.aion.data.entities.SquadMemberEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface SocialRepository {
    fun getAllSquads(): Flow<List<SquadEntity>>
    suspend fun createSquad(squad: SquadEntity)
    fun getSquadMembers(squadId: String): Flow<List<SquadMemberEntity>>
    suspend fun addMemberToSquad(member: SquadMemberEntity)
}

@Singleton
class SocialRepositoryImpl @Inject constructor(
    private val socialDao: SocialDao
) : SocialRepository {
    override fun getAllSquads() = socialDao.getAllSquads()

    override suspend fun createSquad(squad: SquadEntity) {
        socialDao.insertSquad(squad)
    }

    override fun getSquadMembers(squadId: String) = socialDao.getSquadMembers(squadId)

    override suspend fun addMemberToSquad(member: SquadMemberEntity) {
        socialDao.addMemberToSquad(member)
    }
}
