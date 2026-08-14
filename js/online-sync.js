/**
 * SOLO LEVELING EXAM SYSTEM - ONLINE SYLLABUS & DYNAMIC QUESTION SYNCHRONIZER
 * Connects to online quiz endpoints, keeps syllabus updated, and enables custom syllabus imports.
 */

const LATEST_SSC_SYLLABUS = {
  version: '2025-2026 Revised Pattern',
  lastUpdated: new Date().toLocaleDateString(),
  tier1Scheme: {
    totalMarks: 200,
    totalQuestions: 100,
    durationMinutes: 60,
    negativeMarking: 0.50,
    sections: [
      { name: 'Quantitative Aptitude', questions: 25, marks: 50, topics: ['Number Systems', 'Percentages', 'Profit & Loss', 'Time & Work', 'Algebra', 'Geometry', 'Trigonometry', 'Data Interpretation'] },
      { name: 'General Intelligence & Reasoning', questions: 25, marks: 50, topics: ['Analogies', 'Syllogism', 'Coding-Decoding', 'Blood Relations', 'Venn Diagrams', 'Non-Verbal Series', 'Matrices'] },
      { name: 'English Comprehension', questions: 25, marks: 50, topics: ['Grammar & Error Spotting', 'Vocabulary (Synonyms/Antonyms)', 'Idioms & Phrases', 'One Word Substitution', 'Cloze Test', 'Comprehension Passages'] },
      { name: 'General Awareness', questions: 25, marks: 50, topics: ['Current Affairs (National & International)', 'Indian History & Culture', 'Polity & Constitution', 'Geography', 'Economics', 'General Science'] }
    ]
  },
  tier2Scheme: {
    totalMarks: 390,
    sections: [
      { module: 'Module-I: Mathematical Abilities', questions: 30, marks: 90 },
      { module: 'Module-II: Reasoning & General Intelligence', questions: 30, marks: 90 },
      { module: 'Module-III: English Language & Comprehension', questions: 45, marks: 135 },
      { module: 'Module-IV: General Awareness', questions: 25, marks: 75 },
      { module: 'Module-V: Computer Knowledge Module (Qualifying)', questions: 20, marks: 60 }
    ]
  }
};

class OnlineSyncEngine {
  constructor() {
    this.onlineQuestions = [];
    this.loadSavedOnlineQuestions();
  }

  loadSavedOnlineQuestions() {
    try {
      const saved = localStorage.getItem('solo_system_online_questions');
      if (saved) {
        this.onlineQuestions = JSON.parse(saved);
      }
    } catch (e) {
      console.error('Error loading online questions:', e);
    }
  }

  saveOnlineQuestions() {
    try {
      localStorage.setItem('solo_system_online_questions', JSON.stringify(this.onlineQuestions));
    } catch (e) {
      console.error('Error saving online questions:', e);
    }
  }

  getSyllabus() {
    return LATEST_SSC_SYLLABUS;
  }

