function openPlanModal() {
    const modal = document.getElementById('planModal');
    const content = document.getElementById('modalContent');
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