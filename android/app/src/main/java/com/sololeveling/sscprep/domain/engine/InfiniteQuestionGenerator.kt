package com.sololeveling.sscprep.domain.engine

import com.sololeveling.sscprep.domain.model.Question
import kotlin.random.Random

object InfiniteQuestionGenerator {

    private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
    private fun lcm(a: Int, b: Int): Int = (a * b) / gcd(a, b)

    private fun formatOptions(correctVal: String, wrongVals: List<String>): Pair<List<String>, Int> {
        val uniqueOptions = mutableListOf(correctVal)
        for (w in wrongVals) {
            if (w != correctVal && !uniqueOptions.contains(w)) {
                uniqueOptions.add(w)
            }
        }
        var extraCounter = 1
        while (uniqueOptions.size < 4) {
            uniqueOptions.add("None of these ($extraCounter)")
            extraCounter++
        }
        val shuffled = uniqueOptions.take(4).shuffled()
        val correctIndex = shuffled.indexOf(correctVal)
        return Pair(shuffled, correctIndex)
    }

    // =========================================================================
    // 1. QUANTITATIVE APTITUDE GENERATOR
    // =========================================================================
    fun generateQuantQuestion(difficulty: String = "Medium"): Question {
        val topics = listOf("profit_loss", "time_work", "algebra", "si_ci", "percentage", "speed_time", "geometry", "trig")
        val topic = topics.random()

        return when (topic) {
            "profit_loss" -> {
                val cp = Random.nextInt(5, 50) * 100
                val markup = listOf(20, 25, 30, 40, 50).random()
                val discount = listOf(10, 15, 20, 25).random()
                val mp = cp * (100 + markup) / 100
                val sp = mp * (100 - discount) / 100
                val profit = sp - cp
                val isProfit = profit >= 0
                val absProfit = kotlin.math.abs(profit)

                val correctAns = "₹$absProfit ${if (isProfit) "Profit" else "Loss"}"
                val wrongOptions = listOf(
                    "₹${absProfit + 50} Profit",
                    "₹${absProfit + 100} Loss",
                    "₹${absProfit - 50} Profit"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_quant_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "Quantitative Aptitude",
                    topic = "Profit, Loss & Discount",
                    difficulty = difficulty,
                    examTag = "SSC CGL Infinite Drill",
                    question = "A merchant marks an article at $markup% above Cost Price (₹$cp) and gives a discount of $discount% on Marked Price. What is the net profit/loss?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Marked Price = $cp × (1 + $markup/100) = ₹$mp.\nSelling Price = $mp × (1 - $discount/100) = ₹$sp.\nNet Result = $sp - $cp = ₹$profit.",
                    trick = "Effective % change = +$markup - $discount - ($markup × $discount)/100."
                )
            }
            "time_work" -> {
                val dayA = listOf(10, 12, 15, 20, 24, 30).random()
                val dayB = listOf(12, 15, 20, 30, 40, 60).random()
                val totalWork = lcm(dayA, dayB)
                val effA = totalWork / dayA
                val effB = totalWork / dayB
                val combinedEff = effA + effB
                val workDays = Random.nextInt(2, 5)
                val workDone = workDays * combinedEff
                val remWork = totalWork - workDone
                val g = gcd(remWork, totalWork)
                val remNum = remWork / g
                val remDen = totalWork / g

                val correctAns = "$remNum/$remDen"
                val wrongOptions = listOf(
                    "${remNum + 1}/$remDen",
                    "${remNum - 1}/$remDen",
                    "1/2",
                    "3/4"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_work_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "Quantitative Aptitude",
                    topic = "Time & Work",
                    difficulty = difficulty,
                    examTag = "SSC CGL Infinite Drill",
                    question = "Hunter A can complete a task in $dayA days, and Hunter B in $dayB days. If they work together for $workDays days, what fraction of work remains?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Total Units = LCM($dayA, $dayB) = $totalWork units.\nA's efficiency = $effA u/day, B's efficiency = $effB u/day.\nWork in $workDays days = $workDays × $combinedEff = $workDone units.\nRemaining fraction = $remWork / $totalWork = $remNum / $remDen.",
                    trick = "Remaining = 1 - Days × (1/A + 1/B)."
                )
            }
            "algebra" -> {
                val k = Random.nextInt(3, 9)
                val k2 = k * k - 2
                val k3 = k * k * k - 3 * k

                val correctAns = "$k3"
                val wrongOptions = listOf(
                    "${k3 + 12}",
                    "${k3 - 15}",
                    "${k3 + 24}"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_alg_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "Quantitative Aptitude",
                    topic = "Algebra & Polynomials",
                    difficulty = difficulty,
                    examTag = "SSC CGL Infinite Drill",
                    question = "If x + 1/x = $k, what is the value of x³ + 1/x³?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Formula: x³ + 1/x³ = (x + 1/x)³ - 3(x + 1/x) = $k³ - 3($k) = ${k * k * k} - ${3 * k} = $k3.",
                    trick = "Direct formula shortcut: k³ - 3k."
                )
            }
            "speed_time" -> {
                val speed1 = listOf(40, 50, 60, 75, 90).random()
                val speed2 = listOf(60, 75, 90, 100, 120).random()
                val avgSpeed = (2 * speed1 * speed2) / (speed1 + speed2)

                val correctAns = "$avgSpeed km/h"
                val wrongOptions = listOf(
                    "${(speed1 + speed2) / 2} km/h",
                    "${avgSpeed + 5} km/h",
                    "${avgSpeed - 8} km/h"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_spd_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "Quantitative Aptitude",
                    topic = "Speed, Time & Distance",
                    difficulty = difficulty,
                    examTag = "SSC CGL Infinite Drill",
                    question = "A car travels from City A to City B at $speed1 km/h and returns at $speed2 km/h. What is the average speed of the entire journey?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Average Speed for equal distances = (2 × s₁ × s₂) / (s₁ + s₂) = (2 × $speed1 × $speed2) / ($speed1 + $speed2) = $avgSpeed km/h.",
                    trick = "Harmonic mean formula: 2xy / (x + y)."
                )
            }
            "geometry" -> {
                val angleA = listOf(40, 50, 60, 70, 80).random()
                val incenterAngle = 90 + (angleA / 2)

                val correctAns = "$incenterAngle°"
                val wrongOptions = listOf(
                    "${180 - angleA}°",
                    "${90 + angleA}°",
                    "${2 * angleA}°"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_geom_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "Quantitative Aptitude",
                    topic = "Geometry - Incenter",
                    difficulty = difficulty,
                    examTag = "SSC CGL Infinite Drill",
                    question = "In ΔABC, I is the incenter of the triangle. If ∠BAC = $angleA°, what is the measure of ∠BIC?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Incenter Angle Formula: ∠BIC = 90° + (∠A / 2) = 90° + ($angleA / 2) = $incenterAngle°.",
                    trick = "Angle at Incenter = 90° + A/2. Angle at Orthocenter = 180° - A. Angle at Circumcenter = 2A."
                )
            }
            else -> {
                val p = Random.nextInt(2, 25) * 1000
                val r = listOf(5, 8, 10, 12, 15).random()
                val t = listOf(2, 3, 4).random()
                val si = (p * r * t) / 100

                val correctAns = "₹$si"
                val wrongOptions = listOf(
                    "₹${si + 250}",
                    "₹${si - 200}",
                    "₹${si + 500}"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_si_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "Quantitative Aptitude",
                    topic = "Simple Interest",
                    difficulty = difficulty,
                    examTag = "SSC CGL Infinite Drill",
                    question = "Find the simple interest on a principal of ₹$p at an annual interest rate of $r% for $t years.",
                    options = opts,
                    correct = correctIdx,
                    explanation = "SI = (P × R × T) / 100 = ($p × $r × $t) / 100 = ₹$si.",
                    trick = "Total % interest = $r × $t = ${r * t}%. ${r * t}% of $p = ₹$si."
                )
            }
        }
    }