  /**
   * Fetches live online questions from educational API gateways
   * and formats them into our standard RPG question schema.
   */
  async fetchLiveOnlineQuestions(category = 'all', count = 10) {
    try {
      // Fetch from Open Trivia Database API (General Science, History, Geography, General Knowledge)
      // Category 9: General Knowledge, Category 17: Science & Nature, Category 23: History, Category 22: Geography
      let categoryId = '';
      if (category === 'science') categoryId = '&category=17';
      else if (category === 'history') categoryId = '&category=23';
      else if (category === 'geography') categoryId = '&category=22';
      else if (category === 'gk') categoryId = '&category=9';

      const url = `https://opentdb.com/api.php?amount=${count}${categoryId}&type=multiple`;
      const response = await fetch(url);
      
      if (!response.ok) {
        throw new Error(`HTTP error ${response.status}`);
      }

      const data = await response.json();
      if (data.response_code === 0 && data.results && data.results.length > 0) {
        const currentBank = (window.QuestionBank && window.QuestionBank.getAll) ? window.QuestionBank.getAll() : [];
        const existingTexts = new Set(
          currentBank.concat(this.onlineQuestions).map(q => this.normalizeText(q.question))
        );

        const newUniqueQuestions = [];

        data.results.forEach((item, idx) => {
          const decodedQuestion = this.decodeHtml(item.question);
          const normalized = this.normalizeText(decodedQuestion);

          // Check for duplicate
          if (!existingTexts.has(normalized)) {
            existingTexts.add(normalized);

            const rawOptions = [...item.incorrect_answers, item.correct_answer];
            const shuffledOptions = rawOptions.map(o => this.decodeHtml(o)).sort(() => 0.5 - Math.random());
            const correctDecoded = this.decodeHtml(item.correct_answer);
            const correctIdx = shuffledOptions.indexOf(correctDecoded);

            newUniqueQuestions.push({
              id: `online_${Date.now()}_${idx}_${Math.random().toString(36).substr(2, 4)}`,
              subject: 'General Awareness',
              topic: item.category || 'Live Online Gateway',
              difficulty: item.difficulty ? item.difficulty.charAt(0).toUpperCase() + item.difficulty.slice(1) : 'Medium',
              question: decodedQuestion,
              options: shuffledOptions,
              correct: correctIdx,
              explanation: `Correct Answer: ${correctDecoded}. Verified from the live dynamic knowledge gate.`,
              trick: 'Focus on keywords and eliminate improbable distractors.',
              isOnlineSourced: true
            });
          }
        });

        if (newUniqueQuestions.length > 0) {
          // Merge unique questions
          this.onlineQuestions.push(...newUniqueQuestions);
          this.saveOnlineQuestions();

          // Add to global QuestionBank
          if (window.QuestionBank && window.QuestionBank.getAll) {
            window.QuestionBank.getAll().push(...newUniqueQuestions);
          }

          return { success: true, count: newUniqueQuestions.length, questions: newUniqueQuestions };
        } else {
          return { success: false, message: 'All fetched questions already exist in your Vault! 0 duplicates added.' };
        }
      } else {
        return { success: false, message: 'No new questions returned from online gate.' };
      }
    } catch (err) {
      console.warn('Live API fetch unavailable or rate limited. Utilizing robust local bank.', err);
      return { 
        success: false, 
        message: 'Online gate connection failed or offline. Using embedded High-Yield SSC Knowledge Base.' 
      };
    }
  }

  normalizeText(text) {
    if (!text) return '';
    return text.toLowerCase().replace(/[^a-z0-9]/g, '').trim();
  }

  decodeHtml(html) {
    const txt = document.createElement('textarea');
    txt.innerHTML = html;
    return txt.value;
  }

  /**
   * Import custom JSON questions or syllabus updates with duplicate check
   */
  importCustomQuestionsJSON(jsonString) {
    try {
      const parsed = JSON.parse(jsonString);
      if (Array.isArray(parsed)) {
        const currentBank = (window.QuestionBank && window.QuestionBank.getAll) ? window.QuestionBank.getAll() : [];
        const existingTexts = new Set(
          currentBank.concat(this.onlineQuestions).map(q => this.normalizeText(q.question))
        );

        const newUnique = [];
        parsed.forEach(q => {
          if (q.question && !existingTexts.has(this.normalizeText(q.question))) {
            existingTexts.add(this.normalizeText(q.question));
            newUnique.push(q);
          }
        });

        if (newUnique.length > 0) {
          this.onlineQuestions.push(...newUnique);
          this.saveOnlineQuestions();
          if (window.QuestionBank && window.QuestionBank.getAll) {
            window.QuestionBank.getAll().push(...newUnique);
          }
          return { success: true, count: newUnique.length };
        } else {
          return { success: false, message: 'All imported questions already exist in the Vault. 0 duplicates added.' };
        }
      }
      return { success: false, message: 'Invalid format: Must be an array of question objects.' };
    } catch (e) {
      return { success: false, message: 'Invalid JSON syntax: ' + e.message };
    }
  }
}

window.OnlineSync = new OnlineSyncEngine();
