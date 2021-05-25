(async function () {
    const main = document.querySelector('main');
    const params = new URLSearchParams(window.location.search);
    const response = params.has('group') ? await fetch(`/groups/${params.get('group')}`, {method: 'get'}) : await fetch('/tasks', {method: 'get'});
    if (response.ok) {
        const tasks = await response.json();
        const list = document.createElement('ul');
        tasks.forEach(task => list.appendChild(createTask(task)));
        main.prepend(list);
    }
    const form = document.querySelector('form');
    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        const resp = await fetch('/tasks', {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                description: form.elements.description.value,
                deadline: form.elements.deadline.value && form.elements.deadline.value + ':00.000'
            })
        });
        if (resp.ok) {
            const taskFromServer = await resp.json();
            document.querySelector('ul').appendChild(createTask(taskFromServer));
            form.reset();
        }
    });

    function createTask({ id, description, deadline, done }) {
        const result = document.createElement('li');
        result.innerHTML = `
        <label>
            <input type="checkbox" ${done ? ' checked' : ''}/>
            ${description}
            <small>${deadline && deadline.replace('T', ' ')}</small>
        </label>
    `;
        result.querySelector('input').addEventListener('click', async (e) => {
            const response = await fetch(`/tasks/${id}`, { method: 'PATCH' });
            if (!response.ok) {
                e.target.checked = !e.target.checked;
            }
        });
        return result;
    }
})();