/**
 * admin-common.js
 * Shared utilities for all admin management pages (Grammar, Vocabulary, Dictation).
 * Handles: Category CRUD, Pagination, Category Tree, Modal toggle.
 */

// ─── Pagination State ────────────────────────────────────────────
let currentCategoryFilter = null;
let currentPage = 1;
let itemsPerPage = 10;

// ─── Category Helpers ────────────────────────────────────────────
function getDescendantCategoryIds(catId, rawCategories) {
    let ids = [catId];
    rawCategories.filter(c => c.parent && c.parent.id === catId).forEach(child => {
        ids = ids.concat(getDescendantCategoryIds(child.id, rawCategories));
    });
    return ids;
}

// ─── Category CRUD ───────────────────────────────────────────────
async function promptAddCategory(event, parentId) {
    if (event) event.stopPropagation();
    const name = prompt("Enter new category name:");
    if (!name || name.trim() === '') return;
    const formData = new FormData();
    formData.append('name', name);
    if (parentId) formData.append('parentId', parentId);
    try {
        const response = await fetch('/admin/api/category', { method: 'POST', body: formData });
        if (response.ok) window.location.reload();
        else alert("Failed to add category.");
    } catch (e) { alert("Network error."); }
}

async function promptEditCategory(event, id, oldName) {
    if (event) event.stopPropagation();
    const name = prompt("Edit category name:", oldName);
    if (!name || name.trim() === '' || name === oldName) return;
    const formData = new FormData();
    formData.append('name', name);
    try {
        const response = await fetch('/admin/api/category/' + id, { method: 'PUT', body: formData });
        if (response.ok) window.location.reload();
        else alert("Failed to edit category.");
    } catch (e) { alert("Network error."); }
}

async function promptDeleteCategory(event, id) {
    if (event) event.stopPropagation();
    if (!confirm("Are you sure you want to delete this category?")) return;
    try {
        const response = await fetch('/admin/api/category/' + id, { method: 'DELETE' });
        if (response.ok) window.location.reload();
        else alert("Cannot delete category.");
    } catch (e) { alert("Network error."); }
}

// ─── Pagination Controls ─────────────────────────────────────────
function changeItemsPerPage() {
    const select = document.getElementById('itemsPerPageSelect');
    itemsPerPage = parseInt(select.value, 10);
    currentPage = 1;
    applyFilters();
}

function changePage(delta) {
    currentPage += delta;
    applyFilters();
}

function jumpToPage() {
    const select = document.getElementById('currentPageSelect');
    currentPage = parseInt(select.value, 10);
    applyFilters();
}

/**
 * Updates the pagination UI (page select, buttons, item count).
 * @param {number} totalItems
 */
function updatePaginationUI(totalItems) {
    const totalPages = Math.max(1, Math.ceil(totalItems / itemsPerPage));
    if (currentPage > totalPages) currentPage = totalPages;
    if (currentPage < 1) currentPage = 1;

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;

    const countEl = document.getElementById('itemCountDisplay');
    if (countEl) {
        const upTo = Math.min(endIndex, totalItems);
        const startDisp = totalItems === 0 ? 0 : startIndex + 1;
        countEl.innerText = `Showing ${startDisp} to ${upTo} of ${totalItems} item(s)`;
    }

    const pageSelect = document.getElementById('currentPageSelect');
    if (pageSelect) {
        if (pageSelect.options.length !== totalPages) {
            pageSelect.innerHTML = '';
            for (let i = 1; i <= totalPages; i++) {
                const opt = document.createElement('option');
                opt.value = i;
                opt.text = i;
                pageSelect.appendChild(opt);
            }
        }
        pageSelect.value = currentPage;
    }

    const totalPagesDisplay = document.getElementById('totalPagesDisplay');
    if (totalPagesDisplay) totalPagesDisplay.innerText = '/ ' + totalPages;

    const prevBtn = document.getElementById('prevPageBtn');
    const nextBtn = document.getElementById('nextPageBtn');
    if (prevBtn) prevBtn.disabled = currentPage <= 1;
    if (nextBtn) nextBtn.disabled = currentPage >= totalPages;

    // Highlight active category in sidebar
    document.querySelectorAll('.cat-item').forEach(el => el.classList.remove('font-bold', 'text-blue-600', 'bg-blue-50'));
    if (currentCategoryFilter !== null) {
        const el = document.getElementById('cat-el-' + currentCategoryFilter);
        if (el) el.classList.add('font-bold', 'text-blue-600', 'bg-blue-50');
    }

    return { startIndex, endIndex };
}

