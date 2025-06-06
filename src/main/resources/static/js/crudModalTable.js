// crudModalTable.js
export function initCrudModalTable(config) {
  const {
    tableId,
    modalId,
    formId,
    apiBase,
    fields,
    idField,
    displayIdField,
    addButtonId,
    deleteButtonId,
    modalLabelId,
    editButtonId,
    saveButtonId,
  } = config;

  const $table = $(`#${tableId}`);
  const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById(modalId));
  const form = document.getElementById(formId);
  const $btnDeleteSelected = $(`#${deleteButtonId}`);

  const fieldInputs = fields.reduce((acc, key) => {
    acc[key] = document.getElementById(key);
    return acc;
  }, {
    [idField]: document.getElementById(idField),
    ...(displayIdField ? { [displayIdField]: document.getElementById(displayIdField) } : {}),
  });

  const ajax = (url, method, data) =>
    $.ajax({ url, method, contentType: 'application/json', data: JSON.stringify(data) });

  const formatDateTime = val => val ? new Date(val).toLocaleString() : '';

  const setFormData = data => {
    fieldInputs[idField].value = data.id || '';
    if (displayIdField) {
      const displayEl = document.getElementById('idFormGroup');
      fieldInputs[displayIdField].value = data.id || '';
      displayEl.style.display = data.id ? 'block' : 'none';
    }
    fields.forEach(f => fieldInputs[f].value = data[f] || '');
  };

  const getFormData = () => {
    const data = { createdByUserId: 1 }; // Placeholder
    fields.forEach(f => data[f] = fieldInputs[f].value);
    return data;
  };

  const setEditable = editable => {
    fields.forEach(f => {
      const el = fieldInputs[f];

      if (!el) return;

      if (el.tagName === 'SELECT') {
        el.disabled = !editable;
      } else {
        if (editable) {
          el.removeAttribute('readonly');
        } else {
          el.setAttribute('readonly', 'readonly');
        }
      }
    });
  };

  const resetForm = () => {
    form.reset();
    setFormData({});
    form.classList.remove('was-validated');

    // Ensure dropdown is disabled initially
    fields.forEach(f => {
      const el = fieldInputs[f];
      if (el && el.tagName === 'SELECT') {
        el.disabled = true;
      }
    });
  };

  const refreshTable = () => $table.bootstrapTable('refresh');

  const switchModalMode = (mode) => {
    const label = document.getElementById(modalLabelId);
    const editBtn = document.getElementById(editButtonId);
    const saveBtn = document.getElementById(saveButtonId);

    if (mode === 'view') {
      setEditable(false);
      label.textContent = 'View Record';
      editBtn.classList.remove('d-none');
      saveBtn.classList.add('d-none');
    } else if (mode === 'edit') {
      setEditable(true);
      label.textContent = 'Edit Record';
      editBtn.classList.add('d-none');
      saveBtn.classList.remove('d-none');
    } else if (mode === 'add') {
      resetForm();
      setEditable(true);
      label.textContent = 'Add New Record';
      editBtn.classList.add('d-none');
      saveBtn.classList.remove('d-none');
    }
  };

  // Events
  $table.on('post-body.bs.table', () => {
    $table.find('tbody tr').each((_, tr) => {
      const $cells = $(tr).find('td');
      $cells.each((idx, cell) => {
        const colText = $(cell).text();
        if (colText && colText.match(/^\d{4}-\d{2}-\d{2}/)) {
          $(cell).text(formatDateTime(colText));
        }
      });
    });
  });

  $table.on('check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table', () => {
    $btnDeleteSelected.prop('disabled', !$table.bootstrapTable('getSelections').length);
  });

  $table.on('click-row.bs.table', (e, row, $element, field) => {
    console.log('Row clicked:', row);

    if (field === 'state') {
      // Ignore clicks on checkboxes for selection
      return;
    }

    // Fill modal inputs with row data
    setFormData(row);

    // Switch modal to readonly/view mode
    switchModalMode('view');

    // Show modal dialog
    modal.show();
  });

  document.getElementById(addButtonId).addEventListener('click', () => switchModalMode('add'));
  document.getElementById(editButtonId).addEventListener('click', () => switchModalMode('edit'));

  form.addEventListener('submit', e => {
    e.preventDefault();
    e.stopPropagation();
    if (!form.checkValidity()) {
      form.classList.add('was-validated');
      return;
    }

    const id = fieldInputs[idField].value;
    const url = id ? `${apiBase}/${id}` : apiBase;
    const method = id ? 'PUT' : 'POST';

    ajax(url, method, getFormData())
      .done(() => {
        alert(`Record ${id ? 'updated' : 'added'} successfully!`);
        modal.hide();
        refreshTable();
      })
      .fail(() => alert('Error saving record.'));
  });

  $btnDeleteSelected.on('click', () => {
    const selected = $table.bootstrapTable('getSelections');
    if (!selected.length || !confirm(`Delete ${selected.length} selected records?`)) return;

    Promise.all(selected.map(row => $.ajax({ url: `${apiBase}/${row.id}`, method: 'DELETE' })))
      .then(() => {
        alert('Deleted successfully.');
        refreshTable();
        $btnDeleteSelected.prop('disabled', true);
      })
      .catch(() => alert('Some deletions failed.'));
  });
}


/**
 * Load dropdown options from API into a <select> element and optionally cache it in a map.
 * 
 * @param {string} selectId - The DOM ID of the select element.
 * @param {string} url - The API endpoint to fetch data from.
 * @param {Map} [map=null] - Optional Map object to store id-to-name mapping.
 * @param {string} [labelField='name'] - Field in the API response to display as label.
 * @param {string} [valueField='id'] - Field in the API response to use as option value.
 */
export async function loadDropdown(selectId, url, map = null, labelField = 'name', valueField = 'id') {
  try {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`Failed to fetch from ${url}`);
    const data = await res.json();

    const select = document.getElementById(selectId);
    if (!select) throw new Error(`Select element with id '${selectId}' not found`);

    select.innerHTML = '<option value="" disabled selected>Select an option</option>';

    data.forEach(item => {
      const option = document.createElement('option');
      option.value = item[valueField];
      option.textContent = item[labelField];
      select.appendChild(option);

      if (map) map.set(item[valueField], item[labelField]);
    });
  } catch (error) {
    console.error(error);
    alert(`Error loading options for #${selectId}: ${error.message}`);
  }
}

/**
 * Escapes a code string (Java, Python, XML, etc.) to be safely displayed in HTML.
 * Optionally replaces newlines with <br> tags.
 * 
 * @param {string} code - The raw code string to escape.
 * @param {boolean} [applyBreakReplace=false] - Whether to replace newline characters with <br>.
 * @returns {string} - The HTML-safe escaped string.
 */
export function escapeCodeString(code, applyBreakReplace = false) {
  if (typeof he === 'undefined') {
    console.warn('he.js is not loaded. Include https://cdnjs.cloudflare.com/ajax/libs/he/1.2.0/he.min.js');
    return code;
  }
  const escaped = he.encode(code, { useNamedReferences: true });
  return applyBreakReplace ? escaped.replace(/\n/g, '<br>') : escaped;
}