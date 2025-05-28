package com.exercisetracker.ui.stats


import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.ui.navigation.NavigationDestination

object StatsDestination : NavigationDestination {
    override val route = "stats"
    override val titleRes = R.string.stats_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatsViewModel = viewModel(factory = StatsViewModel.Factory)
){
    Scaffold (
        topBar = { TopAppBar(
            title = stringResource(R.string.stats_screen),
            canNavigateBack = false,
        ) },
    ) { innerPadding ->
        val searchQuery by viewModel.searchQuery
        val chartData by viewModel.chartData
        val exercises by viewModel.searchResults
        val isSearchActive by viewModel.isSearchActive

        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                onSearch = { },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                active = isSearchActive,
                onActiveChange = viewModel::onSearchActiveChanged,
                placeholder = { Text("Поиск упражнений...") },
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyColumn {
                    items(exercises) { exercise ->
                        Text(
                            text = exercise.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectExercise(exercise) }
                                .padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            chartData?.let { data ->
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    lineChartData = data
                )
            } ?: Text(
                text = "Выберите упражнение для отображения статистики",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}