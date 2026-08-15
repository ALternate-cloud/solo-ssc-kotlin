package com.sololeveling.sscprep.data

import com.sololeveling.sscprep.domain.model.Question

object QuestionVaultData {

    val questions: List<Question> = listOf(
        // =========================================================================
        // 1. QUANTITATIVE APTITUDE (MATHEMATICS)
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
            topic = "Trigonometry",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "If tan θ + cot θ = 2 (where 0° < θ < 90°), what is the value of tan⁷ θ + cot⁷ θ?",
            options = listOf("1", "2", "4", "14"),
            correct = 1,
            explanation = "tan θ + cot θ = 2 holds true when tan θ = 1 (i.e. θ = 45°). Since cot 45° = 1, tan⁷(45°) + cot⁷(45°) = 1⁷ + 1⁷ = 1 + 1 = 2.",
            trick = "Whenever x + 1/x = 2 or tan θ + cot θ = 2, the value is always 1 for each term. Thus 1ⁿ + 1ⁿ = 2."
        ),
        Question(
            id = "q_quant_5",
            subject = "Quantitative Aptitude",
            topic = "Geometry & Circles",
            difficulty = "Medium",
            examTag = "SSC CHSL 2024",
            question = "Two concentric circles have radii 13 cm and 5 cm. What is the length of the chord of the larger circle which touches the smaller circle as a tangent?",
            options = listOf("24 cm", "12 cm", "18 cm", "20 cm"),
            correct = 0,
            explanation = "Let O be the center. The radius to the point of tangency on the inner circle is perpendicular to the chord and bisects it. In right triangle formed: Hypotenuse R = 13 cm, Perpendicular r = 5 cm. Base = √(13² - 5²) = √(169 - 25) = √144 = 12 cm. Total chord length = 2 × 12 = 24 cm.",
            trick = "Pythagorean triplet (5, 12, 13). Chord length = 2 × 12 = 24 cm."
        ),
        Question(
            id = "q_quant_6",
            subject = "Quantitative Aptitude",
            topic = "Simple & Compound Interest",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-1 2024",
            question = "The difference between compound interest (compounded annually) and simple interest on a certain sum at 10% per annum for 3 years is ₹620. Find the principal sum.",
            options = listOf("₹20,000", "₹18,000", "₹22,500", "₹25,000"),
            correct = 0,
            explanation = "Difference for 3 years formula: D = P(R/100)² * (300 + R)/100.\n620 = P * (10/100)² * (310/100) = P * (1/100) * (31/10) = P * 31 / 1000.\nP = (620 * 1000) / 31 = 20 * 1000 = ₹20,000.",
            trick = "Formula D = P * (R/100)² * (3 + R/100). Or ratio for 3 years: 3a + 1 = 31 units = 620 => 1 unit = 20 => P = 1000 * 20 = ₹20,000."
        ),
        Question(
            id = "q_quant_7",
            subject = "Quantitative Aptitude",
            topic = "Speed, Time & Distance",
            difficulty = "Medium",
            examTag = "SSC CPO 2024",
            question = "A train travelling at 72 km/h crosses a 200 m long platform in 22 seconds. What is the length of the train?",
            options = listOf("240 m", "220 m", "250 m", "200 m"),
            correct = 0,
            explanation = "Speed in m/s = 72 * (5/18) = 20 m/s. Total distance covered in 22 s = Speed * Time = 20 * 22 = 440 m. Total distance = Length of train + Length of platform. Length of train = 440 - 200 = 240 m.",
            trick = "Speed = 20 m/s. Distance = 440 m. Train length = 440 - 200 = 240 m."
        ),

        // =========================================================================
        // 2. REASONING & GENERAL INTELLIGENCE
        // =========================================================================
        Question(
            id = "q_reas_1",
            subject = "General Intelligence & Reasoning",
            topic = "Syllogism",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Statements:\nI. All hunters are brave.\nII. Some brave people are tacticians.\n\nConclusions:\nI. Some tacticians are hunters.\nII. All brave people are hunters.\nIII. Some brave people are brave tacticians.",
            options = [
                "Only Conclusion III follows",
                "Both I and II follow",
                "Only Conclusion I follows",
                "None follows"
            ],
            correct = 0,
            explanation = "Statement I gives All H are B (A type). Statement II gives Some B are T (I type). Combining A + I with 'brave' in predicate and subject does not yield a definite relation between H and T. Conclusion I is not definitely true. All B are H is the illicit conversion of All H are B. Conclusion III is directly implied by statement II.",
            trick = "No definite link between Extremes (Hunters and Tacticians). Only direct restatement/sub-relation holds."
        ),
        Question(
            id = "q_reas_2",
            subject = "General Intelligence & Reasoning",
            topic = "Coding-Decoding",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "In a certain code language, if 'MONARCH' is coded as 'NPOBSDI', how will 'HUNTER' be coded in that same language?",
            options = ["IVOUFS", "IUOSET", "IVNTFR", "ITOUFS"],
            correct = 0,
            explanation = "Letter shift pattern: Each letter is replaced by its immediate next letter (+1 in alphabetical order):\nM(+1)=N, O(+1)=P, N(+1)=O, A(+1)=B, R(+1)=S, C(+1)=D, H(+1)=I.\nApplying to HUNTER:\nH(+1)=I, U(+1)=V, N(+1)=O, T(+1)=U, E(+1)=F, R(+1)=S => IVOUFS.",
            trick = "+1 forward shift across each letter."
        ),
        Question(
            id = "q_reas_3",
            subject = "General Intelligence & Reasoning",
            topic = "Blood Relations",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Pointing to a photograph of a man, Jin-Woo said, 'His mother is the only daughter of my mother.' How is Jin-Woo related to the man in the photo?",
            options = ["Maternal Uncle", "Father", "Brother", "Grandfather"],
            correct = 0,
            explanation = "'Only daughter of my mother' = Jin-Woo's sister. 'His mother is [Jin-Woo's sister]'. Therefore, the man in the photo is Jin-Woo's sister's son (nephew), making Jin-Woo the man's Maternal Uncle.",
            trick = "Break the chain from the end: 'Only daughter of my mother' = sister. Sister's son => speaker is Maternal Uncle."
        ),
        Question(
            id = "q_reas_4",
            subject = "General Intelligence & Reasoning",
            topic = "Number Series",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-1 2024",
            question = "Find the missing number in the sequence: 7, 17, 41, 85, ?, 257",
            options = ["153", "149", "165", "172"],
            correct = 1,
            explanation = "Differences between consecutive terms:\n17 - 7 = 10\n41 - 17 = 24\n85 - 41 = 44\nSecond differences:\n24 - 10 = 14\n44 - 24 = 20 (+6)\nNext second difference = 20 + 6 = 26.\nNext first difference = 44 + 26 = 70.\nMissing term = 85 + 70 = 149.\nCheck next: 26 + 6 = 32 => 70 + 32 = 102 => 149 + 102 = 251 (or pattern: 2n² + 5n). Specifically: 2(1)²+5=7, 2(2)²+9=17, 2(3)²+23... Alternate: (2n+1)² - 2, 3²-2=7, 4²+1=17, 6²+5=41, 9²+4=85, 12²+5=149.",
            trick = "Double difference progression: +10, +24, +44, +70, +102 with step of +14, +20, +26, +32."
        ),

