/*
 * Script for handling sorting and search controls on the expense history page.
 * It uses the main search form elements to update the sort state and submit the form,
 * minimizing data duplication.
 */

// Global references to the main form elements (fetched when the script loads)
// These now correctly find the elements due to the ID fix in the HTML.
const sortHiddenInput = document.getElementById('sortHiddenInput');
const searchForm = document.getElementById('searchForm');
const searchTermInput = document.getElementById('searchTermInput');

// --- Function to clear the search input and resubmit the form ---
function clearSearchAndSubmit() {
    if (searchTermInput) {
        searchTermInput.value = '';
    }
    if (searchForm) {
        // Submitting the form with a blank searchTerm will reset the filter
        searchForm.submit();
    }
}

// --- 1. Function for Sort Field Dropdown Change ---
// Called by the dropdown's onchange event (in the fragment)
function updateSortField(newField) {
    if (!sortHiddenInput || !searchForm) return;

    // Get the current direction from the hidden input in the fragment
    const currentDirElement = document.getElementById('currentSortDir');
    const currentDir = currentDirElement ? currentDirElement.value : 'DESC'; // Default to DESC

    // Update the main hidden input with the new field and existing direction
    sortHiddenInput.value = newField + ',' + currentDir;

    // Submit the main search form
    searchForm.submit();
}

// --- 2. Function for Direction Toggle Button Click ---
// Called by the direction button's onclick event.
function toggleSortDirection() {
    if (!sortHiddenInput || !searchForm) return;

    const currentSortValue = sortHiddenInput.value; // e.g., "date,DESC"

    // Split to isolate the field and current direction
    const parts = currentSortValue.split(',');
    let field = parts[0];

    // Ensure currentDir is properly extracted, defaulting to DESC
    let currentDir = parts.length > 1 && parts[1] ? parts[1].toUpperCase() : 'DESC';

    // Determine the new direction (toggle)
    const newDir = (currentDir === 'ASC') ? 'DESC' : 'ASC';

    // Update the main hidden sort input with the new direction
    sortHiddenInput.value = field + ',' + newDir;

    // Submit the main search form
    searchForm.submit();
}
