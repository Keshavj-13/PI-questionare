
const qs = (sel, p=document) => p.querySelector(sel);
const qsa = (sel, p=document) => [...p.querySelectorAll(sel)];

// Remove forced test login/teacher for production
// window.localStorage.setItem('loggedIn', 'true');
// window.localStorage.setItem('role', 'teacher');
// window.localStorage.setItem('username', 'Test Teacher');

const App = (() => {
  // State will be loaded from backend
  const state = {questions: []};
  const dom = {};
  const cache = () => {
    dom.feed = qs('#feed'); dom.modal=qs('#modal'); dom.askBtn=qs('#askBtn');
    dom.cancelBtn=qs('#cancelBtn'); dom.postBtn=qs('#postBtn'); dom.questionInput=qs('#questionInput');
    dom.search=qs('#searchInput'); dom.year=qs('#year'); dom.cats=qsa('.category'); dom.avatar=qs('#accountBtn');
  };
  const render = list => {
    // Only show answer form if user is logged in and is a teacher
    const user = localStorage.getItem('user');
    let isTeacher = false;
    if (user) {
      try {
        const u = JSON.parse(user);
        isTeacher = u.role === 'teacher';
      } catch {}
    }
    // Helper to render answer with YouTube embed if present
    function renderAnswer(a) {
      const ytMatch = a.text.match(/(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/watch\?v=|youtu\.be\/)([\w-]{11})/);
      if (ytMatch) {
        const vid = ytMatch[1];
        return `<div class="answer"><strong>${a.author}:</strong><br><iframe width="320" height="180" src="https://www.youtube.com/embed/${vid}" frameborder="0" allowfullscreen style="margin:0.5rem 0;"></iframe></div>`;
      }
      return `<div class="answer"><strong>${a.author}:</strong> ${a.text}</div>`;
    }
    dom.feed.innerHTML = list.map(q => {
      const answerForm = isTeacher ? `
        <form class="answer-form" data-qid="${q.id}" style="margin-top:0.7rem;">
          <div class="answer-form-row">
            <input type="text" class="answer-input" placeholder="Type your answer…" required />
            <button type="submit" class="btn-primary">Post Answer</button>
          </div>
        </form>
      ` : '';
      const imageTag = q.image ? `<img src="${q.image}" alt="Question image" style="max-width:100%;max-height:180px;margin:0.5rem 0;border-radius:0.7rem;" />` : '';
      return `
      <div class="card">
        <h3>${q.text}</h3>
        ${imageTag}
        <div class="meta">Asked by ${q.author} · ${q.time}</div>
        <button class="btn-primary btn-secondary toggleAns">${q.answers.length} Answer${q.answers.length===1?'':'s'}</button>
        <div class="answers" style="display:none;">${(q.answers||[]).map(renderAnswer).join('')}${answerForm}</div>
      </div>`;
    }).join('');
    qsa('.toggleAns',dom.feed).forEach(b=>b.addEventListener('click',()=>{const a=b.nextElementSibling;a.style.display=a.style.display==='block'?'none':'block';}));
    // Add answer form listeners if teacher
    if (isTeacher) {
      qsa('.answer-form', dom.feed).forEach(form => {
        form.addEventListener('submit', async e => {
          e.preventDefault();
          const qid = form.getAttribute('data-qid');
          const input = form.querySelector('.answer-input');
          const answerText = input.value.trim();
          if (!answerText) return;
          await postAnswer(qid, answerText);
          input.value = '';
        });
      });
    }
  };
  // Post an answer to backend (or dummy)
  const postAnswer = async (questionId, text) => {
    const author = window.localStorage.getItem('username') || 'Teacher';
    try {
      const res = await fetch(`/api/questions/${questionId}/answers`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({text, author})
      });
      if (!res.ok) throw new Error('Failed to post answer');
      await fetchQuestions();
    } catch (e) {
      // Fallback for demo: update dummyQuestions
      const q = dummyQuestions.find(q => q.id == questionId);
      if (q) {
        q.answers = q.answers || [];
        q.answers.push({author, text});
        state.questions = dummyQuestions;
        render(state.questions);
      }
    }
  };
  // Dummy questions for demo
  const dummyQuestions = [
    {id:1,author:'Alex',topic:'chemistry',text:'Why does ice float?',time:'2h ago',answers:[{author:'Dr. B',text:'Hydrogen bonds...'}]},
    {id:2,author:'Samira',topic:'physics',text:'Speed vs velocity?',time:'5h ago',answers:[]}
  ];

  // Fetch questions from backend, fallback to dummy
  const fetchQuestions = async () => {
    try {
      const res = await fetch('/api/questions');
      if (!res.ok) throw new Error('Failed to fetch');
      const data = await res.json();
      state.questions = data;
      render(state.questions);
    } catch (e) {
      state.questions = dummyQuestions;
      render(state.questions);
    }
  };

  // Post a new question to backend
  const postQuestion = async (text) => {
    try {
      const res = await fetch('/api/questions', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({text})
      });
      if (!res.ok) throw new Error('Failed to post');
      await fetchQuestions();
    } catch (e) {
      alert('Could not post question.');
    }
  };

  const listen = () => {
    dom.search && dom.search.addEventListener('input', () => {
      const term=dom.search.value.toLowerCase();
      render(state.questions.filter(q=>q.text.toLowerCase().includes(term)));
    });
    dom.askBtn && dom.askBtn.addEventListener('click', ()=>{dom.modal.classList.add('active');dom.questionInput.focus();});
    dom.cancelBtn && dom.cancelBtn.addEventListener('click', closeModal);
    dom.modal && dom.modal.addEventListener('click',e=> {if(e.target===dom.modal)closeModal();});
    dom.postBtn && dom.postBtn.addEventListener('click', ()=> {
      const text=dom.questionInput.value.trim(); if(!text) return;
      postQuestion(text);
      closeModal();
    });
    dom.cats.forEach(c=>c.addEventListener('click',()=>{render(state.questions.filter(q=>q.topic===c.dataset.topic));}));
    dom.avatar && dom.avatar.addEventListener('click', () => {
      // Simulate login state: replace with real check in production
      const loggedIn = window.localStorage.getItem('loggedIn') === 'true';
      if (loggedIn) {
        window.location.href = 'profile.html';
      } else {
        const loginModal = document.getElementById('loginModal');
        if (loginModal) loginModal.style.display = 'flex';
        const closeBtn = document.getElementById('closeLoginModal');
        if (closeBtn) closeBtn.onclick = () => { loginModal.style.display = 'none'; };
      }
    });
  };
  const closeModal = () => {dom.modal.classList.remove('active');dom.questionInput.value='';};
  const init = () => {
    cache();
    fetchQuestions();
    listen();
    dom.year && (dom.year.textContent=new Date().getFullYear());
  };
  return {init};
})();
document.addEventListener('DOMContentLoaded', App.init);
