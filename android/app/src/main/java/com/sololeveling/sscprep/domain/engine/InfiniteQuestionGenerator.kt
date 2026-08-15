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

    fun generateQuantQuestion(difficulty: String = "Medium"): Question {
        val topics = listOf("profit_loss", "time_work", "algebra", "si_ci", "percentage", "speed_time")
        val topic = topics.random()

        return when (topic) {
            "profit_loss" -> {
                val cp = Random.nextInt(5, 50) * 100 // e.g. 1500
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
                    examTag = "Demon Castle Infinite Gauntlet",
                    question = "A merchant marks an article at $markup% above Cost Price (₹$cp) and gives a discount of $discount% on Marked Price. What is the net profit/loss?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Marked Price = $cp * (1 + $markup/100) = ₹$mp.\nSelling Price = $mp * (1 - $discount/100) = ₹$sp.\nNet Result = $sp - $cp = ₹$profit.",
                    trick = "Effective % change = +$markup - $discount - ($markup * $discount)/100."
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
                    examTag = "Demon Castle Infinite Gauntlet",
                    question = "Hunter A can complete a dungeon in $dayA days, and Hunter B in $dayB days. If they raid together for $workDays days, what fraction of the dungeon remains?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Total Units = LCM($dayA, $dayB) = $totalWork.\nA's efficiency = $effA u/day, B's efficiency = $effB u/day.\nWork in $workDays days = $workDays × $combinedEff = $workDone units.\nRemaining fraction = $remWork / $totalWork = $remNum / $remDen.",
                    trick = "Remaining = 1 - Days * (1/A + 1/B)."
                )
            }
            "algebra" -> {
                val k = Random.nextInt(3, 8)
                val k2 = k * k - 2
                val k3 = k * k * k - 3 * k

                val correctAns = "$k3"
                val wrongOptions = listOf(
                    "${k3 + 10}",
                    "${k3 - 12}",
                    "${k3 + 24}"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_alg_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "Quantitative Aptitude",
                    topic = "Algebra & Identities",
                    difficulty = difficulty,
                    examTag = "Demon Castle Infinite Gauntlet",
                    question = "If x + 1/x = $k, what is the value of x³ + 1/x³?",
                    options = opts,
                    correct = correctIdx,
                    explanation = "Formula: x³ + 1/x³ = (x + 1/x)³ - 3(x + 1/x) = $k³ - 3($k) = ${k * k * k} - ${3 * k} = $k3.",
                    trick = "Direct shortcut: k³ - 3k."
                )
            }
            else -> {
                val p = Random.nextInt(2, 20) * 1000
                val r = listOf(5, 8, 10, 12, 15).random()
                val t = listOf(2, 3, 4, 5).random()
                val si = (p * r * t) / 100

                val correctAns = "₹$si"
                val wrongOptions = listOf(
                    "₹${si + 200}",
                    "₹${si - 150}",
                    "₹${si + 400}"
                )
                val (opts, correctIdx) = formatOptions(correctAns, wrongOptions)

                Question(
                    id = "inf_si_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
                    subject = "Quantitative Aptitude",
                    topic = "Simple Interest",
                    difficulty = difficulty,
                    examTag = "Demon Castle Infinite Gauntlet",
                    question = "Find the simple interest on a principal sum of ₹$p invested at an annual rate of $r% for $t years.",
                    options = opts,
                    correct = correctIdx,
                    explanation = "SI = (P × R × T) / 100 = ($p × $r × $t) / 100 = ₹$si.",
                    trick = "Interest = $p * ($r * $t)% = $p * ${r * t / 100.0} = ₹$si."
                )
            }
        }
    }

    fun generateReasoningQuestion(): Question {
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

        return Question(
            id = "inf_reas_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
            subject = "General Intelligence & Reasoning",
            topic = "Number Analogy",
            difficulty = "Medium",
            examTag = "Demon Castle Infinite Gauntlet",
            question = "Select the related number from the given alternatives: $n1 : $n2 :: $n3 : ?",
            options = opts,
            correct = correctIdx,
            explanation = "Pattern: (Number × $mult) + $add = Second Number.\nHere: ($n1 × $mult) + $add = $n2.\nSimilarly: ($n3 × $mult) + $add = $n4.",
            trick = "Check ratio and multiplier addition pattern."
        )
    }

    fun generateBatch(count: Int): List<Question> {
        val list = mutableListOf<Question>()
        for (i in 0 until count) {
            if (i % 2 == 0) {
                list.add(generateQuantQuestion())
            } else {
                list.add(generateReasoningQuestion())
            }
        }
        return list
    }
}
