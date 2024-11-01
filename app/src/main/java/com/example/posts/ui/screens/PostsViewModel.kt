/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.posts.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.posts.PostsApplication
import com.example.posts.data.PostsRepository
import com.example.posts.model.Post
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface PostsUiState {
    data class Success(val posts: List<Post>) : PostsUiState
    object Error : PostsUiState
    object Loading : PostsUiState
}

class PostsViewModel(private val postsRepository: PostsRepository) : ViewModel() {
    var postsUiState: PostsUiState by mutableStateOf(PostsUiState.Loading)
        private set

    init {
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch {
            postsUiState = PostsUiState.Loading
            postsUiState = try {
                PostsUiState.Success(postsRepository.getPosts())
            } catch (e: IOException) {
                PostsUiState.Error
            } catch (e: HttpException) {
                PostsUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostsApplication)
                val postsRepository = application.container.postsRepository
                PostsViewModel(postsRepository = postsRepository)
            }
        }
    }
}
