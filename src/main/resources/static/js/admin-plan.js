
function openPlanModal() {
    const modal = document.getElementById('planModal');
    const content = document.getElementById('modalContent');
    const form = modal.querySelector("form");

    // ✅ reset toàn bộ form
    form.reset();

    // ✅ reset hidden id (nếu có)
    document.getElementById("planId").value = 0;

    // ✅ bỏ check tất cả checkbox (quan trọng)
    document.querySelectorAll('#planModal input[type="checkbox"]').forEach(cb => {
        cb.checked = false;
    });

    modal.classList.remove('hidden');
    modal.classList.add('flex');

    setTimeout(() => {
        content.classList.remove('scale-95');
        content.classList.add('scale-100');
    }, 10);
}

function closePlanModal() {
    const modal = document.getElementById('planModal');
    const content = document.getElementById('modalContent');
    content.classList.remove('scale-100');
    content.classList.add('scale-95');
    setTimeout(() => {
        modal.classList.add('hidden');
        modal.classList.remove('flex');
    }, 200);
}

function switchTab(event, tabId) {
    // Ẩn tất cả panels
    document.querySelectorAll('.tab-panel').forEach(p => p.classList.add('hidden'));
    // Hiện panel được chọn
    document.getElementById(tabId).classList.remove('hidden');

    // Cập nhật style nút bấm
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('border-blue-600', 'text-blue-600', 'font-bold');
        btn.classList.add('border-transparent', 'text-gray-500', 'font-medium');
    });
    event.currentTarget.classList.add('border-blue-600', 'text-blue-600', 'font-bold');
    event.currentTarget.classList.remove('border-transparent', 'text-gray-500', 'font-medium');
}

// Đóng modal khi click ra ngoài vùng trắng
window.onclick = function (event) {
    const modal = document.getElementById('planModal');
    if (event.target == modal) closePlanModal();
}
function parseArray(data) {
    if (!data) return [];

    try {
        // nếu là "[1,2]" → OK
        return JSON.parse(data);
    } catch (e) {
        // fallback nếu là "1,2"
        return data.split(',').map(x => x.trim());
    }
}

function setChecked(arr, name) {
    let checkboxes = document.querySelectorAll(`input[name="${name}"]`);

    checkboxes.forEach(cb => {
        // ép cùng kiểu string để so sánh
        cb.checked = arr.map(String).includes(String(cb.value));
    });
}

function createArticleModal(btn) {
    openPlanModal();

    const data = btn.dataset;

    document.getElementById('planId').value = data.id || '';
    document.getElementById('planName').value = data.name || '';
    document.getElementById('planPrice').value = data.price || '';
    document.getElementById('planDuration').value = data.duration || '';
    document.getElementById('planDescription').value = data.description || '';

    // ✅ FIX active
    document.getElementById('planActive').value = data.active;

    // ✅ parse JSON
    setChecked(parseArray(data.articles), 'articles');
    setChecked(parseArray(data.quizzes), 'quizzes');
    setChecked(parseArray(data.videos), 'videos');
    setChecked(parseArray(data.dictation), 'dictationTopics');
    setChecked(parseArray(data.grammar), 'grammars');
    setChecked(parseArray(data.vocabulary), 'vocabularies');

    console.log(parseArray(data.dictation));
    console.log(parseArray(data.vocabulary));
}