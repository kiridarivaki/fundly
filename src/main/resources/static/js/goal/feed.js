const activityContainer = document.getElementById('activityFeedContainer');
const loadMoreBtn = document.getElementById('loadMoreButton');
const modal = document.getElementById('contributionModal');
const openModalBtn = document.getElementById('openContributionModalBtn');
const closeModalBtn = document.getElementById('closeContributionModalBtn');

let currentPage = 0;

function showContributionModal() {
    if (modal) {
        modal.style.display = 'flex';
    }
}

function hideContributionModal() {
    if (modal) {
        modal.style.display = 'none';
    }
}

if (openModalBtn) {
    openModalBtn.addEventListener('click', showContributionModal);
}
if (closeModalBtn) {
    closeModalBtn.addEventListener('click', hideContributionModal);
}

if (modal) {
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            hideContributionModal();
        }
    });
}

function formatTimestamp(timestamp) {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    const options = {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
        hour12: true
    };
    return date.toLocaleString('en-US', options);
}

function createActivityItemHtml(event) {
    const timestampDisplay = formatTimestamp(event.timestamp);

    const eventTypeDisplay = event.eventTypeDisplayName;

    const amountDisplay = event.amount
        ? `<br><span class="event-amount">Amount: $${event.amount}</span>`
        : '';

    return `
        <div class="activity-item">
            <span class="event-timestamp">${timestampDisplay}</span>
            <span>
                <strong class="event-user">${event.userFirstName || event.userName} :</strong>
                <span class="event-type">${eventTypeDisplay}</span><br>
                <span class="event-description">${event.description || ''}</span>
                ${amountDisplay}
            </span>
        </div>
    `;
}

function appendEventToFeed(event, isLive = false) {
    const rawHtml = createActivityItemHtml(event);

    if (isLive) {
        activityContainer.insertAdjacentHTML('afterbegin', rawHtml);
    }
    else {
        activityContainer.insertAdjacentHTML('beforeend', rawHtml);
    }
}

function loadMoreHistory() {
    currentPage++;
    const endpoint = `/goal/${GOAL_ID}/feed?page=${currentPage}`;

    loadMoreBtn.disabled = true;
    loadMoreBtn.textContent = 'Loading...';

    fetch(endpoint)
        .then(response => {
            const contentType = response.headers.get("content-type");
            if (!response.ok || !contentType || !contentType.includes("application/json")) {
                throw new Error('Server returned non-JSON response. Check controller mapping for history loading.');
            }
            return response.json();
        })
        .then(newEvents => {
            if (newEvents && newEvents.length > 0) {
                newEvents.forEach(event => {
                    appendEventToFeed(event, false);
                });
                loadMoreBtn.disabled = false;
                loadMoreBtn.textContent = 'Load More History';
            } else {
                loadMoreBtn.style.display = 'none';
                activityContainer.insertAdjacentHTML('beforeend', '<p>No more events found.</p>');
            }
        })
        .catch(error => {
            console.error('Error loading history:', error);
            loadMoreBtn.textContent = 'Error Loading (Click to Retry)';
            loadMoreBtn.disabled = false;
            currentPage--;
        });
}

if (typeof GOAL_ID !== 'undefined' && GOAL_ID) {
    connect(GOAL_ID, appendEventToFeed);
}
