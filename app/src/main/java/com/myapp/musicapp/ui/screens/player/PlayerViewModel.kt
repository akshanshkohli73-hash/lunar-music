package com.myapp.musicapp.ui.screens.player

import androidx.lifecycle.ViewModel
import com.myapp.musicapp.player.PlayerConnection

class PlayerViewModel(
    val playerConnection: PlayerConnection
) : ViewModel()
