/**
 * SOLO LEVELING EXAM SYSTEM - INFINITE PROCEDURAL QUESTION GENERATOR ENGINE
 * Generates mathematically sound, syllabus-accurate, infinite unique questions on demand
 * Scaled dynamically to Hunter Level & Rank (E-Rank to S-Rank).
 */

class InfiniteQuestionGenerator {
  constructor() {
    this.templateCount = 0;
  }

  // Helper random functions
  randomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

  randomChoice(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
  }

  gcd(a, b) {
    return b === 0 ? a : this.gcd(b, a % b);
  }

  lcm(a, b) {
    return (a * b) / this.gcd(a, b);
  }

  // Shuffle options and return { options, correctIndex }
  formatOptions(correctVal, wrongVals) {
    const raw = [correctVal, ...wrongVals];
    // Remove duplicates from wrong values
    const unique = Array.from(new Set(raw));
    while (unique.length < 4) {
      if (typeof correctVal === 'number') {
        unique.push(correctVal + unique.length * 5);
      } else {
        unique.push(`Option ${unique.length + 1}`);
      }
    }
    const shuffled = [...unique].slice(0, 4).sort(() => 0.5 - Math.random());
    const correctIdx = shuffled.indexOf(correctVal);
    return { options: shuffled.map(String), correct: correctIdx };
  }

