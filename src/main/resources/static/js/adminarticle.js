let modal, modalTitle, modalDialog;
let isModalInitialized = false;

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

window.openArticleModal = function (type = 'create', articleId = null) {
    initializeModal();

    if (!modal) {
        console.error('Modal element not found');
        return;
    }

    console.log("da an " + type);

    if (type === 'create') {
        modalTitle.innerText = "Add a new Article";
        resetForm();
    } else {
        modalTitle.innerText = "Update Article";
        fillDemoData();
    }

    modal.classList.remove('hidden', 'pointer-events-none');
    modal.classList.add('pointer-events-auto');

    setTimeout(() => {
        modal.classList.remove('opacity-0');
        modal.classList.add('opacity-100');
        modalDialog.classList.remove('scale-95');
        modalDialog.classList.add('scale-100');
    }, 10);
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

function resetForm() {
    document.getElementById('articleTitle').value = '';
    document.getElementById('thumbnailPreview').src = 'https://via.placeholder.com/150';
    document.getElementById('audioFileName').innerText = 'lion.mp3';
    document.getElementById('contentEditor').innerHTML = '<br>';
}

function fillDemoData() {
    document.getElementById('articleTitle').value = 'Building the City of the Future';
    document.getElementById('thumbnailPreview').src = 'https://i.ibb.co/Xz9K5Yn/demo-thumbnail.png';
    document.getElementById('audioFileName').innerText = 'lion.mp3';
    document.getElementById('contentEditor').innerHTML = 'Thành phố tương lai<br><br>Các nhà khoa học đang nghiên cứu...';
}