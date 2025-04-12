document.addEventListener('DOMContentLoaded', () => {
    const gradesTableBody = document.querySelector('#gradesTable tbody');
    const gradeForm = document.getElementById('gradeForm');
    const logoutBtn = document.getElementById('logoutBtn');

    // Function to load grade history for the logged-in student
    async function loadGradeHistory() {
        try {
            const response = await fetch('http://localhost:8082/grades', {
                // Adjust this URL if your grade endpoint is different.
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${getToken()}`,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('Failed to load grade history');
            }
            const grades = await response.json();
            gradesTableBody.innerHTML = '';
            grades.forEach(grade => {
                const row = document.createElement('tr');
                // Adjust property names based on your backend response format.
                row.innerHTML = `
            <td>${grade.courseName || grade.courseId}</td>
            <td>${grade.grade}</td>
            <td>${grade.date || ''}</td>
          `;
                gradesTableBody.appendChild(row);
            });
        } catch (error) {
            console.error('Error loading grade history:', error);
            gradesTableBody.innerHTML = '<tr><td colspan="3">Failed to load grade history.</td></tr>';
        }
    }

    // Handle grade entry submission (for faculty)
    gradeForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const courseId = document.getElementById('courseId').value;
        const studentId = document.getElementById('studentId').value;
        const gradeValue = document.getElementById('grade').value;

        try {
            const response = await fetch('http://localhost:8082/grades', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${getToken()}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    courseId: courseId,
                    studentId: studentId,
                    grade: gradeValue
                })
            });
            if (!response.ok) {
                throw new Error('Grade submission failed');
            }
            alert('Grade submitted successfully!');
            // Reload grade history to reflect new changes
            loadGradeHistory();
        } catch (error) {
            console.error('Error submitting grade:', error);
            alert('Failed to submit grade.');
        }
    });

    // Logout function: remove token and redirect to login page
    logoutBtn.addEventListener('click', () => {
        removeToken();
        window.location.href = 'login.html';
    });

    // Initial load: fetch and display the student's grade history
    loadGradeHistory();
});
