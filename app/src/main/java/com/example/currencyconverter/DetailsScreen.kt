package com.example.currencyconverter

import android.graphics.Paint
import android.service.autofill.FillEventHistory
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
// for a 'val' variable
import androidx.compose.runtime.getValue

// for a `var` variable also add
import androidx.compose.runtime.setValue

// or just
import androidx.compose.runtime.*
import com.example.currencyconverter.model.DetailsViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

@Composable
fun DetailsScreen( viewModel: DetailsViewModel = hiltViewModel(),from:String?,to:String?) {
    var called by rememberSaveable { mutableStateOf(false) }


    if (called.not()){
    
        viewModel.fromLiveData.value=from
        viewModel.toLiveData.value=to
        viewModel.getLastThreedays()
        viewModel.getCurrencyToOthers()
        called = true

    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

                .padding(5.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                style = TextStyle(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                fontSize = 17.sp,
                text = viewModel.fromLiveData?.value.toString()
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray),
                style = TextStyle(fontWeight = FontWeight.Bold), textAlign = TextAlign.Center,
                text = "History Chart"
            )
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.Center

            ) {


                viewModel.LastThreeDaysHistory.observeAsState().value?.let {
                    LineChartView(
                        history = it, modifier = Modifier
                            .height(200.dp)
                            .width(350.dp)

                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                  ,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.LightGray),
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        text = "Last Three days prices"
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(5.dp)
                            .fillMaxWidth(1f),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        viewModel.LastThreeDaysHistory.observeAsState().value?.let {
                            it.forEach { item ->
                                Text(
                                    style = TextStyle(fontSize = 17.sp),
                                    text = "${item[1]} ${viewModel.toLiveData?.value.toString()}  -  ${item[0]}"
                                )
                            }


                        }
                    }

                }
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(10.dp)
                        .background(color = Color.LightGray)
                )
                Column(
                    modifier = Modifier


                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.LightGray),
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        text = "Other currencies"
                    )
                    viewModel.currencyToOthers.observeAsState().value?.let {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(5.dp)
                                .fillMaxWidth(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            it.forEach { item ->
                                Text(
                                    style = TextStyle(
                                        textAlign = TextAlign.Center,
                                        fontSize = 17.sp
                                    ), text = "${item[1]} : ${item[0]}"
                                )

                            }

                        }
                    }
                }
            }

        }
    }
}

@Composable
fun LineChartView(
    history: List<List<String>>,
    modifier: Modifier = Modifier,
    graphColor: Color = Color.Green
) {
    val spacing = 90f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    val upperValue = remember(history) {
        (history.maxOfOrNull {
            it[1].toFloat()
        }) ?: 0
    }
    val lowerValue = remember(history) {
        history.minOfOrNull { it[1].toFloat() } ?: 0
    }
    val textPaint = remember {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = 50f
        }
    }
    Canvas(modifier = modifier.padding(start = 8.dp)) {

        val spacePerDate = (size.width - spacing) / history.size
        (0 until history.size step 1).forEach { i ->
            val h = history[i]
            val date = h[0]
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    date, spacing + i * spacePerDate,
                    size.height - 5,
                    textPaint
                )
            }
        }
        val priceStep = (upperValue.toFloat() - lowerValue.toFloat()) / 3f
        (0..2).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    (lowerValue.toFloat() + priceStep * i).toString(),
                    6f,
                    size.height - spacing - i * size.height / 3f,
                    textPaint
                )
            }

        }
        var lastx = 0f
        val strokePath = Path().apply {
            val height = size.height
            for (i in history.indices) {
                val h = history[i]
                val nextH = history.getOrNull(i + 1) ?: history.last()
                val leftRatio =
                    (h[1].toFloat() - lowerValue.toFloat()) / (upperValue.toFloat() - lowerValue.toFloat())
                val rightRatio =
                    (nextH[1].toFloat() - lowerValue.toFloat()) / (upperValue.toFloat() - lowerValue.toFloat())
                val x1 = spacing + i * spacePerDate
                val x2 = spacing + (i + 1) * spacePerDate
                val y1 = height - spacing - (leftRatio * height)
                val y2 = height - spacing - (rightRatio * height)
                if (i == 0) {
                    moveTo(
                        x1 + 40f,
                        y1
                    )
                }
                lastx = (x1 + x2) / 2f
                quadraticBezierTo(x1, y1, lastx, (y1 + y2) / 2f)
            }
        }
        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastx, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }
        drawPath(
            path = fillPath, brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing

            )
        )
        drawPath(
            path = strokePath, color = graphColor, style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}
