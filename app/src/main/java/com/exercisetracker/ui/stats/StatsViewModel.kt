package com.exercisetracker.ui.stats


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import com.exercisetracker.data.entities.DateMaxWeight
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.data.repositories.ExerciseRepository
import com.exercisetracker.ui.workouts.trackerApplication
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StatsViewModel(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _searchResults = mutableStateOf(emptyList<Exercise>())
    val searchResults: State<List<Exercise>> = _searchResults

    private val _chartData = mutableStateOf<LineChartData?>(null)
    val chartData: State<LineChartData?> = _chartData

    private val _isSearchActive = mutableStateOf(false)
    val isSearchActive: State<Boolean> = _isSearchActive

    private val _selectedExerciseName = mutableStateOf("")
    val selectedExerciseName: State<String> = _selectedExerciseName

    init {
        viewModelScope.launch {
            snapshotFlow { _searchQuery.value }
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    _searchResults.value = if (query.isBlank()) emptyList()
                    else exerciseRepository.searchExercisesFlow("%$query%").filterNotNull().first()
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSearchActiveChanged(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            _searchQuery.value = _selectedExerciseName.value
        }
    }

    fun selectExercise(exercise: Exercise) {
        _selectedExerciseName.value = exercise.name
        _searchQuery.value = exercise.name
        _isSearchActive.value = false

        viewModelScope.launch {
            exerciseRepository.getExerciseStats(exercise.exerciseId)
                .collect { stats ->
                    _chartData.value = if (stats.isNotEmpty()) convertToChartData(stats) else null
                }
        }
    }

    private fun convertToChartData(stats: List<DateMaxWeight>): LineChartData {
        val sortedStats = stats.sortedBy { it.date }

        val points = sortedStats.mapIndexed { index, stat ->
            Point(
                x = index.toFloat(),
                y = stat.maxWeight.toFloat()
            )
        }

        // Используем даты напрямую из базы данных без форматирования
        val xAxisLabels = sortedStats.map { it.date }

        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .backgroundColor(Color.Transparent)
            .steps(points.size - 1)
            .labelData { i -> if (i < xAxisLabels.size) xAxisLabels[i] else "" }
            .labelAndAxisLinePadding(15.dp)
            .build()

        val maxWeight = points.maxOfOrNull { it.y } ?: 0f
        val minWeight = points.minOfOrNull { it.y } ?: 0f
        val yAxisRange = maxWeight - minWeight
        val yAxisStep = if (yAxisRange > 0) yAxisRange / 5 else 1f

        val yAxisData = AxisData.Builder()
            .steps(5)
            .backgroundColor(Color.Transparent)
            .labelAndAxisLinePadding(20.dp)
            .labelData { i ->
                val value = minWeight + (i * yAxisStep)
                String.format("%.1f", value)
            }
            .build()

        val linePlotData = LinePlotData(
            lines = listOf(
                co.yml.charts.ui.linechart.model.Line(
                    dataPoints = points,
                    lineStyle = LineStyle(
                        color = Color.Blue,
                        lineType = LineType.Straight(isDotted = false)
                    ),
                    intersectionPoint = co.yml.charts.ui.linechart.model.IntersectionPoint(
                        color = Color.Red
                    )
                )
            )
        )

        return LineChartData(
            linePlotData = linePlotData,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            backgroundColor = Color.White
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                StatsViewModel(
                    exerciseRepository = trackerApplication().container.exerciseRepository
                )
            }
        }
    }
}