document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');

    loginForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        // Usually you’d call your auth-service or some endpoint like /auth/login
        try {
            const response = await fetch('http://localhost:8081/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            if (response.ok) {
                const data = await response.json();
                // Store token in localStorage (or sessionStorage)
                setToken(data.accessToken); // from token.js
                // Redirect to dashboard
                window.location.href = 'dashboard.html';
            } else {
                alert('Login failed. Check your credentials.');
            }
        } catch (error) {
            console.error('Error during login:', error);
            alert('Failed to login. Check console for more details.');
        }
    });
});
