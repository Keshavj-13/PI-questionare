const qs = (sel, p=document) => p.querySelector(sel);
const qsa = (sel, p=document) => [...p.querySelectorAll(sel)];

// Auth helper
const auth = {
  token: localStorage.getItem('token'),
  user: JSON.parse(localStorage.getItem('user') || 'null'),
  headers() {
    return this.token ? { 'Authorization': `Bearer ${this.token}` } : {};
  },
  isTeacher() {
    return this.user?.role === 'TEACHER';
  }
};

// Time formatting helper
const formatTime = (dateString) => {
  const date = new Date(dateString);
  const now = new Date();
  const diff = (now - date) / 1000; // in seconds
  
  if (diff < 60) return 'just now';
  if (diff < 3600) return `${Math.floor(diff/60)}m ago`;
  if (diff < 86400) return `${Math.floor(diff/3600)}h ago`;
  return `${Math.floor(diff/86400)}d ago`;
};

const App = (() => {
  const state = { questions: [] };
  const dom = {};
  
  const cache = () => {
    dom.feed = qs('#feed'); 
    dom.modal = qs('#modal'); 
    dom.askBtn = qs('#askBtn');
    dom.cancelBtn = qs('#cancelBtn'); 
    dom.postBtn = qs('#postBtn'); 
    dom.questionInput = qs('#questionInput');
    dom.search = qs('#searchInput'); 
    dom.year = qs('#year'); 
    dom.cats = qsa('.category'); 
    dom.avatar = qs('#accountBtn');
    dom.questionTitle = qs('#questionTitle');
    dom.questionContent = qs('#questionContent');
  };

  const render = list => {
    dom.feed.innerHTML = list.map(q => {
      const imageTag = q.imageUrls?.length 
        ? `<img src="${q.imageUrls[0]}" alt="Question" class="q-image">` 
        : '';
      
      const answerForm = auth.isTeacher() ? `
        <form class="answer-form" data-qid="${q.id}" style="margin-top:0.7rem;">
          <div class="answer-form-row">
            <input type="text" class="answer-input" placeholder="Type your answer…" required />
            <button type="submit" class="btn-primary">Post Answer</button>
          </div>
        </form>
      ` : '';
      
      return `
      <div class="card">
        <a href="question.html?id=${q.id}" style="text-decoration:none;color:inherit">
          <h3>${q.title}</h3>
          ${imageTag}
          <p>${q.content}</p>
        </a>
        <div class="meta">
          ${q.author.username} · ${q.topic || 'General'} · ${formatTime(q.createdAt)}
        </div>
        <button class="btn-secondary toggleAns">
          ${q.answerCount || 0} Answer${q.answerCount === 1 ? '' : 's'}
        </button>
        <div class="answers" style="display:none;">
          ${(q.answers || []).map(ans => `
            <div class="answer">
              <strong>${ans.author.username}:</strong> 
              ${ans.body}
              <div class="meta">${formatTime(ans.createdAt)}</div>
            </div>
          `).join('')}
          ${answerForm}
        </div>
      </div>`;
    }).join('');
    
    // Attach event listeners
    qsa('.toggleAns', dom.feed).forEach(b => {
      b.addEventListener('click', () => {
        const answers = b.nextElementSibling;
        answers.style.display = answers.style.display === 'block' ? 'none' : 'block';
      });
    });
    
    // Teacher answer forms
    if (auth.isTeacher()) {
      qsa('.answer-form', dom.feed).forEach(form => {
        form.addEventListener('submit', async e => {
          e.preventDefault();
          const qid = form.dataset.qid;
          const input = form.querySelector('.answer-input');
          const body = input.value.trim();
          if (!body) return;
          
          try {
            const res = await fetch(`/api/questions/${qid}/answers`, {
              method: 'POST',
              headers: { 
                'Content-Type': 'application/json',
                ...auth.headers()
              },
              body: JSON.stringify({ body })
            });
            
            if (!res.ok) throw new Error('Failed to post answer');
            input.value = '';
            await fetchQuestions();
          } catch (err) {
            alert(err.message || 'Error posting answer');
          }
        });
      });
    }
  };

  // Fetch questions from backend
  const fetchQuestions = async () => {
    try {
      const res = await fetch('/api/questions');
      if (!res.ok) throw new Error('Failed to fetch questions');
      state.questions = await res.json();
      render(state.questions);
    } catch (err) {
      console.error('Fetch error:', err);
      dom.feed.innerHTML = '<div class="card">Failed to load questions. Try refreshing.</div>';
    }
  };

  // Post new question
  const postQuestion = async (title, content) => {
    try {
      const res = await fetch('/api/questions', {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          ...auth.headers()
        },
        body: JSON.stringify({ title, content })
      });
      
      if (!res.ok) throw new Error('Failed to post question');
      await fetchQuestions();
      return true;
    } catch (err) {
      alert(err.message || 'Error posting question');
      return false;
    }
  };

  const listen = () => {
    // Search functionality
    dom.search?.addEventListener('input', () => {
      const term = dom.search.value.toLowerCase();
      render(state.questions.filter(q => 
        q.title.toLowerCase().includes(term) || 
        q.content.toLowerCase().includes(term)
      ));
    });
    
    // Ask modal
    dom.askBtn?.addEventListener('click', () => {
      if (!auth.token) {
        document.getElementById('loginModal').style.display = 'flex';
        return;
      }
      dom.modal.classList.add('active');
      dom.questionTitle.focus();
    });
    
    // Modal controls
    dom.cancelBtn?.addEventListener('click', () => {
      dom.modal.classList.remove('active');
      dom.questionTitle.value = '';
      dom.questionContent.value = '';
    });
    
    dom.modal?.addEventListener('click', e => {
      if (e.target === dom.modal) dom.modal.classList.remove('active');
    });
    
    dom.postBtn?.addEventListener('click', async () => {
      const title = dom.questionTitle.value.trim();
      const content = dom.questionContent.value.trim();
      
      if (!title || !content) {
        alert('Please enter both title and content');
        return;
      }
      
      const success = await postQuestion(title, content);
      if (success) {
        dom.modal.classList.remove('active');
        dom.questionTitle.value = '';
        dom.questionContent.value = '';
      }
    });
    
    // Category filters
    dom.cats?.forEach(c => c.addEventListener('click', () => {
      const topic = c.dataset.topic;
      render(state.questions.filter(q => q.topic === topic));
    }));
    
    // Profile avatar
    dom.avatar?.addEventListener('click', () => {
      if (auth.token) {
        location.href = 'profile.html';
      } else {
        document.getElementById('loginModal').style.display = 'flex';
      }
    });
  };

  const init = () => {
    cache();
    fetchQuestions();
    listen();
    dom.year && (dom.year.textContent = new Date().getFullYear());
  };

  return { init };
})();

document.addEventListener('DOMContentLoaded', App.init);