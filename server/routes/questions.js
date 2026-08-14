/**
 * SOLO LEVELING EXAM SYSTEM - QUESTIONS DATABASE API
 */

const express = require('express');
const router = express.Router();
const db = require('../database');

// 1. GET ALL QUESTIONS (with optional filter by subject / difficulty)
router.get('/', (req, res) => {
  try {
    const { subject, difficulty } = req.query;
    let query = 'SELECT * FROM questions';
    const params = [];
    const conditions = [];

    if (subject && subject !== 'all') {
      conditions.push('subject = ?');
      params.push(subject);
    }
    if (difficulty && difficulty !== 'all') {
      conditions.push('difficulty = ?');
      params.push(difficulty);
    }

    if (conditions.length > 0) {
      query += ' WHERE ' + conditions.join(' AND ');
    }

    const rows = db.prepare(query).all(...params);
    const questions = rows.map(r => ({
      id: r.id,
      subject: r.subject,
      topic: r.topic,
      difficulty: r.difficulty,
      question: r.question,
      options: JSON.parse(r.options_json || '[]'),
      correct: r.correct,
      explanation: r.explanation,
      trick: r.trick
    }));

    res.json({ success: true, count: questions.length, questions });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// 2. ADD / BULK INSERT QUESTIONS
router.post('/bulk', (req, res) => {
  try {
    const { questions } = req.body;
    if (!Array.isArray(questions) || questions.length === 0) {
      return res.status(400).json({ success: false, message: 'Invalid questions payload.' });
    }

    const insertStmt = db.prepare(`
      INSERT OR IGNORE INTO questions (id, subject, topic, difficulty, question, options_json, correct, explanation, trick)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    `);

    const insertMany = db.transaction((qList) => {
      let inserted = 0;
      for (const q of qList) {
        const info = insertStmt.run(
          q.id || `q_${Date.now()}_${Math.random().toString(36).substr(2, 5)}`,
          q.subject,
          q.topic || 'General',
          q.difficulty || 'Medium',
          q.question,
          JSON.stringify(q.options || []),
          q.correct !== undefined ? q.correct : 0,
          q.explanation || '',
          q.trick || ''
        );
        if (info.changes > 0) inserted += 1;
      }
      return inserted;
    });

    const count = insertMany(questions);
    res.json({ success: true, inserted: count, totalReceived: questions.length });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

module.exports = router;
