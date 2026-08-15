package com.sololeveling.sscprep.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sololeveling.sscprep.domain.model.SyllabusTopic
import com.sololeveling.sscprep.ui.components.SystemWindowCard
import com.sololeveling.sscprep.ui.theme.*

val SSC_SYLLABUS_SECTIONS = listOf(
    SyllabusTopic(
        id = "s_quant_arith",
        subject = "Quantitative Aptitude",
        name = "Arithmetic Mastery (Percentages, Profit & Loss, SI/CI)",
        weightage = "8-10 Questions (Tier-1) • 12-14 Questions (Tier-2)",
        highYieldFormulas = listOf(
            "Net % Change = a + b + (ab/100)",
            "Diff for 2 yrs CI - SI = P(R/100)²",
            "Diff for 3 yrs CI - SI = P(R/100)² * (300 + R)/100"
        )
    ),
    SyllabusTopic(
        id = "s_quant_adv",
        subject = "Quantitative Aptitude",
        name = "Advanced Math (Algebra, Geometry, Trigonometry, Mensuration)",
        weightage = "12-14 Questions (Tier-1) • 14-16 Questions (Tier-2)",
        highYieldFormulas = listOf(
            "If x + 1/x = k => x² + 1/x² = k² - 2",
            "If x + 1/x = k => x³ + 1/x³ = k³ - 3k",
            "Area of Δ with inradius r and semiperimeter s: Δ = r * s"
        )
    ),
    SyllabusTopic(
        id = "s_reas_logic",
        subject = "General Intelligence & Reasoning",
        name = "Logical Deductions (Syllogism, Analogy, Coding-Decoding)",
        weightage = "15-18 Questions (Tier-1) • 18-20 Questions (Tier-2)",
        highYieldFormulas = listOf(
            "Opposite Letter Sum Rule: A+Z = 1+26 = 27",
            "Rule of Syllogism: All + All = All, All + No = No"
        )
    ),
    SyllabusTopic(
        id = "s_eng_grammar",
        subject = "English Language & Comprehension",
        name = "Grammar & Vocabulary (Spotting Errors, Cloze Test, Idioms)",
        weightage = "25 Questions (Tier-1) • 45 Questions (Tier-2)",
        highYieldFormulas = listOf(
            "Neither... nor -> Verb agrees with closest subject",
            "Scarcely / Hardly -> followed by 'when', not 'than'",
            "No sooner -> followed by 'than'"
        )
    ),
    SyllabusTopic(
        id = "s_ga_polity",
        subject = "General Awareness",
        name = "Polity, History, Economy, Science & Current Affairs",
        weightage = "25 Questions (Tier-1) • 25 Questions (Tier-2)",
        highYieldFormulas = listOf(
            "Art 123: President Ordinance",
            "Art 213: Governor Ordinance",
            "Art 324: Election Commission"
        )
    )
)

@Composable
fun SyllabusScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SystemBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            SystemWindowCard(borderColor = SystemPrimary) {
                Text(
                    text = "[OFFICIAL SYLLABUS DIRECTORY]",
                    color = SystemPrimary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "SSC CGL / CHSL SYLLABUS VAULT",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Comprehensive topic breakdown with exam weightages and high-yield formulas.",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        items(SSC_SYLLABUS_SECTIONS.size) { idx ->
            val section = SSC_SYLLABUS_SECTIONS[idx]
            SyllabusTopicCard(topic = section)
        }
    }
}

@Composable
fun SyllabusTopicCard(topic: SyllabusTopic) {
    var expanded by remember { mutableStateOf(false) }

    SystemWindowCard(borderColor = SystemBorder) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = SystemPrimary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SystemPrimary.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = topic.subject,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = SystemPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = topic.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Weightage: ${topic.weightage}",
            color = SystemGold,
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (expanded) "HIDE FORMULAS" else "VIEW HIGH-YIELD FORMULAS ⚡", color = SystemPrimary, fontSize = 11.sp)
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SystemSurfaceElevated, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                topic.highYieldFormulas.forEach { formula ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("•", color = SystemGold, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(formula, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