    // =========================================================================
    // 2. GENERAL INTELLIGENCE & REASONING GENERATOR
    // =========================================================================
    fun generateReasoningQuestion(difficulty: String = "Medium"): Question {
        val types = listOf("number_analogy", "series", "coding", "direction")
        val type = types.random()

        return when (type) {
            "series" -> {
                val start = Random.nextInt(2, 10)
                val diff = Random.nextInt(3, 8)
                val s1 = start
                val s2 = s1 + diff
                val s3 = s2 + diff * 2
                val s4 = s3 + diff * 3
                val s5 = s4 + diff * 4

                val correctAns = "$s5"
                val wrongOptions = listOf(
                    "${s5 + diff}",
                    "${s5 - diff}",
                    "${s5 + 2}"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_series_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "General Intelligence & Reasoning",
                    topic = "Number Series",
                    difficulty = difficulty,
                    examTag = "SSC CGL Infinite Drill",
                    question = "Find the missing number in the series: $s1, $s2, $s3, $s4, ?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Step difference: +$diff, +${diff * 2}, +${diff * 3}, +${diff * 4}.\nNext term = $s4 + ${diff * 4} = $s5.",
                    trick = "Find the common second-order step difference."
                )
            }
            "coding" -> {
                val words = listOf("KING" to "LJOH", "DARK" to "EBTL", "RAID" to "SBJE", "SOLO" to "TPMP")
                val pair = words.random()
                val word = pair.first
                val code = pair.second

                val correctAns = code
                val wrongOptions = listOf(
                    code.reversed(),
                    "${code.take(3)}Z",
                    "${code.take(2)}AB"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_code_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "General Intelligence & Reasoning",
                    topic = "Coding-Decoding",
                    difficulty = difficulty,
                    examTag = "SSC CGL Infinite Drill",
                    question = "In a certain code language, each letter is shifted forward by +1 position. What is the code for '$word'?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Each letter shifts forward by +1: ${word.map { "$it -> ${(it.code + 1).toChar()}" }.joinToString(", ")}.",
                    trick = "+1 positional shift for each alphabet."
                )
            }
            else -> {
                val n1 = Random.nextInt(3, 12)
                val mult = Random.nextInt(3, 6)
                val add = Random.nextInt(2, 7)
                val n2 = n1 * mult + add
                val n3 = Random.nextInt(4, 15)
                val n4 = n3 * mult + add

                val correctAns = "$n4"
                val wrongOptions = listOf(
                    "${n4 + 3}",
                    "${n4 - 4}",
                    "${n4 + mult}"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_reas_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "General Intelligence & Reasoning",
                    topic = "Number Analogy",
                    difficulty = difficulty,
                    examTag = "SSC CGL Infinite Drill",
                    question = "Select the related number from the given alternatives: $n1 : $n2 :: $n3 : ?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Logic: (First Number × $mult) + $add = Second Number.\n($n1 × $mult) + $add = $n2.\nSimilarly: ($n3 × $mult) + $add = $n4.",
                    trick = "Check multiplier and addition offset pattern."
                )
            }
        }
    }