        // =========================================================================
        // 3. ENGLISH LANGUAGE & COMPREHENSION
        // =========================================================================
        Question(
            id = "q_eng_1",
            subject = "English Language",
            topic = "Idioms & Phrases",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "What is the meaning of the idiom: 'To burn the candle at both ends'?",
            options = [
                "To work excessively hard from early morning until late night",
                "To waste money extravagantly on luxuries",
                "To create unnecessary conflicts between colleagues",
                "To be completely careless about future consequences"
            ],
            correct = 0,
            explanation = "'To burn the candle at both ends' means to work extremely hard, going to bed late and waking up early, exhausting one's physical or mental resources.",
            trick = "Burning both ends = exhausting energy/time from both sides."
        ),
        Question(
            id = "q_eng_2",
            subject = "English Language",
            topic = "Spotting Errors",
            difficulty = "Hard",
            examTag = "SSC CGL Tier-2 2024",
            question = "Identify the segment that contains a grammatical error:\n'Neither the Inspector (A) / nor his subordinates (B) / was present at the raid site (C) / during the operation. (D)'",
            options = [
                "was present at the raid site (C)",
                "Neither the Inspector (A)",
                "nor his subordinates (B)",
                "No error (D)"
            ],
            correct = 0,
            explanation = "Rule of Proximity for 'Neither... nor': When two subjects are joined by 'neither... nor', the verb must agree with the closer subject. The subject closest to the verb is 'subordinates' (plural). Hence, the verb should be 'were present', not 'was present'.",
            trick = "Neither A nor B -> Verb agrees with B! Subordinates = Plural -> were."
        ),
        Question(
            id = "q_eng_3",
            subject = "English Language",
            topic = "One Word Substitution",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Select the word which means the same as the group of words given:\n'A person who is unable to pay his debts.'",
            options = ["Insolvent", "Stoic", "Altruist", "Iconoclast"],
            correct = 0,
            explanation = "An 'Insolvent' (or Bankrupt) is a person who cannot pay his debts. Stoic = indifferent to pain/pleasure. Altruist = one who works for others' welfare. Iconoclast = one who attacks cherished beliefs.",
            trick = "In- (not) + solvent (capable of meeting financial obligations) = Insolvent."
        ),

        // =========================================================================
        // 4. GENERAL AWARENESS & POLITY
        // =========================================================================
        Question(
            id = "q_ga_1",
            subject = "General Awareness",
            topic = "Indian Polity & Constitution",
            difficulty = "Medium",
            examTag = "SSC CGL Tier-1 2024",
            question = "Which Article of the Indian Constitution empowers the President of India to issue Ordinances during the recess of Parliament?",
            options = ["Article 123", "Article 213", "Article 72", "Article 356"],
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
            options = ["Stagflation", "Hyperinflation", "Deflation", "Reflation"],
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
            options = ["Gopal Krishna Gokhale", "Bal Gangadhar Tilak", "Lala Lajpat Rai", "Dadabhai Naoroji"],
            correct = 0,
            explanation = "Gopal Krishna Gokhale founded the Servants of India Society in 1905 to train Indians to devote themselves to the service of the nation.",
            trick = "Gokhale (Political guru of Mahatma Gandhi) -> Servants of India Society (1905)."
        )
    )

    fun getRandomBatch(count: Int, subjectFilter: String? = null): List<Question> {
        val filtered = if (subjectFilter != null && subjectFilter != "Full Mock Exam") {
            questions.filter { it.subject.equals(subjectFilter, ignoreCase = true) }
        } else {
            questions
        }
        val pool = if (filtered.isNotEmpty()) filtered else questions
        val shuffled = pool.shuffled()
        if (shuffled.size >= count) return shuffled.take(count)

        // If count is larger than bank, generate procedural questions to fill
        val result = mutableListOf<Question>()
        result.addAll(shuffled)
        while (result.size < count) {
            result.add(com.sololeveling.sscprep.domain.engine.InfiniteQuestionGenerator.generateQuantQuestion())
        }
        return result
    }
}
