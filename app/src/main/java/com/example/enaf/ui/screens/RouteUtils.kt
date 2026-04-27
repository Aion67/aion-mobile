package com.example.enaf.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.enaf.data.local.EnafDatabaseProvider
import com.example.enaf.data.repository.LocalRepository
import com.example.enaf.data.repository.RoomLocalRepository
import com.example.enaf.data.seed.ensureLocalSeedData

@Composable
fun rememberSeededLocalRepository(): LocalRepository {
    val context = LocalContext.current
    val localRepository = remember(context) {
        RoomLocalRepository(EnafDatabaseProvider.get(context))
    }

    LaunchedEffect(localRepository) {
        ensureLocalSeedData(localRepository)
    }

    return localRepository
}
