package com.elevenetc.zipher.shared

import kotlinx.coroutines.CoroutineDispatcher

internal expect val Main: CoroutineDispatcher
internal expect val Background: CoroutineDispatcher