package com.example.aion.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aion.data.entities.SquadEntity
import com.example.aion.data.entities.SquadMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SocialDao {
    @Query("SELECT * FROM squads")
    fun getAllSquads(): Flow<List<SquadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSquad(squad: SquadEntity)

    @Query("SELECT * FROM squad_members WHERE squadId = :squadId")
    fun getSquadMembers(squadId: String): Flow<List<SquadMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMemberToSquad(member: SquadMemberEntity)
}