    // =========================================================================
    // 3. ENGLISH LANGUAGE GENERATOR
    // =========================================================================
    fun generateEnglishQuestion(difficulty: String = "Medium"): Question {
        val vocabBank = listOf(
            Triple("A person who compiles a dictionary", "Lexicographer", listOf("Cartographer", "Calligrapher", "Bibliophile")),
            Triple("One who looks at the bright side of things", "Optimist", listOf("Pessimist", "Pacifist", "Philanthropist")),
            Triple("Fear of enclosed or confined places", "Claustrophobia", listOf("Acrophobia", "Hydrophobia", "Agoraphobia")),
            Triple("Something that cannot be avoided or prevented", "Inevitable", listOf("Infallible", "Inaudible", "Incredible")),
            Triple("A medicine that counteracts the effects of poison", "Antidote", listOf("Antibiotic", "Antiseptic", "Antigen")),
            Triple("Choose the correct Synonym of 'EPHEMERAL':", "Transitory (Short-lived)", listOf("Permanent", "Enduring", "Eternal")),
            Triple("Choose the correct Antonym of 'OBSTINATE':", "Flexible / Yielding", listOf("Stubborn", "Rigid", "Dogmatic"))
        )
        val selected = vocabBank.random()
        val (opts, correctIdx) = formatOptions(selected.second, selected.third)

        return Question(
            id = "inf_eng_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
            subject = "English Language",
            topic = "Vocabulary & One-Word Substitution",
            difficulty = difficulty,
            examTag = "SSC CGL Infinite Drill",
            question = selected.first,
            options = opts,
            correct = correctIdx,
            explanation = "'${selected.second}' is the exact grammatical term for: ${selected.first}.",
            trick = "Root word breakdown: Lexis (word/dictionary) + Grapher (writer/compiler)."
        )
    }

