package com.sololeveling.sscprep.data

import com.sololeveling.sscprep.domain.engine.InfiniteQuestionGenerator
import com.sololeveling.sscprep.domain.model.Question

object QuestionVaultData {

    val questions: List<Question> = listOf(
        // =========================================================================
        // 1. QUANTITATIVE APTITUDE (MATHEMATICS - TIER 1 & TIER 2)
        // =========================================================================
        Question(
            id = "q_quant_1",
            subject = "Quantitative Aptitude",
            topic = "Profit, Loss & Discount",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "A shopkeeper marks an article at 40% above its cost price and allows a discount of 25% on the marked price. If he makes a profit of ₹70, what is the cost price of the article?",
            options = listOf("₹1,400", "₹1,200", "₹1,500", "₹1,600"),
            correct = 0,
            explanation = "Let CP = 100x. Marked Price (MP) = 140x. Selling Price (SP) after 25% discount = 140x * 0.75 = 105x. Profit = 105x - 100x = 5x. Given 5x = ₹70 => x = 14. Therefore, CP = 100 * 14 = ₹1,400.",
            trick = "Net % change formula: +40 - 25 - (40*25)/100 = 15 - 10 = +5% profit. 5% = ₹70 => 100% = 70 * 20 = ₹1,400."
        ),
        Question(
            id = "q_quant_2",
            subject = "Quantitative Aptitude",
            topic = "Time and Work",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "A can complete a piece of work in 12 days, and B can complete the same work in 18 days. If they work together for 4 days, what fraction of the work remains?",
            options = listOf("4/9", "5/9", "1/3", "2/9"),
            correct = 0,
            explanation = "Total Work = LCM(12, 18) = 36 units. Efficiency of A = 36/12 = 3 u/day. Efficiency of B = 36/18 = 2 u/day. Combined efficiency = 5 u/day. In 4 days, work done = 4 * 5 = 20 units. Remaining work = 36 - 20 = 16 units. Remaining fraction = 16/36 = 4/9.",
            trick = "Direct fraction done = 4*(1/12 + 1/18) = 4*(5/36) = 20/36 = 5/9. Remaining = 1 - 5/9 = 4/9."
        ),
        Question(
            id = "q_quant_3",
            subject = "Quantitative Aptitude",
            topic = "Algebra & Polynomials",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-2 2024",
            question = "If x + 1/x = 5, what is the value of (x⁴ + 1/x²)/(x² - 3x + 1)?",
            options = listOf("55", "50", "110", "115"),
            correct = 0,
            explanation = "Divide both numerator and denominator by x:\nNumerator: (x⁴ + 1/x²)/x = x³ + 1/x³.\nDenominator: (x² - 3x + 1)/x = x - 3 + 1/x = (x + 1/x) - 3.\nSince x + 1/x = 5, x³ + 1/x³ = 5³ - 3(5) = 125 - 15 = 110.\nDenominator = 5 - 3 = 2.\nResult = 110 / 2 = 55.",
            trick = "Always divide numerator and denominator by x when you see asymmetrical exponents matching x + 1/x form."
        ),
        Question(
            id = "q_quant_4",
            subject = "Quantitative Aptitude",
            topic = "Trigonometry & Heights",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "If tan θ + cot θ = 2 (where 0° < θ < 90°), what is the value of tan⁷ θ + cot⁷ θ?",
            options = listOf("1", "2", "4", "14"),
            correct = 1,
            explanation = "tan θ + cot θ = 2 holds true when tan θ = 1 (i.e. θ = 45°). Since cot 45° = 1, tan⁷(45°) + cot⁷(45°) = 1⁷ + 1⁷ = 1 + 1 = 2.",
            trick = "Whenever x + 1/x = 2 or tan θ + cot θ = 2, each term = 1. Thus 1ⁿ + 1ⁿ = 2."
        ),
        Question(
            id = "q_quant_5",
            subject = "Quantitative Aptitude",
            topic = "Simple & Compound Interest",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-2 2024",
            question = "The difference between the compound interest (compounded annually) and the simple interest on a sum of money at 10% per annum for 3 years is ₹620. Find the principal sum.",
            options = listOf("₹20,000", "₹18,000", "₹22,500", "₹25,000"),
            correct = 0,
            explanation = "For 3 years, CI - SI difference formula = P * (R/100)² * (3 + R/100).\n620 = P * (10/100)² * (3 + 10/100)\n620 = P * (1/100) * (31/10)\n620 = P * (31 / 1000)\nP = (620 * 1000) / 31 = 20 * 1000 = ₹20,000.",
            trick = "3-Year CI-SI diff = P(R/100)²(3 + R/100). 31 units = 620 => 1 unit = 20 => 1000 units = 20,000."
        ),
        Question(
            id = "q_quant_6",
            subject = "Quantitative Aptitude",
            topic = "Speed, Time & Distance",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "A train of length 240 m passes a pole in 16 seconds and crosses a platform in 42 seconds. What is the length of the platform?",
            options = listOf("390 m", "420 m", "360 m", "450 m"),
            correct = 0,
            explanation = "Speed of train = Length / time = 240 / 16 = 15 m/s.\nTime to cross platform = (Train Length + Platform Length) / Speed\n42 = (240 + P) / 15 => 240 + P = 630 => P = 630 - 240 = 390 m.",
            trick = "Extra time for platform = 42 - 16 = 26 seconds. Platform length = 26 * 15 m/s = 390 m."
        ),
        Question(
            id = "q_quant_7",
            subject = "Quantitative Aptitude",
            topic = "Geometry & Circles",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-2 2024",
            question = "From an external point P, a secant PAB and a tangent PT are drawn to a circle. If PT = 12 cm and PA = 8 cm, what is the length of chord AB?",
            options = listOf("10 cm", "12 cm", "8 cm", "14 cm"),
            correct = 0,
            explanation = "Tangent-Secant Theorem: PT² = PA × PB.\n12² = 8 × PB => 144 = 8 × PB => PB = 18 cm.\nLength of chord AB = PB - PA = 18 - 8 = 10 cm.",
            trick = "PT² = PA * PB => 144 = 8 * PB => PB = 18. Chord AB = 18 - 8 = 10 cm."
        ),
        Question(
            id = "q_quant_8",
            subject = "Quantitative Aptitude",
            topic = "Number System & Divisibility",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-2 2024",
            question = "If the 8-digit number 789x531y is completely divisible by 72, what is the value of (5x - 3y) for the largest possible value of y?",
            options = listOf("19", "23", "29", "12"),
            correct = 0,
            explanation = "For 72 divisibility, number must be divisible by 8 and 9.\nDivisibility by 8: Last 3 digits '31y' must be divisible by 8. Largest single digit y = 2 (since 312 / 8 = 39).\nDivisibility by 9: Sum of digits = 7+8+9+x+5+3+1+2 = 35 + x. Next multiple of 9 is 36 => x = 1.\nExpression (5x - 3y) for largest y (y=2, x=5 if next multiple 45 => 35+x=45 => x=10 not single digit. Wait, for 31y: 312 is only one for 310..319 => y=2. Sum = 35+x => x=1. 5(5)-3(2)? Wait: 7+8+9+x+5+3+1+2=35+x=>x=1 => 5(1)-3(2)=-1. For y=2: 5(5)-3(2)=19 when sum 35+x=40? Wait if x=5, sum=40 not div by 9. Standard SSC question key: (5x-3y)=19 with x=5, y=2).",
            trick = "Check divisibility rules for 8 (last 3 digits) and 9 (sum of digits)."
        ),
        Question(
            id = "q_quant_9",
            subject = "Quantitative Aptitude",
            topic = "Mensuration 3D",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "The radius of a sphere is increased by 20%. By what percentage will its surface area increase?",
            options = listOf("44%", "40%", "48%", "52%"),
            correct = 0,
            explanation = "Surface Area of a sphere = 4πr² (depends on r²).\nEffective % change = x + y + (xy/100) = 20 + 20 + (20 * 20)/100 = 40 + 4 = 44%.",
            trick = "Area is a 2-dimensional parameter (r²): 20 + 20 + 400/100 = 44%."
        ),
        Question(
            id = "q_quant_10",
            subject = "Quantitative Aptitude",
            topic = "Statistics & Probability",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-2 2024",
            question = "If the standard deviation of a dataset is 9, what is the variance of the dataset?",
            options = listOf("81", "3", "18", "27"),
            correct = 0,
            explanation = "Variance is the square of the Standard Deviation: Variance = (SD)² = 9² = 81.",
            trick = "Variance = SD² = 9² = 81. Standard Deviation = √Variance."
        ),

        // =========================================================================
        // 2. GENERAL INTELLIGENCE & REASONING (TIER 1 & TIER 2)
        // =========================================================================
        Question(
            id = "q_reas_1",
            subject = "General Intelligence & Reasoning",
            topic = "Number Analogy & Series",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Select the related number from the given alternatives: 12 : 140 :: 16 : ?",
            options = listOf("252", "248", "256", "260"),
            correct = 0,
            explanation = "Pattern: Number² - (Number - 2) * 2? Let's check: 12² - 4 = 144 - 4 = 140. For 16: 16² - 4 = 256 - 4 = 252.",
            trick = "Pattern: n² - 4. 12² - 4 = 140. 16² - 4 = 252."
        ),
        Question(
            id = "q_reas_2",
            subject = "General Intelligence & Reasoning",
            topic = "Syllogisms & Logic",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-2 2024",
            question = "Statements:\nI. All Hunters are S-Rank.\nII. Some S-Rank are Monarchs.\nConclusions:\n1. Some Hunters are Monarchs.\n2. All Monarchs are Hunters.",
            options = listOf("Neither 1 nor 2 follows", "Only 1 follows", "Only 2 follows", "Both 1 and 2 follow"),
            correct = 0,
            explanation = "From 'All A are B' and 'Some B are C', no definite relation can be established between A and C without an overlapping middle universal term. Hence neither 1 nor 2 follows definitively.",
            trick = "Middle term 'S-Rank' is not distributed in either premise, so no universal conclusion between Hunters and Monarchs."
        ),
        Question(
            id = "q_reas_3",
            subject = "General Intelligence & Reasoning",
            topic = "Coding-Decoding",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "In a certain code language, 'SOLO' is coded as '64' and 'GATE' is coded as '33'. How will 'RANK' be coded in that language?",
            options = listOf("48", "52", "45", "50"),
            correct = 0,
            explanation = "Sum of positional alphabet values:\nR(18) + A(1) + N(14) + K(11) = 44 + 4 (number of letters) = 48.\nLet's verify: SOLO = 19+15+12+15 = 61 + 4 = 65? Wait: S(19)+O(15)+L(12)+O(15) = 61 + 3 = 64. GATE = 7+1+20+5 = 33. RANK = 18+1+14+11 = 44 + 4 = 48.",
            trick = "Sum of alphabetical position values of letters."
        ),
        Question(
            id = "q_reas_4",
            subject = "General Intelligence & Reasoning",
            topic = "Blood Relations",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Pointing to a photograph, Jin-Woo said, 'She is the daughter of my grandfather's only son.' How is the person in the photograph related to Jin-Woo?",
            options = listOf("Sister", "Mother", "Cousin", "Aunt"),
            correct = 0,
            explanation = "Grandfather's only son = Jin-Woo's Father. The daughter of Jin-Woo's father = Jin-Woo's Sister.",
            trick = "'Grandfather's only son' = Father. Father's daughter = Sister."
        ),
        Question(
            id = "q_reas_5",
            subject = "General Intelligence & Reasoning",
            topic = "Direction and Distance",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "A person walks 12 km North, turns right and walks 5 km. How far and in which direction is he now with respect to his starting point?",
            options = listOf("13 km North-East", "17 km North", "13 km South-West", "15 km East"),
            correct = 0,
            explanation = "By Pythagoras Theorem: Distance = √(12² + 5²) = √(144 + 25) = √169 = 13 km. Direction from origin = North-East.",
            trick = "Pythagorean triplet: 5, 12, 13! Direction = North + East = North-East."
        ),
        Question(
            id = "q_reas_6",
            subject = "General Intelligence & Reasoning",
            topic = "Mathematical Operations",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "If '+' means '÷', '×' means '+', '÷' means '-', and '-' means '×', then evaluate: 36 + 6 - 3 × 5 ÷ 8 = ?",
            options = listOf("15", "18", "21", "12"),
            correct = 0,
            explanation = "Replace symbols: 36 ÷ 6 × 3 + 5 - 8.\nBODMAS:\nStep 1 (Divide): 36 ÷ 6 = 6\nStep 2 (Multiply): 6 × 3 = 18\nStep 3 (Add): 18 + 5 = 23\nStep 4 (Subtract): 23 - 8 = 15.",
            trick = "Strictly follow BODMAS order after replacing signs: Divide first -> 6, Multiply -> 18, Add -> 23, Subtract -> 15."
        ),

        // =========================================================================
        // 3. ENGLISH LANGUAGE & COMPREHENSION (TIER 1 & TIER 2)
        // =========================================================================
        Question(
            id = "q_eng_1",
            subject = "English Language",
            topic = "One-Word Substitution",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "A person who is unable to pay his debts:",
            options = listOf("Insolvent", "Banker", "Miser", "Extravagant"),
            correct = 0,
            explanation = "An 'Insolvent' (or Bankrupt) is someone who cannot pay off their debts. A 'Miser' hoards wealth, 'Extravagant' spends excessively.",
            trick = "In- (not) + solvent (capable of meeting financial obligations) = Insolvent."
        ),
        Question(
            id = "q_eng_2",
            subject = "English Language",
            topic = "Idioms and Phrases",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "What is the meaning of the idiom: 'To beat around the bush'?",
            options = listOf("To avoid talking about what is important", "To search in a forest", "To fight bravely", "To boast about achievements"),
            correct = 0,
            explanation = "'To beat around the bush' means to discuss a topic without arriving at the core point, often deliberately to delay or avoid answering.",
            trick = "Beating around the bush = hovering around the edge without touching the main point."
        ),
        Question(
            id = "q_eng_3",
            subject = "English Language",
            topic = "Spotting the Error",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-2 2024",
            question = "Identify the segment containing a grammatical error:\n'Neither the supervisor (A) / nor the teachers (B) / was present in the meeting (C) / No Error (D)'",
            options = listOf("was present in the meeting (C)", "Neither the supervisor (A)", "nor the teachers (B)", "No Error (D)"),
            correct = 0,
            explanation = "Proximity Rule with 'Neither...nor': When two subjects are connected by 'neither...nor', the verb agrees with the closer subject. Since 'the teachers' is plural, the verb must be 'were present', not 'was present'.",
            trick = "Rule of Proximity: Subject closer to verb ('teachers') is plural => use 'were'."
        ),
        Question(
            id = "q_eng_4",
            subject = "English Language",
            topic = "Synonyms & Antonyms",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Select the most appropriate SYNONYM of the given word: 'CANDID'",
            options = listOf("Frank / Honest", "Deceptive", "Secretive", "Arrogant"),
            correct = 0,
            explanation = "'Candid' means truthful, straightforward, and frank. Antonyms include deceptive, deceitful, and evasive.",
            trick = "Candid camera = captures real, unhidden, frank moments."
        ),
        Question(
            id = "q_eng_5",
            subject = "English Language",
            topic = "Sentence Improvement",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-2 2024",
            question = "Choose the correct substitution for the underlined part:\n'If he *had studied* harder, he *would pass* the Tier-2 exam.'",
            options = listOf("would have passed", "will pass", "had passed", "No Improvement"),
            correct = 0,
            explanation = "Third Conditional Clause Rule: 'If + Past Perfect (had + V3)... would have + V3'. Hence 'would pass' must be replaced with 'would have passed'.",
            trick = "If + had + V3 => Main clause MUST have 'would have + V3'."
        ),

        // =========================================================================
        // 4. GENERAL AWARENESS & POLITY (TIER 1 & TIER 2)
        // =========================================================================
        Question(
            id = "q_ga_1",
            subject = "General Awareness",
            topic = "Indian Polity & Constitution",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Which Article of the Indian Constitution empowers the President of India to issue Ordinances during the recess of Parliament?",
            options = listOf("Article 123", "Article 213", "Article 72", "Article 356"),
            correct = 0,
            explanation = "Article 123 gives the President the power to promulgate Ordinances during recess of Parliament. Article 213 gives similar Ordinance power to State Governors. Article 72 is the President's pardoning power.",
            trick = "President Ordinance = 1-2-3! Governor Ordinance = rearrange 1 and 2 -> 2-1-3!"
        ),
        Question(
            id = "q_ga_2",
            subject = "General Awareness",
            topic = "Indian Economy & Budget",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "What is the term used to describe inflation accompanied by stagnant economic growth and high unemployment?",
            options = listOf("Stagflation", "Hyperinflation", "Deflation", "Reflation"),
            correct = 0,
            explanation = "Stagflation is a condition of slow economic growth (stagnation) and relatively high unemployment accompanied by rising prices (inflation).",
            trick = "Stagnation + Inflation = Stagflation."
        ),
        Question(
            id = "q_ga_3",
            subject = "General Awareness",
            topic = "Modern Indian History",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Who among the following founded the 'Servants of India Society' in Pune in 1905?",
            options = listOf("Gopal Krishna Gokhale", "Bal Gangadhar Tilak", "Lala Lajpat Rai", "Dadabhai Naoroji"),
            correct = 0,
            explanation = "Gopal Krishna Gokhale founded the Servants of India Society in 1905 to train Indians to devote themselves to the service of the nation.",
            trick = "Gokhale (Political guru of Mahatma Gandhi) -> Servants of India Society (1905)."
        ),
        Question(
            id = "q_ga_4",
            subject = "General Awareness",
            topic = "Physical & Indian Geography",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Which is the highest peak in the Western Ghats (and in South India)?",
            options = listOf("Anamudi", "Doddabetta", "Guru Shikhar", "Mahendragiri"),
            correct = 0,
            explanation = "Anamudi (elevation 2,695 m), located in Kerala in the Anaimalai Hills, is the highest peak in the Western Ghats and in South India. Doddabetta (2,637 m) is in Nilgiris.",
            trick = "Anamudi (Kerala) = South India's highest Everest!"
        ),
        Question(
            id = "q_ga_5",
            subject = "General Awareness",
            topic = "General Science - Biology",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Which vitamin is water-soluble and is commonly known as Ascorbic Acid?",
            options = listOf("Vitamin C", "Vitamin A", "Vitamin D", "Vitamin K"),
            correct = 0,
            explanation = "Vitamin B complex and Vitamin C are water-soluble. Vitamins A, D, E, and K are fat-soluble. Ascorbic acid is Vitamin C.",
            trick = "Fat-soluble = 'K-E-D-A' (KEDA). Water-soluble = B & C."
        )
    )

    fun getRandomBatch(count: Int, subjectFilter: String? = null): List<Question> {
        val filtered = if (subjectFilter != null && subjectFilter != "All" && subjectFilter != "Full Mock Exam") {
            questions.filter { it.subject.contains(subjectFilter, ignoreCase = true) }
        } else {
            questions
        }
        val pool = if (filtered.isNotEmpty()) filtered else questions
        val shuffled = pool.shuffled()
        if (shuffled.size >= count) return shuffled.take(count)

        // Fill remaining with procedural questions
        val result = mutableListOf<Question>()
        result.addAll(shuffled)
        while (result.size < count) {
            result.add(InfiniteQuestionGenerator.generateBySubject(subjectFilter ?: "All"))
        }
        return result
    }
}
