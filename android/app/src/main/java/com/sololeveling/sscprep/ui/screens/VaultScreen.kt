package com.sololeveling.sscprep.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.data.QuestionVaultData
import com.sololeveling.sscprep.domain.engine.InfiniteQuestionGenerator
import com.sololeveling.sscprep.domain.model.Question
import com.sololeveling.sscprep.ui.components.SoloGlowingButton
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*
import com.sololeveling.sscprep.ui.viewmodel.MainViewModel

@Composable
fun VaultScreen(viewModel: MainViewModel) {
    val bookmarkedIds by viewModel.bookmarkedQuestions.collectAsState()
    var currentVaultMode by remember { mutableStateOf("pyq") } // "pyq" or "infinite"

    var selectedSubject by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val subjects = listOf("All", "Quantitative Aptitude", "General Intelligence & Reasoning", "English Language", "General Awareness", "Bookmarked")

    // Filtered static questions
    val filteredQuestions = remember(selectedSubject, searchQuery, bookmarkedIds) {
        QuestionVaultData.questions.filter { q ->
            val matchesSubject = when (selectedSubject) {
                "All" -> true
                "Bookmarked" -> bookmarkedIds.contains(q.id)
                else -> q.subject.contains(selectedSubject, ignoreCase = true)
            }
            val matchesSearch = searchQuery.isBlank() ||
                    q.question.contains(searchQuery, ignoreCase = true) ||
                    q.topic.contains(searchQuery, ignoreCase = true) ||
                    q.subject.contains(searchQuery, ignoreCase = true)
            matchesSubject && matchesSearch
        }
    }

    // Infinite Generator State
    var infiniteSubject by remember { mutableStateOf("Quantitative Aptitude") }
    var infiniteQuestions by remember { mutableStateOf(InfiniteQuestionGenerator.generateBatch(5, "Quantitative Aptitude")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Mode Switcher Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SystemSurfaceElevated, RoundedCornerShape(10.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = { currentVaultMode = "pyq" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentVaultMode == "pyq") SystemPrimary else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text(
                    text = "📚 PYQ VAULT (${QuestionVaultData.questions.size})",
                    color = if (currentVaultMode == "pyq") SystemBg else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = { currentVaultMode = "infinite" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentVaultMode == "infinite") SystemGold else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text(
                    text = "⚡ INFINITE GENERATOR",
                    color = if (currentVaultMode == "infinite") SystemBg else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (currentVaultMode == "pyq") {
            // --- PYQ VAULT MODE ---
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search topics, formulas, questions...", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = SystemPrimary) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = SystemPrimary,
                    unfocusedBorderColor = SystemBorder,
                    focusedContainerColor = SystemSurface,
                    unfocusedContainerColor = SystemSurface
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subject Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(subjects.size) { idx ->
                    val subject = subjects[idx]
                    val isSelected = subject == selectedSubject
                    Surface(
                        modifier = Modifier.clickable { selectedSubject = subject },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) SystemPrimary else SystemSurface,
                        border = BorderStroke(1.dp, if (isSelected) SystemPrimary else SystemBorder)
                    ) {
                        Text(
                            text = subject,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = if (isSelected) SystemBg else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Question List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(filteredQuestions.size) { idx ->
                    val q = filteredQuestions[idx]
                    val isBookmarked = bookmarkedIds.contains(q.id)
                    QuestionVaultCard(
                        question = q,
                        isBookmarked = isBookmarked,
                        onToggleBookmark = { viewModel.toggleBookmark(q.id) }
                    )
                }
            }
        } else {
            // --- INFINITE GENERATOR MODE ---
            val infiniteSubjects = listOf("Quantitative Aptitude", "General Intelligence & Reasoning", "English Language", "General Awareness", "All-Rounder Mixed")

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    SystemWindowCard(borderColor = SystemGold, glowEffect = true) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("⚡", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("ENDLESS PROCEDURAL TRAINING", color = SystemGold, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("The System dynamically computes fresh numbers, formulas, and options so you can practice infinitely.", color = TextSecondary, fontSize = 12.sp)

                            Spacer(modifier = Modifier.height(12.dp))

                            // Subject Selection Tabs
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(infiniteSubjects.size) { idx ->
                                    val sub = infiniteSubjects[idx]
                                    val isSel = sub == infiniteSubject
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isSel) SystemGold else SystemSurfaceElevated,
                                        border = BorderStroke(1.dp, if (isSel) SystemGold else SystemBorder),
                                        modifier = Modifier.clickable {
                                            infiniteSubject = sub
                                            infiniteQuestions = InfiniteQuestionGenerator.generateBatch(5, sub)
                                        }
                                    ) {
                                        Text(
                                            text = sub,
                                            color = if (isSel) SystemBg else TextPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            SoloGlowingButton(
                                text = "GENERATE 5 FRESH QUESTIONS ⚡",
                                onClick = {
                                    infiniteQuestions = InfiniteQuestionGenerator.generateBatch(5, infiniteSubject)
                                    viewModel.soundAndHaptics.playClick()
                                },
                                containerColor = SystemGold,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                items(infiniteQuestions.size) { idx ->
                    val q = infiniteQuestions[idx]
                    val isBookmarked = bookmarkedIds.contains(q.id)
                    QuestionVaultCard(
                        question = q,
                        isBookmarked = isBookmarked,
                        onToggleBookmark = { viewModel.toggleBookmark(q.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionVaultCard(
    question: Question,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit
) {
    var expandedSolution by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }

    SystemWindowCard(borderColor = SystemBorder) {
        // Tag & Subject Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = SystemPrimary.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, SystemPrimary.copy(alpha = 0.4f))
            ) {
                Text(
                    text = "${question.subject} • ${question.topic}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = SystemPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = onToggleBookmark, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) SystemGold else TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Question Text (Explicit high-contrast)
        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Options
        question.options.forEachIndexed { optIdx, optText ->
            val isSelected = selectedOption == optIdx
            val isCorrect = optIdx == question.correct
            val optionColor = when {
                selectedOption != null && isCorrect -> SystemSuccess
                selectedOption != null && isSelected && !isCorrect -> SystemCrimson
                isSelected -> SystemPrimary
                else -> SystemSurfaceElevated
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clickable { selectedOption = optIdx },
                shape = RoundedCornerShape(8.dp),
                color = optionColor.copy(alpha = if (selectedOption != null) 0.2f else 0.5f),
                border = BorderStroke(
                    1.dp,
                    if (selectedOption != null && (isCorrect || isSelected)) optionColor else SystemBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val prefix = ('A' + optIdx).toString()
                    Text(
                        text = "($prefix)",
                        color = if (selectedOption != null && isCorrect) SystemSuccess else SystemPrimary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = optText,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Solution & Ruler's Trick Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { expandedSolution = !expandedSolution }) {
                Text(
                    text = if (expandedSolution) "HIDE SYSTEM SHORTCUT & SOLUTION" else "REVEAL SOLUTION & SHORTCUT TRICK ⚡",
                    color = SystemGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        AnimatedVisibility(visible = expandedSolution) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(SystemSurfaceElevated, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "STEP-BY-STEP EXPLANATION:",
                    color = SystemPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = question.explanation,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (!question.trick.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = SystemBorder)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚡ RULER'S SHORTCUT TRICK:",
                        color = SystemGold,
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = question.trick,
                        color = SystemGold.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
