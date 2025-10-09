document.addEventListener('DOMContentLoaded', () => {
    const toggleButtons = document.querySelectorAll('.toggle-goals');

    const FRAGMENT_API_URL = '/goal/list-fragment';

    let expandedContent = null;

    const loadGoals = async (contentElement, scope) => {
        contentElement.innerHTML = `<p style="color: green; font-style: italic;">Loading ${scope} goals...</p>`;

        try {
            const response = await fetch(`${FRAGMENT_API_URL}?scope=${scope}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const listHtml = await response.text();

            contentElement.innerHTML = listHtml;
            contentElement.setAttribute('data-loaded', 'true');

        } catch (error) {
            console.error('Failed to load goals:', error);
            contentElement.innerHTML = `<p style="color: red; font-weight: bold;">Error loading goals. Please try again.</p>`;
        }
    };

    const toggleGoals = (button) => {
        const scope = button.getAttribute('data-scope');
        const contentId = button.getAttribute('aria-controls');
        const contentElement = document.getElementById(contentId);
        const arrow = button.querySelector('.arrow');
        const isLoaded = contentElement.getAttribute('data-loaded') === 'true';

        if (expandedContent === contentElement) {
            contentElement.classList.remove('expanded');
            button.setAttribute('aria-expanded', 'false');
            contentElement.setAttribute('aria-hidden', 'true');
            arrow.classList.remove('rotate-180');
            expandedContent = null;
            return;
        }

        if (expandedContent) {
            expandedContent.classList.remove('expanded');
            expandedContent.setAttribute('aria-hidden', 'true');

            const prevButton = expandedContent.parentNode.querySelector('.toggle-goals');
            if (prevButton) {
                prevButton.setAttribute('aria-expanded', 'false');
                prevButton.querySelector('.arrow').classList.remove('rotate-180');
            }
        }

        contentElement.classList.add('expanded');
        button.setAttribute('aria-expanded', 'true');
        contentElement.setAttribute('aria-hidden', 'false');
        arrow.classList.add('rotate-180');
        expandedContent = contentElement; // Update the state

        if (!isLoaded) {
            const contentDiv = contentElement.querySelector('div');
            loadGoals(contentDiv, scope);
        }
    };

    toggleButtons.forEach(button => {
        button.addEventListener('click', () => toggleGoals(button));
    });
});