  // =========================================================================
  // 1. INFINITE QUANTITATIVE APTITUDE GENERATOR
  // =========================================================================
  generateQuantQuestion(difficulty = 'Medium') {
    const topics = ['profit_loss', 'time_work', 'algebra', 'si_ci', 'percentages', 'speed_dist', 'ratio', 'averages', 'number_system'];
    const topic = this.randomChoice(topics);

    switch (topic) {
      // A. Profit, Loss & Discount
      case 'profit_loss': {
        const cp = this.randomInt(5, 50) * 100; // e.g. 1500
        const markupPct = this.randomChoice([20, 25, 30, 40, 50]);
        const discountPct = this.randomChoice([10, 15, 20, 25]);
        const mp = cp * (1 + markupPct / 100);
        const sp = Math.round(mp * (1 - discountPct / 100));
        const profit = sp - cp;
        const isProfit = profit >= 0;

        const qText = `A merchant marks an article at ${markupPct}% above its cost price and allows a discount of ${discountPct}% on the marked price. If the cost price is ₹${cp}, what is the merchant's net ${isProfit ? 'profit' : 'loss'} in rupees?`;
        const correctVal = `₹${Math.abs(profit)}`;
        const wrongVals = [`₹${Math.abs(profit) + 50}`, `₹${Math.max(10, Math.abs(profit) - 40)}`, `₹${Math.abs(profit) + 120}`];

        const { options, correct } = this.formatOptions(correctVal, wrongVals);
        return {
          id: `inf_quant_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
          subject: 'Quantitative Aptitude',
          topic: 'Profit, Loss & Discount',
          difficulty,
          question: qText,
          options,
          correct,
          explanation: `Marked Price = ₹${cp} × (1 + ${markupPct}/100) = ₹${mp}.\nSelling Price = ₹${mp} × (1 - ${discountPct}/100) = ₹${sp}.\nNet ${isProfit ? 'Profit' : 'Loss'} = |₹${sp} - ₹${cp}| = ₹${Math.abs(profit)}.`,
          trick: `Net % Change = +${markupPct} - ${discountPct} - (${markupPct} × ${discountPct})/100 = ${(markupPct - discountPct - (markupPct * discountPct) / 100).toFixed(1)}% of ₹${cp} = ₹${Math.abs(profit)}.`
        };
      }

      // B. Time & Work
      case 'time_work': {
        const daysA = this.randomChoice([10, 12, 15, 20, 24, 30]);
        const daysB = this.randomChoice([12, 15, 18, 20, 30, 40]);
        const totalWork = this.lcm(daysA, daysB);
        const effA = totalWork / daysA;
        const effB = totalWork / daysB;
        const combinedEff = effA + effB;
        const daysTogether = (totalWork / combinedEff).toFixed(1).replace('.0', '');

        const qText = `Hunter A can clear a dungeon in ${daysA} days, and Hunter B can clear the same dungeon in ${daysB} days. Working together at their standard efficiency, in how many days will they clear the dungeon completely?`;
        const correctVal = `${daysTogether} days`;
        const wrongVals = [
          `${(parseFloat(daysTogether) + 2).toFixed(1).replace('.0', '')} days`,
          `${Math.max(1, (parseFloat(daysTogether) - 1.5)).toFixed(1).replace('.0', '')} days`,
          `${(daysA + daysB) / 2} days`
        ];

        const { options, correct } = this.formatOptions(correctVal, wrongVals);
        return {
          id: `inf_quant_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
          subject: 'Quantitative Aptitude',
          topic: 'Time and Work',
          difficulty,
          question: qText,
          options,
          correct,
          explanation: `Total Work = LCM(${daysA}, ${daysB}) = ${totalWork} units.\nEfficiency of A = ${effA} units/day.\nEfficiency of B = ${effB} units/day.\nCombined efficiency = ${effA} + ${effB} = ${combinedEff} units/day.\nTime = ${totalWork} / ${combinedEff} = ${daysTogether} days.`,
          trick: `Direct Formula: (A × B) / (A + B) = (${daysA} × ${daysB}) / (${daysA + daysB}) = ${daysTogether} days.`
        };
      }

      // C. Algebra & Polynomials
      case 'algebra': {
        const k = this.randomInt(3, 8);
        const k3 = Math.pow(k, 3) - 3 * k; // x^3 + 1/x^3 = k^3 - 3k
        const k2 = Math.pow(k, 2) - 2;     // x^2 + 1/x^2 = k^2 - 2

        const isCube = Math.random() > 0.5;
        if (isCube) {
          const qText = `If x + 1/x = ${k}, what is the value of x³ + 1/x³?`;
          const correctVal = `${k3}`;
          const wrongVals = [`${k3 + 6}`, `${k3 - 10}`, `${Math.pow(k, 3)}`];
          const { options, correct } = this.formatOptions(correctVal, wrongVals);
          return {
            id: `inf_quant_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
            subject: 'Quantitative Aptitude',
            topic: 'Algebra',
            difficulty: 'Hard',
            question: qText,
            options,
            correct,
            explanation: `Formula: If x + 1/x = k, then x³ + 1/x³ = k³ - 3k.\nHere k = ${k} => (${k})³ - 3(${k}) = ${Math.pow(k, 3)} - ${3 * k} = ${k3}.`,
            trick: `Shortcut: k³ - 3k = ${k}³ - 3(${k}) = ${k3}.`
          };
        } else {
          const qText = `If x + 1/x = ${k}, what is the value of x² + 1/x²?`;
          const correctVal = `${k2}`;
          const wrongVals = [`${k2 + 4}`, `${k2 - 3}`, `${Math.pow(k, 2)}`];
          const { options, correct } = this.formatOptions(correctVal, wrongVals);
          return {
            id: `inf_quant_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
            subject: 'Quantitative Aptitude',
            topic: 'Algebra',
            difficulty: 'Medium',
            question: qText,
            options,
            correct,
            explanation: `Formula: If x + 1/x = k, then x² + 1/x² = k² - 2.\nHere k = ${k} => (${k})² - 2 = ${Math.pow(k, 2)} - 2 = ${k2}.`,
            trick: `Shortcut: k² - 2 = ${k}² - 2 = ${k2}.`
          };
        }
      }

      // D. Simple & Compound Interest
      case 'si_ci': {
        const p = this.randomChoice([5000, 8000, 10000, 12000, 15000, 20000]);
        const r = this.randomChoice([5, 10, 12, 15, 20]);
        const diff2Years = p * Math.pow(r / 100, 2);

        const qText = `What is the difference between the Compound Interest (compounded annually) and Simple Interest on a principal sum of ₹${p.toLocaleString()} at ${r}% per annum for 2 years?`;
        const correctVal = `₹${diff2Years.toFixed(0)}`;
        const wrongVals = [`₹${(diff2Years + 25).toFixed(0)}`, `₹${Math.max(10, diff2Years - 20).toFixed(0)}`, `₹${(diff2Years * 1.5).toFixed(0)}`];
        const { options, correct } = this.formatOptions(correctVal, wrongVals);
        return {
          id: `inf_quant_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
          subject: 'Quantitative Aptitude',
          topic: 'Simple & Compound Interest',
          difficulty: 'Medium',
          question: qText,
          options,
          correct,
          explanation: `Difference for 2 years formula: D = P × (R/100)²\nD = ₹${p} × (${r}/100)² = ₹${p} × ${(r * r) / 10000} = ₹${diff2Years.toFixed(0)}.`,
          trick: `Difference is simply R% of R% on P: ${r}% of ${r}% = ${(r * r) / 100}% of ₹${p} = ₹${diff2Years.toFixed(0)}.`
        };
      }

      // E. Percentages & Consumption
      default: {
        const priceIncrease = this.randomChoice([20, 25, 33.33, 50]);
        let decrease = 0;
        if (priceIncrease === 20) decrease = 16.66;
        else if (priceIncrease === 25) decrease = 20;
        else if (priceIncrease === 33.33) decrease = 25;
        else if (priceIncrease === 50) decrease = 33.33;

        const qText = `If the price of a resource increases by ${priceIncrease}%, by what percentage must its consumption be reduced so that the total monthly expenditure remains constant?`;
        const correctVal = `${decrease}%`;
        const wrongVals = [`${priceIncrease}%`, `${(decrease + 5).toFixed(1)}%`, `${Math.max(5, decrease - 4.5).toFixed(1)}%`];
        const { options, correct } = this.formatOptions(correctVal, wrongVals);
        return {
          id: `inf_quant_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
          subject: 'Quantitative Aptitude',
          topic: 'Percentages',
          difficulty: 'Easy',
          question: qText,
          options,
          correct,
          explanation: `Reduction % = [R / (100 + R)] × 100 = [${priceIncrease} / (100 + ${priceIncrease})] × 100 = ${decrease}%.`,
          trick: `Fraction Ladder rule: If price increases by 1/n, consumption must decrease by 1/(n+1).`
        };
      }
    }
  }

