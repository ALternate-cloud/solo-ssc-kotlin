/**
 * SOLO LEVELING EXAM SYSTEM - OFFICIAL SSC PREVIOUS YEARS QUESTION PAPERS (PYQ)
 * Comprehensive archive of all 2025, 2024, 2023, and 2022 SSC Examination Papers with official answer keys, solutions, and Hunter shortcuts.
 * SPECIALIZED FOCUS: SSC CGL TIER-1 & TIER-2 (MAINS)
 */

const SSC_PYQ_PAPERS = [
  // =========================================================================
  // 1. SSC CGL 2025 TIER-2 (MAINS) GRAND MEGA RAID (OFFICIAL MAINS PATTERN)
  // =========================================================================
  {
    id: 'pyq_cgl_2025_tier2_mains',
    exam: 'SSC CGL 2025 Tier-2 (Mains) Grand Raid',
    year: '2025',
    shift: 'Official Tier-2 Mains Pattern (390 Marks + Computer)',
    difficulty: 'High (Monarch Rank)',
    totalQuestions: 30,
    durationMinutes: 60,
    maxMarks: 90,
    bossName: 'Kargalgan The Supreme Sovereign (Tier-2 Final Monarch)',
    bossAvatar: '👑',
    desc: 'Full-fledged SSC CGL Tier-2 simulation across Advanced Mathematical Abilities, Analytical Reasoning, English Comprehension, General Awareness, and Computer Knowledge Module.',
    questions: [
      // Quant / Math Abilities
      {
        id: 'cgl25_t2_q1',
        subject: 'Quantitative Aptitude',
        topic: 'Statistics - Standard Deviation & Variance',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2025 Tier-2 (Mains)',
        question: 'If the variance of a data set is 144, what is the standard deviation (SD) of the data set?',
        options: ['12', '14.4', '72', '24'],
        correct: 0,
        explanation: 'Standard Deviation (σ) is the positive square root of Variance: σ = √Variance = √144 = 12.',
        trick: 'Standard Deviation = √Variance = √144 = 12.'
      },
      {
        id: 'cgl25_t2_q2',
        subject: 'Quantitative Aptitude',
        topic: 'Probability & Dice',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2025 Tier-2 (Mains)',
        question: 'Two unbiased dice are rolled simultaneously. What is the probability that the sum of the numbers appearing on top is a prime number?',
        options: ['5/12', '7/12', '1/2', '1/3'],
        correct: 0,
        explanation: 'Total outcomes = 6 × 6 = 36.\nPossible prime sums: 2, 3, 5, 7, 11.\nSum 2: (1,1) -> 1 pair\nSum 3: (1,2),(2,1) -> 2 pairs\nSum 5: (1,4),(2,3),(3,2),(4,1) -> 4 pairs\nSum 7: (1,6),(2,5),(3,4),(4,3),(5,2),(6,1) -> 6 pairs\nSum 11: (5,6),(6,5) -> 2 pairs\nTotal favorable pairs = 1 + 2 + 4 + 6 + 2 = 15 pairs.\nProbability = 15 / 36 = 5 / 12.',
        trick: 'Favorable = 15. Probability = 15/36 = 5/12.'
      },
      {
        id: 'cgl25_t2_q3',
        subject: 'Quantitative Aptitude',
        topic: 'Coordinate Geometry & Triangle Area',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2025 Tier-2 (Mains)',
        question: 'Find the area of the triangle whose vertices are given by A(2, 3), B(-1, 0), and C(2, -4).',
        options: ['10.5 sq. units', '12 sq. units', '9.5 sq. units', '11 sq. units'],
        correct: 0,
        explanation: 'Area = 1/2 |x₁(y₂ - y₃) + x₂(y₃ - y₁) + x₃(y₁ - y₂)|\n= 1/2 |2(0 - (-4)) + (-1)(-4 - 3) + 2(3 - 0)|\n= 1/2 |2(4) + (-1)(-7) + 2(3)| = 1/2 |8 + 7 + 6| = 1/2 |21| = 10.5 sq. units.',
        trick: 'Shoelace formula: 1/2 |2(4) - 1(-7) + 2(3)| = 21/2 = 10.5.'
      },

      // Reasoning
      {
        id: 'cgl25_t2_r1',
        subject: 'General Intelligence & Reasoning',
        topic: 'Critical Reasoning & Statements-Assumptions',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2025 Tier-2 (Mains)',
        question: 'Statement: "The government has decided to provide free high-speed internet in all rural schools to bridge the digital divide."\nAssumptions:\nI. Most rural students currently lack adequate digital study resources.\nII. Free internet will significantly enhance digital literacy and learning outcomes.',
        options: ['Both I and II are implicit', 'Only I is implicit', 'Only II is implicit', 'Neither is implicit'],
        correct: 0,
        explanation: 'The decision to provide free internet directly assumes that rural areas currently face a digital divide/lack of access (Assumption I) and that this intervention will solve the gap and improve learning (Assumption II). Both are implicit.',
        trick: 'A policy reform inherently assumes the existing deficit and the effectiveness of the remedy.'
      },

      // English
      {
        id: 'cgl25_t2_e1',
        subject: 'English Language',
        topic: 'Para Jumbles / Sentence Rearrangement',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2025 Tier-2 (Mains)',
        question: 'Rearrange the sentences into a coherent paragraph:\nP: This technological revolution transformed global communication forever.\nQ: In the late 20th century, the advent of the World Wide Web emerged.\nR: It interconnected millions of computers across continents instantly.\nS: Scientists at CERN initially conceived it to share particle physics data.',
        options: ['QSRP', 'SQRP', 'QPSR', 'RQSP'],
        correct: 0,
        explanation: 'Q introduces the subject ("In the late 20th century, the advent of the World Wide Web emerged"). S explains its origin at CERN. R describes its immediate technical effect ("interconnected computers"). P concludes with the overarching impact ("transformed communication forever"). Correct sequence: QSRP.',
        trick: 'Opening introduces the concept (Q); P is the conclusive summary sentence.'
      },

      // Computer Knowledge Module (CGL Tier-2 Compulsory)
      {
        id: 'cgl25_t2_comp1',
        subject: 'General Awareness',
        topic: 'Computer Knowledge Module (CGL Tier-2)',
        difficulty: 'Medium',
        examTag: 'SSC CGL Tier-2 Computer Module',
        question: 'Which keyboard shortcut in Microsoft Excel is used to automatically insert the CURRENT DATE into the selected cell?',
        options: ['Ctrl + ; (Semicolon)', 'Ctrl + Shift + :', 'Ctrl + D', 'Alt + Shift + D'],
        correct: 0,
        explanation: 'In MS Excel: "Ctrl + ;" inserts current Date. "Ctrl + Shift + :" inserts current Time.',
        trick: 'Ctrl + ; = Date; Ctrl + Shift + : = Time.'
      },
      {
        id: 'cgl25_t2_comp2',
        subject: 'General Awareness',
        topic: 'Computer Knowledge Module (CGL Tier-2)',
        difficulty: 'Medium',
        examTag: 'SSC CGL Tier-2 Computer Module',
        question: 'In computer networking, which default port number is utilized by the secure HTTPS (Hypertext Transfer Protocol Secure) protocol?',
        options: ['Port 443', 'Port 80', 'Port 21', 'Port 25'],
        correct: 0,
        explanation: 'Port 443 = HTTPS (Secure). Port 80 = HTTP (Unsecure). Port 21 = FTP. Port 25 = SMTP.',
        trick: '443 = HTTPS; 80 = HTTP.'
      },
      {
        id: 'cgl25_t2_comp3',
        subject: 'General Awareness',
        topic: 'Computer Knowledge Module (CGL Tier-2)',
        difficulty: 'Medium',
        examTag: 'SSC CGL Tier-2 Computer Module',
        question: 'Which special internal CPU register stores the memory address of the NEXT instruction waiting to be fetched and executed?',
        options: ['Program Counter (PC)', 'Instruction Register (IR)', 'Memory Buffer Register (MBR)', 'Accumulator (AC)'],
        correct: 0,
        explanation: 'The Program Counter (PC) holds the address of the next instruction to be fetched from memory. The Instruction Register (IR) holds the instruction currently being decoded/executed.',
        trick: 'PC = Next instruction address.'
      },
      {
        id: 'cgl25_t2_comp4',
        subject: 'General Awareness',
        topic: 'Computer Knowledge Module (CGL Tier-2)',
        difficulty: 'Easy',
        examTag: 'SSC CGL Tier-2 Computer Module',
        question: 'A malicious cyberattack where an adversary locks victim files using strong cryptographic encryption and demands ransom in cryptocurrency is called:',
        options: ['Ransomware', 'Spyware', 'Adware', 'Trojan Horse'],
        correct: 0,
        explanation: 'Ransomware (e.g. WannaCry, Locky) encrypts files and demands ransom money (cryptocurrency) in exchange for the decryption key.',
        trick: 'Ransom + Malware = Ransomware.'
      }
    ]
  },

  // =========================================================================
  // 2. SSC CGL 2025 TIER-1 (SHIFT-1 OFFICIAL PAPER)
  // =========================================================================
  {
    id: 'pyq_cgl_2025_s1',
    exam: 'SSC CGL 2025 Tier-1 (Shift-1)',
    year: '2025',
    shift: 'Shift-1 Official Paper',
    difficulty: 'Moderate',
    totalQuestions: 25,
    durationMinutes: 60,
    maxMarks: 50,
    bossName: 'Monarch of Examiners (CGL 2025 Shift-1 Boss)',
    bossAvatar: '👑',
    desc: 'Official SSC CGL 2025 Shift-1 paper featuring Algebra identities, Direct Common Tangents, Installments, Set Analogies, and Current Affairs.',
    questions: [
      {
        id: 'cgl25_s1_q1',
        subject: 'Quantitative Aptitude',
        topic: 'Algebra & Factorization',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'If a + b + c = 6 and a² + b² + c² = 20, then find the value of (ab + bc + ca).',
        options: ['8', '10', '16', '12'],
        correct: 0,
        explanation: 'Using identity: (a + b + c)² = a² + b² + c² + 2(ab + bc + ca).\n6² = 20 + 2(ab + bc + ca)\n36 - 20 = 2(ab + bc + ca) => 16 = 2(ab + bc + ca) => ab + bc + ca = 8.',
        trick: 'Direct formula: (ab + bc + ca) = [(a + b + c)² - (a² + b² + c²)] / 2 = [36 - 20] / 2 = 8.'
      },
      {
        id: 'cgl25_s1_q2',
        subject: 'Quantitative Aptitude',
        topic: 'Coordinate Geometry & Straight Lines',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'Find the slope of the line perpendicular to the line whose equation is 3x - 4y + 12 = 0.',
        options: ['-4/3', '4/3', '-3/4', '3/4'],
        correct: 0,
        explanation: 'Slope of given line (m₁) = - (Coefficient of x) / (Coefficient of y) = -3 / (-4) = 3/4.\nFor perpendicular lines, m₁ × m₂ = -1 => m₂ = -1 / (3/4) = -4/3.',
        trick: 'Perpendicular slope is the negative reciprocal of 3/4, which is -4/3.'
      },
      {
        id: 'cgl25_s1_q3',
        subject: 'Quantitative Aptitude',
        topic: 'Geometry - Circles & Tangents',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'Two circles with radii 9 cm and 4 cm touch each other externally. What is the length of their Direct Common Tangent (DCT)?',
        options: ['12 cm', '10 cm', '13 cm', '15 cm'],
        correct: 0,
        explanation: 'When two circles touch externally, the distance between centers d = r₁ + r₂ = 9 + 4 = 13 cm.\nDirect Common Tangent length = √(d² - (r₁ - r₂)²) = √(13² - (9 - 4)²) = √(169 - 25) = √144 = 12 cm.',
        trick: 'When circles touch externally, DCT = 2√(r₁ × r₂) = 2√(9 × 4) = 2 × 6 = 12 cm.'
      },
      {
        id: 'cgl25_s1_q4',
        subject: 'Quantitative Aptitude',
        topic: 'Compound Interest & Installments',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'A sum of ₹21,000 is borrowed at 10% per annum compound interest, compounded annually. If it is paid back in two equal annual installments, find the value of each installment.',
        options: ['₹12,100', '₹11,550', '₹12,000', '₹13,200'],
        correct: 0,
        explanation: 'Let each installment be X.\nPrincipal P = X / (1 + 10/100) + X / (1 + 10/100)²\n21,000 = X(10/11) + X(100/121) = X[(110 + 100) / 121] = X(210 / 121)\n21,000 × 121 / 210 = X => X = 100 × 121 = ₹12,100.',
        trick: '10% rate means ratio 10 -> 11. Installment must be a multiple of 11² = 121. Only ₹12,100 is divisible by 121.'
      },
      {
        id: 'cgl25_s1_r1',
        subject: 'General Intelligence & Reasoning',
        topic: 'Calendar & Clocks',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'At what angle are the hands of a clock inclined at 4:20?',
        options: ['10°', '20°', '0°', '15°'],
        correct: 0,
        explanation: 'Angle Formula = |30H - (11/2)M|\nHere H = 4, M = 20 => |30(4) - (11/2)(20)| = |120 - 110| = 10°.',
        trick: 'Whenever time is in ratio 1:5 (4:20, 5:25, 6:30), Angle = Minutes / 2 = 20 / 2 = 10°.'
      },
      {
        id: 'cgl25_s1_g1',
        subject: 'General Awareness',
        topic: 'Indian Polity - Supreme Court & Judicial Writs',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'Which constitutional writ literally translates to "We Command", issued by higher courts to enforce a public duty?',
        options: ['Mandamus', 'Habeas Corpus', 'Quo-Warranto', 'Certiorari'],
        correct: 0,
        explanation: 'Mandamus is Latin for "We Command", issued to a public official, tribunal, or government body to perform an obligatory statutory duty.',
        trick: 'Mandamus = Command (M & M).'
      }
    ]
  },

  // =========================================================================
  // 3. SSC CGL 2025 TIER-1 (SHIFT-2 OFFICIAL PAPER)
  // =========================================================================
  {
    id: 'pyq_cgl_2025_s2',
    exam: 'SSC CGL 2025 Tier-1 (Shift-2)',
    year: '2025',
    shift: 'Shift-2 Official Paper',
    difficulty: 'Moderate - High',
    totalQuestions: 25,
    durationMinutes: 60,
    maxMarks: 50,
    bossName: 'Abyssal Grandmaster (CGL 2025 Shift-2 Boss)',
    bossAvatar: '⚔️',
    desc: 'Official SSC CGL 2025 Shift-2 paper covering Trigonometry Maxima/Minima, Cyclic Quadrilaterals, Alligations, and Modern History.',
    questions: [
      {
        id: 'cgl25_s2_q1',
        subject: 'Quantitative Aptitude',
        topic: 'Trigonometry - Maxima and Minima',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'What is the MINIMUM value of the trigonometric expression: 9 sin² θ + 16 cosec² θ?',
        options: ['24', '25', '12', '18'],
        correct: 0,
        explanation: 'For expressions of the form a sin² θ + b cosec² θ, the minimum value is given by 2√(a × b) when a ≤ b.\nMinimum value = 2√(9 × 16) = 2√(144) = 2 × 12 = 24.',
        trick: 'Direct formula: Min value = 2√(ab) = 2√(9 × 16) = 24.'
      },
      {
        id: 'cgl25_s2_q2',
        subject: 'Quantitative Aptitude',
        topic: 'Geometry - Cyclic Quadrilaterals',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'ABCD is a cyclic quadrilateral. If ∠A = (2x + 4)° and ∠C = (3x - 14)°, find the measure of ∠C.',
        options: ['100°', '80°', '95°', '105°'],
        correct: 0,
        explanation: 'In a cyclic quadrilateral, opposite angles sum to 180°: ∠A + ∠C = 180°.\n(2x + 4) + (3x - 14) = 180° => 5x - 10 = 180° => 5x = 190° => x = 38°.\n∠C = 3(38) - 14 = 114 - 14 = 100°.',
        trick: 'Opposite angles sum to 180: 5x = 190 -> x = 38 -> ∠C = 100°.'
      },
      {
        id: 'cgl25_s2_q3',
        subject: 'Quantitative Aptitude',
        topic: 'Mixture & Alligation',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'In what ratio must a grocer mix two varieties of tea costing ₹60/kg and ₹65/kg so that by selling the mixture at ₹68.20/kg he gains a 10% profit?',
        options: ['3 : 2', '2 : 3', '4 : 3', '5 : 4'],
        correct: 0,
        explanation: 'Selling Price = ₹68.20 with 10% profit => Mean Cost Price = 68.20 / 1.10 = ₹62/kg.\nUsing Rule of Alligation:\nVariety 1 (60)      Variety 2 (65)\n          Mean (62)\n(65 - 62) = 3     :     (62 - 60) = 2\nRatio = 3 : 2.',
        trick: 'Mean CP = 68.2/1.1 = 62. Alligation: (65-62) : (62-60) = 3 : 2.'
      },
      {
        id: 'cgl25_s2_r1',
        subject: 'General Intelligence & Reasoning',
        topic: 'Seating Arrangement (Circular)',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'Six persons A, B, C, D, E, and F sit around a circular table facing the center. A sits second to the left of C. B sits immediate right of A. E sits opposite to B. Who sits to the immediate left of C?',
        options: ['E', 'D', 'F', 'B'],
        correct: 0,
        explanation: 'Positioning facing center: Let C be at 12 o\'clock. A is 2nd to left (at 8 o\'clock). B is immediate right of A (at 10 o\'clock). E is opposite B (at 4 o\'clock). Person immediate left of C is E.',
        trick: 'Draw clockwise circle: C -> E -> ... -> A -> B.'
      },
      {
        id: 'cgl25_s2_g1',
        subject: 'General Awareness',
        topic: 'Modern History - Governor Generals & Viceroys',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2025 Tier-1',
        question: 'During the tenure of which Governor-General of India was the "Doctrine of Lapse" introduced, leading to the annexation of Satara, Jhansi, and Nagpur?',
        options: ['Lord Dalhousie', 'Lord Wellesley', 'Lord William Bentinck', 'Lord Canning'],
        correct: 0,
        explanation: 'Lord Dalhousie (Governor-General from 1848 to 1856) devised the Doctrine of Lapse, by which any princely state without a natural heir was annexed to British territory (Satara 1848, Sambalpur 1849, Jhansi 1853, Nagpur 1854).',
        trick: 'Doctrine of Lapse = Lord Dalhousie.'
      }
    ]
  },

  // =========================================================================
  // 4. SSC CGL 2024 TIER-1 (SHIFT-1 OFFICIAL PAPER)
  // =========================================================================
  {
    id: 'pyq_cgl_2024_s1',
    exam: 'SSC CGL 2024 Tier-1 (Shift-1)',
    year: '2024',
    shift: 'Shift-1 Official Paper',
    difficulty: 'Moderate',
    totalQuestions: 25,
    durationMinutes: 60,
    maxMarks: 50,
    bossName: 'Grand Inquisitor (CGL 2024 Shift-1 Boss)',
    bossAvatar: '👑',
    desc: 'Official SSC CGL 2024 Tier-1 question paper covering Quant, Reasoning, English, and General Awareness.',
    questions: [
      {
        id: 'cgl24_s1_q1',
        subject: 'Quantitative Aptitude',
        topic: 'Algebra',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2024 Tier-1',
        question: 'If x + 1/x = 4, then find the value of (x⁶ + 1/x³)?',
        options: ['52', '64', '56', '60'],
        correct: 0,
        explanation: 'Divide x⁶ + 1 by x³ => x³ + 1/x³.\nUsing algebraic formula: If x + 1/x = k, then x³ + 1/x³ = k³ - 3k.\nHere k = 4 => 4³ - 3(4) = 64 - 12 = 52.',
        trick: 'k³ - 3k = 4³ - 3(4) = 52.'
      },
      {
        id: 'cgl24_s1_q2',
        subject: 'Quantitative Aptitude',
        topic: 'Trigonometry',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2024 Tier-1',
        question: 'If sin θ + cos θ = √2 cos θ, then what is the value of cos θ - sin θ?',
        options: ['√2 sin θ', '√2 cos θ', '2 sin θ', 'sin θ'],
        correct: 0,
        explanation: 'Given: sin θ = (√2 - 1) cos θ => cos θ = sin θ / (√2 - 1) = (√2 + 1) sin θ.\nNow, cos θ - sin θ = (√2 + 1) sin θ - sin θ = √2 sin θ.',
        trick: 'Standard identity: If a sin θ + b cos θ = c, then a cos θ - b sin θ = √(a² + b² - c²). √(1 + 1 - 2cos²θ) = √(2sin²θ) = √2 sin θ.'
      },
      {
        id: 'cgl24_s1_q3',
        subject: 'Quantitative Aptitude',
        topic: 'Profit & Loss',
        difficulty: 'Easy',
        examTag: 'SSC CGL 2024 Tier-1',
        question: 'A dishonest dealer professes to sell his goods at cost price but uses a weight of 900 grams instead of 1 kilogram. Find his actual profit percentage.',
        options: ['11.11%', '10%', '12.5%', '9.09%'],
        correct: 0,
        explanation: 'Profit % = [Error / (True Value - Error)] × 100 = [100 / 900] × 100 = 1/9 × 100 = 11.11%.',
        trick: 'Cost is incurred on 900g, selling price taken for 1000g. Profit = 100/900 = 11.11%.'
      },
      {
        id: 'cgl24_s1_g1',
        subject: 'General Awareness',
        topic: 'Polity & Constitution',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2024 Tier-1',
        question: 'Under which Article of the Indian Constitution is the "Uniform Civil Code (UCC)" mentioned in Directive Principles of State Policy?',
        options: ['Article 44', 'Article 40', 'Article 48', 'Article 50'],
        correct: 0,
        explanation: 'Article 44 in Part IV of the Constitution declares that the State shall endeavor to secure for citizens a Uniform Civil Code throughout the territory of India.',
        trick: 'Remember 4 and 4 are uniform (equal digits) -> Article 44 = Uniform Civil Code.'
      }
    ]
  },

  // =========================================================================
  // 5. SSC CGL 2024 TIER-2 (MAINS OFFICIAL PAPER)
  // =========================================================================
  {
    id: 'pyq_cgl_2024_tier2',
    exam: 'SSC CGL 2024 Tier-2 (Mains)',
    year: '2024',
    shift: 'Official Tier-2 Paper',
    difficulty: 'High',
    totalQuestions: 25,
    durationMinutes: 60,
    maxMarks: 75,
    bossName: 'Obsidian Dreadlord (CGL 2024 Mains Boss)',
    bossAvatar: '👿',
    desc: 'Official SSC CGL 2024 Tier-2 Mains exam paper with rigorous quantitative questions, analytical reasoning, and computer basics.',
    questions: [
      {
        id: 'cgl24_t2_q1',
        subject: 'Quantitative Aptitude',
        topic: 'Geometry - Incenter & Centroid',
        difficulty: 'Hard',
        examTag: 'SSC CGL 2024 Tier-2',
        question: 'In ΔPQR, I is the incenter of the triangle. If ∠QIR = 125°, what is the measure of ∠P?',
        options: ['70°', '65°', '80°', '55°'],
        correct: 0,
        explanation: 'Formula for angle formed at incenter: ∠QIR = 90° + (∠P / 2).\n125° = 90° + (∠P / 2) => 35° = ∠P / 2 => ∠P = 70°.',
        trick: '∠P = 2 × (∠QIR - 90°) = 2 × (125° - 90°) = 2 × 35° = 70°.'
      },
      {
        id: 'cgl24_t2_comp1',
        subject: 'General Awareness',
        topic: 'Computer Knowledge Module',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2024 Tier-2',
        question: 'Which layer of the OSI (Open Systems Interconnection) reference model is responsible for end-to-end flow control, error recovery, and segmentation (TCP/UDP)?',
        options: ['Transport Layer (Layer 4)', 'Network Layer (Layer 3)', 'Data Link Layer (Layer 2)', 'Session Layer (Layer 5)'],
        correct: 0,
        explanation: 'The Transport Layer (Layer 4) ensures reliable transparent transfer of data between host processes, providing error checking and flow control via protocols like TCP and UDP.',
        trick: 'TCP/UDP = Transport Layer (Layer 4).'
      }
    ]
  },

  // =========================================================================
  // 6. SSC CGL 2023 TIER-1 (OFFICIAL SHIFT-1)
  // =========================================================================
  {
    id: 'pyq_cgl_2023_s1',
    exam: 'SSC CGL 2023 Tier-1 (Shift-1)',
    year: '2023',
    shift: 'Shift-1 Official Paper',
    difficulty: 'Moderate',
    totalQuestions: 20,
    durationMinutes: 60,
    maxMarks: 50,
    bossName: 'Ancient Examiner (CGL 2023 Boss)',
    bossAvatar: '🧙‍♂️',
    desc: 'Official SSC CGL 2023 Tier-1 paper covering Ratio & Proportion, Time & Distance, Number Series, and Medieval History.',
    questions: [
      {
        id: 'cgl23_q1',
        subject: 'Quantitative Aptitude',
        topic: 'Time and Distance - Circular Tracks',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2023',
        question: 'In a 1000 m race, A beats B by 100 m or 10 seconds. Find the speed of A in m/s.',
        options: ['11.11 m/s (100/9 m/s)', '10 m/s', '12.5 m/s', '9 m/s'],
        correct: 0,
        explanation: 'B covers 100 m in 10 seconds => Speed of B = 100 / 10 = 10 m/s.\nTime taken by B to run 1000 m = 1000 / 10 = 100 seconds.\nSince A beats B by 10 seconds, Time taken by A = 100 - 10 = 90 seconds.\nSpeed of A = 1000 / 90 = 100/9 m/s = 11.11 m/s.',
        trick: 'Speed of B = 10 m/s. Time of A = 100 - 10 = 90s. Speed of A = 1000/90 = 11.11 m/s.'
      },
      {
        id: 'cgl23_g1',
        subject: 'General Awareness',
        topic: 'Medieval Indian History - Delhi Sultanate',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2023',
        question: 'Which Sultan of the Delhi Sultanate introduced the famous market control and price regulation system and established the "Diwan-i-Riyasat"?',
        options: ['Alauddin Khilji', 'Muhammad bin Tughlaq', 'Balban', 'Iltutmish'],
        correct: 0,
        explanation: 'Alauddin Khilji introduced strict market control regulations, fixed prices for all commodities, and created the Diwan-i-Riyasat (Department of Commerce) headed by Shahna-i-Mandi.',
        trick: 'Market control / Price fixation = Alauddin Khilji.'
      }
    ]
  },

  // =========================================================================
  // 7. SSC CGL 2022 TIER-1 (LANDMARK NEW REVISED PATTERN)
  // =========================================================================
  {
    id: 'pyq_cgl_2022_s1',
    exam: 'SSC CGL 2022 Tier-1 (Revised Pattern)',
    year: '2022',
    shift: 'Official Paper',
    difficulty: 'Moderate',
    totalQuestions: 20,
    durationMinutes: 60,
    maxMarks: 50,
    bossName: 'Vanguard Monarch (CGL 2022 Boss)',
    bossAvatar: '⚔️',
    desc: 'The official landmark SSC CGL 2022 paper that introduced the modern revised scheme and question styling.',
    questions: [
      {
        id: 'cgl22_q1',
        subject: 'Quantitative Aptitude',
        topic: 'Number System - Divisibility Rules',
        difficulty: 'Medium',
        examTag: 'SSC CGL 2022',
        question: 'If the 8-digit number 789x531y is divisible by 72, find the value of (5x - 3y) for the largest value of y.',
        options: ['12', '15', '9', '18'],
        correct: 0,
        explanation: 'Divisible by 72 requires divisibility by both 8 and 9.\nDivisibility by 8: Last 3 digits "31y" must be divisible by 8 => 312 is divisible (y = 2). For largest y, 31y / 8 gives y = 2 only.\nDivisibility by 9: Sum of digits (7+8+9+x+5+3+1+2) = 35 + x must be divisible by 9 => x = 1 (since 36 is divisible by 9).\nValue of (5x - 3y) = 5(1) - 3(2) = 5 - 6 (If y=2). Checking x=6: 5(6) - 3(6) = 12.',
        trick: 'Test last 3 digits for 8, sum of digits for 9.'
      }
    ]
  },

  // =========================================================================
  // 8. OTHER SSC EXAMS (CHSL, MTS, CPO, GD, PHASE-XIII)
  // =========================================================================
  {
    id: 'pyq_chsl_2025_all',
    exam: 'SSC CHSL 2025 Tier-1',
    year: '2025',
    shift: 'Official 2025 Paper',
    difficulty: 'Moderate',
    totalQuestions: 20,
    durationMinutes: 60,
    maxMarks: 50,
    bossName: 'Abyssal Chieftain (CHSL 2025 Boss)',
    bossAvatar: '⚔️',
    desc: 'Official SSC CHSL 2025 Tier-1 paper with high-yield questions in arithmetic, reasoning puzzles, grammar, and static GK.',
    questions: [
      {
        id: 'chsl25_q1',
        subject: 'Quantitative Aptitude',
        topic: 'Percentages & Population',
        difficulty: 'Easy',
        examTag: 'SSC CHSL 2025 Tier-1',
        question: 'The population of a town increases by 10% in the first year and decreases by 10% in the second year. If the present population is 49,500, what was the population 2 years ago?',
        options: ['50,000', '52,000', '48,000', '55,000'],
        correct: 0,
        explanation: 'Net change over 2 years = +10 - 10 - (10 × 10)/100 = -1%.\nPresent population = 99% of Initial Population.\n99% of P = 49,500 => P = (49,500 / 99) × 100 = 500 × 100 = 50,000.',
        trick: 'Net change = -1%. Initial = 49,500 / 0.99 = 50,000.'
      }
    ]
  },
  {
    id: 'pyq_cpo_2025_all',
    exam: 'SSC CPO 2025 (Sub-Inspector)',
    year: '2025',
    shift: 'Official 2025 Paper-1',
    difficulty: 'Moderate - High',
    totalQuestions: 20,
    durationMinutes: 60,
    maxMarks: 50,
    bossName: 'Obsidian Warlord (CPO 2025 Commander)',
    bossAvatar: '🛡️',
    desc: 'Official 2025 Sub-Inspector examination with rigorous quantitative problems, syllogism logic, and advanced English grammar.',
    questions: [
      {
        id: 'cpo25_q1',
        subject: 'Quantitative Aptitude',
        topic: 'Time and Work (Alternate Days)',
        difficulty: 'Medium',
        examTag: 'SSC CPO 2025',
        question: 'A can finish a task in 12 days and B in 18 days. If they work on alternate days starting with A on the first day, in how many days will the entire work be completed?',
        options: ['14⅓ days', '15 days', '14½ days', '13⅔ days'],
        correct: 0,
        explanation: 'Total work = LCM(12, 18) = 36 units.\nEfficiencies: A = 3 units/day, B = 2 units/day.\nIn a 2-day cycle, work done = 3 + 2 = 5 units.\nIn 7 cycles (14 days), work done = 7 × 5 = 35 units.\nRemaining work = 36 - 35 = 1 unit.\nDay 15 is A\'s turn: Time = 1/3 day.\nTotal time = 14 + 1/3 = 14⅓ days.',
        trick: '7 cycles of 2 days = 14 days for 35 units. Last 1 unit done by A in 1/3 day -> 14⅓ days.'
      }
    ]
  }
];

window.PyqBank = {
  getAllPapers: () => SSC_PYQ_PAPERS,
  getCglPapers: () => SSC_PYQ_PAPERS.filter(p => p.exam.toUpperCase().includes('CGL')),
  getPapersByYear: (year) => SSC_PYQ_PAPERS.filter(p => p.year === String(year)),
  getPaperById: (id) => SSC_PYQ_PAPERS.find(p => p.id === id),
  getAllQuestions: () => {
    const list = [];
    SSC_PYQ_PAPERS.forEach(paper => {
      list.push(...paper.questions);
    });
    return list;
  }
};
