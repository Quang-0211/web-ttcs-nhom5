let modal, modalTitle, modalDialog;
let isModalInitialized = false;
console.log("JS LOADED OK");
function initializeModal() {
    if (!isModalInitialized) {
        modal = document.getElementById('articleModal');
        modalTitle = document.getElementById('modalTitle');
        if (modal) {
            modalDialog = modal.querySelector('.transform');
            isModalInitialized = true;
            console.log('Modal initialized successfully');
        } else {
            console.warn('Modal element not found in DOM');
        }
    }
}

window.openArticleModal = function (type = 'create', articleId = null, title = null, img = null, audio = null, content = null, categoryId = null) {
    initializeModal();

    if (!modal) {
        console.error('Modal element not found');
        return;
    }

    console.log("da an " + type);
    modal.classList.remove('hidden', 'pointer-events-none');
    modal.classList.add('pointer-events-auto');
    console.log(articleId, typeof (articleId), title, img, audio, content, categoryId)
    setTimeout(() => {
        modal.classList.remove('opacity-0');
        modal.classList.add('opacity-100');
        modalDialog.classList.remove('scale-95');
        modalDialog.classList.add('scale-100');
    }, 10);

    if (type === 'create') {
        modalTitle.innerText = "Add a new Article";
        document.getElementById('articleId').value = "";
        document.getElementById('articleTitle').value = '';
        document.getElementById('previewImg').src = "";
        document.getElementById('audioPreview').src = '';
        document.getElementById('contentEditor').innerHTML = '';
    } else {
        modalTitle.innerText = "Update Article";
        document.getElementById('articleId').value = articleId + "";
        document.getElementById('articleTitle').value = title;
        document.getElementById('previewImg').src = img || "https://i.ibb.co/Xz9K5Yn/demo-thumbnail.png";
        document.getElementById("category-id").value = categoryId + "";
        document.getElementById('audioPreview').src = audio;
        document.getElementById('contentEditor').value = content;
    }

}

window.closeArticleModal = function () {
    initializeModal();

    if (!modal) return;

    modalDialog.classList.remove('scale-100');
    modalDialog.classList.add('scale-95');
    modal.classList.remove('opacity-100');
    modal.classList.add('opacity-0');
    modal.classList.add('pointer-events-none');
    modal.classList.remove('pointer-events-auto');

    setTimeout(() => {
        modal.classList.add('hidden');
    }, 300);
}

// xem truoc anh
document.getElementById('imageInput').addEventListener('change', function (e) {
    const file = e.target.files[0];
    if (!file) return;

    // Hiển thị tên file
    document.getElementById('imageFileName').textContent = file.name;

    // Preview ảnh
    const img = document.querySelector('#previewImg');
    img.src = URL.createObjectURL(file);
});

// nghe truoc audio
document.getElementById('audioInput').addEventListener('change', function (e) {
    const file = e.target.files[0];
    if (!file) return;

    // Hiển thị tên file
    document.getElementById('audioFileName').textContent = file.name;

    // Tạo URL để phát
    const audio = document.getElementById('audioPreview');
    audio.src = URL.createObjectURL(file);
});

document.querySelectorAll(".publish-checkbox").forEach(checkbox => {
    checkbox.addEventListener("change", function () {
        const articleId = this.dataset.articleId;
        const status = this.checked;
        console.log(articleId, status);
        fetch('articles/publish', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'articleId=' + articleId + '&status=' + status
        });
    });
});