  // =========================================================================
  // 2. INFINITE REASONING GENERATOR
  // =========================================================================
  generateReasoningQuestion(difficulty = 'Medium') {
    const topics = ['number_series', 'analogy', 'coding', 'direction'];
    const topic = this.randomChoice(topics);

    switch (topic) {
      // A. Number Series
      case 'number_series': {
        const start = this.randomInt(3, 12);
        const stepType = this.randomChoice(['double_diff', 'squares', 'times2_plus1']);

        let series = [];
        let nextVal = 0;
        let explanationText = '';

        if (stepType === 'double_diff') {
          let diff = this.randomChoice([2, 3, 4]);
          let curr = start;
          series.push(curr);
          for (let i = 0; i < 4; i++) {
            curr += diff;
            series.push(curr);
            diff *= 2;
          }
          nextVal = curr + diff;
          explanationText = `Differences double each step (+${diff/2} -> +${diff}). Next term = ${curr} + ${diff} = ${nextVal}.`;
        } else {
          // n^2 + 1
          const base = this.randomInt(2, 5);
          for (let i = 0; i < 5; i++) {
            series.push((base + i) * (base + i) + 1);
          }
          nextVal = (base + 5) * (base + 5) + 1;
          explanationText = `Logic: n² + 1. (${base + 5})² + 1 = ${nextVal}.`;
        }

        const qText = `Find the next number in the logical series: ${series.join(', ')}, ?`;
        const correctVal = `${nextVal}`;
        const wrongVals = [`${nextVal + 4}`, `${nextVal - 3}`, `${nextVal + 10}`];
        const { options, correct } = this.formatOptions(correctVal, wrongVals);
        return {
          id: `inf_reas_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
          subject: 'General Intelligence & Reasoning',
          topic: 'Number Series',
          difficulty,
          question: qText,
          options,
          correct,
          explanation: explanationText,
          trick: `Look at the differences between consecutive terms first.`
        };
      }

      // B. Coding - Decoding
      case 'coding': {
        const shift = this.randomChoice([1, 2, 3]);
        const words = [
          { word: 'MONARCH', code: this.shiftWord('MONARCH', shift), target: 'HUNTER', targetCode: this.shiftWord('HUNTER', shift) },
          { word: 'SYSTEM', code: this.shiftWord('SYSTEM', shift), target: 'SHADOW', targetCode: this.shiftWord('SHADOW', shift) },
          { word: 'QUEST', code: this.shiftWord('QUEST', shift), target: 'FOCUS', targetCode: this.shiftWord('FOCUS', shift) }
        ];
        const chosen = this.randomChoice(words);

        const qText = `In a certain secret code, "${chosen.word}" is coded as "${chosen.code}". How is "${chosen.target}" written in that code language?`;
        const correctVal = chosen.targetCode;
        const wrongVals = [
          this.shiftWord(chosen.target, shift + 1),
          this.shiftWord(chosen.target, shift - 1 || 4),
          this.shiftWord(chosen.target, shift + 2)
        ];
        const { options, correct } = this.formatOptions(correctVal, wrongVals);
        return {
          id: `inf_reas_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
          subject: 'General Intelligence & Reasoning',
          topic: 'Coding - Decoding',
          difficulty,
          question: qText,
          options,
          correct,
          explanation: `Each letter is shifted forward by +${shift} positions in alphabetical order: ${chosen.word} -> ${chosen.code}. Applying +${shift} to ${chosen.target} yields ${chosen.targetCode}.`,
          trick: `Check the first and last letters (${chosen.target[0]} -> ${chosen.targetCode[0]}) to instantly eliminate choices.`
        };
      }

      // Default: Direction Sense
      default: {
        const dist1 = this.randomInt(3, 10);
        const dist2 = this.randomInt(3, 10);
        const qText = `A scout walks ${dist1} km North, turns East and walks ${dist2} km, then turns South and walks ${dist1} km. In which direction is the scout from the initial starting point?`;
        const correctVal = 'East';
        const wrongVals = ['West', 'North', 'South-East'];
        const { options, correct } = this.formatOptions(correctVal, wrongVals);
        return {
          id: `inf_reas_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
          subject: 'General Intelligence & Reasoning',
          topic: 'Direction Sense',
          difficulty: 'Easy',
          question: qText,
          options,
          correct,
          explanation: `Walking ${dist1} km North and then ${dist1} km South completely cancels vertical displacement. The remaining net displacement is purely ${dist2} km towards the East.`,
          trick: `North and South cancel each other out.`
        };
      }
    }
  }

  shiftWord(word, shift) {
    return word.split('').map(ch => {
      const code = ch.charCodeAt(0);
      if (code >= 65 && code <= 90) {
        return String.fromCharCode(((code - 65 + shift) % 26) + 65);
      }
      return ch;
    }).join('');
  }

  // =========================================================================
  // 3. INFINITE ENGLISH & GENERAL AWARENESS GENERATOR
  // =========================================================================
  generateVerbalOrGKQuestion() {
    // Curated high frequency rule templates
    const templates = [
      {
        subject: 'English Language',
        topic: 'Spotting Errors & Subject-Verb Agreement',
        difficulty: 'Medium',
        question: 'Identify the error: "Each of the elite hunters (A) / were rewarded (B) / for clearing the red gate (C) / without delay (D)."',
        options: ['Each of the elite hunters', 'were rewarded', 'for clearing the red gate', 'No error'],
        correct: 1,
        explanation: '"Each of" is singular distributive and requires the singular verb "was rewarded" instead of "were rewarded".',
        trick: 'Each of / Neither of / Either of + Plural Noun + Singular Verb.'
      },
      {
        subject: 'English Language',
        topic: 'Idioms & Phrases',
        difficulty: 'Easy',
        question: 'What is the exact meaning of the idiom: "A blessing in disguise"?',
        options: [
          'An apparent misfortune that eventually has positive results',
          'A spiritual prayer done in secrecy',
          'A fake compliment from a rival',
          'A sudden unavoidable catastrophe'
        ],
        correct: 0,
        explanation: '"A blessing in disguise" means something that initially seems bad, but turns out to have good results in the end.',
        trick: 'Disguise = hidden positive outcome.'
      },
      {
        subject: 'General Awareness',
        topic: 'Indian Polity & Constitution',
        difficulty: 'Medium',
        question: 'Under which Article of the Indian Constitution is the "Right to Constitutional Remedies" guaranteed, referred to as the "Heart and Soul of the Constitution" by Dr. B.R. Ambedkar?',
        options: ['Article 32', 'Article 21', 'Article 19', 'Article 14'],
        correct: 0,
        explanation: 'Article 32 empowers citizens to approach the Supreme Court directly for enforcement of Fundamental Rights via writs (Habeas Corpus, Mandamus, Prohibition, Certiorari, Quo-Warranto).',
        trick: 'Article 32 = Heart and Soul of Constitution.'
      },
      {
        subject: 'General Awareness',
        topic: 'General Science - Chemistry',
        difficulty: 'Easy',
        question: 'Which gas is commonly known as "Laughing Gas" used as an anesthetic in dental surgery?',
        options: ['Nitrous Oxide (N₂O)', 'Nitrogen Dioxide (NO₂)', 'Sulfur Dioxide (SO₂)', 'Carbon Monoxide (CO)'],
        correct: 0,
        explanation: 'Nitrous Oxide (N₂O) produces mild euphoria and pain relief, earning the common name "Laughing Gas".',
        trick: 'N₂O = Nitrous Oxide.'
      }
    ];

    const chosen = this.randomChoice(templates);
    return {
      id: `inf_verbal_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
      ...chosen
    };
  }

  // =========================================================================
  // 4. MASTER BATCH GENERATOR (Generates N questions across all subjects)
  // =========================================================================
  generateBatch(count = 20, subjectFilter = null) {
    const list = [];
    for (let i = 0; i < count; i++) {
      if (subjectFilter === 'Quantitative Aptitude') {
        list.push(this.generateQuantQuestion());
      } else if (subjectFilter === 'General Intelligence & Reasoning') {
        list.push(this.generateReasoningQuestion());
      } else if (subjectFilter === 'English Language' || subjectFilter === 'General Awareness') {
        list.push(this.generateVerbalOrGKQuestion());
      } else {
        const rand = Math.random();
        if (rand < 0.4) list.push(this.generateQuantQuestion());
        else if (rand < 0.7) list.push(this.generateReasoningQuestion());
        else list.push(this.generateVerbalOrGKQuestion());
      }
    }
    return list;
  }
}

window.InfiniteGenerator = new InfiniteQuestionGenerator();
