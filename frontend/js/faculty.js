document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = document.getElementById('logoutBtn');
    const courseList = document.getElementById('courseList');

    async function loadCourses() {
        try {
            const courses = await getCourses(); // from api.js
            courseList.innerHTML = '';
            courses.forEach(course => {
                courseList.innerHTML += `<p>${course.id} - ${course.name}</p>`;
            });
        } catch (error) {
            console.error('Error loading faculty courses:', error);
        }
    }

    // Logout
    logoutBtn.addEventListener('click', () => {
        removeToken();
        window.location.href = 'login.html';
    });

    loadCourses();
});
