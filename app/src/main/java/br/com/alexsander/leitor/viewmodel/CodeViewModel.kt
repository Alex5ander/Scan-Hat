package br.com.alexsander.leitor.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.data.CodeDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CodeViewModel(private val codeDAO: CodeDAO) : ViewModel() {
    val codes = codeDAO.getAll()

    companion object {
        fun provideFactory(
            dao: CodeDAO,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory() {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return CodeViewModel(dao) as T
                }
            }

    }

    fun insert(code: Code) {
        viewModelScope.launch(Dispatchers.IO) {
            codeDAO.insert(code)
        }
    }

    fun delete(code: Code) {
        viewModelScope.launch(Dispatchers.IO) {
            codeDAO.delete(code)
        }
    }
}