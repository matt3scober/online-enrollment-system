document.addEventListener('DOMContentLoaded', () => {
    const courseList = document.getElementById('courseList');
    const enrollForm = document.getElementById('enrollForm');
    const logoutBtn = document.getElementById('logoutBtn');

    // Fetch and display courses
    async function loadCourses() {
        try {
            const courses = await getCourses(); // from api.js
            courseList.innerHTML = '';
            courses.forEach(course => {
                courseList.innerHTML += `<p>${course.id} - ${course.name} (${course.credits} credits)</p>`;
            });
        } catch (error) {
            console.error('Error loading courses:', error);
            courseList.innerHTML = '<p>Failed to load courses.</p>';
        }
    }

    // Handle enrollment
    enrollForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const courseId = document.getElementById('courseId').value;
        try {
            await enrollInCourse(courseId); // from api.js
            alert('Enrollment successful!');
            // Optionally refresh the list or show enrollment info
        } catch (error) {
            console.error('Error enrolling:', error);
            alert('Failed to enroll.');
        }
    });

    // Logout
    logoutBtn.addEventListener('click', () => {
        removeToken();
        window.location.href = 'login.html';
    });

    // Initial load
    loadCourses();
});