    // =========================================================================
    // 4. GENERAL AWARENESS GENERATOR
    // =========================================================================
    fun generateGaQuestion(difficulty: String = "Medium"): Question {
        val gaBank = listOf(
            Triple("Which Article of the Indian Constitution provides for the 'Right to Constitutional Remedies' (Heart and Soul of Constitution)?", "Article 32", listOf("Article 21", "Article 19", "Article 14")),
            Triple("Which Amendment to the Constitution of India added Fundamental Duties in Part IV-A?", "42nd Constitutional Amendment Act (1976)", listOf("44th Amendment Act", "86th Amendment Act", "73rd Amendment Act")),
            Triple("Which river is known as the 'Sorrow of Bengal'?", "Damodar River", listOf("Kosi River", "Hooghly River", "Brahmaputra River")),
            Triple("What is the chemical name of Vitamin C?", "Ascorbic Acid", listOf("Retinol", "Tocopherol", "Thiamine")),
            Triple("Who founded the Brahmo Samaj in Kolkata in 1828?", "Raja Ram Mohan Roy", listOf("Swami Dayanand Saraswati", "Swami Vivekananda", "Ishwar Chandra Vidyasagar")),
            Triple("Which gland in the human body is known as the 'Master Gland'?", "Pituitary Gland", listOf("Thyroid Gland", "Adrenal Gland", "Pancreas"))
        )
        val selected = gaBank.random()
        val (opts, correctIdx) = formatOptions(selected.second, selected.third)

        return Question(
            id = "inf_ga_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
            subject = "General Awareness",
            topic = "Polity, History & Science",
            difficulty = difficulty,
            examTag = "SSC CGL Infinite Drill",
            question = selected.first,
            options = opts,
            correct = correctIdx,
            explanation = "Correct Answer: ${selected.second}. High-yield SSC CGL Tier-1 static fact.",
            trick = "Article 32 = Dr. B.R. Ambedkar called it the 'Heart and Soul' because it allows directly approaching Supreme Court via Writs."
        )
    }

    // =========================================================================
    // 5. GENERAL DISPATCHER & BATCH GENERATOR
    // =========================================================================
    fun generateBySubject(subject: String, difficulty: String = "Medium"): Question {
        return when {
            subject.contains("Quant", ignoreCase = true) || subject.contains("Math", ignoreCase = true) -> generateQuantQuestion(difficulty)
            subject.contains("Reason", ignoreCase = true) || subject.contains("Intelligence", ignoreCase = true) -> generateReasoningQuestion(difficulty)
            subject.contains("English", ignoreCase = true) -> generateEnglishQuestion(difficulty)
            subject.contains("Awareness", ignoreCase = true) || subject.contains("GA", ignoreCase = true) || subject.contains("GK", ignoreCase = true) -> generateGaQuestion(difficulty)
            else -> listOf(generateQuantQuestion(difficulty), generateReasoningQuestion(difficulty), generateEnglishQuestion(difficulty), generateGaQuestion(difficulty)).random()
        }
    }

    fun generateBatch(count: Int, subjectFilter: String? = null): List<Question> {
        val list = mutableListOf<Question>()
        for (i in 0 until count) {
            val q = if (subjectFilter != null && subjectFilter != "All" && subjectFilter != "Full Mock Exam") {
                generateBySubject(subjectFilter)
            } else {
                when (i % 4) {
                    0 -> generateQuantQuestion()
                    1 -> generateReasoningQuestion()
                    2 -> generateEnglishQuestion()
                    else -> generateGaQuestion()
                }
            }
            list.add(q)
        }
        return list
    }
}
