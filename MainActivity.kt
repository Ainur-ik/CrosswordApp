package com.example.crosswordapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFE3F2FD)
                ) {
                    CrosswordScreen()
                }
            }
        }
    }
}

private const val GRID_SIZE = 8

@Composable
fun CrosswordScreen() {
    var input by remember { mutableStateOf("Kotlin, Android, Code, App") }
    var grid by remember { mutableStateOf(Array(GRID_SIZE) { Array(GRID_SIZE) { ' ' } }) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Генератор кроссвордов", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Слова через запятую") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        val words = input.split(",").map { it.trim().uppercase() }.filter { it.isNotEmpty() }
                        if (words.isEmpty()) {
                            message = "Введите хотя бы одно слово"
                            return@Button
                        }
                        if (words.any { it.length > GRID_SIZE }) {
                            message = "Слово длиннее $GRID_SIZE букв не поместится"
                            return@Button
                        }
                        grid = generateCrossword(words)
                        message = "Готово! Слов: ${words.size}"
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Сгенерировать", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (message.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(message, color = Color(0xFF00796B), fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(20.dp))
        Text("Результат:", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D47A1))
        Spacer(Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                for (r in 0 until GRID_SIZE) {
                    Row {
                        for (c in 0 until GRID_SIZE) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(2.dp)
                                    .background(
                                        color = if (grid[r][c] == ' ') Color(0xFFF5F5F5) else Color(0xFFBBDEFB),
                                        shape = RoundedCornerShape(6.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = grid[r][c].toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0D47A1)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun generateCrossword(words: List<String>): Array<Array<Char>> {
    val grid = Array(GRID_SIZE) { Array(GRID_SIZE) { ' ' } }
    var row = 0
    for (word in words) {
        var placed = false
        for (r in row until GRID_SIZE) {
            if (word.length <= GRID_SIZE) {
                var fits = true
                for (i in word.indices) {
                    if (grid[r][i] != ' ' && grid[r][i] != word[i]) {
                        fits = false
                        break
                    }
                }
                if (fits) {
                    for (i in word.indices) grid[r][i] = word[i]
                    row = r + 1
                    placed = true
                    break
                }
            }

        }
    }
    return grid
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun CrosswordPreview() {
    MaterialTheme {
        CrosswordScreen()
    }
}