package com.xbot.anilibriarefresh.ui.feature.title

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.components.MessageContent
import com.xbot.anilibriarefresh.ui.components.SnackbarManager
import com.xbot.domain.model.TitleModel
import com.xbot.domain.repository.TitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TitleViewModel @Inject constructor(
    repository: TitleRepository,
    savedStateHandle: SavedStateHandle,
    snackbarManager: SnackbarManager
) : ViewModel() {
    val state: StateFlow<TitleModel?> = repository.getTitle(savedStateHandle["titleId"]!!)
        .catch { error ->
            //TODO: информативные сообщения для разного типа ошибок
            snackbarManager.showMessage(
                title = MessageContent.String(error.message ?: "")
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    fun onAction(action: TitleScreenAction) {
        //TODO: Actions handling
    }
}

sealed interface TitleScreenAction {
    //TODO: Actions for title screen
}