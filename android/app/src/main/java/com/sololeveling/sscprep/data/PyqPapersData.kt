package com.sololeveling.sscprep.data

import com.sololeveling.sscprep.domain.model.PyqPaper
import com.sololeveling.sscprep.domain.model.Question

object PyqPapersData {

    val papers: List<PyqPaper> = listOf(
        PyqPaper(
            id = "pyq_cgl_2025_tier2_mains",
            exam = "SSC CGL 2025 Tier-2 (Mains) Grand Raid",
            year = "2025",
            shift = "Official Mains Pattern (390 Marks)",
            difficulty = "Monarch Rank",
            totalQuestions = 10,
            durationMinutes = 15,
            maxMarks = 30,
            bossName = "Kargalgan The Supreme Sovereign (Tier-2 Final Monarch)",
            bossAvatar = "👑",
            desc = "Official simulation across Mathematical Abilities, Reasoning, English Comprehension, and General Awareness with live Boss Battle mechanics.",
            questions = listOf(
                Question(
                    id = "cgl25_t2_q1",
                    subject = "Quantitative Aptitude",
                    topic = "Statistics - Standard Deviation & Variance",
                    difficulty = "Hard",
                    examTag = "SSC CGL 2025 Tier-2 (Mains)",
                    question = "If the variance of a data set is 144, what is the standard deviation (SD) of the data set?",
                    options = listOf("12", "14.4", "72", "24"),
                    correct = 0,
                    explanation = "Standard Deviation (σ) is the positive square root of Variance: σ = √Variance = √144 = 12.",
                    trick = "Standard Deviation = √Variance = √144 = 12."
                ),
                Question(
                    id = "cgl25_t2_q2",
                    subject = "Quantitative Aptitude",
                    topic = "Probability & Dice",
                    difficulty = "Hard",
                    examTag = "SSC CGL 2025 Tier-2 (Mains)",
                    question = "Two unbiased dice are rolled simultaneously. What is the probability that the sum of the numbers appearing on top is a prime number?",
                    options = listOf("5/12", "7/12", "1/2", "1/3"),
                    correct = 0,
                    explanation = "Total outcomes = 6 × 6 = 36.\nPossible prime sums: 2, 3, 5, 7, 11.\nSum 2: (1,1) -> 1 pair\nSum 3: (1,2),(2,1) -> 2 pairs\nSum 5: (1,4),(2,3),(3,2),(4,1) -> 4 pairs\nSum 7: (1,6),(2,5),(3,4),(4,3),(5,2),(6,1) -> 6 pairs\nSum 11: (5,6),(6,5) -> 2 pairs\nTotal favorable pairs = 1 + 2 + 4 + 6 + 2 = 15 pairs.\nProbability = 15 / 36 = 5 / 12.",
                    trick = "Favorable = 15. Probability = 15/36 = 5/12."
                ),
                Question(
                    id = "cgl25_t2_q3",
                    subject = "Quantitative Aptitude",
                    topic = "Coordinate Geometry",
                    difficulty = "Hard",
                    examTag = "SSC CGL 2025 Tier-2 (Mains)",
                    question = "Find the area of the triangle whose vertices are given by A(2, 3), B(-1, 0), and C(2, -4).",
                    options = listOf("10.5 sq. units", "12 sq. units", "9.5 sq. units", "11 sq. units"),
                    correct = 0,
                    explanation = "Area = 1/2 |x₁(y₂ - y₃) + x₂(y₃ - y₁) + x₃(y₁ - y₂)|\n= 1/2 |2(0 - (-4)) + (-1)(-4 - 3) + 2(3 - 0)|\n= 1/2 |2(4) + (-1)(-7) + 2(3)| = 1/2 |8 + 7 + 6| = 1/2 |21| = 10.5 sq. units.",
                    trick = "Shoelace formula: 1/2 |2(4) - 1(-7) + 2(3)| = 21/2 = 10.5."
                ),
                Question(
                    id = "cgl25_t2_q4",
                    subject = "General Intelligence & Reasoning",
                    topic = "Statement and Assumptions",
                    difficulty = "Hard",
                    examTag = "SSC CGL 2025 Tier-2 (Mains)",
                    question = "Statement: 'The government has decided to provide free high-speed Wi-Fi across all public universities to promote digital research.'\nAssumptions:\nI. Most students in public universities will utilize the internet for research.\nII. Free Wi-Fi will reduce university drop-out rates.",
                    options = listOf(
                        "Only Assumption I is implicit",
                        "Only Assumption II is implicit",
                        "Both I and II are implicit",
                        "Neither is implicit"
                    ),
                    correct = 0,
                    explanation = "The stated objective of providing Wi-Fi is specifically 'to promote digital research'. Thus, it assumes students will use it for that intended purpose (Assumption I). There is no causal premise linking Wi-Fi directly to student drop-out retention in the statement.",
                    trick = "Check direct alignment with the stated policy objective."
                ),
                Question(
                    id = "cgl25_t2_q5",
                    subject = "English Language",
                    topic = "Active / Passive Voice",
                    difficulty = "Medium",
                    examTag = "SSC CGL 2025 Tier-2 (Mains)",
                    question = "Change to Passive Voice: 'The Shadow Monarch summoned thousands of elite shadow soldiers in an instant.'",
                    options = listOf(
                        "Thousands of elite shadow soldiers were summoned by the Shadow Monarch in an instant.",
                        "Thousands of elite shadow soldiers have been summoned by the Shadow Monarch in an instant.",
                        "Thousands of elite shadow soldiers are summoned by the Shadow Monarch in an instant.",
                        "Thousands of elite shadow soldiers had been summoned by the Shadow Monarch in an instant."
                    ),
                    correct = 0,
                    explanation = "Simple Past active ('summoned') converts to Passive Voice with 'were + V3 (summoned)' for a plural object ('thousands of soldiers').",
                    trick = "Simple past 'summoned' -> was/were + summoned. Subject is plural -> were summoned."
                )
            )
        ),
        PyqPaper(
            id = "pyq_cgl_2024_tier1_shift1",
            exam = "SSC CGL 2024 Tier-1 (Official Shift 1)",
            year = "2024",
            shift = "Shift 1 (Tier-1 Prelims)",
            difficulty = "S-Rank",
            totalQuestions = 8,
            durationMinutes = 10,
            maxMarks = 16,
            bossName = "Cerberus the Gatekeeper of Tier-1",
            bossAvatar = "🐺",
            desc = "Authentic questions from SSC CGL 2024 Tier-1 exam with detailed time-saving shortcuts.",
            questions = listOf(
                Question(
                    id = "cgl24_t1_q1",
                    subject = "Quantitative Aptitude",
                    topic = "Percentages & Population",
                    difficulty = "Medium",
                    examTag = "SSC CGL 2024 Tier-1",
                    question = "The population of a city increases at the rate of 5% per annum. If the present population is 1,85,220, what was its population 3 years ago?",
                    options = listOf("1,60,000", "1,50,000", "1,65,000", "1,70,000"),
                    correct = 0,
                    explanation = "5% = 1/20. Population multiplier per year = 21/20. Let P be population 3 years ago: P * (21/20)³ = 1,85,220. P * 9261 / 8000 = 1,85,220. Notice 185220 / 9261 = 20. P = 20 * 8000 = 1,60,000.",
                    trick = "21³ = 9261. 9261 × 20 = 185220. Therefore P = 20 × 20³ = 20 × 8000 = 1,60,000."
                ),
                Question(
                    id = "cgl24_t1_q2",
                    subject = "General Awareness",
                    topic = "Indian Geography & Rivers",
                    difficulty = "Medium",
                    examTag = "SSC CGL 2024 Tier-1",
                    question = "Which of the following rivers originates from the Trimbakeshwar plateau in the Nashik district of Maharashtra?",
                    options = listOf("Godavari", "Krishna", "Kaveri", "Mahanadi"),
                    correct = 0,
                    explanation = "The Godavari (often called Dakshin Ganga) originates from Trimbakeshwar near Nashik, Maharashtra. Krishna originates from Mahabaleshwar, Kaveri from Talakaveri (Brahmagiri hills), and Mahanadi from Sihawa in Chhattisgarh.",
                    trick = "Trimbakeshwar (Nashik) -> Godavari (Longest river in Peninsular India)."
                )
            )
        )
    )
}