// ─── Category Tree Rendering ─────────────────────────────────────
/**
 * Initializes the category sidebar tree.
 * @param {Array} rawCategories - All categories from server
 * @param {string} rootName - Root category name to search for (e.g. 'grammar', 'vocab', 'dictation')
 * @param {Function} filterFn - Function to call when a category is clicked, receives categoryId
 */
async function initCategoryTree(rawCategories, rootName, filterFn) {
    const container = document.getElementById('categoryTreeContainer');
    if (!container) return null;

    // Find or create root category
    let root = rawCategories.find(c => !c.parent && c.name.toLowerCase().includes(rootName));
    if (!root) {
        const formData = new FormData();
        formData.append('name', rootName.charAt(0).toUpperCase() + rootName.slice(1));
        try {
            const res = await fetch('/admin/api/category', { method: 'POST', body: formData });
            if (res.ok) {
                root = await res.json();
                rawCategories.push(root);
            }
        } catch (e) { console.error("Error creating root category"); }
    }

    const rootId = root ? root.id : null;

    // Bind the Add Category & Reset Category buttons
    if (rootId) {
        const addBtn = document.getElementById('addCategoryBtn');
        if (addBtn) addBtn.onclick = (e) => promptAddCategory(e, rootId);
    }
    const resetBtn = document.getElementById('resetCategoryBtn');
    if (resetBtn) resetBtn.onclick = () => filterFn(null);

    const children = rawCategories.filter(c => c.parent && c.parent.id === rootId);

    // Update dropdown in modal form
    const categorySelect = document.querySelector('select[name="categoryId"]');
    if (categorySelect) {
        categorySelect.innerHTML = '';
        children.forEach(cat => {
            const opt = document.createElement('option');
            opt.value = cat.id;
            opt.innerText = cat.name;
            categorySelect.appendChild(opt);
        });
    }

    // Render tree
    if (children.length === 0) {
        container.innerHTML = '<div class="text-sm text-gray-400 p-2">No categories found.</div>';
    } else {
        const ul = document.createElement('ul');
        ul.className = 'space-y-1';
        children.forEach(cat => {
            const li = document.createElement('li');
            const safeName = cat.name.replace(/'/g, "\\'");

            const div = document.createElement('div');
            div.className = "cat-item flex items-center justify-between p-1.5 rounded hover:bg-gray-50 group cursor-pointer text-gray-700 hover:text-blue-600 transition";
            div.id = 'cat-el-' + cat.id;
            div.onclick = () => filterFn(cat.id);

            div.innerHTML = `
                <div class="flex items-center gap-2">
                    <i class="fa fa-folder-o text-blue-400 text-xs"></i>
                    <span class="text-sm select-none">${cat.name}</span>
                </div>
                <div class="hidden group-hover:flex items-center gap-2 pr-1">
                    <button onclick="promptEditCategory(event, ${cat.id}, '${safeName}')" class="text-gray-400 hover:text-blue-500" title="Rename"><i class="fa fa-pencil text-[10px]"></i></button>
                    <button onclick="promptDeleteCategory(event, ${cat.id})" class="text-gray-400 hover:text-red-500" title="Delete"><i class="fa fa-trash text-[10px]"></i></button>
                </div>
            `;
            li.appendChild(div);
            ul.appendChild(li);
        });
        container.appendChild(ul);
    }

    return rootId;
}

// ─── Modal Helpers ───────────────────────────────────────────────
function toggleModal(show) {
    const modal = document.getElementById('addModal');
    const dialog = document.getElementById('modalDialog');
    if (show) {
        modal.classList.remove('hidden');
        requestAnimationFrame(() => {
            modal.classList.remove('opacity-0');
            dialog.classList.remove('scale-95');
            dialog.classList.add('scale-100');
        });
    } else {
        modal.classList.add('opacity-0');
        dialog.classList.remove('scale-100');
        dialog.classList.add('scale-95');
        setTimeout(() => {
            modal.classList.add('hidden');
            document.getElementById('addForm').reset();
        }, 300);
    }
}
