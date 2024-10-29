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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.posts.R
import com.example.posts.model.Post

@Composable
fun HomeScreen(
    postsUiState: PostsUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (postsUiState) {
        is PostsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is PostsUiState.Success -> PostsColumnScreen(
            postsUiState.posts, contentPadding = contentPadding, modifier = modifier.fillMaxWidth()
        )
        is PostsUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun PostsColumnScreen(
    posts: List<Post>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 4.dp),
        contentPadding = contentPadding,
    ) {
        items(posts, key = Post::id) { post ->
            PostCard(
                post = post,
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun PostCard(post: Post, modifier: Modifier = Modifier) {
    val commonPadding = PaddingValues(8.dp)

    Surface(
        modifier = modifier.clip(
            RoundedCornerShape(
                size = 20.dp
            )
        ),
        tonalElevation = 16.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(commonPadding),
                text = post.title,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier.padding(commonPadding),
                text = post.body,
                textAlign = TextAlign.Start,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}