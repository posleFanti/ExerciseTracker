@file:OptIn(ExperimentalMaterial3Api::class)

package com.exercisetracker.ui.exercises

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.exercisetracker.R
import com.exercisetracker.TopAppBar
import com.exercisetracker.data.entities.Exercise
import com.exercisetracker.ui.navigation.NavigationDestination
import com.exercisetracker.ui.theme.ExerciseTrackerTheme
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

object ExerciseEditDestination : NavigationDestination {
    override val route = "exercise_edit"
    override val titleRes = R.string.exercise_edit_title
    const val exerciseIdArg = "exerciseId"
    val routeWithArgs = "$route/{$exerciseIdArg}"
}

@Composable
fun ExerciseEditScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExerciseEditViewModel = viewModel(factory = ExerciseEditViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = viewModel.exerciseUiState.exerciseDetails.name,
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        ExerciseEditBody(
            exerciseUiState = viewModel.exerciseUiState,
            onExerciseValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateExercise()
                    navigateBack()
                }
            },
            onImageUpdate = viewModel::updateImage,
            onImageClear = viewModel::clearImage,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ExerciseEditBody(
    exerciseUiState: ExerciseUiState,
    onExerciseValueChange: (ExerciseDetails) -> Unit,
    onImageUpdate: (Bitmap?) -> Unit,
    onImageClear: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        ExerciseEditForm(
            exerciseDetails = exerciseUiState.exerciseDetails,
            onValueChange = onExerciseValueChange,
            onImageUpdate = onImageUpdate,
            onImageClear = onImageClear,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Сохранить")
        }
    }
}

@Composable
private fun ExerciseEditForm(
    exerciseDetails: ExerciseDetails,
    onValueChange: (ExerciseDetails) -> Unit,
    onImageUpdate: (Bitmap?) -> Unit,
    onImageClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
                onImageUpdate(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = exerciseDetails.name,
            onValueChange = { onValueChange(exerciseDetails.copy(name = it)) },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text(
            text = "Фото тренажера",
            style = MaterialTheme.typography.titleMedium
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    imagePickerLauncher.launch("image/*")
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                exerciseDetails.image?.let { imageByteArray ->
                    val bitmap = imageByteArray.toBitmap()
                    bitmap?.let { bmp ->
                        val ratio = bmp.width.toFloat() / bmp.height.toFloat()
                        Box (
                            //modifier = Modifier.height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "Фото тренажера",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(ratio)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = onImageClear,
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Удалить фото",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                } ?: run {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Добавить фото",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Добавить фото тренажера",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseEditFormPreview() {
    ExerciseTrackerTheme {
        ExerciseEditForm(
            onValueChange = {},
            onImageClear = {},
            onImageUpdate = {},
            exerciseDetails = ExerciseDetails(
                name = "Название упражнения",
            )
        )
    }
}