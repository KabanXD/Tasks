package com.example.calculatormisha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.calculatormisha.ui.theme.ComposeCalculatorTheme
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var input by remember { mutableStateOf("0") }
    var result by remember { mutableStateOf("0") }
    var currentOperation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = input,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Light
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            maxLines = 1,
            textAlign = TextAlign.End
        )

        Text(
            text = result,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            maxLines = 1,
            textAlign = TextAlign.End
        )

        GridButtons(
            onButtonClick = { button ->
                when (button) {
                    "AC" -> {
                        input = "0"
                        result = "0"
                        currentOperation = ""
                    }
                    "C" -> {
                        input = input.dropLast(1).ifEmpty { "0" }
                    }
                    "=" -> {
                        calculateResult(input, currentOperation, result)?.let {
                            result = it
                            input = it
                            currentOperation = ""
                        }
                    }
                    "+", "-", "×", "÷", "^" -> {
                        if (input != "0") {
                            currentOperation = button
                            result = input
                            input = "0"
                        }
                    }
                    "√" -> {
                        input = sqrt(input.toDouble()).toString()
                    }
                    "x²" -> {
                        input = (input.toDouble().pow(2)).toString()
                    }
                    "%" -> {
                        input = (input.toDouble() / 100).toString()
                    }
                    "." -> {
                        if (!input.contains(".")) input += "."
                    }
                    else -> {
                        input = if (input == "0") button else input + button
                    }
                }
            }
        )
    }
}

@Composable
fun GridButtons(onButtonClick: (String) -> Unit) {
    val buttons = listOf(
        "AC", "C", "%", "÷",
        "√", "x²", "^", "×",
        "7", "8", "9", "-",
        "4", "5", "6", "+",
        "1", "2", "3", "=",
        "0", ".", "(", ")"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        buttons.chunked(4).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { button ->
                    CalculatorButton(
                        text = button,
                        onClick = { onButtonClick(button) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOperator = text in listOf("+", "-", "×", "÷", "=", "^")
    val isFunction = text in listOf("AC", "C", "%", "√", "x²")
    val isNumber = text.matches(Regex("[0-9]"))

    val containerColor = when {
        isOperator -> MaterialTheme.colorScheme.primary
        isFunction -> MaterialTheme.colorScheme.secondary
        text == "=" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        isOperator || text == "=" -> MaterialTheme.colorScheme.onPrimary
        isFunction -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.onSurface
    }

    val shape = if (text == "0")
        CircleShape.copy(
            topStart = CornerSize(16.dp),
            topEnd = CornerSize(16.dp),
            bottomStart = CornerSize(16.dp),
            bottomEnd = CornerSize(16.dp)
        )
    else
        CircleShape

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        modifier = modifier
            .aspectRatio(1f)
            .height(64.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isNumber) FontWeight.Normal else FontWeight.Medium
        )
    }
}

private fun calculateResult(input: String, operation: String, currentResult: String): String? {
    return try {
        val num2 = input.toDouble()
        val num1 = currentResult.toDouble()

        when (operation) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "×" -> (num1 * num2).toString()
            "÷" -> if (num2 != 0.0) (num1 / num2).toString() else "Error"
            "^" -> num1.pow(num2).toString()
            else -> null
        }
    } catch (e: Exception) {
        "Error"
    }
}