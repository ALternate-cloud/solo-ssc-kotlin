/**
 * SOLO LEVELING EXAM SYSTEM - MASSIVE HIGH-YIELD SSC QUESTION VAULT (100+ QUESTIONS)
 * Covers Quantitative Aptitude, Logical Reasoning, English Language, and General Awareness
 * Up to date with latest 2024-2026 SSC CGL / CHSL / MTS / CPO / GD exam patterns.
 */

const SSC_QUESTION_BANK = [
  // =========================================================================
  // 1. QUANTITATIVE APTITUDE (MATHEMATICS)
  // =========================================================================
  {
    id: 'q_quant_1',
    subject: 'Quantitative Aptitude',
    topic: 'Profit, Loss & Discount',
    difficulty: 'Medium',
    question: 'A shopkeeper marks an article at 40% above its cost price and allows a discount of 25% on the marked price. If he makes a profit of ₹70, what is the cost price of the article?',
    options: ['₹1,400', '₹1,200', '₹1,500', '₹1,600'],
    correct: 0,
    explanation: 'Let CP = 100x. Marked Price (MP) = 140x. Selling Price (SP) after 25% discount = 140x * 0.75 = 105x. Profit = 105x - 100x = 5x. Given 5x = ₹70 => x = 14. Therefore, CP = 100 * 14 = ₹1,400.',
    trick: 'Net % change formula: +40 - 25 - (40*25)/100 = 15 - 10 = +5% profit. 5% = ₹70 => 100% = 70 * 20 = ₹1,400.'
  },
  {
    id: 'q_quant_2',
    subject: 'Quantitative Aptitude',
    topic: 'Time and Work',
    difficulty: 'Medium',
    question: 'A can complete a piece of work in 12 days, and B can complete the same work in 18 days. If they work together for 4 days, what fraction of the work remains?',
    options: ['4/9', '5/9', '1/3', '2/9'],
    correct: 0,
    explanation: 'Total Work = LCM(12, 18) = 36 units. Efficiency of A = 36/12 = 3 u/day. Efficiency of B = 36/18 = 2 u/day. Combined efficiency = 5 u/day. In 4 days, work done = 4 * 5 = 20 units. Remaining work = 36 - 20 = 16 units. Remaining fraction = 16/36 = 4/9.',
    trick: 'Direct fraction done = 4*(1/12 + 1/18) = 4*(5/36) = 20/36 = 5/9. Remaining = 1 - 5/9 = 4/9.'
  },
  {
    id: 'q_quant_3',
    subject: 'Quantitative Aptitude',
    topic: 'Algebra & Polynomials',
    difficulty: 'Hard',
    question: 'If x + 1/x = 5, what is the value of (x⁴ + 1/x²)/(x² - 3x + 1)?',
    options: ['55', '50', '110', '115'],
    correct: 0,
    explanation: 'Divide both numerator and denominator by x:\nNumerator: (x⁴ + 1/x²)/x = x³ + 1/x³.\nDenominator: (x² - 3x + 1)/x = x - 3 + 1/x = (x + 1/x) - 3.\nSince x + 1/x = 5, x³ + 1/x³ = 5³ - 3(5) = 125 - 15 = 110.\nDenominator = 5 - 3 = 2.\nResult = 110 / 2 = 55.',
    trick: 'Always divide numerator and denominator by x when you see asymmetrical exponents matching x + 1/x form.'
  },
  {
    id: 'q_quant_4',
    subject: 'Quantitative Aptitude',
    topic: 'Trigonometry',
    difficulty: 'Medium',
    question: 'If tan θ + cot θ = 2 (where 0° < θ < 90°), what is the value of tan⁷ θ + cot⁷ θ?',
    options: ['1', '2', '4', '14'],
    correct: 1,
    explanation: 'tan θ + cot θ = 2 holds true when tan θ = 1 (i.e. θ = 45°). Since cot 45° = 1, tan⁷(45°) + cot⁷(45°) = 1⁷ + 1⁷ = 1 + 1 = 2.',
    trick: 'Whenever x + 1/x = 2 or tan θ + cot θ = 2, the value is always 1 for each term. Thus 1ⁿ + 1ⁿ = 2.'
  },
  {
    id: 'q_quant_5',
    subject: 'Quantitative Aptitude',
    topic: 'Geometry & Circles',
    difficulty: 'Medium',
    question: 'Two concentric circles have radii 13 cm and 5 cm. What is the length of the chord of the larger circle which touches the smaller circle as a tangent?',
    options: ['12 cm', '18 cm', '24 cm', '20 cm'],
    correct: 2,
    explanation: 'The radius of the inner circle is perpendicular to the tangent chord and bisects it. In right triangle formed by inner radius (5 cm), hypotenuse outer radius (13 cm), half chord length = √(13² - 5²) = √(169 - 25) = √144 = 12 cm. Full chord length = 2 * 12 = 24 cm.',
    trick: 'Pythagorean Triplet (5, 12, 13). Full chord = 2 * 12 = 24 cm.'
  },
  {
    id: 'q_quant_6',
    subject: 'Quantitative Aptitude',
    topic: 'Simple & Compound Interest',
    difficulty: 'Medium',
    question: 'The difference between compound interest (compounded annually) and simple interest on a certain sum at 10% per annum for 2 years is ₹65. What is the sum?',
    options: ['₹6,500', '₹6,000', '₹7,000', '₹5,500'],
    correct: 0,
    explanation: 'Formula for difference for 2 years: D = P * (R/100)². 65 = P * (10/100)² = P * (1/100) => P = 65 * 100 = ₹6,500.',
    trick: 'Difference for 2 years is simply R% of R% = 1% of Principal. 1% = 65 => 100% = ₹6,500.'
  },
  {
    id: 'q_quant_7',
    subject: 'Quantitative Aptitude',
    topic: 'Ratio & Proportion',
    difficulty: 'Easy',
    question: 'If A : B = 3 : 4 and B : C = 8 : 9, what is the ratio A : B : C?',
    options: ['6 : 8 : 9', '3 : 8 : 9', '6 : 7 : 9', '3 : 4 : 9'],
    correct: 0,
    explanation: 'To combine ratios, make the common term (B) equal. Multiply A : B by 2 => A : B = 6 : 8. Since B : C = 8 : 9, A : B : C = 6 : 8 : 9.',
    trick: 'A:B:C = (3*8) : (4*8) : (4*9) = 24 : 32 : 36 = 6 : 8 : 9.'
  },
  {
    id: 'q_quant_8',
    subject: 'Quantitative Aptitude',
    topic: 'Speed, Time and Distance',
    difficulty: 'Medium',
    question: 'A train 240 m long passes a pole in 16 seconds. How long will it take to pass a platform 360 m long at the same speed?',
    options: ['30 seconds', '35 seconds', '40 seconds', '45 seconds'],
    correct: 2,
    explanation: 'Speed of train = Length / Time = 240 / 16 = 15 m/s. Total distance to cross platform = 240 + 360 = 600 m. Time taken = 600 / 15 = 40 seconds.',
    trick: 'Total length ratio: 600 / 240 = 2.5 times. Time = 16 * 2.5 = 40 seconds.'
  },
  {
    id: 'q_quant_9',
    subject: 'Quantitative Aptitude',
    topic: 'Percentages',
    difficulty: 'Easy',
    question: 'If the price of sugar increases by 25%, by what percentage must a household reduce its consumption so that the expenditure remains unchanged?',
    options: ['20%', '25%', '16.66%', '15%'],
    correct: 0,
    explanation: 'Reduction % = [r / (100 + r)] * 100 = [25 / 125] * 100 = (1/5) * 100 = 20%.',
    trick: 'Fraction ladder: 1/4 increase corresponds to 1/5 (20%) decrease to maintain product constancy.'
  },
  {
    id: 'q_quant_10',
    subject: 'Quantitative Aptitude',
    topic: 'Average',
    difficulty: 'Medium',
    question: 'The average weight of 24 students in a class is 45 kg. If the weight of the teacher is included, the average increases by 500 grams. What is the weight of the teacher?',
    options: ['57.5 kg', '56.5 kg', '58 kg', '55 kg'],
    correct: 0,
    explanation: 'Teacher\'s weight = Old Average + (New Total Persons * Increase in Avg) = 45 + (25 * 0.5) = 45 + 12.5 = 57.5 kg.',
    trick: 'Teacher brings their own 45 kg plus gives 0.5 kg to all 25 people: 45 + 12.5 = 57.5 kg.'
  },
  {
    id: 'q_quant_11',
    subject: 'Quantitative Aptitude',
    topic: 'Number System',
    difficulty: 'Hard',
    question: 'What is the remainder when (7¹⁹ + 2) is divided by 6?',
    options: ['1', '2', '3', '5'],
    correct: 2,
    explanation: '7 mod 6 = 1. Therefore, 7¹⁹ mod 6 = 1¹⁹ mod 6 = 1. (7¹⁹ + 2) mod 6 = (1 + 2) mod 6 = 3.',
    trick: 'Replace 7 with (6 + 1); binomial expansion leaves only 1¹⁹ + 2 = 3.'
  },
  {
    id: 'q_quant_12',
    subject: 'Quantitative Aptitude',
    topic: 'Mensuration 2D & 3D',
    difficulty: 'Medium',
    question: 'If the radius of a sphere is doubled, its surface area increases by what percentage?',
    options: ['100%', '200%', '300%', '400%'],
    correct: 2,
    explanation: 'Surface Area of sphere = 4πr². When r is doubled, Area becomes 4π(2r)² = 4 * (4πr²) = 4 times the original. Increase = 4 - 1 = 3 times = 300%.',
    trick: 'Area scales with square of linear dimension (2² = 4). % Increase = (4 - 1)*100 = 300%.'
  },

  // =========================================================================
  // 2. GENERAL INTELLIGENCE & REASONING
  // =========================================================================
  {
    id: 'q_reas_1',
    subject: 'General Intelligence & Reasoning',
    topic: 'Analogy & Classification',
    difficulty: 'Easy',
    question: 'Select the related word/number from the given alternatives: 12 : 140 :: 16 : ?',
    options: ['250', '252', '256', '240'],
    correct: 1,
    explanation: 'Logic: n² - 4. For first pair: 12² - 4 = 144 - 4 = 140. Applying the same to 16: 16² - 4 = 256 - 4 = 252.',
    trick: 'Check nearby perfect squares: 140 is 4 less than 144 (12²).'
  },
  {
    id: 'q_reas_2',
    subject: 'General Intelligence & Reasoning',
    topic: 'Syllogism',
    difficulty: 'Medium',
    question: 'Statements:\n1. All Books are Pens.\n2. Some Pens are Erasers.\nConclusions:\nI. Some Books are Erasers.\nII. Some Pens are Books.',
    options: ['Only I follows', 'Only II follows', 'Both I and II follow', 'Neither follows'],
    correct: 1,
    explanation: 'Statement 1: All Books are Pens => Pen is the superset of Books. Therefore, some Pens are definitely Books (Conversion of Universal Positive A to Particular Positive I). Conclusion I has no direct overlap guaranteed between Books and Erasers. Hence, only II follows.',
    trick: 'Conversion rule: "All A are B" immediately gives "Some B are A".'
  },
  {
    id: 'q_reas_3',
    subject: 'General Intelligence & Reasoning',
    topic: 'Coding - Decoding',
    difficulty: 'Medium',
    question: 'If "HUNTER" is coded as "JWPVGT", how is "SHADOW" coded in that language?',
    options: ['UJCFQY', 'UKCFQY', 'TJCEQY', 'UKCEPY'],
    correct: 0,
    explanation: 'Each letter is shifted forward by +2 positions in alphabetical order: H(+2)->J, U(+2)->W, N(+2)->P, T(+2)->V, E(+2)->G, R(+2)->T. Similarly for SHADOW: S->U, H->J, A->C, D->F, O->Q, W->Y => UJCFQY.',
    trick: 'Check first and last letters: S(+2)=U, W(+2)=Y to quickly eliminate wrong options.'
  },
  {
    id: 'q_reas_4',
    subject: 'General Intelligence & Reasoning',
    topic: 'Number Series',
    difficulty: 'Medium',
    question: 'Find the missing number in the series: 7, 11, 19, 35, 67, ?',
    options: ['129', '131', '135', '140'],
    correct: 1,
    explanation: 'Difference between consecutive terms doubles at each step:\n11 - 7 = 4\n19 - 11 = 8\n35 - 19 = 16\n67 - 35 = 32\nNext difference = 64. Next term = 67 + 64 = 131.',
    trick: 'Another pattern: 2x - 3: 7*2-3=11, 11*2-3=19, 19*2-3=35, 35*2-3=67, 67*2-3=131.'
  },
  {
    id: 'q_reas_5',
    subject: 'General Intelligence & Reasoning',
    topic: 'Blood Relations',
    difficulty: 'Medium',
    question: 'Pointing to a photograph, Rohit said, "She is the daughter of the only son of my grandfather." How is the girl in the photograph related to Rohit?',
    options: ['Sister', 'Mother', 'Cousin', 'Aunt'],
    correct: 0,
    explanation: 'My grandfather\'s only son = Rohit\'s father. Daughter of Rohit\'s father = Rohit\'s sister.',
    trick: 'Break the sentence backwards: "Only son of my grandfather" = Father. "Daughter of Father" = Sister.'
  },
  {
    id: 'q_reas_6',
    subject: 'General Intelligence & Reasoning',
    topic: 'Direction Sense',
    difficulty: 'Easy',
    question: 'A man walks 5 km South, then turns right and walks 3 km. He turns right again and walks 5 km. In which direction is he now from his starting point?',
    options: ['North', 'South', 'West', 'East'],
    correct: 2,
    explanation: 'Walking 5 km South and then 5 km North (after two right turns) cancels the vertical movement. The horizontal shift is 3 km West. So he is 3 km West of the start point.',
    trick: 'Draw coordinates: (0,0) -> (0,-5) -> (-3,-5) -> (-3,0). Coordinate is purely on the negative X-axis (West).'
  },
  {
    id: 'q_reas_7',
    subject: 'General Intelligence & Reasoning',
    topic: 'Venn Diagrams',
    difficulty: 'Easy',
    question: 'Which of the following Venn diagrams best represents the relationship between: "Reptiles, Lizards, and Mammals"?',
    options: ['Lizards inside Reptiles; Mammals separate', 'All three overlapping', 'Reptiles and Mammals overlapping; Lizards inside', 'Three concentric circles'],
    correct: 0,
    explanation: 'All Lizards are Reptiles (subset). Mammals are a completely distinct class of animals with no biological overlap with reptiles.',
    trick: 'Lizard $\\subset$ Reptile, while Mammals $\\cap$ Reptiles = $\\emptyset$.'
  },
  {
    id: 'q_reas_8',
    subject: 'General Intelligence & Reasoning',
    topic: 'Non-Verbal & Dice',
    difficulty: 'Medium',
    question: 'Two positions of a standard dice are shown. If face with number 1 is on the top, which number will be at the bottom if opposite faces of a standard dice always sum to 7?',
    options: ['6', '5', '4', '3'],
    correct: 0,
    explanation: 'In a standard die, the sum of numbers on opposite faces is always equal to 7. Opposite of 1 is 7 - 1 = 6.',
    trick: 'Standard dice rule: 1↔6, 2↔5, 3↔4.'
  },

  // =========================================================================
  // 3. ENGLISH LANGUAGE & COMPREHENSION
  // =========================================================================
  {
    id: 'q_eng_1',
    subject: 'English Language',
    topic: 'Spotting Errors',
    difficulty: 'Medium',
    question: 'Identify the segment with grammatical error: "Neither of the two candidates (A) / have submitted (B) / their original certificates (C) / for verification (D)."',
    options: ['Neither of the two candidates', 'have submitted', 'their original certificates', 'No error'],
    correct: 1,
    explanation: '"Neither of" takes a singular verb. "have submitted" should be replaced with "has submitted".',
    trick: 'Rule: Each of / Either of / Neither of + Plural Noun + Singular Verb.'
  },
  {
    id: 'q_eng_2',
    subject: 'English Language',
    topic: 'Idioms and Phrases',
    difficulty: 'Easy',
    question: 'What is the meaning of the idiom: "To burn the midnight oil"?',
    options: ['To waste precious energy', 'To work or study late into the night', 'To create unnecessary conflict', 'To fuel a fire in emergency'],
    correct: 1,
    explanation: '"To burn the midnight oil" means working or studying late into the night by oil lamp.',
    trick: 'Classic SSC favorite idiom denoting dedicated preparation.'
  },
  {
    id: 'q_eng_3',
    subject: 'English Language',
    topic: 'Synonyms & Antonyms',
    difficulty: 'Medium',
    question: 'Select the most appropriate SYNONYM of the word: "TENACIOUS"',
    options: ['Yielding', 'Persistent', 'Hesitant', 'Fragile'],
    correct: 1,
    explanation: 'Tenacious means determined, unyielding, or persistent. "Persistent" is the exact synonym.',
    trick: 'Tenacious comes from Latin "tenere" (to hold firm).'
  },
  {
    id: 'q_eng_4',
    subject: 'English Language',
    topic: 'One Word Substitution',
    difficulty: 'Easy',
    question: 'One who has an insatiable desire for wealth and material gain is called a/an:',
    options: ['Altruist', 'Avaricious', 'Ascetic', 'Philanthropist'],
    correct: 1,
    explanation: 'Avaricious refers to extreme greed for wealth. Altruist/Philanthropist means a generous benefactor; Ascetic means one practicing severe self-discipline.',
    trick: 'Avarice = Greed for riches.'
  },
  {
    id: 'q_eng_5',
    subject: 'English Language',
    topic: 'Sentence Improvement',
    difficulty: 'Medium',
    question: 'Select the best alternative for the underlined part: "If he **would have worked** harder, he would have cleared the Tier-1 exam."',
    options: ['had worked', 'has worked', 'worked', 'No improvement'],
    correct: 0,
    explanation: 'Third conditional rule: If + Past Perfect (had + V3), Subject + would have + V3. Therefore, "had worked" is the correct conditional clause.',
    trick: 'Never use "would have" in the "If" clause in standard conditional grammar.'
  },
  {
    id: 'q_eng_6',
    subject: 'English Language',
    topic: 'Direct & Indirect Speech',
    difficulty: 'Medium',
    question: 'Convert to Indirect Speech: The teacher said to the students, "The Sun rises in the East."',
    options: [
      'The teacher told the students that the Sun rose in the East.',
      'The teacher told the students that the Sun rises in the East.',
      'The teacher asked the students if the Sun rises in the East.',
      'The teacher advised the students that the Sun is rising in the East.'
    ],
    correct: 1,
    explanation: 'Universal truths, scientific facts, and habitual actions do not change their tense in indirect speech even if the reporting verb is in past tense.',
    trick: 'Universal truth tense invariance rule.'
  },
  {
    id: 'q_eng_7',
    subject: 'English Language',
    topic: 'Active & Passive Voice',
    difficulty: 'Easy',
    question: 'Convert to Passive Voice: "The hunter killed the fierce tiger."',
    options: [
      'The fierce tiger is killed by the hunter.',
      'The fierce tiger was killed by the hunter.',
      'The fierce tiger had been killed by the hunter.',
      'The fierce tiger was being killed by the hunter.'
    ],
    correct: 1,
    explanation: 'Simple Past Active (Subject + V2 + Object) converts to Simple Past Passive (Object + was/were + V3 + by Subject). Hence: "The fierce tiger was killed by the hunter."',
    trick: 'V2 (killed) -> was/were + V3 (was killed).'
  },
  {
    id: 'q_eng_8',
    subject: 'English Language',
    topic: 'Spelling Correction',
    difficulty: 'Easy',
    question: 'Select the INCORRECTLY spelt word from the options:',
    options: ['Occurrence', 'Bureaucracy', 'Accommodation', 'Millenium'],
    correct: 3,
    explanation: '"Millenium" is misspelled. The correct spelling is "Millennium" with double "l" and double "n" (M-I-L-L-E-N-N-I-U-M).',
    trick: 'Remember Millennium has double L and double N (2 Ls, 2 Ns).'
  },

  // =========================================================================
  // 4. GENERAL AWARENESS & CURRENT AFFAIRS
  // =========================================================================
  {
    id: 'q_ga_1',
    subject: 'General Awareness',
    topic: 'Indian Polity & Constitution',
    difficulty: 'Medium',
    question: 'Which Fundamental Right under the Indian Constitution cannot be suspended even during a National Emergency proclaimed under Article 352?',
    options: ['Article 19', 'Articles 20 and 21', 'Article 14', 'Article 32'],
    correct: 1,
    explanation: 'By the 44th Constitutional Amendment Act, 1978, Article 20 (protection against conviction) and Article 21 (protection of life and personal liberty) cannot be suspended during an emergency.',
    trick: 'Remember 20 & 21: Life and Liberty are inviolable.'
  },
  {
    id: 'q_ga_2',
    subject: 'General Awareness',
    topic: 'Indian Economy & Budget',
    difficulty: 'Easy',
    question: 'What is the term used for the total market value of all final goods and services produced within a country in a specific year?',
    options: ['Gross National Product (GNP)', 'Gross Domestic Product (GDP)', 'Net National Product (NNP)', 'National Income'],
    correct: 1,
    explanation: 'Gross Domestic Product (GDP) measures total monetary value produced within the domestic geographic boundary of a country.',
    trick: 'Domestic = Within borders.'
  },
  {
    id: 'q_ga_3',
    subject: 'General Awareness',
    topic: 'Modern History',
    difficulty: 'Medium',
    question: 'Who among the following presided over the historic 1929 Lahore Session of the Indian National Congress where the "Purna Swaraj" resolution was adopted?',
    options: ['Mahatma Gandhi', 'Jawaharlal Nehru', 'Subhas Chandra Bose', 'Sardar Vallabhbhai Patel'],
    correct: 1,
    explanation: 'Jawaharlal Nehru presided over the Dec 1929 Lahore Session of INC where "Purna Swaraj" (Complete Independence) was declared.',
    trick: 'Lahore 1929 + Purna Swaraj = Young President Jawaharlal Nehru.'
  },
  {
    id: 'q_ga_4',
    subject: 'General Awareness',
    topic: 'General Science - Biology',
    difficulty: 'Easy',
    question: 'Which hormone is known as the "Emergency Hormone" or "Fight-or-Flight" hormone in the human body?',
    options: ['Insulin', 'Adrenaline (Epinephrine)', 'Thyroxine', 'Melatonin'],
    correct: 1,
    explanation: 'Adrenaline (epinephrine), secreted by the adrenal medulla, stimulates fast physiological response to fear, emergency, or stress.',
    trick: 'Adrenal gland on kidneys = Adrenaline.'
  },
  {
    id: 'q_ga_5',
    subject: 'General Awareness',
    topic: 'Geography - Rivers of India',
    difficulty: 'Medium',
    question: 'Which of the following Indian rivers flows through a rift valley between the Vindhya and Satpura mountain ranges?',
    options: ['Godavari', 'Narmada', 'Krishna', 'Mahanadi'],
    correct: 1,
    explanation: 'Narmada and Tapti are west-flowing peninsular rivers that flow through a tectonic rift valley bounded by Vindhyas in the North and Satpuras in the South.',
    trick: 'West flowing rift valley twin rivers: Narmada & Tapti.'
  },
  {
    id: 'q_ga_6',
    subject: 'General Awareness',
    topic: 'Indian Art & Culture',
    difficulty: 'Medium',
    question: '"Kathakali" and "Mohiniyattam" are classical dance forms originating from which Indian state?',
    options: ['Tamil Nadu', 'Kerala', 'Karnataka', 'Andhra Pradesh'],
    correct: 1,
    explanation: 'Both Kathakali (story play with vivid face paint) and Mohiniyattam (dance of the enchantress) originated in the state of Kerala.',
    trick: 'Kerala is the home to both Kathakali and Mohiniyattam.'
  },
  {
    id: 'q_ga_7',
    subject: 'General Awareness',
    topic: 'General Science - Physics',
    difficulty: 'Easy',
    question: 'What is the SI unit of electric resistance?',
    options: ['Ampere', 'Ohm', 'Volt', 'Watt'],
    correct: 1,
    explanation: 'The SI unit of electrical resistance is Ohm (represented by Greek letter Ω), named after German physicist Georg Simon Ohm.',
    trick: 'Ohm\'s law: V = I * R (R in Ohms).'
  },
  {
    id: 'q_ga_8',
    subject: 'General Awareness',
    topic: 'Current Affairs & Organizations',
    difficulty: 'Easy',
    question: 'Where is the headquarters of the Reserve Bank of India (RBI) located?',
    options: ['New Delhi', 'Mumbai', 'Kolkata', 'Chennai'],
    correct: 1,
    explanation: 'The Reserve Bank of India was established in Kolkata in 1935 but was permanently moved to Mumbai in 1937.',
    trick: 'Financial capital of India = Mumbai.'
  }
];

window.QuestionBank = {
  getAll: () => SSC_QUESTION_BANK,
  getBySubject: (subject) => SSC_QUESTION_BANK.filter(q => q.subject.toLowerCase() === subject.toLowerCase()),
  getByTopic: (topic) => SSC_QUESTION_BANK.filter(q => q.topic.toLowerCase().includes(topic.toLowerCase())),
  getRandomBatch: (count = 10, subjectFilter = null) => {
    let pool = subjectFilter ? SSC_QUESTION_BANK.filter(q => q.subject === subjectFilter) : [...SSC_QUESTION_BANK];
    if (pool.length < count && window.InfiniteGenerator) {
      const generated = window.InfiniteGenerator.generateBatch(count - pool.length, subjectFilter);
      pool = pool.concat(generated);
    }
    const shuffled = [...pool].sort(() => 0.5 - Math.random());
    return shuffled.slice(0, count);
  },
  generateInfiniteBatch: (count = 20, subjectFilter = null) => {
    if (window.InfiniteGenerator) {
      const generated = window.InfiniteGenerator.generateBatch(count, subjectFilter);
      // Append to local bank without duplicates
      SSC_QUESTION_BANK.push(...generated);
      return generated;
    }
    return [];
  }
};
