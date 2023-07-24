package dev.cisnux.prime.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import dev.cisnux.prime.R
import dev.cisnux.prime.presentation.ui.theme.PrimeTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Preview(showBackground = true)
@Composable
private fun PrimeSearchBarPreview() {
    var searchBarState by rememberSearchBarState(
        initialQuery = "",
        initialActive = false,
        initialSearch = true
    )
    val fakeData = MutableStateFlow(PagingData.empty<String>()).collectAsLazyPagingItems()

    PrimeTheme {
        PrimeSearchBar(
            query = searchBarState.query,
            onQueryChange = { searchBarState = searchBarState.copy(query = it) },
            active = searchBarState.active,
            onActiveChange = { searchBarState = searchBarState.copy(active = it) },
            keywordSuggestions = fakeData,
            navigateUp = {},
            isSearch = searchBarState.isSearch,
            onSearchChange = { _, _ -> }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PrimeSearchBar(
    query: String,
    onQueryChange: (query: String) -> Unit,
    active: Boolean,
    onSearchChange: (query: String, isSearch: Boolean) -> Unit,
    onActiveChange: (active: Boolean) -> Unit,
    keywordSuggestions: LazyPagingItems<String>,
    isSearch: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
) {
    Box(
        modifier = modifier
            .semantics {
                isTraversalGroup = true
            }
            .zIndex(1f)
            .fillMaxWidth())
    {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                onSearchChange(it, true)
            },
            active = active,
            onActiveChange = onActiveChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.searchbar_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                if (active or isSearch) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_previous_page)
                        )
                    }
                } else Icon(Icons.Default.Search, contentDescription = null)
            },
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = keywordSuggestions.itemCount,
                    contentType = keywordSuggestions.itemContentType { it }) { index ->
                    val keyword = keywordSuggestions[index]
                    keyword?.let {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            },
                            modifier = Modifier
                                .animateItemPlacement()
                                .clickable {
                                    onQueryChange(it)
                                    onSearchChange(it, true)
                                }
                        )
                    }
                }
            }
        }
    }
}
