package us.bojie.mvi.ui

import us.bojie.mvi.util.DataState

interface DataStateListener {
    fun onDataStateChange(dataState: DataState<*>?)
}