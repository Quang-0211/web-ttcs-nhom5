let modal, modalTitle, modalDialog;
let isModalInitialized = false;

function initializeModal() {
    if (!isModalInitialized) {
        modal = document.getElementById('articleModal');
        modalTitle = document.getElementById('modalTitle');
        if (modal) {
            modalDialog = modal.querySelector('.transform');
            isModalInitialized = true;
        }
    }
}

window.openArticleModal = function (type = 'create', articleId = null, title = null, img = null, audio = null, content = null, categoryId = null, courseId = null) {
    initializeModal();

    if (!modal || !modalDialog || !modalTitle) {
        return;
    }

    modal.classList.remove('hidden', 'pointer-events-none');
    modal.classList.add('pointer-events-auto');
    setTimeout(() => {
        modal.classList.remove('opacity-0');
        modal.classList.add('opacity-100');
        modalDialog.classList.remove('scale-95');
        modalDialog.classList.add('scale-100');
    }, 10);

    const courseSelect = document.getElementById('course-id');
    const categorySelect = document.getElementById('category-id');

    if (type === 'create') {
        modalTitle.innerText = 'Add a new Article';
        document.getElementById('articleId').value = '';
        document.getElementById('articleTitle').value = '';
        document.getElementById('previewImg').src = '';
        document.getElementById('audioPreview').src = '';
        document.getElementById('contentEditor').value = '';
        if (categorySelect) {
            categorySelect.value = categorySelect.options.length ? categorySelect.options[0].value : '0';
        }
        if (courseSelect) {
            courseSelect.value = '0';
        }
    } else {
        modalTitle.innerText = 'Update Article';
        document.getElementById('articleId').value = articleId + '';
        document.getElementById('articleTitle').value = title || '';
        document.getElementById('previewImg').src = img || 'https://i.ibb.co/Xz9K5Yn/demo-thumbnail.png';
        if (categorySelect) {
            categorySelect.value = categoryId + '';
        }
        if (courseSelect) {
            courseSelect.value = courseId ? courseId + '' : '0';
        }
        const audioPreview = document.getElementById('audioPreview');
        if (audioPreview) {
            audioPreview.src = audio || '';
        }
        document.getElementById('contentEditor').value = content || '';
    }
}

window.closeArticleModal = function () {
    initializeModal();

    if (!modal || !modalDialog) {
        return;
    }

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

const imageInput = document.getElementById('imageInput');
if (imageInput) {
    imageInput.addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (!file) return;
        const imageFileName = document.getElementById('imageFileName');
        if (imageFileName) {
            imageFileName.textContent = file.name;
        }
        const img = document.querySelector('#previewImg');
        if (img) {
            img.src = URL.createObjectURL(file);
        }
    });
}

const audioInput = document.getElementById('audioInput');
if (audioInput) {
    audioInput.addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (!file) return;
        const audioFileName = document.getElementById('audioFileName');
        if (audioFileName) {
            audioFileName.textContent = file.name;
        }
        const audio = document.getElementById('audioPreview');
        if (audio) {
            audio.src = URL.createObjectURL(file);
        }
    });
}

document.querySelectorAll('.publish-checkbox').forEach(checkbox => {
    checkbox.addEventListener('change', function () {
        const articleId = this.dataset.articleId;
        const status = this.checked;
        fetch('/admin/articles/publish', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'articleId=' + encodeURIComponent(articleId) + '&status=' + encodeURIComponent(status)
        });
    });
});

window.deleteArticle = function (articleId) {
    if (!confirm('Delete this article?')) {
        return;
    }
    fetch('/admin/articles/delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'articleId=' + encodeURIComponent(articleId)
    }).then(() => window.location.reload());
};